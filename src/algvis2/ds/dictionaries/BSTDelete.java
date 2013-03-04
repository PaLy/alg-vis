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
import algvis2.scene.viselem.Node;

class BSTDelete extends BSTFind {
	protected BSTDelete(Visualization visualization, BST D, int x) {
		super(visualization, D, x);
	}

	@Override
	protected void runAlgorithm() {
		super.runAlgorithm();

		if (found != null) {
			Backlight foundBacklight = new Backlight(found, Backlight.RED);
			addVisElem(foundBacklight);

			if (found.isLeaf()) { // case I - leaf
				if (found.isRoot()) {
					D.setRoot(null);
				} else if (found.isLeft()) {
					found.getParent().unlinkLeft();
				} else {
					found.getParent().unlinkRight();
				}
			} else if (found.getLeft() == null || found.getRight() == null) {
				// case II - 1 child
				BSTNode son;
				if (found.getLeft() == null) {
					son = found.getRight();
				} else {
					son = found.getLeft();
				}
				//				if (son.isLeft() == found.isLeft()) {
				//					son.setArc(found.getParent());
				//				} else {
				//					son.pointTo(found.getParent());
				//				}
				//				pause(false);
				//				son.noArc();
				//				son.noArrow();
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
			} else { // case III - 2 children
				BSTNode son = found.getRight();
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
				BSTNode sonsParent = son.getParent(), sonsRight = son.getRight();
				v.setPaint(NodePaint.FOUND);
				//				if (sonsRight == null) {
				//					addStep("bst-delete-succ-unlink");
				//				} else {
				//					addStep("bst-delete-succ-link", sonsRight.getKey(), sonsParent.getKey());
				//					if (son.isLeft()) {
				//						sonsRight.pointTo(sonsParent);
				//					} else {
				//						sonsRight.setArc(sonsParent);
				//					}
				//				}
				pause(false);
				//				if (sonsRight != null) {
				//					sonsRight.noArc();
				//					sonsRight.noArrow();
				//				}
				removeVisElem(v);
				addVisElem(son, ZDepth.TOP);
				if (son.isLeft()) {
					sonsParent.linkLeft(sonsRight);
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
		}
	}
}
