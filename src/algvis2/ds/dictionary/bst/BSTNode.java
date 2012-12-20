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

import algvis2.scene.paint.NodePaint;
import algvis2.scene.shape.Node;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.HashMap;

public class BSTNode extends Node {
	public final ObjectProperty<BSTNode> leftProperty = new SimpleObjectProperty<BSTNode>();
	public final ObjectProperty<BSTNode> rightProperty = new SimpleObjectProperty<BSTNode>();
	public final ObjectProperty<BSTNode> parentNodeProperty = new SimpleObjectProperty<BSTNode>();

	public BSTNode(int key) {
		super(key);
	}

	public BSTNode(int key, NodePaint p) {
		super(key, p);
	}

	public BSTNode(Node v) {
		super(v);
	}

	public void setLeft(BSTNode left) {
		if (left != null && left.layoutXProperty().isBound()) left.removeLayoutXYBindings();
		leftProperty.set(left);
	}

	public void setRight(BSTNode right) {
		if (right != null && right.layoutXProperty().isBound()) right.removeLayoutXYBindings();
		rightProperty.set(right);
	}

	public void setParentNode(BSTNode parent) {
		parentNodeProperty.set(parent);
	}

	public BSTNode getLeft() {
		return leftProperty.get();
	}

	public BSTNode getRight() {
		return rightProperty.get();
	}

	public BSTNode getParentNode() {
		return parentNodeProperty.get();
	}

	public boolean isRoot() {
		return getParentNode() == null;
	}
	
	public boolean isLeaf() {
		return getLeft() == null && getRight() == null;
	}

	public boolean isLeft() {
		return getParentNode() != null && getParentNode().getLeft() == this;
	}

	public boolean isRight() {
		return getParentNode() != null && getParentNode().getRight() == this;
	}

	/**
	 * removes edge between this and left; removes edge between newLeft and its
	 * parent; creates new edge between this and newLeft
	 */
	public void linkLeft(BSTNode newLeft) {
		if (getLeft() != newLeft) {
			if (getLeft() != null) {
				// remove edge between this and left
				unlinkLeft();
			}
			if (newLeft != null) {
				if (newLeft.getParentNode() != null) {
					// remove edge between newLeft and its parent
					newLeft.unlinkParent();
				}
				// create new edge between this and newLeft
				newLeft.setParentNode(this);
			}
			setLeft(newLeft);
		}
	}

	/**
	 * removes edge between this and left
	 */
	public void unlinkLeft() {
		getLeft().setParentNode(null);
		setLeft(null);
	}

	/**
	 * removes edge between this and right; removes edge between newRight and
	 * its parent; creates new edge between this and newRight
	 */
	public void linkRight(BSTNode newRight) {
		if (getRight() != newRight) {
			if (getRight() != null) {
				// remove edge between this and right
				unlinkRight();
			}
			if (newRight != null) {
				if (newRight.getParentNode() != null) {
					// remove edge between newRight and its parent
					newRight.unlinkParent();
				}
				// create new edge between this and newRight
				newRight.setParentNode(this);
			}
			setRight(newRight);
		}
	}

	/**
	 * removes edge between this and right
	 */
	public void unlinkRight() {
		getRight().setParentNode(null);
		setRight(null);
	}

	private void unlinkParent() {
		if (isLeft()) {
			getParentNode().unlinkLeft();
		} else {
			getParentNode().unlinkRight();
		}
	}

	@Override
	public void storeState(HashMap<Object, Object> state) {
		super.storeState(state);
		state.put(leftProperty, leftProperty.get());
		state.put(rightProperty, rightProperty.get());
		state.put(parentNodeProperty, parentNodeProperty.get());
		if (leftProperty.get() != null)
			leftProperty.get().storeState(state);
		if (rightProperty.get() != null)
			rightProperty.get().storeState(state);
	}
}
