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

import javafx.animation.Animation;
import javafx.animation.PauseTransitionBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class AnimationManager {
	final List<List<Animation>> operations = new ArrayList<List<Animation>>();
	private final Visualization visualization;
	private int operationPos = -1;
	private int stepPos = -1; // position after last played step
	
	public AnimationManager(Visualization visualization) {
		this.visualization = visualization;
	}
	
	public void add(List<Animation> operation, boolean isDone) {
		++operationPos;
		if (operationPos < operations.size()) {
			for (int i = 0; i < operations.size() - operationPos; i++) {
				operations.remove(operations.size() - 1);
			}
		}

		if (operation.size() > 1) {
			operation.get(0).setOnFinished(new NextStepHandler(0, operation.get(0)));
			for (int i = 1; i < operation.size() - 1; i++) {
				Animation step = operation.get(i);
				step.setOnFinished(new NextStepHandler(1, step));
			}
			Animation lastStep = operation.get(operation.size() - 1);
			lastStep.setOnFinished(new NextStepHandler(2, lastStep));
		} else {
			operation.get(0).setOnFinished(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					visualization.visPane.refresh();
					visualization.getButtons().setDisabled(false);
					visualization.getButtons().disablePrevious(!hasPrevious());
					visualization.getButtons().disableNext(!hasNext());
				}
			});
		}
		
		operations.add(operation);
		if (isDone) {
			stepPos = operation.size() - 1;
		} else {
			stepPos = -1;
		}
	}
	
	public void playNext() {
		List<Animation> curOp = operations.get(operationPos);
		if (stepPos + 1 == curOp.size()) {
			if (hasNext()) {
				operationPos++;
				stepPos = -1;
				playNext();
			}
		} else {
			stepPos++;
			Animation curStep = curOp.get(stepPos);
			if (curStep.getRate() < 0) curStep.setRate(-curStep.getRate());
			curStep.play();
		}
	}

	public void playPrevious() {
		List<Animation> curOp = operations.get(operationPos);
		if (stepPos == -1) {
			if (hasPrevious()) {
				operationPos--;
				stepPos = operations.get(operationPos).size() - 1;
				playPrevious();
			}
		} else {
			Animation curStep = curOp.get(stepPos);
			if (curStep.getRate() > 0) curStep.setRate(-curStep.getRate());
			stepPos--;
			curStep.play();
		}
	}
	
	public boolean hasNext() {
		return operationPos < operations.size() - 1 || stepPos < operations.get(operationPos).size() - 1;
	}
	
	public boolean hasPrevious() {
		return operationPos > 0 || stepPos > -1;
	}
	
	public void clear() {
		operations.clear();
		operationPos = -1;
	}
	
	private class NextStepHandler implements EventHandler<ActionEvent> {
		/**
		 * 0 = first step
		 * 1 = middle step
		 * 2 = last step
		 */
		private final int pos;
		private final Animation step;

		private NextStepHandler(int pos, Animation step) {
			this.pos = pos;
			this.step = step;
		}

		@Override
		public void handle(ActionEvent actionEvent) {
			visualization.visPane.refresh();
			if (step.getRate() > 0 && pos < 2) {
				if (!visualization.getButtons().isPauseChecked()) {
					PauseTransitionBuilder.create()
							.duration(Duration.seconds(2))
							.onFinished(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent actionEvent) {
									playNext();
								}
							})
							.build()
							.play();
				} else {
					visualization.getButtons().disableNext(!hasNext());
					visualization.getButtons().disablePrevious(!hasPrevious());
				}
			}
			else if (step.getRate() < 0 && pos > 0) {
				if (!visualization.getButtons().isPauseChecked()) {
					PauseTransitionBuilder.create()
							.duration(Duration.seconds(2))
							.onFinished(new EventHandler<ActionEvent>() {
								@Override
								public void handle(ActionEvent actionEvent) {
									playPrevious();
								}
							})
							.build()
							.play();
				} else {
					visualization.getButtons().disableNext(!hasNext());
					visualization.getButtons().disablePrevious(!hasPrevious());
				}
			} else {
				visualization.getButtons().setDisabled(false);
				visualization.getButtons().disableNext(!hasNext());
				visualization.getButtons().disablePrevious(!hasPrevious());
			}
		}
	}
}
