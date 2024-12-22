package com.example.examplemod;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;

@Mod.EventBusSubscriber
public class SkinManager {

    // List of available skins with URLs
    private static final HashMap<String, String> SKIN_LIST = new HashMap<>();

    static {
        SKIN_LIST.put("CustomSkin", "http://textures.minecraft.net/texture/67755b529939475bff9994fd529504b3e144974fbfdddb7cb10a7dc0659b07d27");
    }

    /**
     * Change a player's skin to a specified skin.
     *
     * @param skinName The name of the skin (must exist in SKIN_LIST).
     * @param player   The ServerPlayer instance of the player.
     */
    public static void ChangePlayerSkin(String skinName, ServerPlayer player) {
        if (!SKIN_LIST.containsKey(skinName)) {
            System.out.println("Skin not found: " + skinName);
            return;
        }

        String skinUrl = SKIN_LIST.get(skinName);

        // Get the player's GameProfile
        GameProfile profile = player.getGameProfile();

        // Clear old textures
        profile.getProperties().removeAll("textures");

        // Add the new texture without a signature
        profile.getProperties().put("textures", new Property("textures", encodeSkinData(skinUrl), ""));

        // Respawn the player to update the skin
        respawnPlayer(player);
    }

    /**
     * Respawn the player to apply the skin change.
     *
     * @param player The ServerPlayer instance of the player.
     */
    private static void respawnPlayer(ServerPlayer player) {
        MinecraftServer server = player.getServer();
        if (server == null) {
            return;
        }

        // Save player state
        Level level = player.getLevel();
        ServerGamePacketListenerImpl connection = player.connection;
        GameType gameType = player.gameMode.getGameModeForPlayer();
        GameProfile gameProfile = null;

        // Remove the player from the server
        server.getPlayerList().remove(player);

        // Create a new ServerPlayer instance
        ServerPlayer newPlayer = new ServerPlayer(server, player.getLevel(), player.getGameProfile(), null);
        newPlayer.connection = connection;
        newPlayer.setGameMode(gameType);

        // Add the player back to the server
        server.getPlayerList().placeNewPlayer(connection.getConnection(), newPlayer);

        // Sync player state
        newPlayer.setPos(player.getX(), player.getY(), player.getZ());
    }

    /**
     * Encode the skin data into Base64 format for the player profile.
     *
     * @param url The texture URL.
     * @return Base64-encoded skin data.
     */
    private static String encodeSkinData(String url) {
        String json = String.format("{\"textures\":{\"SKIN\":{\"url\":\"%s\"}}}", url);
        return java.util.Base64.getEncoder().encodeToString(json.getBytes());
    }
}
