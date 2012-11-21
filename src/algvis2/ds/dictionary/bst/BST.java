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

import algvis2.animation.Animations;
import algvis2.core.Visualization;
import algvis2.ds.dictionary.Dictionary;
import algvis2.scene.layout.Layout;
import algvis2.scene.paint.NodePaint;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.HashMap;

public class BST extends Dictionary {
	public static final String DEF_LAYOUT = Layout.BIN_TREE_LAYOUT;
	public final ObjectProperty<BSTNode> rootProperty = new SimpleObjectProperty<BSTNode>();
	// TODO vsade namiesto getValue() dat get()
    // TODO niekedy sa pokazi (asi layout?) pri zmene layout sa to opravi (okrem rootovych synov?)
	
	public BST(Visualization visualization) {
		super(visualization);
		layoutName = DEF_LAYOUT;
        rootProperty.addListener(new ChangeListener<BSTNode>() {
            @Override
            public void changed(ObservableValue<? extends BSTNode> observableValue, BSTNode bstNode, BSTNode bstNode1) {
                wrappingPane.getChildren().clear(); // TODO nejak by mal vediet vymazat len seba
                if (bstNode1 != null) wrappingPane.getChildren().add(bstNode1.getLayout().getPane());
                // zmenit na getLayoutPane()?
            }
        });
	}

	@Override
	public Animation find(int x) {
		SequentialTransition st = new SequentialTransition();
		BSTNode cur = rootProperty.get();
		while (cur != null) {
			st.getChildren().add(Animations.backlight(cur, Color.ORANGE, visPane));
			if (cur.getKey() == x) {
				final BSTNode finalCur = cur;
				st.getChildren().add(new Timeline(
					new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							finalCur.setPaint(NodePaint.GREEN);
						}
					})));
				break;
			} else {
				final BSTNode finalCur = cur;
				st.getChildren().add(new Timeline(
					new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							finalCur.setPaint(NodePaint.FIND);
						}
					}))
				);
				if (cur.getKey() < x) cur = cur.getRight();
				else cur = cur.getLeft();
			}
		}
		return st;
	}

	@Override
	public Animation delete(int x) {
		// TODO
		return null;
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
			wrappingPane.getChildren().clear();
			rootProperty.get().setLayoutR(layoutName);
			wrappingPane.getChildren().add(rootProperty.get().getLayout().getPane());
		} 
	}

	@Override
	public void storeState(HashMap<Object, Object> state) {
        state.put(rootProperty, rootProperty.get());
        if (rootProperty.get() != null) rootProperty.get().storeState(state);
        // TODO mozno nepotrebne? co ak nebude na scene?
	}
}
