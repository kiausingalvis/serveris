package com.example.examplemod.utils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber
public class ArrowTargetUtils {

    private static final double TRACKING_RADIUS = 50.0;  // Radius within which arrows track the dragon
    private static final double ARROW_SPEED = 2.5;       // Speed of the arrows

    // Map to keep track of arrows tracking the dragon
    private static final Map<AbstractArrow, EnderDragon> trackingArrows = new HashMap<>();

    // Method to start tracking an arrow towards an Ender Dragon
    public static void startTrackingArrow(ServerLevel level, AbstractArrow arrow) {
        try {
            // Ensure the arrow doesn't bounce off// Makes arrow pass through objects/entities without bouncing
            arrow.setPierceLevel((byte) 10);  // Sets high pierce level to ensure it embeds into entities

            List<EnderDragon> dragons = level.getEntitiesOfClass(EnderDragon.class, arrow.getBoundingBox().inflate(TRACKING_RADIUS));

            if (!dragons.isEmpty()) {
                EnderDragon dragon = dragons.get(0);  // Target the closest dragon
                trackingArrows.put(arrow, dragon);    // Add the arrow and dragon to the tracking map
                System.out.println("Tracking started for arrow: " + arrow.getId() + " targeting dragon: " + dragon.getId());
            } else {
                System.out.println("No Ender Dragon found within radius for arrow: " + arrow.getId());
            }
        } catch (Exception e) {
            System.err.println("Error starting tracking for arrow: " + arrow.getId());
            e.printStackTrace();
        }
    }

    // Tick event to update the movement of the arrows every tick
    @SubscribeEvent
    public static void onArrowTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            try {
                // Use an iterator to avoid ConcurrentModificationException
                Iterator<Map.Entry<AbstractArrow, EnderDragon>> iterator = trackingArrows.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry<AbstractArrow, EnderDragon> entry = iterator.next();
                    AbstractArrow arrow = entry.getKey();
                    EnderDragon dragon = entry.getValue();

                    if (!arrow.isAlive()) {
                        System.out.println("Arrow " + arrow.getId() + " is no longer alive. Removing from tracking.");
                        iterator.remove();
                        continue;
                    }

                    if (!dragon.isAlive()) {
                        System.out.println("Dragon " + dragon.getId() + " is no longer alive. Removing arrow " + arrow.getId() + " from tracking.");
                        iterator.remove();
                        continue;
                    }

                    // Recalculate direction to dragon
                    Vec3 arrowPosition = arrow.position();
                    Vec3 dragonPosition = dragon.position();
                    Vec3 directionToDragon = dragonPosition.subtract(arrowPosition).normalize();

                    // Update arrow's velocity
                    Vec3 newArrowVelocity = directionToDragon.scale(ARROW_SPEED);
                    arrow.setDeltaMovement(newArrowVelocity);
                    arrow.hasImpulse = true;

                    // Debug print to track arrow movement
                    System.out.println("Arrow " + arrow.getId() + " is tracking Dragon " + dragon.getId() + " at distance: " + arrowPosition.distanceTo(dragonPosition));

                    // Check if the arrow is close enough to hit the dragon
                    if (arrowPosition.distanceTo(dragonPosition) < 1.0) {
                        // Stop arrow and remove it from tracking
                        arrow.setDeltaMovement(Vec3.ZERO);
                        iterator.remove();  // Safe removal using iterator
                        System.out.println("Arrow " + arrow.getId() + " hit the dragon! Stopping tracking.");
                        // Optional: apply damage or effects here

                        // Disable the arrow from further bouncing after hitting the dragon
                        arrow.setNoPhysics(true);
                        arrow.remove(Entity.RemovalReason.KILLED); // Remove the arrow after hitting
                    }
                }
            } catch (Exception e) {
                System.err.println("Error during arrow tracking tick event.");
                e.printStackTrace();
            }
        }
    }
}
