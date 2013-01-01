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

package algvis2.scene.viselem;

import algvis2.animation.AutoTranslateTransition;
import algvis2.core.PropertyStateEditable;
import algvis2.ds.dictionary.rb.RB;
import algvis2.scene.Axis;
import algvis2.scene.layout.ZDepth;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashMap;

public class Marker extends VisElem implements PropertyStateEditable {
	private final ScaleChange scaleChange = new ScaleChange();
	public final ObjectProperty<algvis2.scene.viselem.Node> elem = new SimpleObjectProperty<algvis2.scene.viselem.Node>();
	
	public Marker() {
		super(new Circle());
		setZDepth(ZDepth.TOP);
		getNode().setFill(Color.TRANSPARENT);
		getNode().setStroke(Color.BLACK);

		getNode().layoutXProperty().addListener(new AutoTranslateTransition(getNode(), Axis.X));
		getNode().layoutYProperty().addListener(new AutoTranslateTransition(getNode(), Axis.Y));
		
		this.elem.addListener(new ChangeListener<algvis2.scene.viselem.Node>() {
			@Override
			public void changed(ObservableValue<? extends algvis2.scene.viselem.Node> observableValue, algvis2.scene.viselem.Node node, algvis2.scene.viselem.Node node2) {
				if (node != null) {
					node.getNode().scaleXProperty().removeListener(scaleChange);
					node.getNode().scaleYProperty().removeListener(scaleChange);
				}

				if (node2 != null && node2 != RB.NULL) {
					node2.getNode().scaleXProperty().addListener(scaleChange);
					node2.getNode().scaleYProperty().addListener(scaleChange);
					scaleChange.changed(null, null, null);

					getNode().layoutXProperty().bind(node2.visPaneX);
					getNode().layoutYProperty().bind(node2.visPaneY);

					getNode().setVisible(true);
				}else{
					getNode().setVisible(false);
				}
			}
		});
	}

	@Override
	public Circle getNode() {
		return (Circle) super.getNode();
	}

	@Override
	public void storeState(HashMap<Object, Object> state) {
		state.put(elem, elem.get());
	}

	private final class ScaleChange implements ChangeListener<Number> {
		@Override
		public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
			getNode().setRadius(Math.max(elem.get().getNode().getBoundsInParent().getWidth(),
					elem.get().getNode().getBoundsInParent().getHeight()) * 0.5);
		}
	}
}
