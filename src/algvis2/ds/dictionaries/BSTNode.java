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

import algvis2.scene.paint.NodePaint;
import algvis2.scene.viselem.Edge;
import algvis2.scene.viselem.Node;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.ArrayList;
import java.util.HashMap;

class BSTNode extends Node {
	public final ObjectProperty<BSTNode> leftProperty = new SimpleObjectProperty<>();
	public final ObjectProperty<BSTNode> rightProperty = new SimpleObjectProperty<>();
	public final ObjectProperty<BSTNode> parentProperty = new SimpleObjectProperty<>();

	private Edge leftEdge = new Edge(Node.RADIUS);
	private Edge rightEdge = new Edge(Node.RADIUS);

	public BSTNode(int key) {
		super(key);
		init();
	}

	public BSTNode(int key, NodePaint p) {
		super(key, p);
		init();
	}

	@Deprecated
	public BSTNode(javafx.scene.Node node) {
		super(node);
	}

	private void init() {
		leftProperty.addListener(new ChangeListener<BSTNode>() {
			@Override
			public void changed(ObservableValue<? extends BSTNode> observableValue,
					BSTNode bstNode, BSTNode bstNode2) {
				if (bstNode2 != null)
					leftEdge.bindNodes(BSTNode.this, bstNode2);
			}
		});
		rightProperty.addListener(new ChangeListener<BSTNode>() {
			@Override
			public void changed(ObservableValue<? extends BSTNode> observableValue,
					BSTNode bstNode, BSTNode bstNode2) {
				if (bstNode2 != null)
					rightEdge.bindNodes(BSTNode.this, bstNode2);
			}
		});
	}

	public ArrayList<Edge> getEdges() {
		ArrayList<Edge> res = new ArrayList<>();
		if (getLeft() != null)
			res.add(leftEdge);
		if (getRight() != null)
			res.add(rightEdge);
		return res;
	}

	public void setLeft(BSTNode left) {
		if (left != null && left.getVisual().layoutXProperty().isBound())
			left.removePosBinding();
		leftProperty.set(left);
	}

	public void setRight(BSTNode right) {
		if (right != null && right.getVisual().layoutXProperty().isBound())
			right.removePosBinding();
		rightProperty.set(right);
	}

	public void setParent(BSTNode parent) {
		parentProperty.set(parent);
	}

	public BSTNode getLeft() {
		return leftProperty.get();
	}

	public BSTNode getRight() {
		return rightProperty.get();
	}

	public BSTNode getParent() {
		return parentProperty.get();
	}

	public boolean isRoot() {
		return getParent() == null;
	}

	public boolean isLeaf() {
		return getLeft() == null && getRight() == null;
	}

	public boolean isLeft() {
		return getParent() != null && getParent().getLeft() == this;
	}

	public boolean isRight() {
		return getParent() != null && getParent().getRight() == this;
	}

	/**
	 * removes edge between this and left; removes edge between newLeft and its parent; creates new
	 * edge between this and newLeft
	 */
	public void linkLeft(BSTNode newLeft) {
		if (getLeft() != newLeft) {
			if (getLeft() != null) {
				// remove edge between this and left
				unlinkLeft();
			}
			if (newLeft != null) {
				if (newLeft.getParent() != null) {
					// remove edge between newLeft and its parent
					newLeft.unlinkParent();
				}
				// create new edge between this and newLeft
				newLeft.setParent(this);
			}
			setLeft(newLeft);
		}
	}

	/**
	 * removes edge between this and left
	 */
	public void unlinkLeft() {
		getLeft().setParent(null);
		setLeft(null);
	}

	/**
	 * removes edge between this and right; removes edge between newRight and its parent; creates
	 * new edge between this and newRight
	 */
	public void linkRight(BSTNode newRight) {
		if (getRight() != newRight) {
			if (getRight() != null) {
				// remove edge between this and right
				unlinkRight();
			}
			if (newRight != null) {
				if (newRight.getParent() != null) {
					// remove edge between newRight and its parent
					newRight.unlinkParent();
				}
				// create new edge between this and newRight
				newRight.setParent(this);
			}
			setRight(newRight);
		}
	}

	/**
	 * removes edge between this and right
	 */
	public void unlinkRight() {
		getRight().setParent(null);
		setRight(null);
	}

	public void unlinkParent() {
		if (isLeft()) {
			getParent().unlinkLeft();
		} else {
			getParent().unlinkRight();
		}
	}

	@Override
	public void storeState(HashMap<Object, Object> state) {
		super.storeState(state);
		state.put(leftProperty, leftProperty.get());
		state.put(rightProperty, rightProperty.get());
		state.put(parentProperty, parentProperty.get());
		if (getLeft() != null)
			leftProperty.get().storeState(state);
		if (getRight() != null)
			rightProperty.get().storeState(state);
	}

	@Override
	public BSTNode clone() {
		BSTNode clone = new BSTNode(keyProperty.get(), getPaint());
		clone.setLeft(getLeft());
		clone.setRight(getRight());
		clone.setParent(getParent());

		clone.removeAutoTranslations();
		clone.goTo(this);
		clone.addAutoTranslations();

		return clone;
	}
}
