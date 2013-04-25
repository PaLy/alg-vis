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

package algvis2.ds.persistent;

import algvis2.scene.control.InputField;
import algvis2.ui.ButtonsController;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;

public class PDictButtonsController extends ButtonsController {
	public TextField findField;
	public TextField deleteField;
	public TextField findVersion;

	@Override
	protected PersistentDictVisualization getVisualization() {
		return (PersistentDictVisualization) super.getVisualization();
	}

	public void insertPressed(ActionEvent event) {
		disableOperations(true);
		disableNext(true);
		disablePrevious(true);
		getVisualization().insert(new InputField(insertField).getNonEmptyVI().get(0));
	}

	public void deletePressed(ActionEvent event) {
		disableOperations(true);
		disableNext(true);
		disablePrevious(true);
		getVisualization().delete(new InputField(deleteField).getNonEmptyVI().get(0));
	}

	public void findPressed(ActionEvent event) {
		disableOperations(true);
		disableNext(true);
		disablePrevious(true);
		int versionsCount = getVisualization().getDataStructure().getVersionsCount();
		if (versionsCount > 0) {
			getVisualization().find(new InputField(findField).getNonEmptyVI().get(0),
					new InputField(findVersion).getNonEmptyVI(0, versionsCount).get(0));
		}

	}
}
