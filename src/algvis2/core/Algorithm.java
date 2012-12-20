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

package algvis2.core;

import algvis2.ds.DataStructure;
import algvis2.scene.layout.VisPane;
import algvis2.scene.layout.ZDepth;
import algvis2.scene.shape.Node;
import javafx.animation.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.HashMap;

public abstract class Algorithm implements Runnable {
	protected final Visualization visualization;
	protected final Animations animations;

	private PropertiesState state;
	private PropertiesState startState;
	private final SequentialTransition animation;
	private Timeline backToStart;

	protected Algorithm(DataStructure D) {
		visualization = D.visualization;
		animations = new Animations(visualization.visPane);
		animation = new SequentialTransition();
	}

	@Override
	public void run() {
		begin();
		try {
			runAlgorithm();
		} catch (InterruptedException e) {
			return;
		}
		end();
	}

	public Animation runA() {
		run();
		return animation;
	}

	public abstract void runAlgorithm() throws InterruptedException;

	protected void pause() throws InterruptedException {
		saveChangedProperties();
		animation.getChildren().add(
				new PauseTransition(Duration.seconds(2)));
	}

	void begin() {
		HashMap<Object, Object> preState = new HashMap<Object, Object>();
		visualization.storeState(preState);
		state = new PropertiesState(preState, visualization);
		startState = state;
	}

	void end() {
		saveChangedProperties();
		backToStart = startState.createTimeline();
//		System.out.println("BACKTOSTART:");
//		System.out.println(backToStart.getKeyFrames());
	}

	public Animation getBackToStart() {
		return backToStart;
	}

	public HashMap<String, Object> getResult() {
		return null;
	}

	public void saveChangedProperties() {
		animation.getChildren().add(state.createTimeline());
		HashMap<Object, Object> preState = new HashMap<Object, Object>();
		visualization.storeState(preState);
		state = new PropertiesState(preState, visualization);

		startState.addNewProperties(preState);

//		Timeline t = (Timeline) animation.getChildren().get(animation.getChildren().size() - 1);
//		System.out.println(t.getKeyFrames());
//		System.out.println("*************");
	}

	protected void addAnimation(Animation anim) {
		saveChangedProperties();
		animation.getChildren().add(anim);
	}

	protected void addNode(javafx.scene.Node node, int zDepth) {
		visualization.visPane.add(node, zDepth);
		saveChangedProperties();
	}

	protected void removeNode(javafx.scene.Node node) {
		visualization.visPane.remove(node);
	}

	protected class Animations {
		private final VisPane visPane;

		public Animations(VisPane visPane) {
			this.visPane = visPane;
		}

		public void backlight(final Node node, Paint paint) {
			Circle nodeCircle = (Circle) node.getShape();
			final Circle newCircle = new Circle(nodeCircle.getRadius() * 1.3,
					paint);
			newCircle.centerXProperty().bind(node.visPaneX);
			newCircle.centerYProperty().bind(node.visPaneY);
			newCircle.translateXProperty().bind(node.visPaneTranslateX);
			newCircle.translateYProperty().bind(node.visPaneTranslateY);

			visPane.add(newCircle, ZDepth.BACKLIGHT);

			addAnimation(FadeTransitionBuilder.create().node(newCircle)
					.duration(Duration.millis(300)).fromValue(1).toValue(0)
					.cycleCount(6).autoReverse(true).build());

			visPane.remove(newCircle);
		}
	}
}
