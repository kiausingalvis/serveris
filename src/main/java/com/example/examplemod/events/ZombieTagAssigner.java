package com.example.examplemod.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ZombieTagAssigner {

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();

        // Check if the entity is a Zombie
        if (entity.getType() == EntityType.ZOMBIE && entity instanceof Zombie) {
            Zombie zombie = (Zombie) entity;

            // Assign a unique tag if it doesn't already have one
            CompoundTag data = zombie.getPersistentData();
            if (!data.contains("CustomTag")) {
                data.putString("CustomTag", "Level1Zombie");
                // Set initial custom attributes
                setCustomAttributes(zombie);
            }
        }
    }

    private static void setCustomAttributes(Zombie zombie) {
        CompoundTag data = zombie.getPersistentData();
        String customTag = data.getString("CustomTag");

        if ("Level1Zombie".equals(customTag)) {
            // Set health to 100
            zombie.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(100.0D);
            zombie.setHealth(100.0F);

            // Set custom nametag
            Component customNametag = Component.literal("§4[lvl 1] 100 §4\u2764"); // Heart icon
            zombie.setCustomName(customNametag);
            zombie.setCustomNameVisible(true);
        }
        if ("Level10Zombie".equals(customTag)) {
            // Set health to 100
            zombie.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(1000.0D);
            zombie.setHealth(1000.0F);

            // Set custom nametag
            Component customNametag = Component.literal("§4[lvl 10] 1000 §4\u2764"); // Heart icon
            zombie.setCustomName(customNametag);
            zombie.setCustomNameVisible(true);
        }
    }
}
