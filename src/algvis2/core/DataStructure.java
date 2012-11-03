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
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.FlowPane;

public abstract class DataStructure {
	protected final VisPane visPane;
	protected final FlowPane[] visPaneLayers;

	protected DataStructure(VisPane visPane) {
		this.visPane = visPane;
		ObservableList<Node> ch = visPane.getChildren();
		visPaneLayers = visPane.getChildren().toArray(new FlowPane[ch.size()]);
	}


	abstract public String getStats();

	abstract public void insert(int x);

	abstract public void clear();

	public void random(int n) {
		for (int i = 0; i < n; i++)
			insert(MyRandom.Int(InputField.MAX_VALUE + 1));
	}
}