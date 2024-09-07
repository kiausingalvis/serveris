package com.example.examplemod;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStoppingEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    public static final ForgeConfigSpec SPEC;
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    static {
        SPEC = BUILDER.build();
    }

    public static class PlayerBank {
        private static final Map<UUID, Integer> playerBalances = new HashMap<>();
        private static final Gson gson = new Gson();
        private static final String DATA_FILE = "player_balances.json";

        public static int getBalance(Player player) {
            return playerBalances.getOrDefault(player.getUUID(), 0);
        }

        public static void setBalance(Player player, int balance) {
            playerBalances.put(player.getUUID(), balance);
        }

        public static void addBalance(Player player, int amount) {
            playerBalances.put(player.getUUID(), getBalance(player) + amount);
        }

        public static void loadBalances() {
            try {
                if (Files.exists(Paths.get(DATA_FILE))) {
                    Reader reader = Files.newBufferedReader(Paths.get(DATA_FILE));
                    Type type = new TypeToken<Map<UUID, Integer>>() {}.getType();
                    Map<UUID, Integer> data = gson.fromJson(reader, type);
                    playerBalances.putAll(data);
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void saveBalances() {
            try {
                Writer writer = Files.newBufferedWriter(Paths.get(DATA_FILE));
                gson.toJson(playerBalances, writer);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static class MiningSkill {
        private static final Map<UUID, Integer> playerMiningSkill = new HashMap<>();
        private static final Gson gson = new Gson();
        private static final String DATA_FILE = "player_mining_skill.json";

        public static int getMiningXp(Player player) {
            return playerMiningSkill.getOrDefault(player.getUUID(), 0);
        }

        public static void setMiningXp(Player player, int balance) {
            playerMiningSkill.put(player.getUUID(), balance);
        }

        public static void addMiningXp(Player player, int amount) {
            playerMiningSkill.put(player.getUUID(), getMiningXp(player) + amount);
        }

        public static void loadMiningXp() {
            try {
                if (Files.exists(Paths.get(DATA_FILE))) {
                    Reader reader = Files.newBufferedReader(Paths.get(DATA_FILE));
                    Type type = new TypeToken<Map<UUID, Integer>>() {}.getType();
                    Map<UUID, Integer> data = gson.fromJson(reader, type);
                    playerMiningSkill.putAll(data);
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static void saveMiningXp() {
            try {
                Writer writer = Files.newBufferedWriter(Paths.get(DATA_FILE));
                gson.toJson(playerMiningSkill, writer);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class PlayerEvents {
        @SubscribeEvent
        public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            Player player = event.getEntity();
            PlayerBank.playerBalances.putIfAbsent(player.getUUID(), 0);
            MiningSkill.playerMiningSkill.putIfAbsent(player.getUUID(), 0);
        }

        @SubscribeEvent
        public static void onServerStarting(ServerStartingEvent event) {
            PlayerBank.loadBalances();
            MiningSkill.loadMiningXp();
        }

        @SubscribeEvent
        public static void onServerStopping(ServerStoppingEvent event) {
            PlayerBank.saveBalances();
            MiningSkill.saveMiningXp();
        }
    }
}

