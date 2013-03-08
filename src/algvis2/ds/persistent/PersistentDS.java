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

import algvis2.core.DataStructure;
import algvis2.scene.viselem.Node;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.HashMap;
import java.util.List;

abstract public class PersistentDS extends DataStructure {
	final IntegerProperty versionsCountProperty = new SimpleIntegerProperty(0);
	
	abstract List<Node> dumpVersion(int version);

	int getVersionsCount() {
		return versionsCountProperty.get();
	}

	void incVersionsCount() {
		versionsCountProperty.set(versionsCountProperty.get() + 1);
	}

	@Override
	public void storeState(HashMap<Object, Object> state) {
		state.put(versionsCountProperty, versionsCountProperty.getValue());
	}
}
