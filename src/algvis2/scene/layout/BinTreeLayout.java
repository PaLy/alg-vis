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

package algvis2.scene.layout;

import org.abego.treelayout.Configuration;
import org.abego.treelayout.NodeExtentProvider;
import org.abego.treelayout.TreeLayout;

public class BinTreeLayout<TreeNode> extends TreeLayout<TreeNode> {
	public BinTreeLayout(BinTreeForTreeLayout<TreeNode> tree,
			NodeExtentProvider<TreeNode> nodeExtentProvider, Configuration<TreeNode> configuration) {
		super(tree, nodeExtentProvider, configuration);
	}

	@Override
	public BinTreeForTreeLayout<TreeNode> getTree() {
		return (BinTreeForTreeLayout<TreeNode>) super.getTree();
	}

	@Override
	protected double centerBetweenChildren(TreeNode v) {
		TreeNode llch = getTree().getLastLeftChild(v);
		TreeNode frch = getTree().getFirstRightChild(v);
		if (llch == null && frch != null) {
			return getPrelim(frch)
					- (getNodeSize(v) + getConfiguration().getGapBetweenNodes(null, null)) / 2;
		} else if (frch == null && llch != null) {
			return getPrelim(llch)
					+ (getNodeSize(v) + getConfiguration().getGapBetweenNodes(null, null)) / 2;
		} else {
			return (getPrelim(llch) + getPrelim(frch)) / 2.0;	
		}
	}
}
