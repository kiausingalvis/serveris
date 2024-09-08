package com.example.examplemod.utils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.phys.Vec3;

public class ArrowShooter {

    public static void shootArrow() {
        // Get the level (world) where the player is located
        ServerLevel level = player.getLevel();

        // Create an arrow entity
        Arrow arrow = new Arrow(level, player);

        // Set the arrow's position at the player's eye level
        arrow.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());

        // Get the direction the player is looking
        Vec3 lookVector = player.getLookAngle();

        // Set the arrow's velocity to simulate a fully charged bow (3.0 is the speed multiplier for a full charge)
        arrow.shoot(lookVector.x, lookVector.y, lookVector.z, 3.0F, 0.0F);

        // Set the arrow's damage to that of a fully charged bow shot
        arrow.setBaseDamage(6.0);

        // Set the arrow as critical (fully charged arrows are considered critical)
        arrow.setCritArrow(true);

        // Spawn the arrow in the world
        level.addFreshEntity(arrow);

    }
}
