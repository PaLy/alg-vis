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

import algvis2.scene.text.Fonts;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;

public class AnnotatedEdge extends Edge {
	public AnnotatedEdge(int version) {
		super();
		init(version);
	}

	public AnnotatedEdge(int version, double radius) {
		super(radius);
		init(version);
	}

	private void init(int version) {
		Text text = TextBuilder.create().text(String.valueOf(version)).font(Fonts.TOP_TEXT)
				.stroke(Color.WHITE).strokeType(StrokeType.OUTSIDE).strokeWidth(2).build();

		text.xProperty().bind(
				getVisual().endXProperty().divide(2.0)
						.subtract(text.getBoundsInLocal().getWidth() / 2));
		text.yProperty()
				.bind(getVisual().endYProperty().divide(2.0)
						.add(text.getBoundsInLocal().getHeight() / 2));
		getVisual().getChildren().add(text);
	}
}
