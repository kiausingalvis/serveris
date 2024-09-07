package com.example.examplemod.commands;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@Mod.EventBusSubscriber(modid = "examplemod", bus = EventBusSubscriber.Bus.FORGE, value = Dist.DEDICATED_SERVER)
public class CompactCommand {

    private static final String CANE_COMPACTOR_TAG = "cane_compactor";

    @SubscribeEvent
    public static void onServerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ServerPlayer player = (ServerPlayer) event.player;

            // Check if player has the "cane_compactor" stick in offhand
            ItemStack offhandItem = player.getOffhandItem();
            if (!offhandItem.isEmpty() && hasCaneCompactorTag(offhandItem)) {

                // Check if player has at least 64 sugar cane in inventory
                if (hasEnoughCane(player, 64)) {
                    compactCane(player);
                }
            }
        }
    }

    private static boolean hasCaneCompactorTag(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        return tag != null && tag.contains(CANE_COMPACTOR_TAG, StringTag.class.getModifiers()) && tag.getString(CANE_COMPACTOR_TAG).equals("true");
    }

    private static boolean hasEnoughCane(ServerPlayer player, int amount) {
        return player.getInventory().countItem(Items.SUGAR_CANE) >= amount;
    }

    private static void compactCane(ServerPlayer player) {
        int requiredCaneCount = 64; // Number of sugar cane required
        int enchantedCaneCount = 1; // Number of Enchanted Sugar Cane to give

        // Consume 64 sugar cane
        removeCane(player, requiredCaneCount);

        // Give Enchanted Sugar Cane
        giveEnchantedCane(player, enchantedCaneCount);

        // Optionally, you can send a message to the player here if needed
        // player.sendMessage(new TextComponent("Successfully compacted 64 Sugar Cane into 1 Enchanted Sugar Cane!"), Util.NIL_UUID);

        // You can also log the action to console or perform any other necessary operations
    }

    private static void removeCane(ServerPlayer player, int amount) {
        player.getInventory().removeItem(new ItemStack(Items.SUGAR_CANE, amount));
    }

    private static void giveEnchantedCane(ServerPlayer player, int amount) {
        ItemStack enchantedCane = new ItemStack(Items.SUGAR_CANE);
        enchantedCane.setHoverName(Component.literal("Enchanted Sugar Cane"));
        player.getInventory().add(enchantedCane);
    }
}
