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
import algvis2.scene.viselem.TreeHighlighter;

class PStackPop extends Algorithm {
	private final PStack stack;
	private final int version;
	private final PersistentVisualization visualization;

	protected PStackPop(PersistentVisualization visualization, PStack stack, int version) {
		super(visualization);
		this.version = version;
		this.stack = stack;
		this.visualization = visualization;
	}

	@Override
	protected void runAlgorithm() {
		visualization.setCurAlgVersion(version);
		pause(false);
		
		PStackNode oldVersionTop = stack.getVersion(version).nextNode;
		int nextVersion = stack.getVersionsCount();
		PStackNode.NullNode newVerPointer = new PStackNode.NullNode(nextVersion,
				oldVersionTop.nextNode, getVisualization());

		saveChangedProperties();
		newVerPointer.removePosBinding();

		stack.addVersion(newVerPointer);

		addAnimation(AnimationFactory.scaleInOut(newVerPointer));
		
		visualization.setCurAlgVersion(nextVersion);
		pause(false);
		visualization.setCurAlgVersion(-1);
	}

	@Override
	public PersistentVisualization getVisualization() {
		return (PersistentVisualization) super.getVisualization();
	}
}
