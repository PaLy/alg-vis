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

import algvis2.core.Visualization;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class AnimationManager {
	private final List<Animation> animations = new ArrayList<Animation>();
	private final Visualization visualization;
	private int position = -1;
	
	public AnimationManager(Visualization visualization) {
		this.visualization = visualization;
	}
	
	public void add(Animation animation) {
		if (position > -1) {
			Animation curAnim = animations.get(position);
			if (curAnim.getRate() < 0) {
				curAnim.jumpTo("start");
				--position;
			} else {
				curAnim.jumpTo("end");
			}
		}
		++position;
		if (position < animations.size()) {
			for (int i = 0; i < animations.size() - position; i++) {
				animations.remove(animations.size() - 1);
			}
		}
		animations.add(animation);
	}
	
	public void playNext() {
		Animation curAnim = animations.get(position); 
		if (curAnim.getCurrentTime().toMillis() < curAnim.getTotalDuration().toMillis()) {
			if (curAnim.getRate() < 0) curAnim.setRate(-curAnim.getRate());
			SequentialTransition st = new SequentialTransition(curAnim);
			st.setOnFinished(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					visualization.getButtons().setDisabled(false);
					visualization.getButtons().disableNext(!hasNext());
					visualization.getButtons().disablePrevious(!hasPrevious());
				}
			});
			st.play();
		} else if (position < animations.size() - 1) {
			++position;
			playNext();
		}
	}

	public void playPrevious() {
		Animation curAnim = animations.get(position);
		if (curAnim.getCurrentTime() != Duration.ZERO) {
			if (curAnim.getRate() > 0) curAnim.setRate(-curAnim.getRate());
			SequentialTransition st = new SequentialTransition(curAnim);
			st.setOnFinished(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent actionEvent) {
					visualization.getButtons().setDisabled(false);
					visualization.getButtons().disableNext(!hasNext());
					visualization.getButtons().disablePrevious(!hasPrevious());
				}
			});
			st.setRate(-1);
			st.jumpTo("end");
			st.play();
		} else if (position > 0) {
			--position;
			playPrevious();
		}
	}
	
	public boolean hasNext() {
		return position < animations.size() - 1 || animations.get(position).getCurrentTime().toMillis() < animations.get
				(position).getTotalDuration().toMillis();
	}
	
	public boolean hasPrevious() {
		return position > 0 || (position == 0 && animations.get(position).getCurrentTime() != Duration.ZERO);
	}
	
	public void clear() {
		animations.clear();
		position = -1;
	}
}
