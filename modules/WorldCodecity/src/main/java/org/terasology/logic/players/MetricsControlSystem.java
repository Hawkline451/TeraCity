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

package org.terasology.logic.players;

import org.terasology.config.Config;
import org.terasology.engine.GameEngine;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.event.EventPriority;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.input.Keyboard;
import org.terasology.input.events.KeyDownEvent;
import org.terasology.input.events.MouseXAxisEvent;
import org.terasology.input.events.MouseYAxisEvent;
import org.terasology.logic.characters.CharacterComponent;
import org.terasology.network.ClientComponent;
import org.terasology.registry.In;
import org.terasology.rendering.nui.NUIManager;
import org.terasology.rendering.nui.layers.ingame.metrics.MetricsOverlay;
import org.terasology.rendering.world.WorldRenderer;
import org.terasology.world.WorldProvider;

/**
 * @author Eduardo Riveros
 */
@RegisterSystem(RegisterMode.CLIENT)
public class MetricsControlSystem extends BaseComponentSystem {

    @In
    private GameEngine engine;

    @In
    private WorldProvider world;

    @In
    private WorldRenderer worldRenderer;

    @In
    private Config config;

    @In
    private NUIManager nuiManager;

    private MetricsOverlay overlay;
    

    @Override
    public void initialise() {
        overlay = nuiManager.addOverlay("engine:metricsOverlay", MetricsOverlay.class);
    }


    @ReceiveEvent(components = ClientComponent.class)
    public void onKeyDown(KeyDownEvent event, EntityRef entity) {
        switch (event.getKey().getId()) {
            case Keyboard.KeyId.F2:
                overlay.toggle();
                event.consume();
                break;
            default:
                break;

        }
    }
}