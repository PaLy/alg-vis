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
import algvis2.scene.viselem.VisElem;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class Algorithm implements Runnable {
	protected final Visualization visualization;

	private PropertiesState startState;
	private Timeline startEndTransition;

	private PropertiesState state;
	private SequentialTransition step = new SequentialTransition();
	public final List<Animation> allSteps = new ArrayList<Animation>();
	
	private boolean layoutRequested = true;

	protected Algorithm(DataStructure D) {
		visualization = D.visualization;
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
		startEndTransition = startState.createTimeline(false);
//		System.out.println("BACKTOSTART:");
//		System.out.println(startEndTransition.getKeyFrames());
	}

	public Animation startEndTransition() {
		return startEndTransition;
	}
	
	protected void requestLayout() {
		layoutRequested = true;
	}

	protected void saveChangedProperties() {
		step.getChildren().add(state.createTimeline(layoutRequested));
		layoutRequested = false;
		
		HashMap<Object, Object> preState = new HashMap<Object, Object>();
		visualization.storeState(preState);
		state = new PropertiesState(preState, visualization);

		startState.addNewProperties(preState);

//		Timeline t = (Timeline) step.getChildren().get(step.getChildren().size() - 1);
//		System.out.println(t.getKeyFrames());
//		System.out.println("*************");
	}

	/**
	 * For smaller overhead rather use<br/><br/>
	 * <code>addAnimation(new SequentialTransition(anims));</code><br/><br/>
	 * than<br/><br/> <code>{ addAnimation(anims[0]); 
	 * addAnimation(anims[1]); }</code><br/>
	 * @param anim animation to add to current step
	 */
	protected void addAnimation(Animation anim) {
		layoutRequested = true; // inak by sa napr. node pripojil na DS az po vykonani animacie
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
}
