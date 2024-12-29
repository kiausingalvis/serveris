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
    
    private static final double CLEAVE_LENGTH = 50;
    private static final double CLEAVE_RADIUS = 5;
    private static final float DAMAGE_AMOUNT = 10.0f;
    private static final double FORWARD_ANGLE_THRESHOLD = Math.toRadians(70);
    // Max angle (degrees) to consider entities "in front"
    public static void triggerCleave(ServerPlayer player) {
        ServerLevel world = player.getLevel();
        Vec3 start = player.getEyePosition();
        Vec3 direction = player.getLookAngle().normalize();
        Vec3 end = start.add(direction.scale(CLEAVE_LENGTH));

        System.out.println("[DEBUG] Cleave Start: " + start + ", End: " + end + ", Direction: " + direction);

        AABB cleaveArea = new AABB(
                Math.min(start.x, end.x) - CLEAVE_RADIUS, Math.min(start.y, end.y) - CLEAVE_RADIUS, Math.min(start.z, end.z) - CLEAVE_RADIUS,
                Math.max(start.x, end.x) + CLEAVE_RADIUS, Math.max(start.y, end.y) + CLEAVE_RADIUS, Math.max(start.z, end.z) + CLEAVE_RADIUS
        );

        System.out.println("[DEBUG] Cleave Area: " + cleaveArea);

        List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, cleaveArea,
                entity -> entity != player && entity.isAlive());

        System.out.println("[DEBUG] Entities found in cleave area: " + entities.size());

        for (LivingEntity entity : entities) {
            Vec3 entityPos = entity.position().add(0, entity.getBbHeight() / 2, 0); // Entity center position
            Vec3 toEntity = entityPos.subtract(start).normalize();


            double angle = Math.acos(direction.dot(toEntity));
            if (angle > FORWARD_ANGLE_THRESHOLD) {

                System.out.println("[DEBUG] Skipping entity: " + entity.getName().getString() + " | Angle: " + Math.toDegrees(angle));
                continue;
            }

            if (getClosestPointOnLine(start, end, entityPos).distanceTo(entityPos) <= CLEAVE_RADIUS) {
                entity.hurt(DamageSource.playerAttack(player), DAMAGE_AMOUNT);

                System.out.println("[DEBUG] Damaged entity: " + entity.getName().getString());

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
                System.out.println("[DEBUG] Slash particle triggered at: " + InFrontOfMob);
            }
        }
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
        final double CLEAVE_RADIUS = 50.0;
        final float DAMAGE_AMOUNT = 10.0f;

        System.out.println("[DEBUG] Cleave triggered by: " + player.getName().getString());
        System.out.println("[DEBUG] Player Position: " + playerPos);

        // Define the spherical area of effect
        AABB cleaveArea = new AABB(
                playerPos.x - CLEAVE_RADIUS, playerPos.y - CLEAVE_RADIUS, playerPos.z - CLEAVE_RADIUS,
                playerPos.x + CLEAVE_RADIUS, playerPos.y + CLEAVE_RADIUS, playerPos.z + CLEAVE_RADIUS
        );

        System.out.println("[DEBUG] Cleave Area: " + cleaveArea);

        List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, cleaveArea,
                entity -> entity != player && entity.isAlive());

        System.out.println("[DEBUG] Entities found in cleave area: " + entities.size());

        for (LivingEntity entity : entities) {
            entity.hurt(DamageSource.playerAttack(player), DAMAGE_AMOUNT);

            System.out.println("[DEBUG] Damaged entity: " + entity.getName().getString());

            Vec3 entityPos = entity.position().add(0, entity.getBbHeight() / 2, 0);
            world.sendParticles(
                    ParticleTypes.SWEEP_ATTACK,
                    entityPos.x, entityPos.y, entityPos.z,
                    10, 0.2, 0.2, 0.2, 0.1
            );

            System.out.println("[DEBUG] Slash particle triggered at: " + entityPos);
        }

        world.playSound(null, player.blockPosition(),
                net.minecraft.sounds.SoundEvents.PLAYER_ATTACK_SWEEP,
                net.minecraft.sounds.SoundSource.PLAYERS,
                1.0f, 1.0f);

        System.out.println("[DEBUG] Cleave completed.");
    }



}
