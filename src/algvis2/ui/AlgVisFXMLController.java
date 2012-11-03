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

import algvis2.scene.control.InputField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class AlgVisFXMLController {
	@FXML private TitledPane operationsTitledPane;
	@FXML private Pane visualizationParent;
	@FXML private MenuItem menu_bst;
	@FXML private MenuItem menu_avl;
	@FXML private TextField inputField;
	
	@FXML protected void handleTitledPaneMouseEntered(MouseEvent event) {
		((TitledPane) event.getSource()).setExpanded(true);
	}

	@FXML protected void handleTitlePaneMouseExited(MouseEvent event) {
		((TitledPane) event.getSource()).setExpanded(false);
	}
	
	@FXML protected void selectLanguageEN(ActionEvent event) {
		AlgVis.that.selectLanguage("en");
	}

	@FXML protected void selectLanguageSK(ActionEvent event) {
		AlgVis.that.selectLanguage("sk");
	}

	@FXML protected void selectVisualization(ActionEvent event) {
		Object source = event.getSource();
		if (source.equals(menu_bst)) {
			AlgVis.that.showVisualization(0);
		} else if (source.equals(menu_avl)) {
			AlgVis.that.showVisualization(2);
		}
	}
	
	@FXML protected void insertPressed(ActionEvent event) {
		for (int x : new InputField(inputField).getNonEmptyVI())
			AlgVis.getCurrentVis().getDataStructure().insert(x);
	}

	@FXML protected void randomPressed(ActionEvent event) {
		AlgVis.getCurrentVis().getDataStructure().random(new InputField(inputField).getInt(10));
	}

	@FXML protected void clearPressed(ActionEvent event) {
		AlgVis.getCurrentVis().getDataStructure().clear();
	}
	
	public TitledPane getOperationsPane() {
		return operationsTitledPane;
	}
	
	public Pane getVisualizationParent() {
		return visualizationParent;
	}
}
