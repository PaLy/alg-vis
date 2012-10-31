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

package algvis.core.visual;

import java.awt.geom.Rectangle2D;
import java.util.ConcurrentModificationException;
import java.util.Hashtable;

import javax.swing.undo.StateEditable;

import algvis.core.ARPosition;
import algvis.ui.view.View;

public abstract class VisualElement implements StateEditable {
	protected final String hash = Integer.toString(hashCode());
	protected int zDepth;
	public ARPosition position;

	protected VisualElement(int zDepth) {
		this.zDepth = zDepth;
	}

	public int getZDepth() {
		return zDepth;
	}

	// public void setZDepth(int zDepth) {
	// if (zDepth != this.zDepth) {
	// scene.changeZDepth(this, this.zDepth, zDepth);
	// this.zDepth = zDepth;
	// }
	// }

	protected abstract void draw(View v) throws ConcurrentModificationException;

	protected abstract void move() throws ConcurrentModificationException;
	
	public void relativeTranslate(int dx, int dy){
	}

	protected abstract Rectangle2D getBoundingBox();
	
	protected Rectangle2D getRelativeBoundingBox() {
		return null;
	}
	
	public void resetRelativePosition() {
	}

	protected void endAnimation() {
	}

	protected boolean isAnimationDone() {
		return true;
	}

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		// HashtableStoreSupport.store(state, hash + "zDepth", zDepth);
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		// Object zDepth = state.get(hash + "zDepth");
		// if (zDepth != null) setZDepth((Integer)
		// HashtableStoreSupport.restore(zDepth));
	}

	public ARPosition getPosition() {
		return position;
	}
}
