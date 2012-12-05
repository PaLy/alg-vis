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

import algvis2.core.Visualization;
import algvis2.ds.dictionary.Dictionary;
import algvis2.scene.layout.Layout;
import algvis2.scene.paint.NodePaint;
import javafx.animation.Animation;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.HashMap;

public class BST extends Dictionary {
	public static final String DEF_LAYOUT = Layout.BIN_TREE_LAYOUT;
	public final ObjectProperty<BSTNode> rootProperty = new SimpleObjectProperty<BSTNode>();

	public BST(Visualization visualization) {
		super(visualization);
		layoutName = DEF_LAYOUT;
	}

	@Override
	public Animation[] find(int x) {
		BSTFind bstFind = new BSTFind(this, x);
		return new Animation[] { bstFind.runA(), bstFind.getBackToStart() };
	}

	@Override
	public void delete(int x) {
		// TODO
	}

	@Override
	public String getStats() {
		return null; // TODO
	}

	@Override
	public Animation[] insert(int x) {
		BSTInsert bstInsert = new BSTInsert(this, new BSTNode(x,
				NodePaint.INSERT, getLayout()));
		return new Animation[] { bstInsert.runA(), bstInsert.getBackToStart() };
	}

	public BSTNode getRoot() {
		return rootProperty.get();
	}

	public void setRoot(BSTNode root) {
		rootProperty.set(root);
	}

	protected void leftRot(BSTNode v) {
		final BSTNode u = v.getParentNode();
		if (v.getLeft() == null) {
			u.unlinkRight();
		} else {
			u.linkRight(v.getLeft());
		}
		if (u.isRoot()) {
			setRoot(v);
		} else {
			if (u.isLeft()) {
				u.getParentNode().linkLeft(v);
			} else {
				u.getParentNode().linkRight(v);
			}
		}
		v.linkLeft(u);
	}

	protected void rightRot(BSTNode v) {
		final BSTNode u = v.getParentNode();
		if (v.getRight() == null) {
			u.unlinkLeft();
		} else {
			u.linkLeft(v.getRight());
		}
		if (u.isRoot()) {
			setRoot(v);
		} else {
			if (u.isLeft()) {
				u.getParentNode().linkLeft(v);
			} else {
				u.getParentNode().linkRight(v);
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
	public final void setLayout(String layoutName) {
		super.setLayout(layoutName);
		if (getRoot() != null) {
			getRoot().setLayoutR(layoutName);
		}
		reLayout();
	}

	@Override
	public void reLayout() {
		if (getRoot() != null) {
			getRoot().reLayout();
			dsLayout.rebuild(getRoot().getLayoutPane());
		} else {
			dsLayout.rebuild();
		}
	}

	@Override
	public void storeState(HashMap<Object, Object> state) {
		state.put(rootProperty, rootProperty.get());
		if (rootProperty.get() != null)
			rootProperty.get().storeState(state);
	}
}
