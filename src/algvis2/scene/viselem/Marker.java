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

package algvis2.scene.viselem;

import algvis2.scene.layout.ZDepth;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Marker extends VisElem {
	public Marker(algvis2.scene.viselem.Node elem) {
		super(new Circle());
		setMarkedElem(elem);
	}
	
	private void setMarkedElem(algvis2.scene.viselem.Node elem) {
		setZDepth(ZDepth.TOP);
		getNode().setFill(Color.TRANSPARENT);
		getNode().setStroke(Color.BLACK);
		getNode().setRadius(Math.max(elem.getNode().getBoundsInParent().getWidth(),
				elem.getNode().getBoundsInParent().getHeight()) * 0.5);
		getNode().layoutXProperty().bind(elem.visPaneX.add(elem.visPaneTranslateX));
		getNode().layoutYProperty().bind(elem.visPaneY.add(elem.visPaneTranslateY));

		ScaleChange scaleChange = new ScaleChange(elem.getNode());
		elem.getNode().scaleXProperty().addListener(scaleChange);
		elem.getNode().scaleYProperty().addListener(scaleChange);
	}

	@Override
	public Circle getNode() {
		return (Circle) super.getNode();
	}
	
	private final class ScaleChange implements ChangeListener<Number> {
		private final Node node;

		private ScaleChange(Node node) {
			this.node = node;
		}

		@Override
		public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
			getNode().setRadius(Math.max(node.getBoundsInParent().getWidth(),
					node.getBoundsInParent().getHeight()) * 0.5);
		}
	}
}
