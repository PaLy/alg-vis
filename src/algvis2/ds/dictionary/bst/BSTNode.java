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

package algvis2.ds.dictionary.bst;

import algvis2.scene.layout.Layout;
import algvis2.scene.shape.Node;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.layout.Pane;

import java.util.HashMap;

public class BSTNode extends Node {
	private BinTreeLayout layout;
	public final ObjectProperty<BSTNode> leftProperty = new SimpleObjectProperty<BSTNode>();
    public final ObjectProperty<BSTNode> rightProperty = new SimpleObjectProperty<BSTNode>();
    
	public BSTNode(int key, String layoutName) {
		super(key);
		layout = (BinTreeLayout) Layout.createLayout(layoutName);
        SonChangeListener sonChangeListener = new SonChangeListener();
        leftProperty.addListener(sonChangeListener);
        rightProperty.addListener(sonChangeListener);
		rebuildLayout();
	}
	
	public void setLeft(BSTNode left) {
		leftProperty.set(left);
	}
	
	public void setRight(BSTNode right) {
		rightProperty.set(right);
	}

	public BSTNode getLeft() {
		return leftProperty.get();
	}

	public BSTNode getRight() {
		return rightProperty.get();
	}
	
	public Pane getLayoutPane() {
		return layout.getPane();
	}

	public void setLayoutR(String layoutName) {
		layout = (BinTreeLayout) Layout.createLayout(layoutName);
		if (leftProperty.get() != null) leftProperty.get().setLayoutR(layoutName);
		if (rightProperty.get() != null) rightProperty.get().setLayoutR(layoutName);
		rebuildLayout();
	}

	public void setLayout(String layoutName) {
		layout = (BinTreeLayout) Layout.createLayout(layoutName);
		rebuildLayout();
		recalcAbsPositionR();
	}
	
	private void rebuildLayout() {
		layout.rebuild(
			this,
			leftProperty.get() == null ? null : leftProperty.get(),
			rightProperty.get() == null ? null : rightProperty.get()
		);
	}
	
	public void recalcAbsPositionR() {
		recalcAbsPosition();
		if (leftProperty.get() != null) leftProperty.get().recalcAbsPositionR();
		if (rightProperty.get() != null) rightProperty.get().recalcAbsPositionR();
	}

    @Override
    public void storeState(HashMap<Object, Object> state) {
        super.storeState(state);
//        System.out.println("YEAH " + leftProperty.hashCode() + " " + leftProperty.get());
        state.put(leftProperty, leftProperty.get());
        state.put(rightProperty, rightProperty.get());
        if (leftProperty.get() != null)
            leftProperty.get().storeState(state); // TODO potom vymazat duplikaty
        if (rightProperty.get() != null)
            rightProperty.get().storeState(state);
    }

    private class SonChangeListener implements ChangeListener<BSTNode> {
        @Override
        public void changed(ObservableValue<? extends BSTNode> observableValue, BSTNode bstNode, BSTNode bstNode1) {
            System.out.println(observableValue + " " + bstNode + " " + bstNode1);
            rebuildLayout();
        }
    }
}
