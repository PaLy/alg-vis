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
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Node;

import java.util.Comparator;

public abstract class VisElem {
	private Node visual;
	private ZDepth zDepth;
	/**
	 * refresh of visual
	 */
	protected final BooleanProperty refreshAllowed = new SimpleBooleanProperty(false);
	
	public VisElem(Node visual) {
		this.visual = visual;
	}
	
	public Node getVisual() {
		return visual;
	}

	public ZDepth getZDepth() {
		return zDepth;
	}
	
	public void allowRefresh(boolean value) {
		refreshAllowed.set(value);
	}

	public void setZDepth(ZDepth zDepth) {
		this.zDepth = zDepth;
	}
	
	public static final Comparator<VisElem> ZDEPTH_COMPARATOR = new Comparator<VisElem>() {
		@Override
		public int compare(VisElem o1, VisElem o2) {
			return o1.zDepth.compareTo(o2.zDepth);
		}
	};
}
