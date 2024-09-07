package com.example.examplemod.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ZombieSpawnEvent {

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();

        // Check if the entity is a Zombie
        if (entity.getType() == EntityType.ZOMBIE && entity instanceof Zombie) {
            Zombie zombie = (Zombie) entity;

            // Set health to 100
            CompoundTag data = zombie.getPersistentData();
            String customTag = data.getString("CustomTag");
            String level = "1";
            if("Level1Zombie".equals(customTag)){
                zombie.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(10.0D);
                zombie.setHealth(10.0F);
                level = "1";
                Component customNametag = Component.literal("§4[Lvl " +level+ "]"+ zombie.getHealth() + " §4\u2764");
                zombie.setCustomName(customNametag);
                zombie.setCustomNameVisible(true);
            }else if("Level10Zombie".equals(customTag)){
                zombie.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(1000.0D);
                zombie.setHealth(1000.0F);
                Component customNametag = Component.literal("§b&l<KLAN LEADER>§e§l"+ zombie.getHealth() + " §4\u2764");
                zombie.setCustomName(customNametag);
                zombie.setCustomNameVisible(true);
            }




        }
    }
}
