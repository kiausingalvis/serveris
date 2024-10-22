package com.example.examplemod.worlds;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.core.Registry;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class WorldTeleporter {

    public static void startF6(ServerPlayer player) {
        // Define the key for the custom world "F6"
        ResourceKey<Level> f6WorldKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("minecraft", "F6"));

        // Get the ServerLevel (world) for "F6"
        ServerLevel f6World = player.getServer().getLevel(f6WorldKey);

        if (f6World != null) {
            // Get spawn position in the F6 world
            BlockPos spawnPos = f6World.getSharedSpawnPos();

            // Teleport the player to the F6 world
            player.teleportTo(f6World, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), player.getYRot(), player.getXRot());
            System.out.println("Player teleported to world F6!");
        } else {
            System.out.println("World F6 not found!");
        }
    }
}
