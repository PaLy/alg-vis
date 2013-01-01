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

package algvis2.ds.dictionary.bst;

import algvis2.scene.layout.VisPane;
import algvis2.scene.layout.ZDepth;
import algvis2.scene.viselem.Edge;
import algvis2.scene.viselem.Node;
import algvis2.scene.viselem.VisElem;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompactLayout {
	public static void layout(BST bst, VisPane visPane) {
		if (bst.getRoot() != null) {
			TreeLayout<BSTNode> layout = new TreeLayout<BSTNode>(bst, new BSTNodeExtentProvider(),
					new DefaultConfiguration<BSTNode>(0, Node.RADIUS));
			Map<BSTNode, Rectangle2D.Double> nodeBounds = layout.getNodeBounds();
			for (Map.Entry<BSTNode, Rectangle2D.Double> entry : nodeBounds.entrySet()) {
				if (entry.getKey().getNode().layoutXProperty().isBound()) {
//					entry.getKey().removeLayoutXYBindings();
				} else {
					entry.getKey().getNode().relocate(entry.getValue().getX(), entry.getValue().getY());
				}
			}
		}
		
		visPane.clearLayer(ZDepth.EDGES);
		ArrayList<VisElem> nodes = new ArrayList<VisElem>();
		if (bst.getRoot() != null) {
			createChildrenR(bst.getRoot(), nodes);
			rebuildEdges(bst.getRoot(), visPane);
		}
		visPane.setDsElements(nodes);
	}

	private static void createChildrenR(BSTNode node, List<VisElem> list) {
		if (node != null) {
			list.add(node);
			createChildrenR(node.getLeft(), list);
			createChildrenR(node.getRight(), list);
		}
	}
	
	private static void rebuildEdges(BSTNode node, VisPane visPane) {
		ArrayList<Edge> edges = node.getEdges();
		for (Edge edge : edges) {
			visPane.addNotStorableVisElem(edge);
		}
		if (node.getLeft() != null) {
			rebuildEdges(node.getLeft(), visPane);
		}
		if (node.getRight() != null) {
			rebuildEdges(node.getRight(), visPane);
		}
	}
}
