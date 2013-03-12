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
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;

public class PC_PBSTNode extends Node {
	public final ObjectProperty<PC_PBSTNode> leftProperty = new SimpleObjectProperty<>();
	public final ObjectProperty<PC_PBSTNode> rightProperty = new SimpleObjectProperty<>();
	public final SimpleListProperty<PC_PBSTNode> parentProperty = new SimpleListProperty<>(
			FXCollections.<PC_PBSTNode> observableArrayList());

	public PC_PBSTNode(int key) {
		super(key);
	}

	public PC_PBSTNode(int key, NodePaint paint) {
		super(key, paint);
	}

	@Override
	public PC_PBSTNode clone() {
		PC_PBSTNode clone = new PC_PBSTNode(keyProperty.get(), getPaint());
		if (getLeft() != null) {
			clone.setLeft(getLeft());
		}
		if (getRight() != null) {
			clone.setRight(getRight());
		}

		clone.goTo(this);
		return clone;
	}

	void setRight(PC_PBSTNode right) {
		if (right != null) {
			right.removePosBinding();
		}
		rightProperty.setValue(right);
	}

	void setLeft(PC_PBSTNode left) {
		if (left != null) {
			left.removePosBinding();
		}
		leftProperty.setValue(left);
	}

	PC_PBSTNode getRight() {
		return rightProperty.get();
	}

	PC_PBSTNode getLeft() {
		return leftProperty.get();
	}

	public void addParent(PC_PBSTNode parent) {
		parentProperty.add(parent);
	}

	public PC_PBSTNode getParent(int i) {
		if (i < 0 || i > parentProperty.size() - 1) {
			return null;
		} else {
			return parentProperty.get(i);
		}
	}

	public PC_PBSTNode getLastParent() {
		if (parentProperty.isEmpty()) {
			return null;
		} else {
			return parentProperty.get(parentProperty.size() - 1);
		}
	}

	@Override
	public void storeState(HashMap<Object, Object> state) {
		super.storeState(state);
		state.put(leftProperty, leftProperty.getValue());
		state.put(rightProperty, rightProperty.getValue());
		state.put(parentProperty, parentProperty.getValue().toArray());
		if (getLeft() != null)
			leftProperty.get().storeState(state);
		if (getRight() != null)
			rightProperty.get().storeState(state);
	}

	@Override
	public String toString() {
		String res = String.valueOf(getKey());
		if (getLeft() != null) {
			res += " " + getLeft().getKey();
		}
		if (getRight() != null) {
			res += " " + getRight().getKey();
		}
		res += " " + parentProperty.size() + "\n";
		if (getLeft() != null) {
			res += getLeft().toString();
		}
		if (getRight() != null) {
			res += getRight().toString();
		}
		return res;
	}

	public static class FakeRoot extends PC_PBSTNode {
		private final SimpleListProperty<PC_PBSTNode> children = new SimpleListProperty<>(
				FXCollections.<PC_PBSTNode> observableArrayList());

		public FakeRoot(int key) {
			super(key);
			removePosBinding();
		}

		public void addChild(PC_PBSTNode child) {
			children.add(child);
		}

		public ObservableList<PC_PBSTNode> getChildren() {
			return children.get();
		}

		@Override
		public void storeState(HashMap<Object, Object> state) {
			super.storeState(state);
			state.put(children, children.getValue().toArray());
			for (Node node : getChildren()) {
				node.storeState(state);
			}
		}
	}
}
