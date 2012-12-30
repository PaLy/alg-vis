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

import algvis2.scene.layout.ZDepth;
import algvis2.scene.viselem.Marker;
import algvis2.scene.viselem.Node;
import algvis2.scene.viselem.VisElem;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Algorithm implements Runnable {
	protected final Visualization visualization;
//	protected final Animations animations;

	private PropertiesState state;
	private PropertiesState startState;
	public final List<Animation> allSteps = new ArrayList<Animation>();
	private SequentialTransition step = new SequentialTransition();
	private Timeline backToStart;
	
	private boolean layoutRequested = false;
	private boolean refreshRequested = false;

	protected Algorithm(DataStructure D) {
		visualization = D.visualization;
//		animations = new Animations(visualization.visPane);
	}

	@Override
	public void run() {
		begin();
		runAlgorithm();
		end();
	}

	protected abstract void runAlgorithm();

	protected void pause(boolean relayout) {
		if (relayout) layoutRequested = true;
		saveChangedProperties();
		allSteps.add(step);
		step = new SequentialTransition();
	}

	void begin() {
		HashMap<Object, Object> preState = new HashMap<Object, Object>();
		visualization.storeState(preState);
		state = new PropertiesState(preState, visualization);
		startState = state;
	}

	void end() {
		pause(true);
		backToStart = startState.createTimeline(false, false);
//		System.out.println("BACKTOSTART:");
//		System.out.println(backToStart.getKeyFrames());
	}

	public Animation getBackToStart() {
		return backToStart;
	}
	
	protected void requestLayout() {
		layoutRequested = true;
	}

	protected void requestRefresh() {
		refreshRequested = true;
	}

	public void saveChangedProperties() {
		step.getChildren().add(state.createTimeline(layoutRequested, refreshRequested));
		
		layoutRequested = false;
		refreshRequested = false;
		HashMap<Object, Object> preState = new HashMap<Object, Object>();
		visualization.storeState(preState);
		state = new PropertiesState(preState, visualization);

		startState.addNewProperties(preState);

//		Timeline t = (Timeline) step.getChildren().get(step.getChildren().size() - 1);
//		System.out.println(t.getKeyFrames());
//		System.out.println("*************");
	}

	protected void addAnimation(Animation anim) {
		saveChangedProperties();
		step.getChildren().add(anim);
	}

	protected void addVisElem(VisElem elem, ZDepth zDepth) {
		elem.setZDepth(zDepth);
		addVisElem(elem);
	}

	protected void addVisElem(VisElem elem) {
		visualization.visPane.add(elem);
		saveChangedProperties();
	}

	protected void removeVisElem(VisElem elem) {
		visualization.visPane.remove(elem);
	}
	
	
	private final HashMap<VisElem, Marker> markers = new HashMap<VisElem, Marker>();
	
	protected void addMarker(Node elem) {
		Marker marker = new Marker(elem);
		addVisElem(marker);
		markers.put(elem, marker);
	}
	
	protected void removeMarker(Node elem) {
		Marker marker = markers.get(elem);
		removeVisElem(marker);
		markers.remove(elem);
	}

//	protected class Animations {
//		private final VisPane visPane;
//
//		public Animations(VisPane visPane) {
//			this.visPane = visPane;
//		}
//
//		public void backlight(final Node node, Paint paint) {
//			Circle nodeCircle = (Circle) node.getShape();
//			final Circle newCircle = new Circle(nodeCircle.getRadius() * 1.3,
//					paint);
//			newCircle.centerXProperty().bind(node.visPaneX);
//			newCircle.centerYProperty().bind(node.visPaneY);
//			newCircle.translateXProperty().bind(node.visPaneTranslateX);
//			newCircle.translateYProperty().bind(node.visPaneTranslateY);
//
//			visPane.add(newCircle, ZDepth.BACKLIGHT);
//
//			addAnimation(FadeTransitionBuilder.create().node(newCircle)
//					.duration(Duration.millis(300)).fromValue(1).toValue(0)
//					.cycleCount(6).autoReverse(true).build());
//
//			visPane.remove(newCircle);
//		}
//	}
}
