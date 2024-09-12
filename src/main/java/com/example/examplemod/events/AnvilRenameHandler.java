package com.example.examplemod.events;

import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "examplemod")
public class AnvilRenameHandler {

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack leftSlot = event.getLeft();
        ItemStack rightSlot = event.getRight();

        if (isCustomItem(leftSlot)) {
            if (!event.getName().isEmpty()) {
                event.setCanceled(true);
            }
        }
    }

    private static boolean isCustomItem(ItemStack itemStack) {
        if (itemStack.hasTag()) {
            CompoundTag tag = itemStack.getTag();
            return tag != null && tag.contains("UniqueId");
        }
        return false;
    }
}
