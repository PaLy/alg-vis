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

package algvis2.ds.dictionary;

import algvis.core.MyRandom;
import algvis2.core.Algorithm;
import algvis2.core.DataStructure;
import algvis2.core.Visualization;
import algvis2.scene.control.InputField;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public abstract class Dictionary extends DataStructure {
	protected Dictionary(Visualization visualization) {
		super(visualization);
	}

	abstract public Algorithm insert(int x);
	
	abstract public Algorithm find(int x);

	abstract public Algorithm delete(int x);

	public Animation random(int n) {
		SequentialTransition st = new SequentialTransition();
		for (int i = 0; i < n; i++) {
			st.getChildren().add(insert(MyRandom.Int(InputField.MAX_VALUE + 1)).startEndTransition());
		}
		st.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				visualization.reLayout();
			}
		});
		st.jumpTo("end");
		return st;
	}
}
