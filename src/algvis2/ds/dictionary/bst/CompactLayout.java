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

import algvis2.scene.shape.Edge;
import algvis2.scene.shape.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.util.DefaultConfiguration;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompactLayout extends BinTreeLayout {
	private final BST bst;

	public CompactLayout(BST bst) {
		this.bst = bst;
	}

	@Override
	protected Pane initPane() {
		return new Pane();
	}

	@Override
	public Pane rebuild(BSTNode root) {
		TreeLayout<BSTNode> layout = new TreeLayout<BSTNode>(bst, new BSTNodeExtentProvider(),
				new DefaultConfiguration<BSTNode>(0, Node.RADIUS));
		Map<BSTNode, Rectangle2D.Double> nodeBounds = layout.getNodeBounds();
		for (Map.Entry<BSTNode, Rectangle2D.Double> entry : nodeBounds.entrySet()) {
			entry.getKey().relocate(entry.getValue().getX(), entry.getValue().getY());
		}
		
		pane.getChildren().clear();
		ArrayList<BSTNode> nodes = new ArrayList<BSTNode>();
		createChildrenR(root, nodes);
		rebuildEdges(root);
		pane.getChildren().addAll(nodes);
		
		return pane;
	}

	private void createChildrenR(BSTNode node, List<BSTNode> list) {
		if (node != null) {
			list.add(node);
			createChildrenR(node.getLeft(), list);
			createChildrenR(node.getRight(), list);
		}
	}
	
	private void rebuildEdges(BSTNode node) {
		if (node.getLeft() != null) {
			Edge leftEdge = new Edge();
			bindEdgeStart(leftEdge, node);
			bindEdgeEnd(leftEdge, node.getLeft(), pane);
			GridPane.setConstraints(leftEdge, 0, 0);
			pane.getChildren().add(leftEdge);
			rebuildEdges(node.getLeft());
		}
		if (node.getRight() != null) {
			Edge rightEdge = new Edge();
			bindEdgeStart(rightEdge, node);
			bindEdgeEnd(rightEdge, node.getRight(), pane);
			GridPane.setConstraints(rightEdge, 0, 0);
			pane.getChildren().add(rightEdge);
			rebuildEdges(node.getRight());
		}
	}
}
