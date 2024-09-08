package com.example.examplemod.commands;

import com.example.examplemod.utils.ItemUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;

public class ExampleCommand {

    public static void giveSpecialItem(ServerPlayer player) {
        Item item = Items.DIAMOND_AXE; // Example item

        // Using Component.literal to create the display name
        Component displayName = Component.literal("gamer balls"); // Create a component with literal text
        String uniqueId = "PIMPALAS";

        ItemStack customStack = ItemUtils.addCustomNBTData(item, displayName, uniqueId);
        ItemUtils.giveCustomItemToPlayer(player, customStack);
    }
}
