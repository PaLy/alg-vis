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
import algvis2.scene.viselem.TreeHighlighter;

public class FN_PBSTFind extends Algorithm {
	private final FN_PBST bst;
	private final int version, x;
	protected BinFatNode found = null;
	private final PersistentVisualization visualization;

	public FN_PBSTFind(PersistentVisualization visualization, FN_PBST bst, int version, int x) {
		super(visualization);
		this.bst = bst;
		this.version = version;
		this.x = x;
		this.visualization = visualization;
	}

	@Override
	protected void runAlgorithm() {
		if (version == 0) {
			// TODO nic, prazdny strom neobsahuje ziadny vrchol
		} else {
			TreeHighlighter treeHighlighter = visualization.getAlgorithmHighlighter(version);
			addVisElem(treeHighlighter);
			
			BinFatNode newNode = new BinFatNode(x, NodePaint.FIND);
			addVisElem(newNode, ZDepth.TOP);

			BinFatNode cur = bst.getRoot();
			while (cur != null) {
				newNode.goAbove(cur);
				pause(false);
				if (cur.getKey() == x) {
					found = cur;
					newNode.goTo(cur);
					newNode.setPaint(NodePaint.GREEN);
					pause(false);
					newNode.setPaint(NodePaint.NORMAL);
					break;
				} else {
					if (cur.getKey() < x)
						cur = cur.getRightChild(version);
					else
						cur = cur.getLeftChild(version);
				}
			}

			removeVisElem(newNode);
			removeVisElem(treeHighlighter);
			visualization.highlightOff();
		}
	}
}
