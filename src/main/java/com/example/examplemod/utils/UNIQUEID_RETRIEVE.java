package com.example.examplemod.utils;

import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

public class UNIQUEID_RETRIEVE {

    /**
     * Retrieves the UniqueId from the NBT data of the given ItemStack.
     *
     * @param itemStack The ItemStack from which to retrieve the UniqueId.
     * @return The UniqueId as a String, or null if not found.
     */
    public static String getUniqueId(ItemStack itemStack) {
        if (itemStack.hasTag()) {
            CompoundTag tag = itemStack.getTag();
            if (tag != null && tag.contains("CustomTag")) {
                CompoundTag customTag = tag.getCompound("CustomTag");
                if (customTag.contains("UniqueId")) {
                    return customTag.getString("UniqueId");
                }
            }
        }
        return "NONE";
    }

    public static String getRuneName(ItemStack itemStack) {
        if (itemStack.hasTag()) {
            CompoundTag tag = itemStack.getTag();
            if (tag != null && tag.contains("CustomTag")) {
                CompoundTag customTag = tag.getCompound("CustomTag");
                if (customTag.contains("Rune")) {
                    return customTag.getString("Rune");
                }
            }
        }
        return "NONE";
    }
    public static String getEnchants(ItemStack itemStack) {
        if (itemStack.hasTag()) {
            CompoundTag tag = itemStack.getTag();
            if (tag != null && tag.contains("CustomTag")) {
                CompoundTag customTag = tag.getCompound("CustomTag");
                if (customTag.contains("Enchants")) {
                    return customTag.getString("Enchants");
                }
            }
        }
        return "NONE";
    }
}
