package com.example.examplemod.utils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.phys.Vec3;

public class ArrowShooter {

    public void shootArrow(ServerPlayer player) {
        // Get the level (world) where the player is located
        ServerLevel level = player.getLevel();

        // Get the direction the player is looking
        Vec3 lookVector = player.getLookAngle();

        // Shoot the main arrow straight
        shootSingleArrow(level, player, lookVector, 3.0F, 6.0F, true);

        // Shoot one arrow 15 degrees to the left
        Vec3 leftArrowVector = rotateVector(lookVector, -15);
        shootSingleArrow(level, player, leftArrowVector, 3.0F, 6.0F, false);

        // Shoot one arrow 15 degrees to the right
        Vec3 rightArrowVector = rotateVector(lookVector, 15);
        shootSingleArrow(level, player, rightArrowVector, 3.0F, 6.0F, false);

        // Optionally: Play a sound for shooting the arrows
        player.getLevel().playSound(null, player.getX(), player.getY(), player.getZ(),
                net.minecraft.sounds.SoundEvents.ARROW_SHOOT, player.getSoundSource(), 1.0F, 1.0F);
    }

    // Method to shoot a single arrow
    private void shootSingleArrow(ServerLevel level, ServerPlayer player, Vec3 direction, float velocity, double damage, boolean isCritical) {
        Arrow arrow = new Arrow(level, player);

        // Set arrow's position at the player's eye level
        arrow.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());

        // Shoot the arrow in the given direction with the specified velocity
        arrow.shoot(direction.x, direction.y, direction.z, velocity, 1.0F);

        // Set damage and critical hit status
        arrow.setBaseDamage(damage);
        arrow.setCritArrow(isCritical);

        // Spawn the arrow in the world
        level.addFreshEntity(arrow);
    }

    // Method to rotate a vector by a certain angle (in degrees)
    private Vec3 rotateVector(Vec3 vec, double degrees) {
        double radians = Math.toRadians(degrees);
        double cos = Math.cos(radians);
        double sin = Math.sin(radians);

        // Rotate around the Y axis for a horizontal spread (ignoring Y component for simplicity)
        double newX = vec.x * cos - vec.z * sin;
        double newZ = vec.x * sin + vec.z * cos;

        return new Vec3(newX, vec.y, newZ);
    }
}
