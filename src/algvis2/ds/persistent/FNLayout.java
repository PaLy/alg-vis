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

import algvis2.core.MyNodeExtentProvider;
import algvis2.scene.layout.VisPane;
import algvis2.scene.layout.ZDepth;
import algvis2.scene.viselem.AnnotatedEdge;
import algvis2.scene.viselem.Edge;
import algvis2.scene.viselem.Node;
import org.abego.treelayout.Configuration;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.AbstractTreeForTreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;

import java.awt.geom.Rectangle2D;
import java.util.Map;
import java.util.Set;

public class FNLayout {
	public static void layout(FN_PBST bst, VisPane visPane) {
		AbstractTreeForTreeLayout<BinFatNode> tree = bst.treeForTreeLayout;

		if (tree.getRoot() != null) {
			TreeLayout<BinFatNode> layout = new TreeLayout<>(tree,
					new MyNodeExtentProvider<BinFatNode>(), new DefaultConfiguration<BinFatNode>(
							Node.RADIUS, Node.RADIUS, Configuration.Location.Top,
							Configuration.AlignmentInLevel.TowardsRoot));
			Map<BinFatNode, Rectangle2D.Double> nodeBounds = layout.getNodeBounds();
			for (Map.Entry<BinFatNode, Rectangle2D.Double> entry : nodeBounds.entrySet()) {
				if (entry.getKey().getVisual().layoutXProperty().isBound()) {
					//					entry.getKey().removePosBinding();
					//					System.out.println("nie");
				} else {
					//					System.out.println("Moving " +entry.getKey().getKey() + " " + entry);
					entry.getKey().getVisual()
							.relocate(entry.getValue().getX(), entry.getValue().getY());
				}
			}
		}

		visPane.clearLayer(ZDepth.EDGES);
		if (tree.getRoot() != null) {
			rebuildEdges(tree.getRoot(), visPane);
		}
		visPane.setDsElements(bst.dump());
	}

	private static void rebuildEdges(BinFatNode parentNode, VisPane visPane) {
		rebuildChildren(parentNode.getLeftChildren().entrySet(), parentNode, visPane);
		rebuildChildren(parentNode.getRightChildren().entrySet(), parentNode, visPane);
	}

	private static void rebuildChildren(Set<Map.Entry<Integer, BinFatNode>> children,
			BinFatNode parentNode, VisPane visPane) {
		for (Map.Entry<Integer, BinFatNode> child : children) {
			Edge edge;
			if (child.getValue() instanceof BinFatNode.Null) {
				edge = new AnnotatedEdge(child.getKey());
			} else {
				edge = new AnnotatedEdge(child.getKey(), Node.RADIUS);
			}
			edge.bindNodes(parentNode, child.getValue());
			visPane.addNotStorableVisElem(edge);
			rebuildEdges(child.getValue(), visPane);
		}
	}
}
