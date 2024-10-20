package com.example.examplemod.worlds;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.core.Registry;

public class CustomWorldDimension {
    // Define the resource key for the new world 'F6'
    public static final ResourceKey<Level> F6_DIMENSION_KEY = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation("examplemod", "F6"));

    // Register the dimension type for 'F6'
    public static final ResourceKey<DimensionType> F6_DIMENSION_TYPE = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, new ResourceLocation("examplemod", "F6"));

    // Method to teleport a player to the 'F6' dimension
    public static void startF6(ServerPlayer player) {
        ServerLevel f6World = player.getServer().getLevel(F6_DIMENSION_KEY);

        if (f6World != null) {
            BlockPos spawnPos = f6World.getSharedSpawnPos(); // Get default spawn location in the new world
            player.teleportTo(f6World, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), player.getYRot(), player.getXRot());
            System.out.println("Player teleported to world F6!");
        } else {
            System.out.println("World F6 not found!");
        }
    }
}
