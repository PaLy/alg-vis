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

package algvis2.ds.persistent;

import algvis2.scene.paint.NodePaint;
import algvis2.scene.viselem.Node;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.shape.Line;

import java.util.*;

public class BinFatNode extends Node {
	private final ObjectProperty<TreeMap<Integer, BinFatNode>> leftChildrenProperty = new SimpleObjectProperty<>(
			new TreeMap<Integer, BinFatNode>());
	private final ObjectProperty<TreeMap<Integer, BinFatNode>> rightChildrenProperty = new SimpleObjectProperty<>(
			new TreeMap<Integer, BinFatNode>());
	private final ObjectProperty<BinFatNode> parentProperty = new SimpleObjectProperty<>();

	public BinFatNode(int key, NodePaint paint) {
		super(key, paint);
	}

	public BinFatNode(int key) {
		super(key);
	}

	private void addChild(BinFatNode child, int version, Map<Integer, BinFatNode> children) {
		if (child.getVisual().layoutXProperty().isBound()) {
			child.removePosBinding();
		}
		children.put(version, child);
	}

	public void addLeftChild(BinFatNode child, int version) {
		addChild(child, version, leftChildrenProperty.get());
	}

	public void addRightChild(BinFatNode child, int version) {
		addChild(child, version, rightChildrenProperty.get());
	}

	public TreeMap<Integer, BinFatNode> getLeftChildren() {
		return leftChildrenProperty.get();
	}

	public BinFatNode getLastLeftChild() {
		return getLeftChildren().lastEntry().getValue();
	}

	public TreeMap<Integer, BinFatNode> getRightChildren() {
		return rightChildrenProperty.get();
	}

	public BinFatNode getFirstRightChild() {
		return getRightChildren().firstEntry().getValue();
	}

	BinFatNode getLeftChild(int version) {
		return getLeftChild(version, false);
	}

	BinFatNode getLeftChild(int version, boolean returnNullNodes) {
		return getChild(version, getLeftChildren().entrySet(), returnNullNodes);
	}

	BinFatNode getRightChild(int version) {
		return getRightChild(version, false);
	}

	BinFatNode getRightChild(int version, boolean returnNullNodes) {
		return getChild(version, getRightChildren().entrySet(), returnNullNodes);
	}

	private BinFatNode getChild(int version, Set<Map.Entry<Integer, BinFatNode>> children,
			boolean returnNullNodes) {
		int latestVersion = -1;
		BinFatNode res = null;
		for (Map.Entry<Integer, BinFatNode> entry : children) {
			if (entry.getKey() > latestVersion && entry.getKey() <= version) {
				latestVersion = entry.getKey();
				res = entry.getValue();
			}
		}
		if (res instanceof BinFatNode.Null && !returnNullNodes) {
			res = null;
		}
		return res;
	}

	boolean isLeaf(int version) {
		return getLeftChild(version) == null && getRightChild(version) == null;
	}

	@Override
	public void storeState(HashMap<Object, Object> state) {
		super.storeState(state);
		state.put(leftChildrenProperty, leftChildrenProperty.getValue().entrySet().toArray());
		state.put(rightChildrenProperty, rightChildrenProperty.getValue().entrySet().toArray());
		state.put(parentProperty, parentProperty.getValue());
		for (BinFatNode child : leftChildrenProperty.get().values()) {
			child.storeState(state);
		}
		for (BinFatNode child : rightChildrenProperty.get().values()) {
			child.storeState(state);
		}
	}

	@Override
	public String toString() {
		return getKey() + ": " + Arrays.toString(getLeftChildren().values().toArray()) + " & "
				+ Arrays.toString(getRightChildren().values().toArray());
	}

	public void setParent(BinFatNode parent) {
		parentProperty.set(parent);
	}

	public BinFatNode getParent() {
		return parentProperty.get();
	}

	public boolean isRoot() {
		return getParent() == null;
	}

	public boolean isLeft() {
		BinFatNode parent = getParent();
		if (parent == null) {
			return false;
		} else {
			for (BinFatNode node : parent.getLeftChildren().values()) {
				if (node == this) {
					return true;
				}
			}
			return false;
		}
	}

	public static final class Null extends BinFatNode {
		public Null() {
			super(-1);
			getVisual().getChildren().set(0, new Line(-Node.RADIUS / 2, 0, Node.RADIUS / 2, 0));
			getVisual().getChildren().get(1).setVisible(false);
		}

		@Override
		public String toString() {
			return "nullNode";
		}
	}
}
