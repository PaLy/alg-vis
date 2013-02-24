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

package algvis2.ds.stack;

import algvis2.core.MyNodeExtentProvider;
import algvis2.scene.layout.VisPane;
import algvis2.scene.layout.ZDepth;
import algvis2.scene.viselem.Edge;
import algvis2.scene.viselem.Node;
import org.abego.treelayout.Configuration;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.AbstractTreeForTreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;

import java.awt.geom.Rectangle2D;
import java.util.Map;

public class CompactLayout {
	public static void layout(Stack stack, VisPane visPane) {
		AbstractTreeForTreeLayout<StackNode> stackTree = stack.stackTree;
		
		if (stackTree.getRoot() != null) {
			TreeLayout<StackNode> layout = new TreeLayout<>(stackTree, new MyNodeExtentProvider<StackNode>(),
					new DefaultConfiguration<StackNode>(Node.RADIUS / 2, Node.RADIUS, Configuration.Location.Bottom,
							Configuration.AlignmentInLevel.Center));
			Map<StackNode, Rectangle2D.Double> nodeBounds = layout.getNodeBounds();
			for (Map.Entry<StackNode, Rectangle2D.Double> entry : nodeBounds.entrySet()) {
				if (entry.getKey().getVisual().layoutXProperty().isBound()) {
					// entry.getKey().removePosBinding();
				} else {
					entry.getKey().getVisual().relocate(entry.getValue().getX(), entry.getValue().getY());
				}
			}
		}

		visPane.clearLayer(ZDepth.EDGES);
		rebuildEdges(stackTree.getRoot(), visPane);
		visPane.setDsElements(stack.dump());
	}

	private static void rebuildEdges(StackNode node, VisPane visPane) {
		for (Map.Entry<StackNode, Boolean> entry : node.parentNodes().entrySet()) {
			if (entry.getValue()) {
				Edge edge;
				if (node instanceof StackNode.NullNode) {
					edge = new Edge();
				} else {
					edge = new Edge(Node.RADIUS);
				}
				edge.bindNodes(entry.getKey(), node);
				visPane.addNotStorableVisElem(edge);
				rebuildEdges(entry.getKey(), visPane);
			}
		}
	}
}
