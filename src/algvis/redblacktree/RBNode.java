package algvis.redblacktree;

import java.awt.Color;
import algvis.bst.BSTNode;
import algvis.core.DataStructure;
import algvis.core.Node;
import algvis.core.View;

public class RBNode extends BSTNode {
	boolean red = true;

	public RBNode(DataStructure D, int key, int x, int y) {
		super(D, key, x, y);
	}

	public RBNode(DataStructure D, int key) {
		super(D, key);
	}

	@Override
	public boolean isRoot() {
		return parent.key == Node.NULL;
	}

	@Override
	public boolean isLeaf() {
		return left.key == Node.NULL && right.key == Node.NULL;
	}

	@Override
	public boolean isLeft() {
		return parent.left == this;
	}

	@Override
	public void linkLeft(BSTNode v) {
		left = v;
		if (v.key != Node.NULL) {
			v.parent = this;
		}
	}

	@Override
	public void linkRight(BSTNode v) {
		right = v;
		if (v.key != Node.NULL) {
			v.parent = this;
		}
	}

	@Override
	public void isolate() {
		left = right = parent = ((RB) D).NULL;
	}

	@Override
	public void draw(View v) {
		if (state == Node.INVISIBLE || key == NULL) {
			return;
		}
		bgColor(red ? Color.red : Color.black);
		fgColor(red ? Color.black : Color.white);
		super.draw(v);
	}

	public void drawBigNodes(View v) {
		if (key == NULL) {
			return;
		}
		if (left != null) {
			((RBNode) left).drawBigNodes(v);
		}
		if (right != null) {
			((RBNode) right).drawBigNodes(v);
		}
		if (red && parent != null) {
			v.drawWideLine(x, y, parent.x, parent.y);
		} else {
			v.drawWideLine(x - 1, y, x + 1, y);
		}
	}

	@Override
	public void calc() {
		if (key == NULL) {
			return;
		}
		int ls = 0, rs = 0, lh = 0, rh = 0, lsh = 0, rsh = 0;
		if (left != null) {
			ls = left.size;
			lh = left.height;
			lsh = left.sumh;
		}
		if (right != null) {
			rs = right.size;
			rh = right.height;
			rsh = right.sumh;
		}
		size = ls + rs + 1;
		height = Math.max(lh, rh) + 1;
		sumh = lsh + rsh + size;
	}

	@Override
	public void calcTree() {
		if (key == Node.NULL) {
			return;
		}
		if (left.key != Node.NULL) {
			left.calcTree();
		}
		if (right.key != Node.NULL) {
			right.calcTree();
		}
		calc();
	}

	public void drawTree2(View v) {
		if (((RB)D).mode24) {
			drawBigNodes(v);
		}
		drawTree(v);
	}

	@Override
	public void moveTree() {
		if (left.key != Node.NULL) {
			((RBNode) left).moveTree();
		}
		if (right.key != Node.NULL) {
			((RBNode) right).moveTree();
		}
		move();
	}

	// */@Override
	public void rebox() {
		leftw = (left.key == Node.NULL) ? D.xspan + D.radius : left.leftw
				+ left.rightw;
		rightw = (right.key == Node.NULL) ? D.xspan + D.radius : right.leftw
				+ right.rightw;
	}

	@Override
	public void reboxTree() {
		if (left.key != Node.NULL) {
			((RBNode) left).reboxTree();
		}
		if (right.key != Node.NULL) {
			((RBNode) right).reboxTree();
		}
		rebox();
	}

	private void repos() {
		if (isRoot()) {
			goToRoot();
			D.x1 = -leftw;
			D.x2 = rightw;
			D.y2 = this.toy;
		}
		if (this.toy > D.y2) {
			D.y2 = this.toy;
		}
		if (left.key != Node.NULL) {
			if (((RB) D).mode24) {
				left.goTo(this.tox - left.rightw, this.toy
						+ (((RBNode) left).red ? D.yspan : 2 * D.radius
								+ D.yspan) - (red ? D.yspan : 0));
			} else {
				left.goTo(this.tox - left.rightw, this.toy + 2 * D.radius
						+ D.yspan);
			}
			((RBNode) left).repos();
		}
		if (right.key != Node.NULL) {
			if (((RB) D).mode24) {
				right.goTo(this.tox + right.leftw, this.toy
						+ (((RBNode) right).red ? D.yspan : 2 * D.radius
								+ D.yspan) - (red ? D.yspan : 0));
			} else {
				right.goTo(this.tox + right.leftw, this.toy + 2 * D.radius
						+ D.yspan);
			}
			((RBNode) right).repos();
		}
	}

	@Override
	public void reposition() {
		if (key != NULL) {
			reboxTree();
			repos();
		}
	} // */
}
