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

package algvis2.ds.dictionary.rb;

import algvis2.core.Algorithm;
import algvis2.core.Visualization;
import algvis2.ds.dictionary.bst.BST;
import algvis2.scene.viselem.Node;

public class RB extends BST {
	public static final RBNode NULL = new RBNode(Node.NULL);

	public RB(Visualization visualization) {
		super(visualization);
		NULL.setParent(NULL);
		NULL.setRight(NULL);
		NULL.setLeft(NULL);
		NULL.setRed(false);
	}

	@Override
	public Algorithm insert(int x) {
		RBInsert rbInsert = new RBInsert(this, new RBNode(x));
		rbInsert.run();
		return rbInsert;
	}

	@Override
	public Algorithm delete(int x) {
		RBDelete rbDelete = new RBDelete(this, x);
		rbDelete.run();
		return rbDelete;
	}

	@Override
	public RBNode getRoot() {
		return (RBNode) super.getRoot();
	}
}
