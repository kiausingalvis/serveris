package com.example.examplemod.events;

import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.network.chat.Component;

import static com.example.examplemod.ExampleMod.MODID;
import static com.example.examplemod.utils.ClientMessage.sendServerMessage;

@Mod.EventBusSubscriber(modid = MODID)
public class CustomChatEvent {
    @SubscribeEvent
    public static void FormatMessage(ServerChatEvent event){
        String rankColor = "§4";
        String playerNameColor = "§c";
        String messageColor = "§e";
        String rankName = "[Lv1 Gooner]";
        ServerPlayer player = event.getPlayer();
        String originalMessage = event.getMessage().getString();
        String playerName = player.getName().getString();
        String message = rankColor+rankName+ " "+playerNameColor+playerName+"§f: "+messageColor+originalMessage;
        String message1 = "§c"+"[Lv2 Gooner] "+ "§e"+playerName+"§f: "+"§6"+originalMessage;
        event.setCanceled(true);
        if(event.getPlayer().getName().getString().equals("vdle")){
            sendServerMessage(event.getPlayer().server, message1);
        }else{
            sendServerMessage(event.getPlayer().server, message);
        }

    }
    }
