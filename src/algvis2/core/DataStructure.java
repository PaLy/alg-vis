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

import algvis.core.MyRandom;
import algvis2.scene.control.InputField;
import algvis2.scene.layout.VisPane;
import javafx.animation.Animation;
import javafx.animation.ParallelTransition;

public abstract class DataStructure {
	protected final VisPane visPane;
	protected String layoutName;

	protected DataStructure(VisPane visPane) {
		this.visPane = visPane;
	}

	abstract public String getStats();

	abstract public Animation insert(int x);

	abstract public void clear();

	public Animation random(int n) {
		ParallelTransition pt = new ParallelTransition();
		for (int i = 0; i < n; i++)
			pt.getChildren().add(insert(MyRandom.Int(InputField.MAX_VALUE + 1)));
		return pt;
	}
	
	public void setLayout(String layoutName) {
		this.layoutName = layoutName;
	}
}
