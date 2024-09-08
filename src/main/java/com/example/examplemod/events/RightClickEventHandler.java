package com.example.examplemod.events;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.example.examplemod.utils.ArrowShooter.shootArrow;

@Mod.EventBusSubscriber(modid = "examplemod") // Replace "yourmodid" with your actual mod ID
public class RightClickEventHandler {
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity().createCommandSourceStack().getPlayer();

        if(player.getMainHandItem().getDisplayName().equals("BYBYS")){
            shootArrow((ServerPlayer) player);
        }
    }
}
