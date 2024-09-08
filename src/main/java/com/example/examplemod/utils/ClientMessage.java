package com.example.examplemod.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class ClientMessage {
    public static void sendClientMessage(ServerPlayer player, String message){
        if(player != null){
            player.sendSystemMessage(Component.literal(message));
        }
    }
    public static void sendServerMessage(MinecraftServer server, String message){
        for(ServerPlayer player : server.getPlayerList().getPlayers()){
            if(player!=null){
                player.sendSystemMessage(Component.literal(message));
            }
        }
    }
}
