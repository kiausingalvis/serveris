package com.example.examplemod.utils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.ClipContext;
import static com.example.examplemod.utils.ClientSoundUtils.sendClientSound;

public class TeleportOnRightClickEvent {
    public static void TeleportBlocksLookAngle(ServerPlayer player, int TPDistance) {
            if (player.getUsedItemHand() == InteractionHand.MAIN_HAND && !player.isCrouching()) {
                Vec3 lookDirection = player.getLookAngle();
                Vec3 start = player.position().add(0, player.getEyeHeight(), 0);
                Vec3 end = start.add(lookDirection.scale(TPDistance));
                Vec3 safePosition = getSafePosition(player, start, end);
                if (safePosition != null) {
                    player.teleportTo(safePosition.x, safePosition.y, safePosition.z);
                }
                sendClientSound(player, SoundEvents.ITEM_PICKUP,1,10);
            }
        }
    private static Vec3 getSafePosition(ServerPlayer player, Vec3 start, Vec3 end) {
        ClipContext context = new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player);
        BlockHitResult hitResult = player.getLevel().clip(context);
        Vec3 targetPosition = end;
        if (hitResult.getType() == HitResult.Type.BLOCK) {
            Vec3 hitPos = hitResult.getLocation();
            Vec3 offset = hitResult.getLocation().subtract(start).normalize().scale(0.5); // Move back half a block
            targetPosition = hitPos.subtract(offset);
            targetPosition = new Vec3(
                    Math.floor(targetPosition.x) + 0.5,
                    Math.floor(targetPosition.y) + 0.5,
                    Math.floor(targetPosition.z) + 0.5
            );
        }
        if (isPositionSafe(player, targetPosition)) {
            return targetPosition;
        }
        return null;
    }

    private static boolean isPositionSafe(ServerPlayer player, Vec3 position) {
        return player.getLevel().noCollision(player, player.getBoundingBox().move(position.subtract(player.position())));
    }
}
