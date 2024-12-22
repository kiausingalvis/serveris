package com.example.examplemod.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSetSubtitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitleTextPacket;
import net.minecraft.network.protocol.game.ClientboundSetTitlesAnimationPacket;
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
    public static void sendServerTitle(MinecraftServer server, String title, String subtitle, int fadeIn, int fadeOut, int stay){
        for(ServerPlayer player : server.getPlayerList().getPlayers()){
            if(player!=null){
                player.connection.send(new ClientboundSetTitleTextPacket(Component.nullToEmpty(title)));
            }
            if (subtitle!=null){
                player.connection.send(new ClientboundSetSubtitleTextPacket(Component.nullToEmpty(subtitle)));
            }
            player.connection.send(new ClientboundSetTitlesAnimationPacket(fadeIn, stay, fadeOut));
        }
    }
}
