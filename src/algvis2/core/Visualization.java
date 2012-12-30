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

import algvis2.ds.dictionary.bst.BST;
import algvis2.ds.dictionary.bst.CompactLayout;
import algvis2.scene.layout.VisPane;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public abstract class Visualization implements PropertyStateEditable {
	public final VisPane visPane;
	protected Buttons buttons;
	protected DataStructure dataStructure;
	public final AnimationManager animManager = new AnimationManager(this);

	public Visualization() {
		init();
		visPane = new VisPane(dataStructure);
	}

	/**
	 * init buttonsPane and dataStructure
	 */
	protected abstract void init();

	public Pane getVisPaneWrapper() {
		return visPane.getWrappingPane();
	}
	
	public VisPane getVisPane() {
		return visPane;
	}

	public Pane getButtonsPane(String lang) {
		return buttons.getPane(lang);
	}

	public Buttons getButtons() {
		return buttons;
	}

	public DataStructure getDataStructure() {
		return dataStructure;
	}
	
	public void reLayout() {
		CompactLayout.layout((BST) dataStructure, visPane);
		visPane.refresh();
	}

	@Override
	public void storeState(HashMap<Object, Object> state) {
		visPane.storeState(state);
		dataStructure.storeState(state);
	}
}
