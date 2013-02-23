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

import javafx.animation.Transition;

import java.util.HashSet;

public class AutoAnimsManager {
	private final HashSet<Transition> animations = new HashSet<>(); // TODO pozor na memory leak

	public synchronized void add(Transition animation) {
//		animations.add(animation);
	}

	public synchronized void remove(Transition animation) {
//		animations.remove(animation);
	}

	@Deprecated
	public synchronized void endAll() {
		for (Transition animation : animations) {
			animation.jumpTo("end");
		}
		animations.clear();
	}
}
