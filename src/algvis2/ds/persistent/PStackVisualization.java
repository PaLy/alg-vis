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

import algvis2.core.Algorithm;
import algvis2.core.DataStructure;

public class PStackVisualization extends PersistentVisualization {
	public PStackVisualization() {
		super(PStackVisualization.class.getResource("/algvis2/ds/persistent/PStackButtons.fxml"));
	}

	@Override
	public String getTitle() {
		return "Fully Persistent Stack";
	}

	@Override
	protected DataStructure initDS() {
		return new PStack(this);
	}

	@Override
	public void reLayout() {
		PStackLayout.layout(getDataStructure(), visPane);
		visPane.refresh();
	}

	@Override
	public PStack getDataStructure() {
		return (PStack) super.getDataStructure();
	}

	public void push(int x, int version) {
		visPane.disableVisualsRefresh();
		Algorithm algorithm = getDataStructure().push(x, version);
		addAndPlay(algorithm);
	}

	public boolean pop(int version) {
		visPane.disableVisualsRefresh();
		Algorithm algorithm = getDataStructure().pop(version);
		if (algorithm != null) {
			addAndPlay(algorithm);
			return true;
		} else {
			return false;
		}
	}
}
