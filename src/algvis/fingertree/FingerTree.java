package algvis.fingertree;

import algvis.core.Dictionary;
import algvis.core.View;
import algvis.core.VisPanel;

public class FingerTree extends Dictionary {
	
	public static String dsName = "fingertree";
	final int order = 4;
	FingerNode root = null, v = null, finger = null;
	int xspan = 5, yspan = 15;
	Finger prst;
	
	public FingerTree(VisPanel M, String dsName) {
		super(M, dsName);
		prst = new Finger();
	}
	
	public FingerTree(VisPanel M) {
		super(M, dsName);
	}
	
	public void insert(int x) {
		start(new FingerInsert(this, x));
	}

	public void find(int x) {
		start(new FingerFind(this, x));
	}

	public void delete(int x) {
		start(new FingerDelete(this, x));
	}
	
	public void draw(View V) {
		if (root != null) {
			root.moveTree();
			root.drawTree(V);
			// draw neighbours - change Draw from Node or add another function
		}
		if (v != null) {
			v.move();
			v.draw(V);
		}
		prst.setFinger(finger);
		prst.draw(V);
	}
	
	public void reposition() {
		if (root != null) {
			root._reposition();
			M.screen.V.setBounds(x1, y1, x2, y2);
		}
	}
	
	@Override
	public void clear() {
		root = null;
		setStats();
	}

	@Override
	public String stats() {
		if (root == null) {
			return "#" + M.S.L.getString("nodes") + ": 0;   #"
					+ M.S.L.getString("keys") + ": 0 = 0% "
					+ M.S.L.getString("full") + ";   " + M.S.L.getString("height")
					+ ": 0";
		} else {
			root.calcTree();
			return "#" + M.S.L.getString("nodes") + ": " + root.nnodes + ";   "
					+ "#" + M.S.L.getString("keys") + ": " + root.nkeys + " = "
					+ (100 * root.nkeys) / (root.nnodes * (order - 1)) + "% "
					+ M.S.L.getString("full") + ";   " + M.S.L.getString("height")
					+ ": " + root.height;
		}
	}

	@Override
	public String getName() {
		return "fingertree";
	}

}
