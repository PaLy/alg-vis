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
import algvis2.core.Algorithm;
import javafx.animation.FadeTransitionBuilder;
import javafx.animation.ScaleTransition;
import javafx.animation.ScaleTransitionBuilder;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class BSTInsert extends Algorithm {
	private final BST D;
	private final int x;
	
	protected BSTInsert(BST D, int x) {
		super(D);
		this.D = D;
		this.x = x;
	}

	@Override
	public void runAlgorithm() throws InterruptedException {
		BSTNode newNode = new BSTNode(x, D.getLayout());

		if (D.getRoot() == null) {
			D.setRoot(newNode);
		} else {
			newNode.goAbove(D.getRoot()).play();
			BSTNode cur = D.getRoot();
			while (true) {
//                addAnimation(FadeTransitionBuilder.create()
//                        .node(cur)
//                        .fromValue(1)
//                        .toValue(0.1)
//                        .duration(Duration.millis(250))
//                        .autoReverse(true)
//                        .cycleCount(4)
//                        .build()
//                );
//                addAnimation(Animations.backlight(cur, Color.ORANGE, visualization.visPane));
				pause();
				if (x > cur.getKey()) {
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
        
		addAnimation(st);
	}
}
