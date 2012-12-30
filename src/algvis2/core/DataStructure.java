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

package algvis2.core;

import algvis.core.MyRandom;
import algvis2.scene.control.InputField;
import algvis2.scene.layout.AbsPosition;
import algvis2.scene.layout.ZDepth;
import algvis2.scene.viselem.Node;
import algvis2.scene.viselem.VisElem;
import javafx.animation.Animation;
import javafx.scene.layout.Pane;

public abstract class DataStructure extends VisElem implements PropertyStateEditable, AbsPosition {
	public final Visualization visualization;

	protected DataStructure(Visualization visualization) {
		super(new Pane());
		getNode().setLayoutY(25 + Node.RADIUS * 2.5);
		setZDepth(ZDepth.NODES);
		this.visualization = visualization;
	}

	@Override
	public Pane getNode() {
		return (Pane) super.getNode();
	}

	abstract public String getStats();

	abstract public Algorithm insert(int x);

	abstract public void clear();

	public Animation[] random(int n) {
		Animation[] animations = new Animation[n];
		for (int i = 0; i < n; i++) {
			animations[i] = insert(MyRandom.Int(InputField.MAX_VALUE + 1)).getBackToStart();
		}
		return animations;
	}
}
