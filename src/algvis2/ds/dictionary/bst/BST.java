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

package algvis2.ds.dictionary.bst;

import algvis2.core.Algorithm;
import algvis2.core.Visualization;
import algvis2.ds.dictionary.Dictionary;
import algvis2.scene.paint.NodePaint;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.abego.treelayout.TreeForTreeLayout;

import java.util.ArrayList;
import java.util.HashMap;

public class BST extends Dictionary implements TreeForTreeLayout<BSTNode> {
	public final ObjectProperty<BSTNode> rootProperty = new SimpleObjectProperty<BSTNode>();

	public BST(Visualization visualization) {
		super(visualization);
	}

	@Override
	public Algorithm find(int x) {
		BSTFind bstFind = new BSTFind(this, x);
		bstFind.run();
		return bstFind;
	}

	@Override
	public Algorithm delete(int x) {
		BSTFind bstDelete = new BSTDelete(this, x);
		bstDelete.run();
		return bstDelete;
	}

	@Override
	public String getStats() {
		return null; // TODO
	}

	@Override
	public Algorithm insert(int x) {
		BSTInsert bstInsert = new BSTInsert(this, new BSTNode(x,
				NodePaint.INSERT));
		bstInsert.run();
		return bstInsert;
	}

	public BSTNode getRoot() {
		return rootProperty.get();
	}

	@Override
	public boolean isLeaf(BSTNode bstNode) {
		return bstNode.getLeft() == null && bstNode.getRight() == null;
	}

	@Override
	public boolean isChildOfParent(BSTNode bstNode, BSTNode parentNode) {
		return parentNode.getLeft() == bstNode || parentNode.getRight() == bstNode;
	}

	@Override
	public Iterable<BSTNode> getChildren(BSTNode parentNode) {
		ArrayList<BSTNode> list = new ArrayList<BSTNode>();
		if (parentNode.getLeft() != null) list.add(parentNode.getLeft());
		if (parentNode.getRight() != null) list.add(parentNode.getRight());
		return list;
	}

	@Override
	public Iterable<BSTNode> getChildrenReverse(BSTNode parentNode) {
		ArrayList<BSTNode> list = new ArrayList<BSTNode>();
		if (parentNode.getRight() != null) list.add(parentNode.getRight());
		if (parentNode.getLeft() != null) list.add(parentNode.getLeft());
		return list;
	}

	@Override
	public BSTNode getFirstChild(BSTNode parentNode) {
		if (parentNode.getLeft() != null) return parentNode.getLeft();
		else if (parentNode.getRight() != null) return parentNode.getRight();
		else return null;
	}

	@Override
	public BSTNode getLastChild(BSTNode parentNode) {
		if (parentNode.getRight() != null) return parentNode.getRight();
		else if (parentNode.getLeft() != null) return parentNode.getLeft();
		else return null;
	}

	@Override
	public boolean isLeft(BSTNode node) {
		return node.isLeft();
	}

	@Override
	public boolean isBinaryTree() {
		return true;
	}

	public void setRoot(BSTNode root) {
		rootProperty.set(root);
		if (root != null) root.removeLayoutXYBindings();
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
	 * Rotation is specified by a single vertex v; if v is a left child of its
	 * parent, rotate right, if it is a right child, rotate left
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
	public void storeState(HashMap<Object, Object> state) {
		state.put(rootProperty, rootProperty.get());
		if (rootProperty.get() != null)
			rootProperty.get().storeState(state);
	}

	@Override
	public void recalcAbsPosition() {
		if (getRoot() != null) recalcAbsPositionR(getRoot());
	}
	
	private void recalcAbsPositionR(BSTNode node) {
		node.recalcAbsPosition();
		if (node.getLeft() != null) recalcAbsPositionR(node.getLeft());
		if (node.getRight() != null) recalcAbsPositionR(node.getRight());
	}
}
