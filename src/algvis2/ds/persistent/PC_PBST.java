package algvis2.ds.persistent;

import algvis2.core.Algorithm;
import algvis2.core.MyRandom;
import algvis2.scene.control.InputField;
import algvis2.scene.layout.AbstractBinTreeForTreeLayout;
import algvis2.scene.layout.BinTreeForTreeLayout;
import algvis2.scene.text.Fonts;
import algvis2.scene.viselem.Node;
import algvis2.scene.viselem.VisElem;
import javafx.collections.ObservableList;
import javafx.scene.text.Text;

import java.util.*;

// TODO optimalizovat rekurencie na roznych miestach
public class PC_PBST extends PartiallyPersistentDictionary {
	private final PC_PBSTNode.FakeRoot fakeRoot = new PC_PBSTNode.FakeRoot(-1);
	private final PersistentVisualization visualization;

	public PC_PBST(PersistentVisualization visualization) {
		this.visualization = visualization;
		initTree();
	}

	void addNewRoot(PC_PBSTNode node) {
		node.removePosBinding();
		fakeRoot.addChild(node);
		incVersionsCount();
		int version = versionsCountProperty.get();

		Text verNo = new Text("v" + String.valueOf(version));
		verNo.setFont(Fonts.VER_NO_FONT);
		verNo.setX(-verNo.getBoundsInLocal().getWidth() / 2);
		verNo.setY(-Node.RADIUS - 6);
		verNo.setOnMouseClicked(new PersistentVisualization.VersionHighlight(visualization, version));
		node.getVisual().getChildren().add(verNo);
	}

	PC_PBSTNode getRoot(int version) {
		return fakeRoot.getChildren().get(version - 1);
	}

	PC_PBSTNode.FakeRoot getFakeRoot() {
		return fakeRoot;
	}

	@Override
	public Algorithm insert(PersistentVisualization visualization, int x) {
		PC_PBSTInsert insert = new PC_PBSTInsert(visualization, this, x);
		insert.run();
		return insert;
	}

	@Override
	public Algorithm find(PersistentVisualization visualization, int version, int x) {
		PC_PBSTFind find = new PC_PBSTFind(visualization, this, version, x);
		find.run();
		return find;
	}

	@Override
	public Algorithm delete(PersistentVisualization visualization, int x) {
		return null; // TODO
	}

	@Override
	List<Node> dumpVersion(int version) {
		List<Node> res = new ArrayList<>();
		PC_PBSTNode root = fakeRoot.getChildren().get(version - 1);
		Stack<PC_PBSTNode> todo = new Stack<>();
		todo.push(root);
		while (!todo.empty()) {
			PC_PBSTNode node = todo.pop();
			if (node.getLeft() != null) {
				res.add(node);
				res.add(node.getLeft());
				todo.add(node.getLeft());
			}
			if (node.getRight() != null) {
				res.add(node);
				res.add(node.getRight());
				todo.add(node.getRight());
			}
		}
		if (res.isEmpty()) {
			res.add(root);
		}
		return res;
	}

	@Override
	public void clear() {
		fakeRoot.getChildren().clear();
		versionsCountProperty.setValue(0);
		initTree();
	}

	@Override
	public List<VisElem> dump() {
		Set<VisElem> elements = new HashSet<>();

		java.util.Stack<PC_PBSTNode> todo = new java.util.Stack<>();
		elements.addAll(fakeRoot.getChildren());
		todo.addAll(fakeRoot.getChildren());

		while (!todo.empty()) {
			PC_PBSTNode elem = todo.pop();
			if (elem.getLeft() != null) {
				if (!elements.contains(elem.getLeft())) {
					todo.add(elem.getLeft());
					elements.add(elem.getLeft());
				}
			}
			if (elem.getRight() != null) {
				if (!elements.contains(elem.getRight())) {
					todo.add(elem.getRight());
					elements.add(elem.getRight());
				}
			}
		}

		return new ArrayList<>(elements);

	}

	@Override
	public void recalcAbsPosition() {
		for (PC_PBSTNode node : fakeRoot.getChildren()) {
			recalcAbsPosR(node);
		}
	}

	private void recalcAbsPosR(PC_PBSTNode node) {
		node.recalcAbsPosition();
		if (node.getLeft() != null) {
			recalcAbsPosR(node.getLeft());
		}
		if (node.getRight() != null) {
			recalcAbsPosR(node.getRight());
		}
	}

	@Override
	public void storeState(HashMap<Object, Object> state) {
		super.storeState(state);
		fakeRoot.storeState(state);
	}

	@Override
	public String toString() {
		String res = "******\n";
		for (Node node : fakeRoot.getChildren()) {
			res += node.toString();
		}
		res += "\n******\n";
		return res;
	}
	
	private void initTree() {
		for(int i = 0; i < 10; i++) {
			pinsert(MyRandom.Int(InputField.MAX_VALUE + 1));
		}
	}

	private void pinsert(int x) {
		PC_PBSTNode newNode = new PC_PBSTNode(x);
		if (fakeRoot.getChildren().size() == 0) {
			addNewRoot(newNode);
		} else {
			PC_PBSTNode cur = fakeRoot.getChildren().get(0);
			while(true) {
				if (x > cur.getKey()) {
					if (cur.getRight() == null) {
						cur.setRight(newNode);
						break;
					} else {
						cur = cur.getRight();
					}
				} else {
					if (cur.getLeft() == null) {
						cur.setLeft(newNode);
						break;
					} else {
						cur = cur.getLeft();
					}
				}
			}
			newNode.addParent(cur);
		}
	}

	public final BinTreeForTreeLayout<PC_PBSTNode> treeForTreeLayout = new AbstractBinTreeForTreeLayout<PC_PBSTNode>(
			fakeRoot) {

		@Override
		public PC_PBSTNode getParent(PC_PBSTNode node) {
			if (node.parentProperty.isEmpty()) {
				return null;
			} else if (node.getParent(0).getLeft() == node) {
				return node.getParent(0);
			} else {
				return node.getLastParent();
			}
		}

		@Override
		public List<PC_PBSTNode> getChildrenList(PC_PBSTNode node) {
			if (node instanceof PC_PBSTNode.FakeRoot) {
				return ((PC_PBSTNode.FakeRoot) node).getChildren();
			} else {
				List<PC_PBSTNode> list = new ArrayList<>();
				if (node.getLeft() != null && node.getLeft().getParent(0) == node) {
					list.add(node.getLeft());
				}
				if (node.getRight() != null && node.getRight().getLastParent() == node) {
					list.add(node.getRight());
				}
				return list;
			}
		}

		@Override
		public PC_PBSTNode getLastLeftChild(PC_PBSTNode parentNode) {
			PC_PBSTNode res = null;
			if (parentNode instanceof PC_PBSTNode.FakeRoot) {
				ObservableList<PC_PBSTNode> children = ((PC_PBSTNode.FakeRoot) parentNode)
						.getChildren();
				if (!children.isEmpty()) {
					res = children.get(0);
				}
			} else {
				res = parentNode.getLeft();
				if (res != null && res.getParent(0) != parentNode) {
					res = null;
				}
			}
			return res;
		}

		@Override
		public PC_PBSTNode getFirstRightChild(PC_PBSTNode parentNode) {
			PC_PBSTNode res = null;
			if (parentNode instanceof PC_PBSTNode.FakeRoot) {
				ObservableList<PC_PBSTNode> children = ((PC_PBSTNode.FakeRoot) parentNode)
						.getChildren();
				if (!children.isEmpty()) {
					res = children.get(children.size() - 1);
				}
			} else {
				res = parentNode.getRight();
				if (res != null && res.getLastParent() != parentNode) {
					res = null;
				}
			}
			return res;
		}

	};
}
