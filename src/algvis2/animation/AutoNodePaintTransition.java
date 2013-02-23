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

import algvis2.scene.paint.NodePaint;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.shape.Shape;

public class AutoNodePaintTransition implements AutoAnimation, ChangeListener<NodePaint> {
	private final AutoFillTransition backgroundTransition, textTransition;
	
	public AutoNodePaintTransition(Shape background, Shape text) {
		backgroundTransition = new AutoFillTransition(background);
		textTransition = new AutoFillTransition(text);
	}

	@Override
	public void changed(ObservableValue<? extends NodePaint> observable, NodePaint oldValue, NodePaint newValue) {
		backgroundTransition.changed(null, oldValue.getBackground(), newValue.getBackground());
		textTransition.changed(null, oldValue.getText(), newValue.getText());
	}
}
