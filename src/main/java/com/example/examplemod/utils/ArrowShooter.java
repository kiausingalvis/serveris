package com.example.examplemod.utils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.phys.Vec3;
import static com.example.examplemod.utils.ClientSoundUtils.sendClientSound;
import static com.example.examplemod.utils.AddItemCooldown.addCooldownToItem;

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
        addCooldownToItem(player, player.getMainHandItem().getItem(), 10);
    }
    private static void shootSingleArrow(ServerLevel level, ServerPlayer player, Vec3 direction, float velocity, double damage, boolean isCritical) {
        AbstractArrow arrow = new Arrow(level, player);
        arrow.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
        arrow.shoot(direction.x, direction.y, direction.z, velocity, 1.0F);//
        arrow.setBaseDamage(damage);
        arrow.setCritArrow(isCritical);
        arrow.pickup = Arrow.Pickup.DISALLOWED;
        level.addFreshEntity(arrow);
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
