package com.example.examplemod.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Arrays;
import java.util.Random;
@Mod.EventBusSubscriber
public class FireworkUtils {
    public static void spawnInstantFirework(ServerPlayer player) {
        if (!(player.level instanceof ServerLevel level)) return;

        // List of color combinations (each combo is an array of any length of colors)
        List<int[]> colorCombos = Arrays.asList(
                new int[]{2651799, 2437522, 6719955, 8073150},
                new int[]{8073150, 14188952, 12801229},
                new int[]{15790320, 11250603, 4408131, 1973019},
                new int[]{11743532, 15435844, 14602026},
                new int[]{6719955, 2651799, 11743532},
                new int[]{15790320, 1973019, 6719955, 2651799}
        );

        // Randomly select a color combo
        Random random = new Random();
        int[] selectedCombo = colorCombos.get(random.nextInt(colorCombos.size()));

        // Create firework item with custom colors
        ItemStack fireworkItem = new ItemStack(Items.FIREWORK_ROCKET);
        var fireworkTag = fireworkItem.getOrCreateTagElement("Fireworks");
        var explosionsTag = new net.minecraft.nbt.ListTag();

        var explosion = new net.minecraft.nbt.CompoundTag();
        explosion.putIntArray("Colors", selectedCombo);
        explosion.putByte("Type", (byte) 0); // 0 = small ball (no fade)
        explosionsTag.add(explosion);

        fireworkTag.put("Explosions", explosionsTag);

        // Set a low flight duration to reduce normal lifespan
        fireworkTag.putByte("Flight", (byte) 1); // Flight duration 1 corresponds to a short lifespan

        // Calculate the position 5 blocks in front of the player
        Vec3 lookDirection = player.getLookAngle().scale(5);
        Vec3 spawnPos = player.position().add(lookDirection.x, lookDirection.y+1.5, lookDirection.z);

        // Create the firework entity
        FireworkRocketEntity firework = new FireworkRocketEntity(level, spawnPos.x, spawnPos.y, spawnPos.z, fireworkItem);

        try {
            // Use reflection to set the firework's life to exceed its lifespan
            var lifeField = FireworkRocketEntity.class.getDeclaredField("life");
            lifeField.setAccessible(true);
            lifeField.setInt(firework, 99); // Set life to a value that ensures instant explosion
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // Spawn the firework entity
        level.addFreshEntity(firework);
    }

}
