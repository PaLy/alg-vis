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

import algvis2.animation.AnimationFactory;
import algvis2.core.Algorithm;
import algvis2.core.Visualization;
import algvis2.scene.layout.ZDepth;
import algvis2.scene.paint.NodePaint;

public class FN_PBSTInsert extends Algorithm {
	private final int x;
	private final FN_PBST bst;

	protected FN_PBSTInsert(Visualization visualization, FN_PBST D, int x) {
		super(visualization);
		this.x = x;
		this.bst = D;
	}

	@Override
	protected void runAlgorithm() {
		int lastVersion = bst.getVersionsCount();
		int newVersion = lastVersion + 1;
		BinFatNode newNode = new BinFatNode(x, NodePaint.INSERT);
		addVisElem(newNode, ZDepth.TOP);

		if (bst.getRoot() == null) {
			bst.setRoot(newNode);
		} else {
			BinFatNode cur = bst.getRoot();
			while (true) {
				newNode.goAbove(cur);
				pause(false);
				if (newNode.getKey() > cur.getKey()) {
					BinFatNode rightChild = FN_PBSTAlgorithmUtil.getRightChild(
							cur, lastVersion);
					if (rightChild == null) {
						cur.addRightChild(newNode, newVersion);
						newNode.setParent(cur);
						break;
					} else {
						cur = rightChild;
					}
				} else {
					BinFatNode leftChild = FN_PBSTAlgorithmUtil.getLeftChild(
							cur, lastVersion);
					if (leftChild == null) {
						cur.addLeftChild(newNode, newVersion);
						newNode.setParent(cur);
						break;
					} else {
						cur = leftChild;
					}
				}
			}
		}

		removeVisElem(newNode);

		addAnimation(AnimationFactory.scaleInOut(newNode));

		newNode.setPaint(NodePaint.NORMAL);

		BinFatNode leftNull = new BinFatNode.Null();
		BinFatNode rightNull = new BinFatNode.Null();
		leftNull.removePosBinding();
		rightNull.removePosBinding();
		newNode.addLeftChild(leftNull, newVersion);
		newNode.addRightChild(rightNull, newVersion);

		bst.incVersionsCount();
	}
}
