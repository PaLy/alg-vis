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
import algvis.ds.dictionaries.bst.BSTNode;
import algvis.ui.Fonts;
import algvis.ui.view.ClickListener;
import algvis.ui.view.View;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Hashtable;

// TODO something more general i.e. T extends VisualElement (instead of BSTNode)
public class ArrayOfPointers<T extends BSTNode> extends VisualElement implements ClickListener {
	private int width;
	private int length = 0;
	private final ArrayList<String> arrayS = new ArrayList<String>();
	private final ArrayList<T> arrayT = new ArrayList<T>();
	private ShadeSubtree shade;
	private final Scene scene;
	
	public ArrayOfPointers(Scene scene, int x, int y, int width, int zDepth) {
		super(zDepth);
		position = new ARPosition(x, y);
		this.width = width;
		this.scene = scene;
	}
	
	public void resetShade() {
		if (shade != null) scene.remove(shade);
		scene.add(shade = new ShadeSubtree(arrayT.get(arrayT.size() - 1)));
	}

	public void add(String s, T n) {
		arrayS.add(s);
		arrayT.add(n);
		length++;
		if (shade != null) scene.remove(shade);
		scene.add(shade = new ShadeSubtree(n));
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
			v.drawArrow(recX + width / 2, recY + width, arrayT.get(i).getPosition().relative.x, 
					arrayT.get(i).getPosition().relative.y 
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

	@Override
	public void mouseClicked(int x, int y) {
		int sx = position.relative.x - length * width / 2;
		int ex = position.relative.x + length * width /2;
		if (y > position.relative.y && y < position.relative.y + width && x > sx && x < ex) {
			x -= sx;
			int version = x / width;
			
			double dx = 0 - arrayT.get(version).getRelativeBoundingBox().getCenterX();
			if (shade != null) scene.remove(shade);
			scene.add(shade = new ShadeSubtree(arrayT.get(version)));
			
			for (T n : arrayT) {
				n.relativeTranslate((int) dx, 0);
			}
		}
	}
}
