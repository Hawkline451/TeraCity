package org.terasology.logic.console.commands;

import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.CommandParam;
import org.terasology.logic.console.commandSystem.annotations.Sender;
import org.terasology.logic.health.DoDamageEvent;
import org.terasology.logic.health.DoHealEvent;
import org.terasology.logic.health.EngineDamageTypes;
import org.terasology.logic.health.HealthComponent;
import org.terasology.logic.permission.PermissionManager;
import org.terasology.network.ClientComponent;
import org.terasology.logic.health.HealthSystem;

@RegisterSystem(RegisterMode.AUTHORITY)
public class HealthCommands extends BaseComponentSystem {
	HealthSystem healthSystem = new HealthSystem();
	@Command(shortDescription = "Reduce the player's health by an amount", runOnServer = true)
    public String damage(@Sender EntityRef client, @CommandParam("amount") int amount) {
        ClientComponent clientComp = client.getComponent(ClientComponent.class);
        clientComp.character.send(new DoDamageEvent(amount, EngineDamageTypes.DIRECT.get(), clientComp.character));

        return "Inflicted damage of " + amount;
    }

    @Command(shortDescription = "Restores your health to max", runOnServer = true,
            requiredPermission = PermissionManager.CHEAT_PERMISSION)
    public String healthMax(@Sender EntityRef clientEntity) {
        ClientComponent clientComp = clientEntity.getComponent(ClientComponent.class);
        clientComp.character.send(new DoHealEvent(100000, clientComp.character));
        return "Health restored";
    }

    @Command(shortDescription = "Restores your health by an amount", runOnServer = true,
            requiredPermission = PermissionManager.CHEAT_PERMISSION)
    public void health(@Sender EntityRef client, @CommandParam("amount") int amount) {
        ClientComponent clientComp = client.getComponent(ClientComponent.class);
        clientComp.character.send(new DoHealEvent(amount, clientComp.character));
    }

    @Command(shortDescription = "Set max health", runOnServer = true,
            requiredPermission = PermissionManager.CHEAT_PERMISSION)
    public String setMaxHealth(@Sender EntityRef client, @CommandParam("max") int max) {
        ClientComponent clientComp = client.getComponent(ClientComponent.class);
        HealthComponent health = clientComp.character.getComponent(HealthComponent.class);
        if (health != null) {
            healthSystem.doHeal(clientComp.character, health.maxHealth, clientComp.character, health);
        }
        return "Max health set to " + max;
    }

    @Command(shortDescription = "Set health regen rate", runOnServer = true,
            requiredPermission = PermissionManager.CHEAT_PERMISSION)
    public String setRegenRate(@Sender EntityRef client, @CommandParam("rate") float rate) {
        ClientComponent clientComp = client.getComponent(ClientComponent.class);
        HealthComponent health = clientComp.character.getComponent(HealthComponent.class);
        if (health != null) {
            health.regenRate = rate;
            clientComp.character.saveComponent(health);
        }
        return "Set health regeneration rate to " + rate;
    }

    @Command(shortDescription = "Show your health")
    public String showHealth(@Sender EntityRef client) {
        ClientComponent clientComp = client.getComponent(ClientComponent.class);
        HealthComponent health = clientComp.character.getComponent(HealthComponent.class);
        if (health != null) {
            return "Your health:" + health.currentHealth + " max:" + health.maxHealth + " regen:" + health.regenRate;
        }
        return "I guess you're dead?";
    }
}
