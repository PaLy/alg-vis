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

package algvis2.ds.dictionary.avl;

import algvis2.ds.dictionary.bst.BST;
import algvis2.scene.layout.VisPane;
import algvis2.scene.layout.ZDepth;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class AVL extends BST {
	public AVL(VisPane visPane) {
		super(visPane);
		test();
	}
	
	protected void test() {
//		System.out.println("teraz");
//		HBox hBox = new HBox();
//		hBox.setSpacing(20);
//		Node n1 = new Node(82);
//		Node n2 = new Node(2);
//		Node n3 = new Node(67);
//		Node n4 = new Node(699);
//		hBox.getChildren().addAll(n1, n2, n3, n4);
//
//		FadeTransition fadeTransition = FadeTransitionBuilder.create()
//			.duration(Duration.seconds(2))
//			.node(n1)
//			.fromValue(1)
//			.toValue(0.1)
//			.cycleCount(Timeline.INDEFINITE)
//			.autoReverse(true)
//			.build();
////				fadeTransition.play();
//
//		TranslateTransition translateTransition = TranslateTransitionBuilder.create()
//			.duration(Duration.seconds(2))
//			.node(n2)
//			.byY(200)
////			.cycleCount(Timeline.INDEFINITE)
////			.autoReverse(true)
//			.build();
////		translateTransition.play();
//
//		ScaleTransition scaleTransition = ScaleTransitionBuilder.create()
//			.node(n3)
//			.duration(Duration.seconds(4))
//				//			.toX(3)
//			.toY(3)
//			.cycleCount(Timeline.INDEFINITE)
//			.autoReverse(true)
//			.build();
//		//		scaleTransition.play();
//
//		TranslateTransition translateTransition2 = TranslateTransitionBuilder.create()
//			.duration(Duration.seconds(2))
//			.node(n2)
//			.byY(100)
////			.cycleCount(Timeline.INDEFINITE)
////			.autoReverse(true)
//			.build();
////		translateTransition2.play();
//
//		visPane.add(hBox, ZDepth.TOP);
////		new ParallelTransition(translateTransition, translateTransition2).play();
//		new SequentialTransition(translateTransition, translateTransition2).play();

		final Rectangle rectBasicTimeline = new Rectangle(100, 50, 100, 50);
		rectBasicTimeline.setFill(Color.BROWN);
		visPane.add(rectBasicTimeline, ZDepth.TOP);
		
		final Timeline timeline = new Timeline();
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.setAutoReverse(true);
		final KeyValue kv = new KeyValue(rectBasicTimeline.xProperty(), 300,
			Interpolator.EASE_BOTH);
		final KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
		timeline.getKeyFrames().add(kf);
		timeline.play();
	}
}
