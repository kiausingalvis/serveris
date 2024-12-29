package com.example.examplemod;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Config {

    private static final Gson GSON = new Gson();
    private static final Path DATA_DIR = Paths.get("run/world/data/player_data/");
    private static final Path BALANCE_FILE = DATA_DIR.resolve("player_balances.json");
    private static final Path MINING_XP_FILE = DATA_DIR.resolve("player_mining_skill.json");

    private static final Map<UUID, Integer> playerBalances = new HashMap<>();
    private static final Map<UUID, Integer> playerMiningXp = new HashMap<>();

    public static int getBalance(Player player) {
        return playerBalances.getOrDefault(player.getUUID(), 0);
    }

    public static void addBalance(Player player, int amount) {
        playerBalances.put(player.getUUID(), getBalance(player) + amount);
    }

    public static int getMiningXp(Player player) {
        return playerMiningXp.getOrDefault(player.getUUID(), 0);
    }

    public static void addMiningXp(Player player, int amount) {
        playerMiningXp.put(player.getUUID(), getMiningXp(player) + amount);
    }

    public static void loadPlayerData() {
        try {
            if (!Files.exists(DATA_DIR)) {
                Files.createDirectories(DATA_DIR);
            }
            if (Files.exists(BALANCE_FILE)) {
                try (Reader reader = Files.newBufferedReader(BALANCE_FILE)) {
                    Type type = new TypeToken<Map<UUID, Integer>>() {}.getType();
                    playerBalances.putAll(GSON.fromJson(reader, type));
                }
            }
            if (Files.exists(MINING_XP_FILE)) {
                try (Reader reader = Files.newBufferedReader(MINING_XP_FILE)) {
                    Type type = new TypeToken<Map<UUID, Integer>>() {}.getType();
                    playerMiningXp.putAll(GSON.fromJson(reader, type));
                }
            }
        } catch (IOException e) {
        }
    }

    public static void savePlayerData() {
        try {
            if (!Files.exists(DATA_DIR)) {
                Files.createDirectories(DATA_DIR);
            }
            try (Writer writer = Files.newBufferedWriter(BALANCE_FILE)) {
                GSON.toJson(playerBalances, writer);
            }
            try (Writer writer = Files.newBufferedWriter(MINING_XP_FILE)) {
                GSON.toJson(playerMiningXp, writer);
            }
        } catch (IOException e) {
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        playerBalances.putIfAbsent(player.getUUID(), 0);
        playerMiningXp.putIfAbsent(player.getUUID(), 0);
    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        loadPlayerData();
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        savePlayerData();
    }
}
