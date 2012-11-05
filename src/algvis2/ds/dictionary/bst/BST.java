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

import algvis.core.MyRandom;
import algvis2.core.Dictionary;
import algvis2.scene.layout.Layouts;
import algvis2.scene.layout.VisPane;
import algvis2.scene.layout.ZDepth;
import javafx.animation.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class BST extends Dictionary {
	public static final String DEF_LAYOUT = Layouts.BIN_TREE_LAYOUT;
	private BSTNode root = null;
	
	public BST(VisPane visPane) {
		super(visPane);
		layoutName = DEF_LAYOUT;
	}

	@Override
	public void find(int x) {
		// TODO
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
	public void insert(int x) {
		BSTNode newNode = new BSTNode(x, layoutName);
		
		if (root == null) {
			setRoot(newNode);
		} else {
			BSTNode cur = root;
			while (true) {
				if (x > cur.key) {
					if (cur.getRight() == null) {
						cur.setRight(newNode);
						break;
					} else {
						cur = cur.getRight();
					}
				} else {
					if (cur.getLeft() == null) {
						cur.setLeft(newNode);
						break;
					} else {
						cur = cur.getLeft();
					}
				}
			}
		}

		ScaleTransition st = ScaleTransitionBuilder.create()
			.node(newNode)
			.byX(0.5)
			.byY(0.5)
			.duration(Duration.millis(500))
			.cycleCount(2)
			.autoReverse(true)
			.build();

		FadeTransition ft = FadeTransitionBuilder.create()
			.node(newNode)
			.toValue(0.1)
			.duration(Duration.millis(500))
			.autoReverse(true)
			.cycleCount(Timeline.INDEFINITE)
			.build();
		
		Color color = Color.rgb(MyRandom.Int(256), MyRandom.Int(256), MyRandom.Int(256));
		
		FillTransition fillT = FillTransitionBuilder.create()
			.shape(newNode.getShape())
			.toValue(color)
			.duration(Duration.seconds(1.5))
			.autoReverse(true)
			.cycleCount(Timeline.INDEFINITE)
			.build();
		
//		new SequentialTransition(st, new ParallelTransition(fillT, ft)).play();
		new SequentialTransition(st, fillT).play();
	}

	private void setRoot(BSTNode root) {
		if (this.root != null) visPane.remove(this.root.getLayout());
		this.root = root;
		if (this.root != null) visPane.add(this.root.getLayout(), ZDepth.NODES);
	}

	@Override
	public void clear() {
		setRoot(null);
	}

	@Override
	public void setLayout(String layoutName) {
		super.setLayout(layoutName);
		if (root != null) {
			visPane.remove(root.getLayout());
			root.setLayoutRec(layoutName);
			visPane.add(root.getLayout(), ZDepth.NODES);
		} 
	}
}
