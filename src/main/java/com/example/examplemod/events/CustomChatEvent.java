package com.example.examplemod.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.network.chat.Component;

import static com.example.examplemod.ExampleMod.MODID;
import static com.example.examplemod.utils.ClientMessage.sendServerMessageAsComponent;

@Mod.EventBusSubscriber(modid = MODID)
public class CustomChatEvent {
    @SubscribeEvent
    public static void FormatMessage(ServerChatEvent event){
        ServerPlayer player = event.getPlayer();
        String originalMessage = event.getMessage().getString();
        String playerName = player.getName().getString();
        Component customMessage = Component.literal("§4[GANDON]")
                .append(Component.literal(" §2"+playerName))
                .append(Component.literal("§f: "))
                .append(Component.literal("§6"+originalMessage));

        event.setCanceled(true);
        sendServerMessageAsComponent(event.getPlayer().server, customMessage);
    }
    }
