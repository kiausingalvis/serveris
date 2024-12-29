package com.example.examplemod.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import static com.example.examplemod.utils.ClientSoundUtils.sendClientSound;

public class AddEtherWarp {
    public static InteractionResult teleportIfCrouchRightClick(ServerPlayer player, InteractionHand hand) {
        // Check if player is crouching
        if (player.isCrouching()) {
            // Perform ray tracing to find the block the player is looking at
            BlockHitResult blockHitResult = getBlockLookingAt(player);
            // Ensure the hit result is a block
            if (blockHitResult.getType() == HitResult.Type.BLOCK) {
                BlockPos targetBlockPos = blockHitResult.getBlockPos();

                // Calculate the target position on top of the block (center)
                Vec3 targetPosition = new Vec3(targetBlockPos.getX() + 0.5, targetBlockPos.getY() + 1, targetBlockPos.getZ() + 0.5);

                // Teleport the player to the top center of the block
                player.teleportTo(targetPosition.x, targetPosition.y, targetPosition.z);
                sendClientSound(player, SoundEvents.WOODEN_DOOR_OPEN,1,1);
                return InteractionResult.SUCCESS;

            }
        }

        return InteractionResult.PASS;
    }

    // Perform ray tracing to detect the block the player is looking at
    private static BlockHitResult getBlockLookingAt(Player player) {
        // Get the player's eye position
        Vec3 startVec = player.getEyePosition(1.0F);
        // Get the player's look direction
        Vec3 lookVec = player.getLookAngle();
        // Define the range (distance to check)
        double range = 200.0D;

        // Calculate the end vector for ray tracing
        Vec3 endVec = startVec.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);

        // Perform ray tracing to detect the block
        return player.level.clip(new ClipContext(startVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
    }

}
