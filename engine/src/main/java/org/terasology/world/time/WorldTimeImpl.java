/*
 * Copyright 2013 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.world.time;

import java.util.concurrent.atomic.AtomicLong;

import org.terasology.engine.Time;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.registry.In;
import org.terasology.world.WorldComponent;

/**
 * @author Immortius
 */
public class WorldTimeImpl extends BaseComponentSystem implements WorldTime, UpdateSubscriberSystem {

    private static final float WORLD_TIME_MULTIPLIER = /*48f*/0f;

    private AtomicLong worldTime = new AtomicLong(0);

    @In
    private Time time;

    @In
    private EntityManager entityManager;
    
    public WorldTimeImpl() {
    	this(43200);
    }
    
    public WorldTimeImpl(long initialTime) {
    	this.setTime(initialTime);
    }
    
    @Override
    public long getMilliseconds() {
        return worldTime.get();
    }

    @Override
    public float getSeconds() {
        return worldTime.get() / 1000f;
    }
    
    @Override
    public float getDays() {
        return worldTime.get() / (float) DAY_LENGTH;
    }

    @Override
    public float getTimeRate() {
        return WORLD_TIME_MULTIPLIER;
    }

    @Override
    public void setMilliseconds(long newWorldTime) {
        // TODO: Send network event to update
        this.worldTime.getAndSet(newWorldTime);
    }

    @Override
    public void setDays(float timeInDays) {
        setMilliseconds((long) ((double) timeInDays * DAY_LENGTH));
    }

    @Override
    public void update(float delta) {
        long deltaMs = time.getDeltaInMs();
        if (deltaMs > 0) {
            deltaMs = (long) (deltaMs * WORLD_TIME_MULTIPLIER);
            long startTime = worldTime.getAndAdd(deltaMs);
            long endTime = startTime + deltaMs;

            long startTick = startTime / TICK_EVENT_RATE;
            long endTick = endTime / TICK_EVENT_RATE;

            if (startTick != endTick) {
                long tick = endTime - endTime % TICK_EVENT_RATE;
                getWorldEntity().send(new WorldTimeEvent(tick));
            }

        }
    }

    private EntityRef getWorldEntity() {
        for (EntityRef entity : entityManager.getEntitiesWith(WorldComponent.class)) {
            return entity;
        }
        return EntityRef.NULL;
    }
    
	@Command( shortDescription = "Displays time", helpText = "Displays world time (in milliseconds)" )
    public String time() {
        long currentTime = (worldTime.get() % DAY_LENGTH) / 1000;
        int hours = (int) ((currentTime / 60) / 60) % 24;
        int minutes = (int) (currentTime / 60) % 60;
        int seconds = (int) currentTime % 60;
        return ((hours < 10) ? "0" + hours : hours) + " : " +
        		((minutes < 10) ? "0" + minutes : minutes) + " : " + 
        		((seconds < 10) ? "0" + seconds : seconds);
    }
	
	@Command( shortDescription = "Set world time", helpText = "Receives a value (in seconds) and sets the world time to it")
	public String setTime(@CommandParam( value = "New time" ) Long newTime) {
		this.setMilliseconds((newTime * 1000) % DAY_LENGTH);
		return "Set time to " + time();
	}
}
