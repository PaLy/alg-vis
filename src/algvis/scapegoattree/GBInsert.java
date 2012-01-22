package algvis.scapegoattree;

import algvis.bst.BSTNode;
import algvis.core.Colors;

public class GBInsert extends GBAlg {
	public GBInsert(GBTree T, int x) {
		super(T, x);
		v.bgColor(Colors.INSERT);
		setHeader("insertion");
	}

	@Override
	public void run() {
		if (T.root == null) {
			T.root = v;
			v.goToRoot();
			addStep("newroot");
			mysuspend();
			v.bgColor(Colors.NORMAL);
		} else {
			BSTNode w = T.root;
			v.goAboveRoot();
			addStep("bstinsertstart");
			mysuspend();

			while (true) {
				if (w.key == K) {
					if (((GBNode) w).deleted) {
						addStep("gbinsertunmark");
						((GBNode) w).deleted = false;
						w.bgColor(Colors.NORMAL);
						--T.del;
						T.v = null;
					} else {
						addStep("alreadythere");
						v.goDown();
						v.bgColor(Colors.NOTFOUND);
					}
					return;
				} else if (w.key < K) {
					addStep("bstinsertright", K, w.key);
					if (w.right != null) {
						w = w.right;
					} else {
						w.linkRight(v);
						break;
					}
				} else {
					addStep("bstinsertleft", K, w.key);
					if (w.left != null) {
						w = w.left;
					} else {
						w.linkLeft(v);
						break;
					}
				}
				v.goAbove(w);
				mysuspend();
			}
			v.bgColor(Colors.NORMAL);
			T.reposition();
			mysuspend();

			BSTNode b = null;
			while (w != null) {
				w.calc();
				if (w.height > Math.ceil(T.alpha * T.lg(w.size)) && b == null) {
					b = w;
				}
				w = w.parent;
			}

			// rebuilding
			if (b != null) {
				BSTNode r = b;
				int s = 0;
				addStep("gbtoohigh");
				r.mark();
				mysuspend();
				// to vine
				addStep("gbrebuild1");
				while (r != null) {
					if (r.left == null) {
						r.unmark();
						if (((GBNode) r).deleted) {
							--T.del;
							if (b == r) {
								b = r.right;
							}
							T.v = r;
							if (r.parent == null) {
								T.root = r = r.right;
								if (r != null) {
									r.parent = null;
								}
							} else {
								r.parent.linkRight(r = r.right);
							}
							T.v.goDown();
						} else {
							r = r.right;
							++s;
						}
						if (r != null) {
							r.mark();
						}
					} else {
						if (b == r) {
							b = r.left;
						}
						r.unmark();
						r = r.left;
						r.mark();
						T.rotate(r);
					}
					T.reposition();
					mysuspend();
				}

				// to tree
				addStep("gbrebuild2");
				int c = 1;
				for (int i = 0, l = (int) Math.floor(T.lg(s + 1)); i < l; ++i) {
					c *= 2;
				}
				c = s + 1 - c;

				b = compr(b, c);
				s -= c;
				while (s > 1) {
					b = compr(b, s /= 2);
				}
			}
		}
		T.reposition();
		addStep("done");
		T.v = null;
	}
}
