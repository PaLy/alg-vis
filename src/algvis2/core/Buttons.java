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

package algvis2.core;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class Buttons {
	private Pane pane;
	private final URL fxmlUrl;
	private final State state = new State();

	public Buttons(URL fxmlUrl) {
		this.fxmlUrl = fxmlUrl;
	}

	public Pane getPane(String lang) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setResources(ResourceBundle.getBundle("Messages",
				new Locale(lang)));
		Pane parent = null;
		try {
			parent = (Pane) fxmlLoader.load(fxmlUrl.openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		pane = parent;
		state.update();
		return parent;
	}

	public void setDisabled(boolean disabled) {
		pane.lookup("#buttonInsert").setDisable(disabled);
		Node find = pane.lookup("#buttonFind");
		if (find != null) {
			find.setDisable(disabled);
		}
		pane.lookup("#buttonDelete").setDisable(disabled);
		pane.lookup("#buttonClear").setDisable(disabled);
		Node random = pane.lookup("#buttonRandom");
		if (random != null) {
			random.setDisable(disabled);
		}
		pane.lookup("#buttonPause").setDisable(disabled);
		state.operations = disabled;
	}
	
	public void disableNext(boolean disabled) {
		pane.lookup("#buttonNext").setDisable(disabled);
		state.next = disabled;
	}

	public void disablePrevious(boolean disabled) {
		pane.lookup("#buttonPrevious").setDisable(disabled);
		state.previous = disabled;
	}
	
	public boolean isPauseChecked() {
		return ((CheckBox) pane.lookup("#buttonPause")).isSelected();
	}
	
	private final class State {
		private boolean operations = false;
		private boolean previous = true;
		private boolean next = true;
		private BooleanProperty pauseSelected = new SimpleBooleanProperty();

		private void update() {
			setDisabled(operations);
			disableNext(next);
			disablePrevious(previous);
			if (pauseSelected.getValue() != null) {
				((CheckBox) pane.lookup("#buttonPause")).setSelected(pauseSelected.getValue());
			}
			pauseSelected.bind(((CheckBox) pane.lookup("#buttonPause")).selectedProperty());
		}
	}
}
