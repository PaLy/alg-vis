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

public class PStackButtonsController extends ButtonsController {
	public TextField versionPushField;
	public TextField versionPopField;

	@Override
	protected PStackVisualization getVisualization() {
		return (PStackVisualization) super.getVisualization();
	}

	public void pushPressed(ActionEvent event) {
		disableAllButtons(true);
		int versionsCount = getVisualization().getDataStructure().getVersionsCount();
		getVisualization().push(new InputField(insertField).getNonEmptyVI().get(0),
				new InputField(versionPushField).getNonEmptyVI(0, versionsCount).get(0));
	}

	public void popPressed(ActionEvent event) {
		disableAllButtons(true);
		int versionsCount = getVisualization().getDataStructure().getVersionsCount();
		boolean ok = getVisualization().pop(
				new InputField(versionPopField).getNonEmptyVI(0, versionsCount).get(0));
		if (!ok) {
			disableOperations(false);
			disableNext(!getVisualization().animManager.hasNext());
			disablePrevious(!getVisualization().animManager.hasPrevious());
		}
	}
}
