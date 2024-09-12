package com.example.examplemod.events;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "examplemod")
public class AnvilRenameHandler {

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        event.setCanceled(true);
    }
}
