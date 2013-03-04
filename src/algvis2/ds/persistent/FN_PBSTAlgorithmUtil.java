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

import java.util.Map;
import java.util.Set;

public class FN_PBSTAlgorithmUtil {
	static BinFatNode getLeftChild(BinFatNode node, int version) {
		return getChild(version, node.getLeftChildren().entrySet());
	}

	static BinFatNode getRightChild(BinFatNode node, int version) {
		return getChild(version, node.getRightChildren().entrySet());
	}

	private static BinFatNode getChild(int version, Set<Map.Entry<Integer, BinFatNode>> children) {
		int latestVersion = 0; // TODO ked bude spraveny delete, tak to bude musiet byt asi -1
		BinFatNode res = null;
		for (Map.Entry<Integer, BinFatNode> entry : children) {
			if (entry.getKey() > latestVersion && entry.getKey() <= version) {
				latestVersion = entry.getKey();
				res = entry.getValue();
			}
		}
		if (res instanceof BinFatNode.Null) {
			res = null;
		}
		return res;
	}

	static boolean isLeaf(BinFatNode node, int version) {
		return getLeftChild(node, version) == null && getRightChild(node, version) == null;
	}
}
