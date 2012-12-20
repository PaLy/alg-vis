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

package algvis2.ds.dictionary.rb;

import algvis2.ds.dictionary.bst.BSTNode;
import algvis2.scene.paint.NodePaint;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class RBNode extends BSTNode {
	private final BooleanProperty redProperty = new SimpleBooleanProperty();
	private final RB rb;

	public RBNode(RB rb, int key, String layoutName) {
		super(key, NodePaint.RED);
		this.rb = rb;
		redProperty.set(true);

		redProperty.addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(
					ObservableValue<? extends Boolean> observableValue,
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
		return rb.NULL.equals(super.getLeft()) ? null : (RBNode) super
				.getLeft();
	}

	public RBNode getLeft2() {
		return (RBNode) super.getLeft();
	}

	@Override
	public RBNode getRight() {
		return rb.NULL.equals(super.getRight()) ? null : (RBNode) super
				.getRight();
	}

	public RBNode getRight2() {
		return (RBNode) super.getRight();
	}

	@Override
	public RBNode getParentNode() {
		return (RBNode) super.getParentNode();
	}

	public RBNode getParentNode2() {
		return getParentNode() == null ? rb.NULL : getParentNode();
	}

	@Override
	public void setPaint(NodePaint paint) {
		if (paint.equals(NodePaint.NORMAL)) {
			if (isRed())
				super.setPaint(NodePaint.RED);
			else
				super.setPaint(NodePaint.BLACK);
		} else {
			super.setPaint(paint);
		}
	}
}
