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

import algvis2.ui.AlgVis;
import javafx.animation.FillTransition;
import javafx.animation.FillTransitionBuilder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

public class AutoFillTransition implements AutoAnimation, ChangeListener<Paint> {
	private final Shape shape;
	private FillTransition currentTransition;

	public AutoFillTransition(Shape shape) {
		this.shape = shape;
	}

	@Override
	public void stop() {
		currentTransition.jumpTo("end");
	}

	@Override
	public void changed(ObservableValue<? extends Paint> observableValue,
			Paint oldPaint, Paint newPaint) {
		if (oldPaint != newPaint && oldPaint instanceof Color
				&& newPaint instanceof Color) {
			FillTransition fillTransition = FillTransitionBuilder.create()
					.toValue((Color) newPaint).shape(shape)
					.duration(Duration.seconds(1))
					.onFinished(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							AlgVis.autoAnimsManager
									.remove(AutoFillTransition.this);
						}
					}).build();
			if (currentTransition != null) {
				currentTransition.pause();
				fillTransition.setFromValue((Color) shape.getFill());
			} else {
				fillTransition.setFromValue((Color) oldPaint);
			}
			currentTransition = fillTransition;
			AlgVis.autoAnimsManager.add(this);
			fillTransition.play();
		}
	}
}
