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
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ButtonsController implements Initializable {
	public TextField insertField;
	public VBox operationsButtons;
	public Button buttonNext;
	public Button buttonPrevious;
	public CheckBox buttonPause;

	private Visualization visualization;

	protected Visualization getVisualization() {
		return visualization;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public void randomPressed(ActionEvent event) {
		disableAllButtons(true);
		visualization.random(new InputField(insertField).getInt(10));
		disableOperations(false);
		disablePrevious(false);
	}

	public void clearPressed(ActionEvent event) {
		disableAllButtons(true);
		visualization.clear();
		disableOperations(false);
	}

	public void previousPressed(ActionEvent event) {
		disableAllButtons(true);
		visualization.animManager.playPrevious();
	}

	public void nextPressed(ActionEvent event) {
		disableAllButtons(true);
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

	public void disableAllButtons(boolean value) {
		disableOperations(value);
		disablePrevious(value);
		disableNext(value);
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
