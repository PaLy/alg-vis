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

public class PC_PBSTFind extends Algorithm {
	private final int x, version;
	private final PC_PBST bst;
	private final PersistentVisualization visualization;

	protected PC_PBSTFind(PersistentVisualization visualization, PC_PBST bst, int version, int x) {
		super(visualization);
		this.x = x;
		this.version = version;
		this.bst = bst;
		this.visualization = visualization;
	}

	@Override
	protected void runAlgorithm() {
		visualization.setCurAlgVersion(version);

		PC_PBSTNode newNode = new PC_PBSTNode(x, NodePaint.FIND);
		addVisElem(newNode, ZDepth.TOP);

		PC_PBSTNode cur = bst.getRoot(version);
		while (cur != null) {
			newNode.goAbove(cur);
			pause(false);
			if (cur.getKey() == x) {
				newNode.goTo(cur);
				newNode.setPaint(NodePaint.GREEN);
				pause(false);
				newNode.setPaint(NodePaint.NORMAL);
				break;
			} else {
				if (x > cur.getKey())
					cur = cur.getRight();
				else
					cur = cur.getLeft();
			}
		}

		removeVisElem(newNode);
		visualization.setCurAlgVersion(-1);
	}
}
