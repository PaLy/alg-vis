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

package algvis2.animation;

import algvis2.scene.viselem.Node;
import javafx.animation.Animation;
import javafx.animation.ScaleTransitionBuilder;
import javafx.util.Duration;

public class AnimationFactory {
	// Suppress default constructor for noninstantiability
	private AnimationFactory() {
		throw new AssertionError();
	}

	public static Animation scaleInOut(Node node) {
		return ScaleTransitionBuilder.create().node(node.getVisual()).byX(0.5).byY(0.5)
				.duration(Duration.millis(500)).cycleCount(2).autoReverse(true).build();
	}

}
