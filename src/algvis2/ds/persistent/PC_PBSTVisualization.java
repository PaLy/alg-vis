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

import algvis2.core.DataStructure;
import algvis2.core.MyNodeExtentProvider;
import algvis2.scene.layout.BinTreeForTreeLayout;
import algvis2.scene.layout.BinTreeLayout;
import algvis2.scene.layout.ZDepth;
import algvis2.scene.viselem.Edge;
import algvis2.scene.viselem.Node;
import org.abego.treelayout.Configuration;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;

import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Map;

public class PC_PBSTVisualization extends PersistentDictVisualization {
	public PC_PBSTVisualization() {
		super(PC_PBSTVisualization.class.getResource("/algvis2/ds/persistent/PDictButtons.fxml"));
		reLayout();
	}

	@Override
	public String getTitle() {
		return "Persistent BST with path copying";
	}

	@Override
	protected DataStructure initDS() {
		return new PC_PBST(this);
	}

	@Override
	public void reLayout() {
		layout();
		visPane.refresh();
	}

	@Override
	public PC_PBST getDataStructure() {
		return (PC_PBST) super.getDataStructure();
	}

	private void layout() {
		BinTreeForTreeLayout<PC_PBSTNode> tree = getDataStructure().treeForTreeLayout;

		double gapBetweenLevels = Node.RADIUS;
		TreeLayout<PC_PBSTNode> layout = new BinTreeLayout<>(tree,
				new MyNodeExtentProvider<PC_PBSTNode>(), new DefaultConfiguration<PC_PBSTNode>(
						gapBetweenLevels, Node.RADIUS, Configuration.Location.Top,
						Configuration.AlignmentInLevel.Center));
		Map<PC_PBSTNode, Rectangle2D.Double> nodeBounds = layout.getNodeBounds();
		for (Map.Entry<PC_PBSTNode, Rectangle2D.Double> entry : nodeBounds.entrySet()) {
			if (entry.getKey().getVisual().layoutXProperty().isBound()) {
				//					entry.getKey().removePosBinding();
				//					System.out.println("nie");
				System.out.println("NOOOOOOOOOO");
			} else {
				//					System.out.println("Moving " +entry.getKey().getKey() + " " + entry);
				entry.getKey()
						.getVisual()
						.relocate(entry.getValue().getX(),
								entry.getValue().getY() - gapBetweenLevels - Node.RADIUS * 2);
			}
		}

		visPane.clearLayer(ZDepth.EDGES);
		HashSet<PC_PBSTNode> done = new HashSet<>();
		for (PC_PBSTNode node : getDataStructure().getFakeRoot().getChildren()) {
			rebuildEdges(node, done);
		}
		visPane.setDsElements(getDataStructure().dump());
	}

	private void rebuildEdges(PC_PBSTNode parentNode, HashSet<PC_PBSTNode> done) {
		done.add(parentNode);
		PC_PBSTNode child = parentNode.getLeft();
		if (child != null) {
			rebuildChildren(child, parentNode);
			if (!done.contains(child)) {
				rebuildEdges(child, done);
			}
		}
		child = parentNode.getRight();
		if (child != null) {
			rebuildChildren(child, parentNode);
			if (!done.contains(child)) {
				rebuildEdges(child, done);
			}
		}
	}

	private void rebuildChildren(PC_PBSTNode child, PC_PBSTNode parentNode) {
		Edge edge = new Edge(Node.RADIUS);
		edge.bindNodes(parentNode, child);
		visPane.addNotStorableVisElem(edge);
	}
}
