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

public class BSTVisualization extends DictVisualization {

	public BSTVisualization() {
		super(new BST());
	}

	BSTVisualization(BST bst) {
		super(bst);
	}

	@Override
	public void reLayout() {
		BSTCompactLayout.layout((BST) getDataStructure(), visPane);
		visPane.refresh();
	}

	@Override
	public String getTitle() {
		return "Binary search tree";
	}
}
