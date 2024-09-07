package com.example.examplemod.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ClientMessage {
    public static void sendClientMessage(ServerPlayer player, String message){
        if(player != null){
            player.sendSystemMessage(Component.literal(message));
        }
    }
}
