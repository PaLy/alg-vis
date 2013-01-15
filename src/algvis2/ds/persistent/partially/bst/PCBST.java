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

package algvis2.ds.persistent.partially.bst;

import algvis2.core.Algorithm;
import algvis2.core.Visualization;
import algvis2.ds.dictionary.bst.BST;
import algvis2.ds.dictionary.bst.BSTNode;
import algvis2.scene.paint.NodePaint;

/**
 * Partially persistent binary search tree with path copying
 */
public class PCBST extends BST {
	int time = -1;
	
	public PCBST(Visualization visualization) {
		super(visualization);
	}

	@Override
	public void clear() {
		super.clear();
		time = -1;
	}

	@Override
	public GroupOfBSTNodes getRoot() {
		return (GroupOfBSTNodes) super.getRoot();
	}

	@Override
	public void setRoot(BSTNode root) {
		if (rootProperty.get() == null) {
			if (root != null) {
				rootProperty.set(new GroupOfBSTNodes(root, 0));
				root.removeLayoutXYBindings();
			}
		} else {
			if (root == null) {
				rootProperty.set(null);
			} else {
				// TODO
			}
		}
	}

	@Override
	public Algorithm insert(int x) {
		Insert insert = new Insert(this, new BSTNode(x,
				NodePaint.INSERT));
		insert.run();
		return insert;
	}
}
