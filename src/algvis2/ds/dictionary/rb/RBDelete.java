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

package algvis2.ds.dictionary.rb;

import algvis2.ds.dictionary.bst.BST;
import algvis2.ds.dictionary.bst.BSTFind;
import algvis2.ds.dictionary.bst.BSTNode;
import algvis2.scene.layout.ZDepth;
import algvis2.scene.paint.NodePaint;
import algvis2.scene.viselem.Backlight;
import algvis2.scene.viselem.Node;

public class RBDelete extends BSTFind {
	protected RBDelete(BST D, int x) {
		super(D, x);
	}

	@Override
	protected void runAlgorithm() {
		super.runAlgorithm();
		
		if (found != null) {
			Backlight foundBacklight = new Backlight(found, Backlight.RED);
			addVisElem(foundBacklight);

			RBNode u = (RBNode) found, w = (u.getLeft() != null) ? u.getLeft() : u
					.getRight2();
			((RB) D).NULL.setParent(u.getParent2());
			
			if (found.isLeaf()) { // case I - list
				if (found.isRoot()) {
					D.setRoot(null);
				} else if (found.isLeft()) {
					found.getParent().unlinkLeft();
				} else {
					found.getParent().unlinkRight();
				}
			} else if (found.getLeft() == null || found.getRight() == null) {
				// case IIa - 1 syn
				BSTNode son;
				if (found.getLeft() == null) {
					son = found.getRight();
				} else {
					son = found.getLeft();
				}
				if (found.getLeft() == null) {
					found.unlinkRight();
				} else {
					found.unlinkLeft();
				}
				if (found.isRoot()) {
					D.setRoot(son);
					son.setParent(null);
				} else {
					if (found.isLeft()) {
						found.getParent().linkLeft(son);
					} else {
						found.getParent().linkRight(son);
					}
				}
			} else { // case III - 2 synovia
				RBNode son = (RBNode) found.getRight();
				BSTNode v = new BSTNode(-Node.INF, NodePaint.FIND);
				addVisElem(v, ZDepth.TOP);
				v.goAbove(son);
				pause(false);
				while (son.getLeft() != null) {
					son = son.getLeft();
					v.goAbove(son);
					pause(false);
				}
				v.goTo(son);
				v.setPaint(NodePaint.FOUND);
				
				u = son;
				w = son.getRight2();
				((RB) D).NULL.setParent(son.getParent2());
				
				pause(false);
				removeVisElem(v);
				addVisElem(son, ZDepth.TOP);
				son.setRed(((RBNode) found).isRed());
				if (son.isLeft()) {
					son.getParent().linkLeft(w);
				}
				son.goNextTo(found);
				pause(true);
				
				son.linkLeft(found.getLeft());
				if (found.getRight() != son) {
					son.linkRight(found.getRight());
				}
				if (found.isRoot()) {
					son.setParent(null);
					D.setRoot(son);
				} else if (found.isLeft()) {
					found.getParent().linkLeft(son);
				} else {
					found.getParent().linkRight(son);
				}
				removeVisElem(son);
			} // end case III
			removeVisElem(foundBacklight);
			
			requestLayout();

			if (!u.isRed()) {
				// bubleme nahor
				while (w.getParent2() != ((RB) D).NULL && !w.isRed()) {
					((RB) D).NULL.setRed(false);
					if (w.getParent2().getLeft2() == w) {
						RBNode s = w.getParent2().getRight2();
						if (s != ((RB) D).NULL) addMarker(s);
						pause(false);
						if (s.isRed()) {
							s.setRed(false);
							w.getParent2().setRed(true);
							D.rotate(s);
						} else if (!s.getLeft2().isRed()
								&& !s.getRight2().isRed()) {
							s.setRed(true);
							w = w.getParent2();
						} else if (!s.getRight2().isRed()) {
							s.getLeft2().setRed(false);
							s.setRed(true);
							D.rotate(s.getLeft());
						} else {
							s.setRed(s.getParent2().isRed());
							w.getParent2().setRed(false);
							s.getRight2().setRed(false);
							D.rotate(s);
							w = (RBNode) D.getRoot();
						}
						pause(true);
						if (s != ((RB) D).NULL) removeMarker(s);
					} else {
						RBNode s = w.getParent2().getLeft2();
						if (s != ((RB) D).NULL) addMarker(s);
						if (s.isRed()) {
							pause(false);
							s.setRed(false);
							w.getParent2().setRed(true);
							D.rotate(s);
						} else if (!s.getRight2().isRed()
								&& !s.getLeft2().isRed()) {
							pause(false);
							s.setRed(true);
							w = w.getParent2();
						} else if (!s.getLeft2().isRed()) {
							s.getRight2().setRed(false);
							pause(false);
							s.setRed(true);
							D.rotate(s.getRight2());
						} else {
							pause(false);
							s.setRed(s.getParent2().isRed());
							w.getParent2().setRed(false);
							s.getLeft2().setRed(false);
							D.rotate(s);
							w = (RBNode) D.getRoot();
						}
						pause(true);
						if (s != ((RB) D).NULL) removeMarker(s);
					}
				}
				w.setRed(false);
			}
		}
	}
}
