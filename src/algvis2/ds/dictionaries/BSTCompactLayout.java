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

import algvis2.core.MyNodeExtentProvider;
import algvis2.scene.layout.VisPane;
import algvis2.scene.layout.ZDepth;
import algvis2.scene.viselem.Node;
import org.abego.treelayout.Configuration;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.AbstractTreeForTreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;

import java.awt.geom.Rectangle2D;
import java.util.Map;

class BSTCompactLayout {
	public static void layout(BST bst, VisPane visPane) {
		AbstractTreeForTreeLayout<BSTNode> tree = bst.treeForTreeLayout;
		
		if (tree.getRoot() != null) {
			TreeLayout<BSTNode> layout = new TreeLayout<>(tree, new MyNodeExtentProvider<BSTNode>(),
					new DefaultConfiguration<BSTNode>(0, Node.RADIUS, Configuration.Location.Top,
							Configuration.AlignmentInLevel.AwayFromRoot));
			Map<BSTNode, Rectangle2D.Double> nodeBounds = layout.getNodeBounds();
			for (Map.Entry<BSTNode, Rectangle2D.Double> entry : nodeBounds.entrySet()) {
				if (entry.getKey().getVisual().layoutXProperty().isBound()) {
//					entry.getKey().removePosBinding();
				} else {
					entry.getKey().getVisual().relocate(entry.getValue().getX(), entry.getValue().getY());
				}
			}
		}
		
		visPane.clearLayer(ZDepth.EDGES);
		rebuildEdges(bst.getRoot(), visPane);
		visPane.setDsElements(bst.dump());
	}
	
	private static void rebuildEdges(BSTNode node, VisPane visPane) {
		if (node != null) {
			visPane.addNotStorableVisElemAll(node.getEdges());
			rebuildEdges(node.getLeft(), visPane);
			rebuildEdges(node.getRight(), visPane);
		}
	}
}
