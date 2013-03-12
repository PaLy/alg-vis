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

import algvis2.core.Algorithm;
import algvis2.scene.layout.ZDepth;
import algvis2.scene.paint.NodePaint;
import javafx.animation.ScaleTransitionBuilder;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

class PC_PBSTInsert extends Algorithm {
	private final PC_PBST D;
	private final int x;
	private final PersistentVisualization visualization;

	protected PC_PBSTInsert(PersistentVisualization visualization, PC_PBST D, int x) {
		super(visualization);
		this.D = D;
		this.x = x;
		this.visualization = visualization;
	}

	@Override
	protected void runAlgorithm() {
		int oldVersion = D.getVersionsCount();
		int newVersion = oldVersion + 1;
		//if (oldVersion > 0) {
		//	visualization.highlightVersion(oldVersion);
		//}
		
		PC_PBSTNode newNode = new PC_PBSTNode(x, NodePaint.INSERT);
		addVisElem(newNode, ZDepth.TOP);

		List<PC_PBSTNode> path = new ArrayList<>();
		NodePaint pathColor = NodePaint.GREEN;

		if (newVersion == 1) {
			D.addNewRoot(newNode);
		} else {
			PC_PBSTNode curOld = D.getRoot(oldVersion);
			newNode.goAbove(curOld);
			pause(true);
			PC_PBSTNode curNewParent;
			PC_PBSTNode curNew = curOld.clone();
			curNew.setPaint(pathColor);
			path.add(curNew);
			D.addNewRoot(curNew);
			
			boolean isLeft;
			while (true) {
				if (newNode.getKey() > curOld.getKey()) {
					isLeft = false;
					if (curNew.getLeft() != null) {
						curNew.getLeft().addParent(curNew);
					}
					
					if (curOld.getRight() == null) {
						curNew.setRight(newNode);
						break;
					} else {
						curOld = curOld.getRight();
					}
				} else {
					isLeft = true;
					if (curNew.getRight() != null) {
						curNew.getRight().addParent(curNew);
					}
					
					if (curOld.getLeft() == null) {
						curNew.setLeft(newNode);
						break;
					} else {
						curOld = curOld.getLeft();
					}
				}
				newNode.goAbove(curOld);
				pause(true);
				curNewParent = curNew;
				curNew = curOld.clone();
				curNew.setPaint(pathColor);
				path.add(curNew);
				if (isLeft) {
					curNewParent.setLeft(curNew);
				} else {
					curNewParent.setRight(curNew);
				}
				curNew.addParent(curNewParent);
			}
			newNode.addParent(curNew);
		}

		removeVisElem(newNode);

		addAnimation(ScaleTransitionBuilder.create().node(newNode.getVisual()).byX(0.5).byY(0.5)
				.duration(Duration.millis(500)).cycleCount(2).autoReverse(true).build());

		pause(false);
		newNode.setPaint(NodePaint.NORMAL);
		for (PC_PBSTNode node : path) {
			node.setPaint(NodePaint.NORMAL);
		}
		//visualization.highlightOff();
	}
}
