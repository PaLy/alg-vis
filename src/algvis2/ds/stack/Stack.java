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

package algvis2.ds.stack;

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
import org.abego.treelayout.util.AbstractTreeForTreeLayout;

import java.util.*;

class Stack extends DataStructure {
	final SimpleListProperty<StackNode.NullNode> versions = new SimpleListProperty<>(FXCollections
			.<StackNode.NullNode>observableArrayList());
	private final StackNode bottom = new StackNode.NullNode(-1, null);

	protected Stack() {
		super();
		StackNode.NullNode emptyVersion = new StackNode.NullNode(0, bottom);
		bottom.removePosBinding();
		emptyVersion.removePosBinding();
		
		versions.add(emptyVersion);
	}

	public Algorithm push(Visualization visualization, int x, int version) {
		if (version < 0 || version >= versions.size()) {
			version = versions.size() - 1;
		}
		StackPush stackPush = new StackPush(visualization, this, x, version);
		stackPush.run();
		return stackPush;
	}
	
	public Algorithm push(Visualization visualization, int x) {
		return push(visualization, x, versions.size() - 1); // last version
	}

	@Override
	public Animation random(Visualization visualization, int n) {
		SequentialTransition st = new SequentialTransition();
		for (int i = 0; i < n; i++) {
			st.getChildren().add(push(visualization, MyRandom.Int(InputField.MAX_VALUE + 1),
					MyRandom.Int(versions.size())).startEndTransition());
		}
		return st;
	}

	@Override
	public List<VisElem> dump() {
		List<VisElem> elements = new ArrayList<>();
		
		java.util.Stack<StackNode> todo = new java.util.Stack<>();
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

	public Algorithm pop(Visualization visualization, int version) {
		if (version < 0 || version >= versions.size()) {
			version = versions.size() - 1; // last version
		}
		if (versions.get(version).nextNode.nextNode != null) {
			StackPop stackPop = new StackPop(visualization, this, version);
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


	public final AbstractTreeForTreeLayout<StackNode> stackTree = new AbstractTreeForTreeLayout<StackNode>(bottom) {
		@Override
		public StackNode getParent(StackNode stackNode) {
			return stackNode.nextNode;
		}

		@Override
		public List<StackNode> getChildrenList(StackNode stackNode) {
			List<StackNode> res = new LinkedList<>();
			for (Map.Entry<StackNode, Boolean> entry : stackNode.parentNodes().entrySet()) {
				if (entry.getValue()) {
					res.add(entry.getKey());
				}
			}
			return res;
		}
	};
}
