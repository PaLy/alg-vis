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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.util.*;

public class PropertiesState {
	public final HashMap<Object, Object> preState;
	private final Visualization visualization;

	public PropertiesState(HashMap<Object, Object> preState,
			Visualization visualization) {
		this.preState = preState;
		this.visualization = visualization;
	}

	public Timeline createTimeline(final boolean layoutRequested) {
		final HashMap<WritableValue<Collection>, Object[]> preElements = new HashMap<>();
		final HashMap<WritableValue<Collection>, Object[]> postElements = new HashMap<>();
		ArrayList<KeyValue> firstKeyFrameValues = new ArrayList<>();
		ArrayList<KeyValue> secondKeyFrameValues = new ArrayList<>();

		for (Object key : preState.keySet()) {
			if (key instanceof WritableValue) {
				WritableValue wvKey = (WritableValue) key;
				Object preValue = preState.get(key);
				if (wvKey.getValue() instanceof Collection) {
					Collection collection = (Collection) wvKey.getValue();

					Object[] preVal = (Object[]) preValue;
					if (!Arrays.equals(preVal, collection.toArray())) {
						preElements.put(wvKey, preVal);
						postElements.put(wvKey, collection.toArray());
					}
				} else if (wvKey.getValue() instanceof Map) {
					Map map = (Map) wvKey.getValue();

					Object[] preVal = (Object[]) preValue;
					if (!Arrays.equals(preVal, map.entrySet().toArray())) {
						preElements.put(wvKey, preVal);
						postElements.put(wvKey, map.entrySet().toArray());
					}
				} else {
					if ((preValue != null && !preValue.equals(wvKey.getValue()))
							|| (preValue == null && wvKey.getValue() != null)
							|| (preValue != null && wvKey.getValue() == null)) {
						firstKeyFrameValues.add(new KeyValue(wvKey, preValue));
						secondKeyFrameValues.add(new KeyValue(wvKey, wvKey
								.getValue()));
					}
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
				for (WritableValue writableValue : preElements.keySet()) {
					if (writableValue.getValue() instanceof Collection) {
						Collection collection = (Collection) writableValue
								.getValue();

						collection.clear();
						if (timeline.getRate() > 0)
							Collections.addAll(collection,
									postElements.get(writableValue));
						else if (timeline.getRate() < 0) {
							Collections.addAll(collection,
									preElements.get(writableValue));
						} else {
							System.out.println("WTF");
							return;

						}
					} else if (writableValue.getValue() instanceof Map) {
						Map map = (Map) writableValue.getValue();

						map.clear();
						if (timeline.getRate() > 0) {
							for (Object entry : postElements.get(writableValue)) {
								Map.Entry e = (Map.Entry) entry;
								map.put(e.getKey(), e.getValue());
							}
						} else if (timeline.getRate() < 0) {
							for (Object entry : preElements.get(writableValue)) {
								Map.Entry e = (Map.Entry) entry;
								map.put(e.getKey(), e.getValue());
							}
						} else {
							System.out.println("WTF2");
							return;
						}
					}
				}
				if (layoutRequested) {
					visualization.reLayout();
				}
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
