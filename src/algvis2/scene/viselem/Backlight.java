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

import algvis2.animation.AutoTranslateTransition;
import algvis2.scene.Axis;
import algvis2.scene.layout.ZDepth;
import javafx.animation.Animation;
import javafx.animation.FadeTransitionBuilder;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class Backlight extends VisElem {
	public static final Paint RED = Color.RED;
	public static final Paint GREEN = Color.LIMEGREEN;
	
	public Backlight(Node node, Paint paint) {
		super(new Circle());
		getNode().setRadius(Node.RADIUS * 1.25);
		getNode().layoutXProperty().bind(node.visPaneX);
		getNode().layoutYProperty().bind(node.visPaneY);
//		getNode().setFill(RadialGradientBuilder.create()
//				.centerX(0.5)
//				.centerY(0.5)
//				.radius(1)
//				.stops(new Stop(0, Color.RED), new Stop(1, Color.WHITE))
//				.build());
		getNode().setFill(paint);
		
		setZDepth(ZDepth.BACKLIGHT);
		
		getNode().layoutXProperty().addListener(new AutoTranslateTransition(getNode(), Axis.X));
		getNode().layoutYProperty().addListener(new AutoTranslateTransition(getNode(), Axis.Y));
	}
	
	public Backlight(Node node, Paint paint, boolean blinking) {
		this(node, paint);
		if (blinking) {
			FadeTransitionBuilder.create()
					.node(getNode())
					.duration(Duration.millis(500))
					.fromValue(1)
					.toValue(0)
					.cycleCount(Animation.INDEFINITE)
					.autoReverse(true)
					.build()
					.play();
		}
	}

	@Override
	public Circle getNode() {
		return (Circle) super.getNode();
	}
}
