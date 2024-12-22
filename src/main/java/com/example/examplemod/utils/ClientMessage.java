package com.example.examplemod.utils;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
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
    public static void sendPlayerQuestion(ServerPlayer player, String question){
        Component questionText = Component.literal(question);

        // Create the "Yes" button
        Component yesOption = Component.literal("[Yes]")
                .setStyle(Style.EMPTY
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/iamaretardpleasebanme yes"))
                        .withColor(0x00FF00)); // Green color

        // Create the "No" button
        Component noOption = Component.literal("[No]")
                .setStyle(Style.EMPTY
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/iamaretardpleasebanme no"))
                        .withColor(0xFF0000)); // Red color

        // Combine the message
        Component message = Component.literal("")
                .append(questionText)
                .append(Component.literal(" "))
                .append(yesOption)
                .append(Component.literal(" "))
                .append(noOption);

        // Send the message to the player
        player.sendSystemMessage(message);
    }
}
