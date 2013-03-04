/*******************************************************************************
 * Copyright (c) 2012 Jakub Kováč, Katarína Kotrlová, Pavol Lukča, Viktor Tomkovič, Tatiana Tóthová
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package algvis2.scene.shape;

import algvis2.beans.AngleBinding;
import algvis2.beans.CosBinding;
import algvis2.beans.SinBinding;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Rotate;

public class Arrow extends Group {
	private final Line line = new Line();

	public Arrow(double nodeRadius) {
		Polygon top = new Polygon();
		top.getPoints().addAll(0.0, 0.0, -4.0, -6.0, 4.0, -6.0);

		Rotate rotate = new Rotate();
		rotate.angleProperty().bind(new AngleBinding(line.endYProperty(), line.endXProperty()));
		top.getTransforms().add(rotate);

		top.layoutXProperty().bind(
				line.endXProperty()
						.add(new CosBinding(rotate.angleProperty()).multiply(nodeRadius)));
		top.layoutYProperty().bind(
				line.endYProperty()
						.add(new SinBinding(rotate.angleProperty()).multiply(nodeRadius)));

		getChildren().addAll(line, top);
	}

	public DoubleProperty startXProperty() {
		return layoutXProperty();
	}

	public DoubleProperty startYProperty() {
		return layoutYProperty();
	}

	public DoubleProperty endXProperty() {
		return line.endXProperty();
	}

	public DoubleProperty endYProperty() {
		return line.endYProperty();
	}
}
