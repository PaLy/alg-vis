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

import algvis2.scene.layout.VisPane;
import javafx.scene.layout.Pane;

public abstract class Visualization {
	protected final VisPane visPane;
	protected Buttons buttons;
	protected DataStructure dataStructure;

	public Visualization() {
		visPane = new VisPane();
		init();
	}

	/**
	 * init buttonsPane and dataStructure
	 */
	protected abstract void init();

	public Pane getRootPane() {
		return visPane;
	}

	public Pane getButtonsPane(String lang) {
		return buttons.getPane(lang);
	}
	
	public DataStructure getDataStructure() {
		return dataStructure;
	}
}
