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

package algvis2.ds.dictionaries;

import algvis2.core.Visualization;
import algvis2.scene.layout.ZDepth;
import algvis2.scene.paint.NodePaint;
import algvis2.scene.viselem.Backlight;
import algvis2.scene.viselem.Marker;
import algvis2.scene.viselem.Node;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

class RBDelete extends BSTFind {
	protected RBDelete(Visualization visualization, BST D, int x) {
		super(visualization, D, x);
	}

	@Override
	protected void runAlgorithm() {
		super.runAlgorithm();
		
		if (found != null) {
			Backlight foundBacklight = new Backlight(found, Backlight.RED);
			addVisElem(foundBacklight);

			RBNode u = (RBNode) found, w = (u.getLeft() != null) ? u.getLeft() : u
					.getRight2();
			RB.NULL.setParent(u.getParent2());
			
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
				RB.NULL.setParent(son.getParent2());
				
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

			if (!u.isRed()) {
				requestLayout();
				
				// bubleme nahor
				ObjectProperty<RBNode> s = new SimpleObjectProperty<>();
				Marker sMarker = new Marker();
				sMarker.elem.bind(s);
				addVisElem(sMarker);
				
				while (w.getParent2() != RB.NULL && !w.isRed()) {
					RB.NULL.setRed(false);
					if (w.getParent2().getLeft2() == w) {
						s.set(w.getParent2().getRight2());
						pause(false);
						if (s.get().isRed()) {
							s.get().setRed(false);
							w.getParent2().setRed(true);
							D.rotate(s.get());
						} else if (!s.get().getLeft2().isRed()
								&& !s.get().getRight2().isRed()) {
							s.get().setRed(true);
							w = w.getParent2();
						} else if (!s.get().getRight2().isRed()) {
							s.get().getLeft2().setRed(false);
							s.get().setRed(true);
							D.rotate(s.get().getLeft());
						} else {
							s.get().setRed(s.get().getParent2().isRed());
							w.getParent2().setRed(false);
							s.get().getRight2().setRed(false);
							D.rotate(s.get());
							w = (RBNode) D.getRoot();
						}
						pause(true);
					} else {
						s.set(w.getParent2().getLeft2());
						if (s.get().isRed()) {
							pause(false);
							s.get().setRed(false);
							w.getParent2().setRed(true);
							D.rotate(s.get());
						} else if (!s.get().getRight2().isRed()
								&& !s.get().getLeft2().isRed()) {
							pause(false);
							s.get().setRed(true);
							w = w.getParent2();
						} else if (!s.get().getLeft2().isRed()) {
							s.get().getRight2().setRed(false);
							pause(false);
							s.get().setRed(true);
							D.rotate(s.get().getRight2());
						} else {
							pause(false);
							s.get().setRed(s.get().getParent2().isRed());
							w.getParent2().setRed(false);
							s.get().getLeft2().setRed(false);
							D.rotate(s.get());
							w = (RBNode) D.getRoot();
						}
						pause(true);
					}
				}
				w.setRed(false);
				
				sMarker.elem.unbind();
				removeVisElem(sMarker);
			}
		}
	}
}
