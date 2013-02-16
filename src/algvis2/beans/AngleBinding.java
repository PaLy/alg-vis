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

package algvis2.beans;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.value.ObservableDoubleValue;

public class AngleBinding extends DoubleBinding {
	private final ObservableDoubleValue dy, dx;

	public AngleBinding(ObservableDoubleValue dy, ObservableDoubleValue dx) {
		this.dy = dy;
		this.dx = dx;
		super.bind(dy, dx);
	}

	@Override
	protected double computeValue() {
		return Math.atan2(dy.get(), dx.get()) * 180 / Math.PI - 90;
	}
}
