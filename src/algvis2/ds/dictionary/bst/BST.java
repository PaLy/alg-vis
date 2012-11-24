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

import algvis2.core.Visualization;
import algvis2.ds.dictionary.Dictionary;
import algvis2.scene.layout.Layout;
import javafx.animation.Animation;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.util.HashMap;

public class BST extends Dictionary {
	public static final String DEF_LAYOUT = Layout.BIN_TREE_LAYOUT;
	public final ObjectProperty<BSTNode> rootProperty = new SimpleObjectProperty<BSTNode>();
    // TODO niekedy sa pokazi (asi layout?) pri zmene layout sa to opravi (okrem rootovych synov?)
	
	public BST(Visualization visualization) {
		super(visualization);
		layoutName = DEF_LAYOUT;
        rootProperty.addListener(new ChangeListener<BSTNode>() {
            @Override
            public void changed(ObservableValue<? extends BSTNode> observableValue, BSTNode oldNode, BSTNode newNode) {
                if (newNode != null) {
                    newNode.removeLayoutXYBindings();
                    dsLayout.rebuild(newNode.getLayoutPane());
                } else {
                    dsLayout.rebuild();
                }
                visPane.setTranslateX(0);
                visPane.setTranslateY(0);
            }
        });
	}

	@Override
	public Animation[] find(int x) {
        BSTFind bstFind = new BSTFind(this, x);
        return new Animation[]{bstFind.runA(), bstFind.getBackToStart()};
	}

	@Override
	public void delete(int x) {
		// TODO
	}

	@Override
	public String getStats() {
		return null; // TODO
	}

	@Override
	public Animation[] insert(int x) {
        BSTInsert bstInsert = new BSTInsert(this, x);
        return new Animation[]{bstInsert.runA(), bstInsert.getBackToStart()};
	}
	
	public BSTNode getRoot() {
		return rootProperty.get();
	}

	public void setRoot(BSTNode root) {
        rootProperty.set(root);
	}

	@Override
	public void clear() {
		setRoot(null);
	}

	@Override
	public void setLayout(String layoutName) {
		super.setLayout(layoutName);
		if (rootProperty.get() != null) {
			rootProperty.get().setLayoutR(layoutName);
            dsLayout.rebuild(getRoot().getLayoutPane());
		} 
	}

	@Override
	public void storeState(HashMap<Object, Object> state) {
        state.put(rootProperty, rootProperty.get());
        if (rootProperty.get() != null) rootProperty.get().storeState(state);
	}
}
