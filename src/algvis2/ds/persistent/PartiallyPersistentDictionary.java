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

package algvis2.ds.persistent;

import algvis.core.MyRandom;
import algvis2.core.Algorithm;
import algvis2.core.Visualization;
import algvis2.scene.control.InputField;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;

public abstract class PartiallyPersistentDictionary extends PersistentDS {
	protected PartiallyPersistentDictionary() {
		super();
	}

	abstract public Algorithm insert(Visualization visualization, int x);

	abstract public Algorithm find(Visualization visualization, int version, int x);

	abstract public Algorithm delete(Visualization visualization, int x);

	@Override
	public Animation random(Visualization visualization, int n) {// TODO random creates one version?
		SequentialTransition st = new SequentialTransition();
		for (int i = 0; i < n; i++) {
			Animation animation = insert(visualization, MyRandom.Int(InputField.MAX_VALUE + 1))
					.startEndTransition();
			st.getChildren().add(animation);
		}
		return st;
	}
}
