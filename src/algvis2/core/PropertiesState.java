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

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.WritableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PropertiesState {
	public final HashMap<Object, Object> preState;
	private final Visualization visualization;

	public PropertiesState(HashMap<Object, Object> preState,
			Visualization visualization) {
		this.preState = preState;
		this.visualization = visualization;
	}

	public Timeline createTimeline() {
		final HashMap<Parent, List<Node>> preChildren = new HashMap<Parent, List<Node>>();
		final HashMap<Parent, List<Node>> postChildren = new HashMap<Parent, List<Node>>();
		ArrayList<KeyValue> firstKeyFrameValues = new ArrayList<KeyValue>();
		ArrayList<KeyValue> secondKeyFrameValues = new ArrayList<KeyValue>();

		for (Object key : preState.keySet()) {
			if (key instanceof WritableValue) {
				WritableValue wvKey = (WritableValue) key;
				Object preValue = preState.get(key);
				if ((preValue != null && !preValue.equals(wvKey.getValue()))
						|| (preValue == null && wvKey.getValue() != null)
						|| (preValue != null && wvKey.getValue() == null)) {
					firstKeyFrameValues.add(new KeyValue(wvKey, preValue));
					secondKeyFrameValues.add(new KeyValue(wvKey, wvKey
							.getValue()));
				}
			} else if (key instanceof Parent) {
				Parent pKey = (Parent) key;
				List<Node> preValue = (List<Node>) preState.get(pKey);
				if (!preValue.equals(pKey.getChildrenUnmodifiable())) {
					preChildren.put(pKey, preValue);
					postChildren
							.put(pKey,
									new ArrayList<Node>(pKey
											.getChildrenUnmodifiable()));
				}
			}
		}

		KeyFrame firstKeyFrame = new KeyFrame(Duration.ZERO, null, null,
				firstKeyFrameValues);
		KeyFrame secondKeyFrame = new KeyFrame(Duration.millis(1), null, null,
				secondKeyFrameValues);
		final Timeline timeline = new Timeline(firstKeyFrame, secondKeyFrame);

		timeline.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				for (Parent parent : preChildren.keySet()) {
					ObservableList<Node> children;
					if (parent instanceof Pane)
						children = ((Pane) parent).getChildren();
					else if (parent instanceof Group)
						children = ((Group) parent).getChildren();
					else
						continue;

					children.clear();
					List<Node> newChildren;
					if (timeline.getRate() > 0)
						newChildren = postChildren.get(parent);
					else if (timeline.getRate() < 0) { // TODO vykona sa toto niekedy
						newChildren = preChildren.get(parent);
					} else
						return;

					for (Node child : newChildren) {
						children.add(child);
					}
				}
				visualization.getDataStructure().reLayout(); // TODO tu to je lepsie kvoli prechadzaniu animacie dozadu
//				System.out.println("teraz " + (timeline.getRate() > 0 ? "dopredu" : "dozadu") + " " + k++);
			}
		});
		return timeline;
	}

	public void addNewProperties(HashMap<Object, Object> newStates) {
		for (Object key : newStates.keySet()) {
			if (!preState.containsKey(key))
				preState.put(key, newStates.get(key));
		}
	}
}
