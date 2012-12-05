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

import algvis2.scene.layout.Layout;
import algvis2.scene.paint.NodePaint;
import algvis2.scene.shape.Node;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class BSTNode extends Node {
	protected BinTreeLayout layout;
	public final ObjectProperty<BSTNode> leftProperty = new SimpleObjectProperty<BSTNode>();
	public final ObjectProperty<BSTNode> rightProperty = new SimpleObjectProperty<BSTNode>();
	public final ObjectProperty<BSTNode> parentNodeProperty = new SimpleObjectProperty<BSTNode>();

	public BSTNode(int key, String layoutName) {
		super(key);
		layout = (BinTreeLayout) Layout.createLayout(layoutName);
	}

	public BSTNode(int key, NodePaint p, String layoutName) {
		super(key, p);
		layout = (BinTreeLayout) Layout.createLayout(layoutName);
	}

	public BSTNode(Node v, String layoutName) {
		super(v);
		layout = (BinTreeLayout) Layout.createLayout(layoutName);
	}

	public void setLeft(BSTNode left) {
		leftProperty.set(left);
	}

	public void setRight(BSTNode right) {
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

	public Pane getLayoutPane() {
		return layout.getPane();
	}

	public void setLayoutR(String layoutName) {
		layout = (BinTreeLayout) Layout.createLayout(layoutName);
		if (getLeft() != null)
			getLeft().setLayoutR(layoutName);
		if (getRight() != null)
			getRight().setLayoutR(layoutName);
	}

	public void reLayout() {
		layout.rebuild(this, getLeft(), getRight());
	}

	@Override
	public void recalcAbsPosition() {
		recalcAbsPositionR();
	}

	public void recalcAbsPositionR() {
		super.recalcAbsPosition();
		if (getLeft() != null)
			getLeft().recalcAbsPositionR();
		if (getRight() != null)
			getRight().recalcAbsPositionR();
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
