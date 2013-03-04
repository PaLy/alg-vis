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

package algvis2.ds.dictionaries;

import algvis2.scene.paint.NodePaint;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

class RBNode extends BSTNode {
	private final BooleanProperty redProperty = new SimpleBooleanProperty();

	public RBNode(int key) {
		super(key, NodePaint.RED);
		redProperty.set(true);

		redProperty.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> observableValue,
					Boolean oldValue, Boolean newValue) {
				if (newValue)
					setPaint(NodePaint.RED);
				else
					setPaint(NodePaint.BLACK);
			}
		});
	}

	public void setRed(boolean red) {
		redProperty.set(red);
	}

	public boolean isRed() {
		return redProperty.get();
	}

	@Override
	public RBNode getLeft() {
		return RB.NULL == super.getLeft() ? null : (RBNode) super.getLeft();
	}

	public RBNode getLeft2() {
		return super.getLeft() == null ? RB.NULL : (RBNode) super.getLeft();
	}

	@Override
	public RBNode getRight() {
		return RB.NULL == super.getRight() ? null : (RBNode) super.getRight();
	}

	public RBNode getRight2() {
		return super.getRight() == null ? RB.NULL : (RBNode) super.getRight();
	}

	@Override
	public RBNode getParent() {
		return RB.NULL == super.getParent() ? null : (RBNode) super.getParent();
	}

	public RBNode getParent2() {
		return super.getParent() == null ? RB.NULL : (RBNode) super.getParent();
	}

	@Override
	public void linkLeft(BSTNode newLeft) {
		if (getLeft() != newLeft) {
			if (getLeft() != null) {
				// remove edge between this and left
				unlinkLeft();
			}
			if (newLeft != null && newLeft != RB.NULL) {
				if (newLeft.getParent() != null) {
					// remove edge between newLeft and its parent
					newLeft.unlinkParent();
				}
				// create new edge between this and newLeft
				newLeft.setParent(this);
			}
			setLeft(newLeft);
		}
	}

	@Override
	public void linkRight(BSTNode newRight) {
		if (getRight() != newRight) {
			if (getRight() != null) {
				// remove edge between this and right
				unlinkRight();
			}
			if (newRight != null && newRight != RB.NULL) {
				if (newRight.getParent() != null) {
					// remove edge between newRight and its parent
					newRight.unlinkParent();
				}
				// create new edge between this and newRight
				newRight.setParent(this);
			}
			setRight(newRight);
		}
	}

	@Override
	public void setPaint(NodePaint paint) {
		if (paint == NodePaint.NORMAL) {
			if (isRed())
				super.setPaint(NodePaint.RED);
			else
				super.setPaint(NodePaint.BLACK);
		} else {
			super.setPaint(paint);
		}
	}
}
