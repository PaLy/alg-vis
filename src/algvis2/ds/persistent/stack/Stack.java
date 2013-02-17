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

package algvis2.ds.persistent.stack;

import algvis.core.MyRandom;
import algvis2.core.Algorithm;
import algvis2.core.DataStructure;
import algvis2.core.Visualization;
import algvis2.scene.control.InputField;
import algvis2.scene.viselem.Node;
import algvis2.scene.viselem.VisElem;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import org.abego.treelayout.TreeForTreeLayout;

import java.util.*;

public class Stack extends DataStructure {
	final SimpleListProperty<StackNode.NullNode> versions = new SimpleListProperty<StackNode.NullNode>(FXCollections
			.<StackNode.NullNode>observableArrayList());
	private final StackNode bottom = new StackNode.NullNode(-1, null);

	protected Stack(Visualization visualization) {
		super(visualization);
		StackNode.NullNode emptyVersion = new StackNode.NullNode(0, bottom);
		bottom.removePosBinding();
		emptyVersion.removePosBinding();
		
		versions.add(emptyVersion);
	}

	@Override
	public String getStats() {
		return null; // TODO
	}

	public Algorithm push(int x, int version) {
		if (version < 0 || version >= versions.size()) {
			version = versions.size() - 1;
		}
		StackPush stackPush = new StackPush(this, x, version);
		stackPush.run();
		return stackPush;
	}
	
	public Algorithm push(int x) {
		return push(x, versions.size() - 1); // last version
	}

	@Override
	public Animation random(int n) {
		SequentialTransition st = new SequentialTransition();
		for (int i = 0; i < n; i++) {
			st.getChildren().add(push(MyRandom.Int(InputField.MAX_VALUE + 1),
					MyRandom.Int(versions.size())).startEndTransition());
		}
		st.setOnFinished(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				visualization.reLayout();
			}
		});
		st.jumpTo("end");
		return st;
	}

	@Override
	public List<VisElem> dump() {
		List<VisElem> elements = new ArrayList<VisElem>();
		
		java.util.Stack<StackNode> todo = new java.util.Stack<StackNode>();
		todo.add(bottom);
		elements.add(bottom);
		
		while (!todo.empty()) {
			StackNode elem = todo.pop();
			for (Map.Entry<StackNode, Boolean> entry : elem.parentNodes().entrySet()) {
				if (entry.getValue()) {
					if (!(entry.getKey() instanceof StackNode.NullNode)) {
						todo.add(entry.getKey());
					}
					elements.add(entry.getKey());
				}
			}		
		}
		
		return elements;
	}

	@Override
	public void clear() {
		versions.remove(1, versions.size());
		bottom.parentNodes().clear();
		bottom.parentNodes().put(versions.get(0), true);
	}

	public Algorithm pop(int version) {
		if (version < 0 || version >= versions.size()) {
			version = versions.size() - 1; // last version
		}
		if (versions.get(version).nextNode.nextNode != null) {
			StackPop stackPop = new StackPop(this, version);
			stackPop.run();
			return stackPop;
		} else {
			return null;
		}
	}

	@Override
	public void recalcAbsPosition() {
		recalcAbsPositionR(bottom);
	}

	private void recalcAbsPositionR(Node node) {
		node.recalcAbsPosition();
		if (node instanceof StackNode) {
			for (Map.Entry<StackNode, Boolean> entry : ((StackNode) node).parentNodes().entrySet()) {
				if (entry.getValue()) {
					recalcAbsPositionR(entry.getKey());
				}
			}
		}
	}

	@Override
	public void storeState(HashMap<Object, Object> state) {
		if (versions.getValue() == null) {
			state.put(versions, null);
		} else {
			state.put(versions, versions.getValue().toArray());
		}
		bottom.storeState(state);
	}


	public final StackTree stackTree = new StackTree();

	public class StackTree implements TreeForTreeLayout<Node> {
		@Override
		public StackNode getRoot() {
			return bottom;
		}

		@Override
		public boolean isLeaf(Node node) {
			return node instanceof StackNode && ((StackNode) node).parentNodes().size() == 0;
		}

		@Override
		public boolean isChildOfParent(Node node, Node parentNode) {
			if (parentNode instanceof StackNode) {
				Boolean is = ((StackNode) parentNode).parentNodes().get(node);
				return is == null ? false : is;
			} else {
				return false;
			}
		}

		@Override
		public Iterable<Node> getChildren(Node parentNode) {
			if (parentNode instanceof StackNode) {
				List<Node> res = new LinkedList<Node>();
				for (Map.Entry<StackNode, Boolean> entry : ((StackNode) parentNode).parentNodes().entrySet()) {
					if (entry.getValue()) {
						res.add(entry.getKey());
					}
				}
				return res;
			} else {
				return null;
			}
		}

		@Override
		public Iterable<Node> getChildrenReverse(Node parentNode) {
			if (parentNode instanceof StackNode) {
				LinkedList<Node> res = new LinkedList<Node>();
				for (Map.Entry<StackNode, Boolean> entry : ((StackNode) parentNode).parentNodes().entrySet()) {
					if (entry.getValue()) {
						res.addFirst(entry.getKey());
					}
				}
				return res;
			} else {
				return null;
			}
		}

		@Override
		public Node getFirstChild(Node parentNode) {
			if (parentNode instanceof StackNode) {
				for (Map.Entry<StackNode, Boolean> entry : ((StackNode) parentNode).parentNodes().entrySet()) {
					if (entry.getValue()) {
						return entry.getKey();
					}
				}
			}
			return null;
		}

		@Override
		public Node getLastChild(Node parentNode) {
			if (parentNode instanceof StackNode) {
				Node res = null;
				for (Map.Entry<StackNode, Boolean> entry : ((StackNode) parentNode).parentNodes().entrySet()) {
					if (entry.getValue()) {
						res = entry.getKey();
					}
				}
				return res;
			}
			return null;
		}

		@Override
		public boolean isLeft(Node node) {
			return false;
		}

		@Override
		public boolean isBinaryTree() {
			return false;
		}
	}
}
