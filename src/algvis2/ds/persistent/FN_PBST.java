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

import algvis2.core.Algorithm;
import algvis2.core.Visualization;
import algvis2.scene.layout.AbstractBinTreeForTreeLayout;
import algvis2.scene.layout.BinTreeForTreeLayout;
import algvis2.scene.viselem.Node;
import algvis2.scene.viselem.VisElem;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.*;
import java.util.Stack;

public class FN_PBST extends PartiallyPersistentDictionary {
	private final ObjectProperty<BinFatNode> rootProperty = new SimpleObjectProperty<>();

	public void setRoot(BinFatNode root) {
		rootProperty.set(root);
		if (root != null) {
			root.removePosBinding();
		}
	}

	public BinFatNode getRoot() {
		return rootProperty.get();
	}

	@Override
	public Algorithm insert(Visualization visualization, int x) {
		FN_PBSTInsert bstInsert = new FN_PBSTInsert(visualization, this, x);
		bstInsert.run();
		return bstInsert;
	}

	@Override
	public Algorithm find(Visualization visualization, int x, int version) {
		if (getRoot() != null && version == 0) {
			version = 1;
		}
		if (version > getVersionsCount() || version < 0) {
			version = getVersionsCount();
		}
		FN_PBSTFind bstFind = new FN_PBSTFind(visualization, this, version, x);
		bstFind.run();
		return bstFind;
	}

	@Override
	public Algorithm delete(Visualization visualization, int x) {
		FN_PBSTDelete bstDelete = new FN_PBSTDelete(visualization, this, x);
		bstDelete.run();
		return bstDelete;
	}

	@Override
	public void clear() {
		setRoot(null);
		versionsCountProperty.set(0);
	}

	@Override
	public List<VisElem> dump() {
		Set<VisElem> elements = new HashSet<>();

		if (getRoot() != null) {
			java.util.Stack<BinFatNode> todo = new java.util.Stack<>();
			todo.push(getRoot());
			elements.add(getRoot());

			while (!todo.empty()) {
				BinFatNode elem = todo.pop();
				for (BinFatNode child : elem.getLeftChildren().values()) {
					todo.add(child);
					elements.add(child);
				}
				for (BinFatNode child : elem.getRightChildren().values()) {
					todo.add(child);
					elements.add(child);
				}
			}
		}
		return new ArrayList<>(elements);
	}

	@Override
	public List<Node> dumpVersion(int version) {
		List<Node> elements = new ArrayList<>();

		if (getRoot() != null) { // TODO root sa moze zmenit?? ak povolime delete
			java.util.Stack<BinFatNode> todo = new Stack<>();
			todo.push(getRoot());

			while (!todo.empty()) {
				BinFatNode elem = todo.pop();
				BinFatNode leftChild = elem.getLeftChild(version, true);
				if (leftChild != null) {
					todo.add(leftChild);
					elements.add(elem);
					elements.add(leftChild);
				}
				BinFatNode rightChild = elem.getRightChild(version, true);
				if (rightChild != null) {
					todo.add(rightChild);
					elements.add(elem);
					elements.add(rightChild);
				}
			}
		}
		return elements;
	}

	@Override
	public void recalcAbsPosition() {
		if (getRoot() != null)
			recalcAbsPositionR(getRoot());
	}

	private void recalcAbsPositionR(BinFatNode node) {
		node.recalcAbsPosition();
		for (BinFatNode child : node.getLeftChildren().values()) {
			recalcAbsPositionR(child);
		}
		for (BinFatNode child : node.getRightChildren().values()) {
			recalcAbsPositionR(child);
		}
	}

	@Override
	public void storeState(HashMap<Object, Object> state) {
		super.storeState(state);
		state.put(rootProperty, rootProperty.getValue());
		if (getRoot() != null) {
			getRoot().storeState(state);
		}
	}

	@Override
	public String toString() {
		return getRoot().toString();
	}

	public final BinTreeForTreeLayout<BinFatNode> treeForTreeLayout = new AbstractBinTreeForTreeLayout<BinFatNode>(
			null) {
		@Override
		public BinFatNode getRoot() {
			return rootProperty.get();
		}

		@Override
		public BinFatNode getParent(BinFatNode node) {
			return node.getParent();
		}

		@Override
		public List<BinFatNode> getChildrenList(BinFatNode node) {
			List<BinFatNode> res = new ArrayList<>();
			for (BinFatNode child : node.getLeftChildren().values()) {
				res.add(child);
			}
			for (BinFatNode child : node.getRightChildren().values()) {
				res.add(child);
			}
			return res;
		}

		@Override
		public BinFatNode getLastLeftChild(BinFatNode parentNode) {
			return parentNode.getLastLeftChild();
		}

		@Override
		public BinFatNode getFirstRightChild(BinFatNode parentNode) {
			return parentNode.getFirstRightChild();
		}
	};
}
