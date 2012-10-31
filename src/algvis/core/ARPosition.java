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

package algvis.core;

import algvis.core.history.HashtableStoreSupport;

import javax.swing.undo.StateEditable;
import java.awt.*;
import java.util.Hashtable;

public class ARPosition extends Point implements StateEditable {
	private final String hash = Integer.toString(hashCode());
	public final Point relative;

	public ARPosition(int x, int y) {
		super(x, y);
		relative = new Point(x, y);
	}

	@Override
	public void setLocation(double x, double y) {
		double dx = x - this.x;
		double dy = y - this.y;
		super.setLocation(x, y);
		relative.translate((int) dx, (int) dy);
	}

	@Override
	public void move(int x, int y) {
		int dx = x - this.x;
		int dy = y - this.y;
		super.move(x, y);
		relative.translate(dx, dy);
	}

	@Override
	public void translate(int dx, int dy) {
		super.translate(dx, dy);
		relative.translate(dx, dy);
	}

	public void storeState(Hashtable<Object, Object> state) {
		HashtableStoreSupport.store(state, hash + "x", x);
		HashtableStoreSupport.store(state, hash + "y", y);
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		int nx, ny;
		Object x = state.get(hash + "x");
		Object y = state.get(hash + "y");
		if (x != null) nx = (Integer) HashtableStoreSupport.restore(x);
		else nx = this.x;
		if (y != null) ny = (Integer) HashtableStoreSupport.restore(y);
		else ny = this.y;
		move(nx, ny);
	}
}
