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
import algvis2.scene.layout.AbstractBinTreeForTreeLayout;
import algvis2.scene.paint.NodePaint;
import algvis2.scene.viselem.VisElem;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class BST extends Dictionary {
	public final ObjectProperty<BSTNode> rootProperty = new SimpleObjectProperty<>();

	BST() {
		super();
	}

	@Override
	public Algorithm find(Visualization visualization, int x) {
		BSTFind bstFind = new BSTFind(visualization, this, x);
		bstFind.run();
		return bstFind;
	}

	@Override
	public Algorithm delete(Visualization visualization, int x) {
		BSTFind bstDelete = new BSTDelete(visualization, this, x);
		bstDelete.run();
		return bstDelete;
	}

	@Override
	public Algorithm insert(Visualization visualization, int x) {
		BSTInsert bstInsert = new BSTInsert(visualization, this, new BSTNode(x, NodePaint.INSERT));
		bstInsert.run();
		return bstInsert;
	}

	public BSTNode getRoot() {
		return rootProperty.get();
	}

	public void setRoot(BSTNode root) {
		rootProperty.set(root);
		if (root != null)
			root.removePosBinding();
	}

	protected void leftRot(BSTNode v) {
		final BSTNode u = v.getParent();
		if (v.getLeft() == null) {
			u.unlinkRight();
		} else {
			u.linkRight(v.getLeft());
		}
		if (u.isRoot()) {
			setRoot(v);
		} else {
			if (u.isLeft()) {
				u.getParent().linkLeft(v);
			} else {
				u.getParent().linkRight(v);
			}
		}
		v.linkLeft(u);
	}

	protected void rightRot(BSTNode v) {
		final BSTNode u = v.getParent();
		if (v.getRight() == null) {
			u.unlinkLeft();
		} else {
			u.linkLeft(v.getRight());
		}
		if (u.isRoot()) {
			setRoot(v);
		} else {
			if (u.isLeft()) {
				u.getParent().linkLeft(v);
			} else {
				u.getParent().linkRight(v);
			}
		}
		v.linkRight(u);
	}

	/**
	 * Rotation is specified by a single vertex v; if v is a left child of its parent, rotate right,
	 * if it is a right child, rotate left
	 */
	public void rotate(BSTNode v) {
		if (v.isLeft()) {
			rightRot(v);
		} else {
			leftRot(v);
		}
	}

	@Override
	public void clear() {
		setRoot(null);
	}

	@Override
	public List<VisElem> dump() {
		List<VisElem> elements = new ArrayList<>();

		if (getRoot() != null) {
			java.util.Stack<BSTNode> todo = new java.util.Stack<>();
			todo.push(getRoot());
			elements.add(getRoot());

			while (!todo.empty()) {
				BSTNode elem = todo.pop();
				if (elem.getLeft() != null) {
					todo.add(elem.getLeft());
					elements.add(elem.getLeft());
				}
				if (elem.getRight() != null) {
					todo.add(elem.getRight());
					elements.add(elem.getRight());
				}
			}
		}

		return elements;
	}

	@Override
	public void storeState(HashMap<Object, Object> state) {
		state.put(rootProperty, rootProperty.get());
		if (rootProperty.get() != null)
			rootProperty.get().storeState(state);
	}

	@Override
	public void recalcAbsPosition() {
		if (getRoot() != null)
			recalcAbsPositionR(getRoot());
	}

	private void recalcAbsPositionR(BSTNode node) {
		node.recalcAbsPosition();
		if (node.getLeft() != null)
			recalcAbsPositionR(node.getLeft());
		if (node.getRight() != null)
			recalcAbsPositionR(node.getRight());
	}

	public final AbstractBinTreeForTreeLayout<BSTNode> treeForTreeLayout = new AbstractBinTreeForTreeLayout<BSTNode>(
			null) {
		@Override
		public BSTNode getRoot() {
			return rootProperty.get();
		}

		@Override
		public BSTNode getParent(BSTNode node) {
			return node.getParent();
		}

		@Override
		public List<BSTNode> getChildrenList(BSTNode node) {
			List<BSTNode> res = new ArrayList<>();
			if (node.getLeft() != null)
				res.add(node.getLeft());
			if (node.getRight() != null)
				res.add(node.getRight());

			return res;
		}

		@Override
		public BSTNode getLastLeftChild(BSTNode parentNode) {
			return parentNode.getLeft();
		}

		@Override
		public BSTNode getFirstRightChild(BSTNode parentNode) {
			return parentNode.getRight();
		}
	};
}
