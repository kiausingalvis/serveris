package com.example.examplemod.events;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.network.chat.Component;

import java.util.concurrent.TimeUnit;

import static com.example.examplemod.ExampleMod.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class DamageIndicatorHandler {

    @SubscribeEvent
    public static void onMobHit(LivingHurtEvent event) {
        // Check if the entity hurt is a mob (LivingEntity)
        LivingEntity mob = event.getEntity();
        if (!mob.getLevel().isClientSide()) {
            ServerLevel world = (ServerLevel) mob.getLevel();

            // Get the amount of damage dealt
            float damage = event.getAmount();

            // Spawn the floating text near the mob
            spawnDamageIndicator(world, mob.position(), damage);
        }
    }

    // Function to spawn the floating damage indicator
    private static void spawnDamageIndicator(ServerLevel world, Vec3 position, float damage) {
        // Create an armor stand for the floating text
        ArmorStand armorStand = new ArmorStand(EntityType.ARMOR_STAND, world);
        armorStand.setPos(position.x, position.y + 1.5, position.z); // Slightly above the mob

        // Set the text (damage amount) as the custom name
        Component damageText = Component.literal("§c-" + damage); // Red text
        armorStand.setCustomName(damageText);
        armorStand.setCustomNameVisible(true);

        // Armor stand settings to make it invisible, small, and non-interactive
        armorStand.setInvisible(true);
        armorStand.setInvulnerable(true);
        armorStand.setNoGravity(true);// Makes it tiny and non-collidable

        // Add the armor stand to the world
        world.addFreshEntity(armorStand);

        // Schedule the removal of the armor stand after a short delay (e.g., 20 ticks = 1 second)
        MinecraftServer server = world.getServer();
        if (server != null) {
            server.submitAsync(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    armorStand.remove(Entity.RemovalReason.DISCARDED);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
