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

import algvis2.core.Algorithm;
import algvis2.core.DataStructure;
import algvis2.core.Visualization;
import algvis2.scene.viselem.Node;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import org.abego.treelayout.TreeForTreeLayout;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

	@Override
	public Algorithm insert(int x) {
		return insert(x, versions.size() - 1); // last version
	}

	public Algorithm insert(int x, int version) {
		StackPush stackPush = new StackPush(this, x, version);
		stackPush.run();
		return stackPush;
	}

	@Override
	public void clear() {
		versions.remove(1, versions.size());
		bottom.parentNodes().clear();
		bottom.parentNodes().put(versions.get(0), true);
	}

	@Override
	public Algorithm delete(int version) {
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
			for (Map.Entry<Node, Boolean> entry : ((StackNode) node).parentNodes().entrySet()) {
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
				for (Map.Entry<Node, Boolean> entry : ((StackNode) parentNode).parentNodes().entrySet()) {
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
				for (Map.Entry<Node, Boolean> entry : ((StackNode) parentNode).parentNodes().entrySet()) {
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
				for (Map.Entry<Node, Boolean> entry : ((StackNode) parentNode).parentNodes().entrySet()) {
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
				for (Map.Entry<Node, Boolean> entry : ((StackNode) parentNode).parentNodes().entrySet()) {
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
