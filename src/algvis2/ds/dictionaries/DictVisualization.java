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

package algvis2.ds.dictionaries;

import algvis2.core.Algorithm;
import algvis2.core.Visualization;

abstract class DictVisualization extends Visualization {
	public DictVisualization() {
		super(DictVisualization.class.getResource("/algvis2/ds/dictionaries/DictButtons.fxml"));
	}

	@Override
	public Dictionary getDataStructure() {
		return (Dictionary) super.getDataStructure();
	}

	public void insert(int x) {
		visPane.disableVisualsRefresh();
		Algorithm algorithm = getDataStructure().insert(this, x);
		addAndPlay(algorithm);
	}

	public void delete(int x) {
		visPane.disableVisualsRefresh();
		Algorithm algorithm = getDataStructure().delete(this, x);
		addAndPlay(algorithm);
	}

	public void find(int x) {
		visPane.disableVisualsRefresh();
		Algorithm algorithm = getDataStructure().find(this, x);
		addAndPlay(algorithm);
	}
}
