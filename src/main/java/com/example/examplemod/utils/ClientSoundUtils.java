package com.example.examplemod.utils;

import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class ClientSoundUtils {
    public static void sendClientSound(ServerPlayer player, SoundEvent soundEvent, float volume, float pitch) {
        ClientboundSoundPacket soundPacket = new ClientboundSoundPacket(soundEvent,SoundSource.MASTER, player.getX(),player.getY(),player.getZ(),volume,pitch,player.getLevel().random.nextLong());
        player.connection.send(soundPacket);
    }
}