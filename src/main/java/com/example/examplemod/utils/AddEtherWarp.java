package com.example.examplemod.utils;

import com.example.examplemod.ExampleMod;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeConfigSpec;
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
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.examplemod.utils.ClientSoundUtils.sendClientSound;

public class AddEtherWarp {

    // Method to handle teleporting if player is crouching and right-clicks on a block
    public static InteractionResult teleportIfCrouchRightClick(ServerPlayer player, InteractionHand hand) {
        // Check if player is crouching
        if (player.isCrouching()) {
            // Perform ray tracing to find the block the player is looking at
            BlockHitResult blockHitResult = getBlockLookingAt(player);
                // Ensure the hit result is a block
                if (blockHitResult.getType() == HitResult.Type.BLOCK) {
                    BlockPos targetBlockPos = blockHitResult.getBlockPos();

                    // Calculate the target position on top of the block (center)
                    Vec3 targetPosition = new Vec3(targetBlockPos.getX() + 0.5, targetBlockPos.getY() + 1, targetBlockPos.getZ() + 0.5);

                    // Teleport the player to the top center of the block
                    player.teleportTo(targetPosition.x, targetPosition.y, targetPosition.z);
                    sendClientSound(player, SoundEvents.WOODEN_DOOR_OPEN,1,1);
                    return InteractionResult.SUCCESS;

                }
        }

        return InteractionResult.PASS;
    }

    // Perform ray tracing to detect the block the player is looking at
    private static BlockHitResult getBlockLookingAt(Player player) {
        // Get the player's eye position
        Vec3 startVec = player.getEyePosition(1.0F);
        // Get the player's look direction
        Vec3 lookVec = player.getLookAngle();
        // Define the range (distance to check)
        double range = 200.0D;

        // Calculate the end vector for ray tracing
        Vec3 endVec = startVec.add(lookVec.x * range, lookVec.y * range, lookVec.z * range);

        // Perform ray tracing to detect the block
        return player.level.clip(new ClipContext(startVec, endVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
    }

    @Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Config {

        public static final ForgeConfigSpec SPEC;
        private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

        static {
            SPEC = BUILDER.build();
        }

        public static class PlayerBank {
            private static final Map<UUID, Integer> playerBalances = new HashMap<>();
            private static final Gson gson = new Gson();
            private static final String DATA_FILE = "run/config/PlayerConfigData/player_balances.json";

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
                    System.out.println(Paths.get(DATA_FILE) + "PATH!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    if (Files.exists(Paths.get(DATA_FILE))) {
                        Reader reader = Files.newBufferedReader(Paths.get(DATA_FILE));
                        Type type = new TypeToken<Map<UUID, Integer>>() {}.getType();
                        Map<UUID, Integer> data = gson.fromJson(reader, type);
                        playerBalances.putAll(data);
                        reader.close();
                    }
                } catch (IOException e) {
                }
            }

            public static void saveBalances() {
                try {
                    Writer writer = Files.newBufferedWriter(Paths.get(DATA_FILE));
                    gson.toJson(playerBalances, writer);
                    writer.close();
                } catch (IOException e) {
                }
            }
        }
        public static class MiningSkill {
            private static final Map<UUID, Integer> playerMiningSkill = new HashMap<>();
            private static final Gson gson = new Gson();
            private static final String DATA_FILE = "run/config/PlayerConfigData/player_mining_skill.json";

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
}
