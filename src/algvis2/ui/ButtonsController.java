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

package algvis2.ui;

import algvis2.core.Visualization;
import algvis2.scene.control.InputField;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ButtonsController implements Initializable {
	public TextField insertField;
	public VBox operationsButtons;
	public Button buttonNext;
	public Button buttonPrevious;
	public CheckBox buttonPause;
	
	protected Visualization visualization;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public void randomPressed(ActionEvent event) {
		Animation animation = visualization.getDataStructure()
				.random(new InputField(insertField).getInt(10));

		List<Animation> wrapper = new ArrayList<Animation>();
		wrapper.add(new SequentialTransition(animation));

		visualization.animManager.add(wrapper, true);
		disablePrevious(false);

		visualization.visPane.refresh(); // TODO co?? ...jaaaj to kvoli tomu blbemu pcBST; casom to pojde prec
		visualization.reLayout();
	}

	public void clearPressed(ActionEvent event) {
		visualization.getDataStructure().clear();
		visualization.visPane.clearPane();
		visualization.reLayout();
		visualization.animManager.clear();

		disableNext(true);
		disablePrevious(true);

		visualization.getVisPane().setTranslatePos(0, 0);
	}

	public void previousPressed(ActionEvent event) {
		disableOperations(true);
		disableNext(true);
		disablePrevious(true);
		visualization.animManager.playPrevious();
	}

	public void nextPressed(ActionEvent event) {
		disableOperations(true);
		disableNext(true);
		disablePrevious(true);
		visualization.animManager.playNext();
	}
	
	public void disableOperations(boolean value) {
		operationsButtons.setDisable(value);
	}
	public void disableNext(boolean value) {
		buttonNext.setDisable(value);
	}

	public void disablePrevious(boolean value) {
		buttonPrevious.setDisable(value);
	}
	
	public void setPausesSelected(boolean value) {
		buttonPause.setSelected(value);
	}

	public boolean isPauseSelected() {
		return buttonPause.isSelected();
	}

	public void setVis(Visualization visualization) {
		this.visualization = visualization;
	}
}
