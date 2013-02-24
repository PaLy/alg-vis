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

import algvis2.animation.AnimationFactory;
import algvis2.core.Algorithm;
import algvis2.scene.layout.ZDepth;
import algvis2.scene.paint.NodePaint;

class BSTInsert extends Algorithm {
	protected final BST D;
	protected BSTNode newNode;

	protected BSTInsert(BST D, BSTNode newNode) {
		super(D);
		this.D = D;
		this.newNode = newNode;
	}

	@Override
	public void runAlgorithm() {
		addVisElem(newNode, ZDepth.TOP);

		if (D.getRoot() == null) {
			D.setRoot(newNode);
		} else {
			BSTNode cur = D.getRoot();
			while (true) {
				newNode.goAbove(cur);
				pause(false);
				if (newNode.getKey() > cur.getKey()) {
					if (cur.getRight() == null) {
						cur.linkRight(newNode);
						break;
					} else {
						cur = cur.getRight();
					}
				} else {
					if (cur.getLeft() == null) {
						cur.linkLeft(newNode);
						break;
					} else {
						cur = cur.getLeft();
					}
				}
			}
		}

		removeVisElem(newNode);
		
		addAnimation(AnimationFactory.scaleInOut(newNode));
		
		newNode.setPaint(NodePaint.NORMAL);
	}
}
