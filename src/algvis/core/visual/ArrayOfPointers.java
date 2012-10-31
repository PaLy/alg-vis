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

import algvis.core.ARPosition;
import algvis.core.Node;
import algvis.core.history.HashtableStoreSupport;
import algvis.ui.Fonts;
import algvis.ui.view.View;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Hashtable;

public class ArrayOfPointers extends VisualElement {
	private int width;
	private ARPosition position;
	private int length = 0;
	private final ArrayList<String> arrayS = new ArrayList<String>();
	private final ArrayList<Node> arrayN = new ArrayList<Node>();
	
	public ArrayOfPointers(int x, int y, int width, int zDepth) {
		super(zDepth);
		position = new ARPosition(x, y);
		this.width = width;
	}

	public void add(String s, Node n) {
		arrayS.add(s);
		arrayN.add(n);
		length++;
	}
	
	@Override
	protected void draw(View v) throws ConcurrentModificationException {
		int r = length * width / 2;
		int recX = position.relative.x - r;
		int recY = position.relative.y;
		v.setColor(Color.BLACK);
		for (int i = 0; i < length; i++) {
			v.drawRectangle(new Rectangle.Double(recX, recY, width, width));
			v.drawString(arrayS.get(i), recX + width / 2, recY + width / 2, Fonts.NORMAL);
			v.drawArrow(recX + width / 2, recY + width, arrayN.get(i).position.relative.x, arrayN.get(i).position.relative.y 
					- Node.RADIUS);
			recX += width;
		}
	}

	@Override
	protected void move() throws ConcurrentModificationException {
		// TODO
	}

	@Override
	protected Rectangle2D getBoundingBox() {
		return null; // TODO
	}

	@Override
	public void storeState(Hashtable<Object, Object> state) {
		super.storeState(state);
		HashtableStoreSupport.store(state, hash + "length", length);
	}

	@Override
	public void restoreState(Hashtable<?, ?> state) {
		super.restoreState(state);
		Object length = state.get(hash + "length");
		if (length != null) this.length = (Integer) HashtableStoreSupport.restore(length);
	}
}
