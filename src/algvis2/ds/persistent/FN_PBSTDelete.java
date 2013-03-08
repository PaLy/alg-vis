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

import algvis2.core.Visualization;
import algvis2.scene.layout.ZDepth;
import algvis2.scene.paint.NodePaint;
import algvis2.scene.viselem.Backlight;
import algvis2.scene.viselem.Node;

public class FN_PBSTDelete extends FN_PBSTFind {
	private final FN_PBST bst;

	public FN_PBSTDelete(Visualization visualization, FN_PBST bst, int x) {
		super(visualization, bst, bst.getVersionsCount(), x);
		this.bst = bst;
	}

	@Override
	protected void runAlgorithm() {
		super.runAlgorithm();
		int lastVersion = bst.getVersionsCount();
		int newVersion = lastVersion + 1;

		if (found != null) {
			Backlight foundBacklight = new Backlight(found, Backlight.RED);
			addVisElem(foundBacklight);

			if (found.isLeaf(lastVersion)) { // case I - leaf
				if (found.isRoot()) {
					bst.setRoot(null);
				} else if (found.isLeft()) {
					found.getParent().addLeftChild(new BinFatNode.Null(), newVersion);
				} else {
					found.getParent().addRightChild(new BinFatNode.Null(), newVersion);
				}
			} else {
				BinFatNode leftChild = found.getLeftChild(lastVersion);
				BinFatNode rightChild = found.getRightChild(lastVersion);
				if (leftChild == null || rightChild == null) {
					// case II - 1 child
					BinFatNode son;
					if (leftChild == null) {
						son = rightChild;
					} else {
						son = leftChild;
					}
					//				if (son.isLeft() == found.isLeft()) {
					//					son.setArc(found.getParent());
					//				} else {
					//					son.pointTo(found.getParent());
					//				}
					//				pause(false);
					//				son.noArc();
					//				son.noArrow();
					//					if (leftChild == null) {
					//						found.addRightChild(new BinFatNode.Null(), newVersion);
					//					} else {
					//						found.addLeftChild(new BinFatNode.Null(), newVersion);
					//					}
					if (found.isRoot()) {
						bst.setRoot(son);
						son.setParent(null);
					} else {
						son.setParent(found.getParent());
						if (found.isLeft()) {
							found.getParent().addLeftChild(son, newVersion);
						} else {
							found.getParent().addRightChild(son, newVersion);
						}
					}
				} else { // case III - 2 children
					BinFatNode son = rightChild;
					BinFatNode v = new BinFatNode(-Node.INF, NodePaint.FIND);
					addVisElem(v, ZDepth.TOP);
					v.goAbove(son);
					pause(false);
					BinFatNode sonsLeft = son.getLeftChild(lastVersion);
					while (sonsLeft != null) {
						son = sonsLeft;
						sonsLeft = son.getLeftChild(lastVersion);
						v.goAbove(son);
						pause(false);
					}
					v.goTo(son);
					BinFatNode sonsParent = son.getParent(), sonsRight = son.getRightChild(lastVersion);
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
						sonsRight.setParent(sonsParent);
						sonsParent.addLeftChild(sonsRight, newVersion);
					}
					son.goNextTo(found);

					pause(true);
					leftChild.setParent(son);
					son.addLeftChild(leftChild, newVersion);
					if (rightChild != son) {
						rightChild.setParent(son);
						son.addRightChild(rightChild, newVersion);
					}

					son.setParent(found.getParent());
					if (found.isRoot()) {
						bst.setRoot(son);
					} else if (found.isLeft()) {
						found.getParent().addLeftChild(son, newVersion);
					} else {
						found.getParent().addRightChild(son, newVersion);
					}
					removeVisElem(son);
				} // end case III
			}

			removeVisElem(foundBacklight);
			bst.incVersionsCount();
		}

	}
}
