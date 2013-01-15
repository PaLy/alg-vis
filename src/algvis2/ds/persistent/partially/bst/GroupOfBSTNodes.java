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

import algvis2.ds.dictionary.bst.BSTNode;
import algvis2.scene.viselem.Edge;
import algvis2.scene.viselem.Node;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupOfBSTNodes extends BSTNode {
	private final ObjectProperty<List<BSTNode>> nodes = new SimpleObjectProperty<List<BSTNode>>();
	private int lastTime;

	public GroupOfBSTNodes(BSTNode node, int time) {
		super(new HBox(5));
		nodes.set(new ArrayList<BSTNode>());
		nodes.get().add(node);
		node.removeLayoutXYBindings();
		
		node.setTopText(Integer.toString(time));
		lastTime = 0;
//		getVisual().getChildren().add(node.getVisual()); // TODO nie
	}

	@Override
	public HBox getVisual() {
		return (HBox) super.getVisual();
	}
	
	public List<BSTNode> getNodes() {
		return nodes.get();
	}

	@Override
	public int getKey() {
		return nodes.get().get(0).getKey();
	}

	public void addNode(int time) {
		BSTNode lastNode = nodes.get().get(nodes.get().size() - 1);
		BSTNode newNode = (BSTNode) lastNode.clone();
		nodes.get().add(newNode);
		newNode.removeLayoutXYBindings();
		
		newNode.setTopText(Integer.toString(time));
		lastTime = time;
//		getVisual().getChildren().add(newNode.getVisual()); // TODO nie
	}

	@Override
	public ArrayList<Edge> getEdges() {
		ArrayList<Edge> res = new ArrayList<Edge>();
		for (BSTNode node : nodes.get()) {
			res.addAll(node.getEdges());
		}
		return res;
	}

	@Override
	public GroupOfBSTNodes getLeft() {
		return (GroupOfBSTNodes) super.getLeft();
	}

	@Override
	public void setLeft(BSTNode left) {
		if (leftProperty.get() == null && left != null) {
			if (left.getVisual().layoutXProperty().isBound()) left.removeLayoutXYBindings();
			GroupOfBSTNodes leftGroup = new GroupOfBSTNodes(left, lastTime);
			leftGroup.setParent(this);
			leftProperty.set(leftGroup);
			
//			getLastNode().linkLeft(left); // TODO toho sa uz treba konecne zbavit
			getLastNode().setLeft(left);
		} else {
			// TODO
		}
	}

	@Override
	public GroupOfBSTNodes getRight() {
		return (GroupOfBSTNodes) super.getRight();
	}

	@Override
	public void setRight(BSTNode right) {
		if (rightProperty.get() == null && right != null) {
			if (right.getVisual().layoutXProperty().isBound()) right.removeLayoutXYBindings();
			GroupOfBSTNodes rightGroup = new GroupOfBSTNodes(right, lastTime);
			rightGroup.setParent(this);
			rightProperty.set(rightGroup);
			
//			getLastNode().linkRight(right);
			getLastNode().setRight(right);
		} else {
			// TODO
		}
	}

	@Override
	public GroupOfBSTNodes getParent() {
		return (GroupOfBSTNodes) super.getParent();
	}

	public BSTNode getLastNode() {
		return nodes.get().get(nodes.get().size() - 1);
	}

	@Override
	public void recalcAbsPosition() {
		super.recalcAbsPosition();
		for (Node node : nodes.get()) {
			node.recalcAbsPosition();
		}
	}

	@Override
	public void storeState(HashMap<Object, Object> state) {
		super.storeState(state);
		
		state.put(nodes, nodes.get().toArray());
		for (Node node : nodes.get()) {
			node.storeState(state);
		}
	}
}
