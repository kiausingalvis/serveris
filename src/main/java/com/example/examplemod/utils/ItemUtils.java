package com.example.examplemod.utils;

import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

import java.util.List;

public class ItemUtils {

    /**
     * Adds custom NBT data to an item stack.
     *
     * @param item The item to which the NBT data should be added.
     * @param displayName The display name of the item.
     * @param uniqueId A unique identifier for the item.
     * @return The modified ItemStack with custom NBT data.
     */
    public static ItemStack addCustomNBTData(Item item, Component displayName, String uniqueId) {
        // Create a new ItemStack with the given item
        ItemStack stack = new ItemStack(item);

        // Get or create the NBT tag
        CompoundTag tag = stack.getOrCreateTag();

        // Set the display name
        stack.setHoverName(displayName);

        // Set the custom NBT data
        CompoundTag customTag = tag.getCompound("CustomTag");
        customTag.putString("UniqueId", uniqueId);

        // Apply the custom tag to the item stack
        tag.put("CustomTag", customTag);
        stack.setTag(tag);

        return stack;
    }
    public static void giveCustomItemToPlayer(ServerPlayer player, ItemStack itemStack) {
        if (player.level instanceof ServerLevel serverLevel) {
            player.getInventory().add(itemStack);
            player.inventoryMenu.broadcastChanges();
        }
}
    public static void setItemLore(ItemStack itemStack, List<Component> lore) {
        ListTag loreTag = new ListTag();
        for (Component loreComponent : lore) {
            loreTag.add(StringTag.valueOf(Component.Serializer.toJson(loreComponent)));
        }
        itemStack.getOrCreateTagElement("display").put("Lore", loreTag);
    }
}
