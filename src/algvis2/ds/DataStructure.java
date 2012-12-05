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

package algvis2.ds;

import algvis.core.MyRandom;
import algvis2.core.PropertyStateEditable;
import algvis2.core.Visualization;
import algvis2.scene.control.InputField;
import algvis2.scene.layout.VisPane;
import algvis2.scene.layout.ZDepth;
import algvis2.scene.shape.Node;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.geometry.Insets;
import javafx.scene.layout.FlowPane;

public abstract class DataStructure implements PropertyStateEditable {
	protected final VisPane visPane;
	public final Visualization visualization;
	protected DSDefaultLayout dsLayout = new DSDefaultLayout();
	protected String layoutName;

	protected DataStructure(Visualization visualization) {
		this.visPane = visualization.visPane;
		this.visualization = visualization;
		visPane.add(dsLayout.getPane(), ZDepth.NODES);
		FlowPane.setMargin(dsLayout.getPane(), new Insets(Node.RADIUS * 2.5));
	}

	abstract public String getStats();

	abstract public Animation[] insert(int x);

	abstract public void clear();

	public Animation[] random(int n) {
		SequentialTransition pt = new SequentialTransition();
		SequentialTransition back = new SequentialTransition();
		for (int i = 0; i < n; i++) {
			Animation[] animations = insert(MyRandom
					.Int(InputField.MAX_VALUE + 1));
			pt.getChildren().add(animations[0]);
			back.getChildren().add(animations[1]);
		}
		return new Animation[] { pt, back };
	}

	public void setLayout(String layoutName) {
		this.layoutName = layoutName;
	}

	public String getLayout() {
		return layoutName;
	}

	public abstract void reLayout();
}
