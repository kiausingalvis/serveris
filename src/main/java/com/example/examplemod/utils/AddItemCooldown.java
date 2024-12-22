package com.example.examplemod.utils;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;

public class AddItemCooldown {
    public static void addCooldownToItem(ServerPlayer player, Item item, int CooldownInTicks) {
        ItemCooldowns cooldownManager = player.getCooldowns();
        cooldownManager.addCooldown(item, CooldownInTicks);
    }
}

