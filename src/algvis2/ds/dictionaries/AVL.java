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

package algvis2.ds.dictionaries;

import algvis2.core.Visualization;
import algvis2.ds.dictionaries.BST;
import javafx.animation.Interpolator;
import javafx.animation.SequentialTransition;

class AVL extends BST {
	public AVL(Visualization visualization) {
		super(visualization);
		//		test();
	}

	SequentialTransition sequentialTransition;

//	public Algorithm insert(int x) {
//		//		System.out.println("teraz");
//		//		HBox hBox = new HBox();
//		//		hBox.setSpacing(20);
//		//		Node n1 = new Node(82);
//		//		Node n2 = new Node(2);
//		//		Node n3 = new Node(67);
//		//		Node n4 = new Node(699);
//		//		hBox.getChildren().addAll(n1, n2, n3, n4);
//		//
//		//		FadeTransition fadeTransition = FadeTransitionBuilder.create()
//		//			.duration(Duration.seconds(2))
//		//			.node(n1)
//		//			.fromValue(1)
//		//			.toValue(0.1)
//		//			.cycleCount(Timeline.INDEFINITE)
//		//			.autoReverse(true)
//		//			.build();
//		////				fadeTransition.play();
//		//
//		//		TranslateTransition translateTransition = TranslateTransitionBuilder.create()
//		//			.duration(Duration.seconds(2))
//		//			.node(n2)
//		//			.byY(200)
//		////			.cycleCount(Timeline.INDEFINITE)
//		////			.autoReverse(true)
//		//			.build();
//		////		translateTransition.play();
//		//
//		//		ScaleTransition scaleTransition = ScaleTransitionBuilder.create()
//		//			.node(n3)
//		//			.duration(Duration.seconds(4))
//		//				//			.toX(3)
//		//			.toY(3)
//		//			.cycleCount(Timeline.INDEFINITE)
//		//			.autoReverse(true)
//		//			.build();
//		//		//		scaleTransition.play();
//		//
//		//		TranslateTransition translateTransition2 = TranslateTransitionBuilder.create()
//		//			.duration(Duration.seconds(2))
//		//			.node(n2)
//		//			.byY(100)
//		////			.cycleCount(Timeline.INDEFINITE)
//		////			.autoReverse(true)
//		//			.build();
//		////		translateTransition2.play();
//		//
//		//		visPane.add(hBox, ZDepth.TOP);
//		////		new ParallelTransition(translateTransition, translateTransition2).play();
//		//		new SequentialTransition(translateTransition, translateTransition2).play();
//
//		final Rectangle rectBasicTimeline = new Rectangle(100, 50, 100, 50);
//		rectBasicTimeline.setFill(Color.BROWN);
//		visPane.add(rectBasicTimeline, ZDepth.TOP);
//
//		final Timeline timeline = new Timeline();
//		//		timeline.setCycleCount(Timeline.INDEFINITE);
//		//		timeline.setAutoReverse(true);
//		final KeyValue kv = new KeyValue(rectBasicTimeline.xProperty(), 300,
//				Interpolator.EASE_BOTH);
//		final KeyFrame kf = new KeyFrame(Duration.millis(500), kv);
//		timeline.getKeyFrames().add(kf);
//		timeline.play();
//
//		final boolean[] a = { true };
//		timeline.setOnFinished(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//				System.out.println("nie");
//				if (a[0]) {
//					a[0] = false;
//					//					timeline.setRate(-1);
//					//					timeline.stop();
//					//					timeline.setRate(timeline.getRate());
//					//					timeline.jumpTo(Duration.ZERO);
//					System.out.println(timeline.getStatus());
//					timeline.playFromStart();
//					System.out.println(timeline.getStatus());
//				}
//			}
//		});
//
//		final Node node = new Node(27);
//		//		final ScaleTransition st = ScaleTransitionBuilder.create()
//		//			.node(node)
//		//			.fromX(node.getScaleX())
//		//			.toX(3)
//		//			.duration(Duration.millis(500))
//		//			.build();
//
//		KeyValue kva = new KeyValue(node.scaleXProperty(), 1, new MyInter());
//
//		Timeline st = TimelineBuilder
//				.create()
//				.autoReverse(true)
//				.keyFrames(
//						new KeyFrame(Duration.ZERO, kva),
//						new KeyFrame(Duration.millis(1), new KeyValue(node
//								.scaleXProperty(), 3, new MyInter()))).build();
//		st.setOnFinished(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//				//                st.getRate();
//				System.out.println("ajajaj " + hashCode());
//			}
//		});
//
//		visPane.add(node, ZDepth.NODES);
//		final Node node2 = new Node(270);
//		final ScaleTransition st2 = ScaleTransitionBuilder.create().node(node2)
//				.fromX(node2.getScaleX()).toX(3).duration(Duration.millis(500))
//				.build();
//
//		visPane.add(node2, ZDepth.NODES);
//
//		final boolean[] b = { true };
//		//		st.setOnFinished(new EventHandler<ActionEvent>() {
//		//			@Override
//		//			public void handle(ActionEvent event) {
//		//				if (b[0]) {
//		//					b[0] = false;
//		//					System.out.println(node.scaleXProperty());
//		////					st.stop();
//		////					st.setByX(-2);
//		//					st.setRate(-1);
//		//					System.out.println(st.getDuration());
//		//					st.jumpTo(st.getDuration());
//		////					System.out.println(node.getBoundsInParent());
//		//					st.play();
//		//					System.out.println(node.scaleXProperty());
//		////					System.out.println(node.getBoundsInParent());
//		//				} else {
//		//					System.out.println(node.scaleXProperty());
//		//				}
//		//			}
//		//		});
//		//
//		//		final boolean c[] = {true};
//		//		st2.setOnFinished(new EventHandler<ActionEvent>() {
//		//			@Override
//		//			public void handle(ActionEvent event) {
//		//				if (c[0]) {
//		//					c[0] = false;
//		//					//					st.stop();
//		//					st2.setRate(-1);
//		//					st2.jumpTo(st2.getDuration());
//		//					//					System.out.println(node.getBoundsInParent());
//		//					st2.play();
//		//					//					System.out.println(node.getBoundsInParent());
//		//				}
//		//			}
//		//		});
//
//		if (sequentialTransition == null) {
//			sequentialTransition = new SequentialTransition(st, st2);
//			sequentialTransition.play();
//		} else {
//			System.out.println(sequentialTransition.getCurrentTime());
//			System.out.println(node.scaleXProperty());
//			//					st.stop();
//			//					st.setByX(-2);
//			sequentialTransition.setRate(-sequentialTransition.getRate());
//			//					System.out.println(sequentialTransition.getDuration());
//			//			sequentialTransition.jumpTo(sequentialTransition.getTotalDuration());
//			//					System.out.println(node.getBoundsInParent());
//			sequentialTransition.play();
//			System.out.println(node.scaleXProperty());
//			//					System.out.println(node.getBoundsInParent());
//		}
//
//		sequentialTransition.setOnFinished(new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent event) {
//				//				if (b[0]) {
//				//					b[0] = false;
//				//					System.out.println(node.scaleXProperty());
//				////					st.stop();
//				////					st.setByX(-2);
//				//					sequentialTransition.setRate(-2);
//				////					System.out.println(sequentialTransition.getDuration());
//				//					sequentialTransition.jumpTo(sequentialTransition.getTotalDuration());
//				////					System.out.println(node.getBoundsInParent());
//				//					sequentialTransition.play();
//				//					System.out.println(node.scaleXProperty());
//				////					System.out.println(node.getBoundsInParent());
//				//				} else {
//				//					System.out.println(node.scaleXProperty());
//				//				}
//			}
//		});
//		//		
//		//		sequentialTransition.play();
//		return null;
//	}

	class MyInter extends Interpolator {

		@Override
		protected double curve(double v) {
			return v;
		}

		@Override
		public double interpolate(double v, double v1, double v2) {
			System.out.println(v + " " + v1 + " " + v2 + " " + v + (v1 - v)
					* v2);
			return v + (v1 - v) * v2;
		}
	}
}
