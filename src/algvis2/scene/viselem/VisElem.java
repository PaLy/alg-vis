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

package algvis2.scene.viselem;

import algvis2.scene.layout.ZDepth;
import javafx.scene.Node;

public abstract class VisElem implements Comparable<VisElem> {
	private final Node node;
	private ZDepth zDepth = ZDepth.DEFAULT;
	
	public VisElem(Node node) {
		this.node = node;
	}
	
	public Node getNode() {
		return node;
	}

	public ZDepth getZDepth() {
		return zDepth;
	}

	public void setZDepth(ZDepth zDepth) {
		this.zDepth = zDepth;
	}

	@Override
	public int compareTo(VisElem o) {
		int res = this.zDepth.compareTo(o.zDepth);
		if (res == 0 && !this.equals(o)) res = -1;
		return res;
	}
}
