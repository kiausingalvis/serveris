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
                new int[]{11743532, 15435844, 14602026},  // Combo 1 with 3 colors
                new int[]{4312372, 3887386},              // Combo 2 with 2 colors
                new int[]{6719955, 2437522, 8073150},     // Combo 3 with 3 colors
                new int[]{12801229, 14188952},            // Combo 4 with 2 colors
                new int[]{5320730},                       // Combo 5 with 1 color
                new int[]{15790320, 11250603, 4408131, 1973019} // Combo 6 with 4 colors
                // You can add more combos with different lengths here
        );

        // Randomly select a color combo
        Random random = new Random();
        int[] selectedCombo = colorCombos.get(random.nextInt(colorCombos.size()));

        // Use the selected combo to spawn the firework (same as before)
        ItemStack fireworkItem = new ItemStack(Items.FIREWORK_ROCKET);
        fireworkItem.getOrCreateTag().put("Fireworks", new net.minecraft.nbt.CompoundTag());
        var explosionsTag = fireworkItem.getTag().getCompound("Fireworks").getList("Explosions", 10);
        var explosion = new net.minecraft.nbt.CompoundTag();
        explosion.putIntArray("Colors", selectedCombo);
        explosion.putByte("Type", (byte) 0); // 0 = small ball (no fade)
        explosionsTag.add(explosion);
        fireworkItem.getTag().getCompound("Fireworks").put("Explosions", explosionsTag);

        // Calculate the position 5 blocks in front of the player
        Vec3 lookDirection = player.getLookAngle().scale(5);
        Vec3 spawnPos = player.position().add(lookDirection.x, lookDirection.y, lookDirection.z);

        // Create and spawn the firework entity at the calculated position
        FireworkRocketEntity firework = new FireworkRocketEntity(level, spawnPos.x, spawnPos.y, spawnPos.z, fireworkItem);
        firework.setSilent(true); // Disable sound
        firework.setNoGravity(true); // No gravity, so it doesn't fly
        level.addFreshEntity(firework); // Spawn the firework entity

    }
}
