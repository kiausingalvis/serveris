package com.example.examplemod;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber
public class SukunaCleave {


    private static final int COOLDOWN_TICKS = 100;    // Cooldown in ticks (5 seconds)
    private static final double CLEAVE_LENGTH = 50;  // Length of cleave (blocks)
    private static final double CLEAVE_RADIUS = 5;   // Radius of cleave (blocks)
    private static final float DAMAGE_AMOUNT = 10.0f; // Damage dealt by cleave
    private static final double FORWARD_ANGLE_THRESHOLD = Math.toRadians(70);
    // Max angle (degrees) to consider entities "in front"
    public static void triggerCleave(ServerPlayer player) {
        ServerLevel world = player.getLevel();
        Vec3 start = player.getEyePosition(); // Starting position (player's eye)
        Vec3 direction = player.getLookAngle().normalize(); // Player's look direction
        Vec3 end = start.add(direction.scale(CLEAVE_LENGTH)); // End of cleave path

        // Debug: Log start, end, and direction
        System.out.println("[DEBUG] Cleave Start: " + start + ", End: " + end + ", Direction: " + direction);

        // Define the cleave bounding box
        AABB cleaveArea = new AABB(
                Math.min(start.x, end.x) - CLEAVE_RADIUS, Math.min(start.y, end.y) - CLEAVE_RADIUS, Math.min(start.z, end.z) - CLEAVE_RADIUS,
                Math.max(start.x, end.x) + CLEAVE_RADIUS, Math.max(start.y, end.y) + CLEAVE_RADIUS, Math.max(start.z, end.z) + CLEAVE_RADIUS
        );

        // Debug: Log the cleave area
        System.out.println("[DEBUG] Cleave Area: " + cleaveArea);

        // Find all entities within the bounding box
        List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, cleaveArea,
                entity -> entity != player && entity.isAlive());

        System.out.println("[DEBUG] Entities found in cleave area: " + entities.size());

        // Iterate through entities and process only those in front
        for (LivingEntity entity : entities) {
            Vec3 entityPos = entity.position().add(0, entity.getBbHeight() / 2, 0); // Entity center position
            Vec3 toEntity = entityPos.subtract(start).normalize(); // Vector from player to entity

            // Check if entity is in front (angle between look direction and toEntity is small enough)
            double angle = Math.acos(direction.dot(toEntity));
            if (angle > FORWARD_ANGLE_THRESHOLD) {
                // Debug: Skip entity behind or outside cleave angle
                System.out.println("[DEBUG] Skipping entity: " + entity.getName().getString() + " | Angle: " + Math.toDegrees(angle));
                continue;
            }

            // Damage the entity if it's within cleave radius and in front
            if (getClosestPointOnLine(start, end, entityPos).distanceTo(entityPos) <= CLEAVE_RADIUS) {
                entity.hurt(DamageSource.playerAttack(player), DAMAGE_AMOUNT);

                // Debug: Log damaged entity
                System.out.println("[DEBUG] Damaged entity: " + entity.getName().getString());

                // Trigger slash particles in front of the mob
                Vec3 mobToPlayer = start.subtract(entity.position()).normalize();
                Vec3 InFrontOfMob = entity.position().add(mobToPlayer.scale(0.5)).add(0,entity.getBbHeight() /2, 0);

                world.sendParticles(
                        ParticleTypes.SMOKE,
                        InFrontOfMob.x, InFrontOfMob.y, InFrontOfMob.z,
                        30, 0.4, 0.2, 0.4, 0.001
                );

                world.sendParticles(
                        ParticleTypes.SWEEP_ATTACK,
                        InFrontOfMob.x, InFrontOfMob.y, InFrontOfMob.z,
                        3, 0.4, 0.2, 0.4, 0.1
                );

                // Debug: Log particle position
                System.out.println("[DEBUG] Slash particle triggered at: " + InFrontOfMob);
            }
        }

        // Play cleave sound at player position
        world.playSound(null, player.blockPosition(),
                SoundEvents.WITHER_BREAK_BLOCK,
                SoundSource.MASTER,
                1.0f, 2.0f);
    }


    private static Vec3 getClosestPointOnLine(Vec3 start, Vec3 end, Vec3 point) {
        Vec3 lineVec = end.subtract(start);
        double lineLengthSquared = lineVec.lengthSqr();
        if (lineLengthSquared == 0) return start;

        double t = point.subtract(start).dot(lineVec) / lineLengthSquared;
        t = Math.max(0, Math.min(1, t));
        return start.add(lineVec.scale(t));
    }

    public static void ExpansionCleave(ServerPlayer player) {
        ServerLevel world = player.getLevel();
        Vec3 playerPos = player.position();
        final double CLEAVE_RADIUS = 50.0; // Radius for the cleave effect
        final float DAMAGE_AMOUNT = 10.0f; // Amount of damage dealt

        System.out.println("[DEBUG] Cleave triggered by: " + player.getName().getString());
        System.out.println("[DEBUG] Player Position: " + playerPos);

        // Define the spherical area of effect
        AABB cleaveArea = new AABB(
                playerPos.x - CLEAVE_RADIUS, playerPos.y - CLEAVE_RADIUS, playerPos.z - CLEAVE_RADIUS,
                playerPos.x + CLEAVE_RADIUS, playerPos.y + CLEAVE_RADIUS, playerPos.z + CLEAVE_RADIUS
        );

        System.out.println("[DEBUG] Cleave Area: " + cleaveArea);

        // Find all living entities in the radius (excluding the player)
        List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, cleaveArea,
                entity -> entity != player && entity.isAlive());

        System.out.println("[DEBUG] Entities found in cleave area: " + entities.size());

        for (LivingEntity entity : entities) {
            // Damage the entity
            entity.hurt(DamageSource.playerAttack(player), DAMAGE_AMOUNT);

            System.out.println("[DEBUG] Damaged entity: " + entity.getName().getString());

            // Trigger particles at the entity's location
            Vec3 entityPos = entity.position().add(0, entity.getBbHeight() / 2, 0);
            world.sendParticles(
                    ParticleTypes.SWEEP_ATTACK,
                    entityPos.x, entityPos.y, entityPos.z,
                    10, 0.2, 0.2, 0.2, 0.1
            );

            System.out.println("[DEBUG] Slash particle triggered at: " + entityPos);
        }

        // Play sound effect for the cleave
        world.playSound(null, player.blockPosition(),
                net.minecraft.sounds.SoundEvents.PLAYER_ATTACK_SWEEP,
                net.minecraft.sounds.SoundSource.PLAYERS,
                1.0f, 1.0f);

        System.out.println("[DEBUG] Cleave completed.");
    }



}
