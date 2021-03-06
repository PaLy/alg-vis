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

import algvis2.animation.AnimationFactory;
import algvis2.core.Algorithm;
import algvis2.scene.paint.NodePaint;
import algvis2.scene.viselem.TreeHighlighter;

class PStackPush extends Algorithm {
	private final int x;
	private final int version;
	private final PStack stack;
	private final PersistentVisualization visualization;

	public PStackPush(PersistentVisualization visualization, PStack stack, int x, int version) {
		super(visualization);
		this.x = x;
		this.version = version;
		this.stack = stack;
		this.visualization = visualization;
	}

	@Override
	protected void runAlgorithm() {
		visualization.setCurAlgVersion(version);
		pause(false);
		
		PStackNode node;
		PStackNode oldVersionTop = stack.getVersion(version).nextNode;
		node = new PStackNode(x, oldVersionTop, NodePaint.INSERT);

		int newVerID = stack.getVersionsCount();
		PStackNode.NullNode newVerPointer = new PStackNode.NullNode(newVerID, node, getVisualization());

		saveChangedProperties();
		node.removePosBinding();
		newVerPointer.removePosBinding();

		stack.addVersion(newVerPointer);

		addAnimation(AnimationFactory.scaleInOut(node));
		addAnimation(AnimationFactory.scaleInOut(newVerPointer));

		node.setPaint(NodePaint.NORMAL);
		
		visualization.setCurAlgVersion(newVerID);
		pause(false);
		visualization.setCurAlgVersion(-1);
	}

	@Override
	protected PersistentVisualization getVisualization() {
		return (PersistentVisualization) super.getVisualization();
	}
}
