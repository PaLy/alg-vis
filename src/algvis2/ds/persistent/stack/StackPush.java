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

package algvis2.ds.persistent.stack;

import algvis2.core.Algorithm;

public class StackPush extends Algorithm {
	private final int x;
	private final int version;
	private final Stack stack;

	public StackPush(Stack stack, int x, int version) {
		super(stack);
		this.x = x;
		this.version = version;
		this.stack = stack;
	}

	@Override
	protected void runAlgorithm() {
		StackNode node;
		StackNode oldVersionTop = stack.versions.get(version).nextNode;
		node = new StackNode(x, oldVersionTop);
		
		int newVerID = stack.versions.size();
		StackNode.NullNode newVerPointer = new StackNode.NullNode(newVerID, node);
		
		saveChangedProperties();
		node.removePosBinding();
		newVerPointer.removePosBinding();
		
		stack.versions.add(newVerPointer);
	}
}
