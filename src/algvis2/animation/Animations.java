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

package algvis2.animation;

import algvis2.scene.layout.VisPane;
import algvis2.scene.layout.ZDepth;
import algvis2.scene.shape.Node;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Animations {
	
	public static Animation backlight(final Node node, Paint paint, final VisPane visPane) {
		Circle nodeCircle = (Circle) node.getShape();
		final Circle newCircle = new Circle(
			nodeCircle.getRadius() * 1.3,
			paint);
		newCircle.centerXProperty().bind(node.visPaneX);
		newCircle.centerYProperty().bind(node.visPaneY);

		SequentialTransition st = new SequentialTransition();
		st.getChildren().add(new Timeline(new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				visPane.add(newCircle, ZDepth.BACKLIGHT);
			}
		})));
		
		FadeTransition ft = FadeTransitionBuilder.create()
			.node(newCircle)
			.duration(Duration.millis(500))
			.fromValue(1)
			.toValue(0)
			.cycleCount(4)
			.autoReverse(true)
			.onFinished(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					visPane.remove(newCircle);
				}
			})
			.build();
		st.getChildren().add(ft);
		
		return st;
	}
}
