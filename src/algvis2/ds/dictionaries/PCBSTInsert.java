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

import algvis2.core.Algorithm;
import algvis2.core.Visualization;
import algvis2.scene.layout.ZDepth;
import algvis2.scene.paint.NodePaint;
import javafx.animation.ScaleTransitionBuilder;
import javafx.util.Duration;

class PCBSTInsert extends Algorithm {
	private final PCBST D;
	private final BSTNode newNode;

	protected PCBSTInsert(Visualization visualization, PCBST D, BSTNode newNode) {
		super(visualization);
		this.D = D;
		this.newNode = newNode;
	}

	@Override
	protected void runAlgorithm() {
		D.time++;

		addVisElem(newNode, ZDepth.TOP);

		if (D.getRoot() == null) {
			D.setRoot(newNode);
		} else {
			GroupOfBSTNodes cur = D.getRoot();
			while (true) {
				newNode.goAbove(cur);
				pause(true);
				cur.addNode(D.time);
				GroupOfBSTNodes parent = cur.getParent();
				BSTNode lastOfCur = cur.getLastNode();
				if (parent != null) {
					BSTNode lastOfParent = parent.getLastNode();
					if (cur.isLeft()) {
						lastOfParent.setLeft(lastOfCur);
					} else {
						lastOfParent.setRight(lastOfCur);
					}
				}
				if (newNode.getKey() > cur.getKey()) {
					if (cur.getRight() == null) {
						cur.setRight(newNode);
						break;
					} else {
						cur.getLastNode().setRight(null);
						cur = cur.getRight();
					}
				} else {
					if (cur.getLeft() == null) {
						cur.setLeft(newNode);
						break;
					} else {
						cur.getLastNode().setLeft(null);
						cur = cur.getLeft();
					}
				}
			}
		}

		removeVisElem(newNode);

		addAnimation(ScaleTransitionBuilder.create().node(newNode.getVisual()).byX(0.5).byY(0.5)
				.duration(Duration.millis(500)).cycleCount(2).autoReverse(true).build());

		newNode.setPaint(NodePaint.NORMAL);
	}
}
