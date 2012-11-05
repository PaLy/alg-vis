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

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public interface Layout {
	public static final String BIN_TREE_LAYOUT = "BinTreeLayout";
	public static final String LEFT_BIN_TREE_LAYOUT = "LeftBinTreeLayout";
	public static final String RIGHT_BIN_TREE_LAYOUT = "RightBinTreeLayout";
	
	public void rebuild(Node... nodes);	
	public Pane getPane();
	
	public static class Creator {
		public static Layout get(String layoutName) {
			if (layoutName.equals(BIN_TREE_LAYOUT)) return new BinTreeLayout();
			else if (layoutName.equals(LEFT_BIN_TREE_LAYOUT)) return new LeftBinTreeLayout();
			else if (layoutName.equals(RIGHT_BIN_TREE_LAYOUT)) return new RightBinTreeLayout();
			else return null;
		}
	}
}
