package com.example.examplemod.utils;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

import static com.example.examplemod.utils.ClientSoundUtils.sendClientSound;
import static com.example.examplemod.utils.AddItemCooldown.addCooldownToItem;
import static com.example.examplemod.utils.ArrowParticleUtils.addSingleArrowParticle;
import static com.example.examplemod.utils.ArrowParticleUtils.addMultipleArrowParticles;
import static com.example.examplemod.utils.UNIQUEID_RETRIEVE.*;

public class ArrowShooter {
    public static void shootArrow(ServerPlayer player, Float arrowdamage) {
        ServerLevel level = player.getLevel();
        Vec3 lookVector = player.getLookAngle();
        shootSingleArrow(level, player, lookVector, 3.0F, arrowdamage, false);
        Vec3 leftArrowVector = rotateVector(lookVector, -5);
        shootSingleArrow(level, player, leftArrowVector, 3.0F, arrowdamage, false);
        Vec3 rightArrowVector = rotateVector(lookVector, 5);
        shootSingleArrow(level, player, rightArrowVector, 3.0F, arrowdamage, false);
        sendClientSound(player, SoundEvents.EXPERIENCE_ORB_PICKUP,1,1);
        addCooldownToItem(player, player.getMainHandItem().getItem(), 0);
    }
    private static void shootSingleArrow(ServerLevel level, ServerPlayer player, Vec3 direction, float velocity, double damage, boolean isCritical) {
        AbstractArrow arrow = new Arrow(level, player);
        arrow.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
        arrow.shoot(direction.x, direction.y, direction.z, velocity, 1.0F);
        arrow.setBaseDamage(damage);
        arrow.setCritArrow(isCritical);
        arrow.setPierceLevel((byte)10);
        arrow.pickup = Arrow.Pickup.DISALLOWED;
        level.addFreshEntity(arrow);
        if(getUniqueId(player.getMainHandItem()).equals("TERMINATOR") && getEnchants(player.getMainHandItem()).equals("DragonTracer")){
            ArrowTargetUtils.startTrackingArrow(level, arrow);
        }
        if(getRuneName(player.getMainHandItem()).equals("Smoke")){
            addSingleArrowParticle(arrow, ParticleTypes.LARGE_SMOKE, 0.0f, 1.0f, 0.0f, 0.05f, 1);
        }
        if(getRuneName(player.getMainHandItem()).equals("Notes")){
            addSingleArrowParticle(arrow, ParticleTypes.NOTE, 0.0f, 1.0f, 0.0f, 0.05f, 1);
            sendClientSound(player, SoundEvents.NOTE_BLOCK_PLING,1,1);
        }
        if(getRuneName(player.getMainHandItem()).equals("MultiTest")){
            List<ArrowParticleUtils.ParticleConfig> particleConfigs = new ArrayList<>();
            particleConfigs.add(new ArrowParticleUtils.ParticleConfig(ParticleTypes.EXPLOSION, 0.0f, 0.0f, 0.0f, 0.05f, 1));
            particleConfigs.add(new ArrowParticleUtils.ParticleConfig(ParticleTypes.FIREWORK, 1.0f, 1.0f, 1.0f, 0.05f, 1));
            addMultipleArrowParticles(arrow, particleConfigs);
        }
    }
    private static Vec3 rotateVector(Vec3 vec, double degrees) {
        double radians = Math.toRadians(degrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);
        double newX = vec.x * cos - vec.z * sin;
        double newZ = vec.x * sin + vec.z * cos;
        return new Vec3(newX, vec.y, newZ);
    }
}
