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

package algvis2.core;

import algvis2.ds.DataStructure;
import javafx.animation.Animation;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;

import java.util.HashMap;
import java.util.concurrent.Semaphore;

public abstract class Algorithm implements Runnable {
	protected final Visualization visualization;
	private final Semaphore gate = new Semaphore(1);
	private boolean wrapped = false;
	private Algorithm wrapperAlg;
    
    private PropertiesState state;
    private PropertiesState startState;
    private final SequentialTransition animation;
    private Timeline backToStart;
	
	protected Algorithm(DataStructure D) {
		this.visualization = D.visualization;
        animation = new SequentialTransition();
		try {
			gate.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected Algorithm(DataStructure D, Algorithm a) {
		this(D);
		if (a != null) {
			wrapped = true;
			wrapperAlg = a;
		}
	}

	@Override
	public void run() {
		begin();
		try {
			runAlgorithm();
		} catch (InterruptedException e) {
			return;
		}
		end();
	}
    
    public Animation runA() {
        run();
        return animation;
    }

	public abstract void runAlgorithm() throws InterruptedException;

	protected void pause() throws InterruptedException {
		if (wrapped) {
			wrapperAlg.pause();
		} else {
			saveChangedProperties();
            HashMap<Object, Object> preState = new HashMap<Object, Object>();
            visualization.storeState(preState);
            state = new PropertiesState(preState);
		}
	}

	public void resume() {
		if (wrapped) {
			wrapperAlg.resume();
		} else {
			gate.release();
		}
	}

	void begin() {
        HashMap<Object, Object> preState = new HashMap<Object, Object>();
        visualization.storeState(preState);
        state = new PropertiesState(preState);
        startState = state;
	}

	void end() {
        saveChangedProperties();
        backToStart = startState.createTimeline();
	}
    
    public Animation getBackToStart() {
        return backToStart;
    }

	public HashMap<String, Object> getResult() {
		return null;
	}
    
    public void saveChangedProperties() {
        animation.getChildren().add(state.createTimeline());
    }
    
    protected void addAnimation(Animation anim) {
        saveChangedProperties();
        HashMap<Object, Object> preState = new HashMap<Object, Object>();
        visualization.storeState(preState);
        state = new PropertiesState(preState);
        animation.getChildren().add(anim);
    }
}
