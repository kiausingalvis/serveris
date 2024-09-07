package com.example.examplemod.events;

import com.example.examplemod.ExampleMod;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BlockBreakEventHandler {


    private static final Logger LOGGER = LogManager.getLogger();
    private static final List<BlockRespawnTask> blockRespawnTasks = new ArrayList<>();
    private static final List<BlockPlaceTask> blockPlaceTasks = new ArrayList<>();

    private static final Map<Block, Integer> Blocks_given_xp = ImmutableMap.<Block, Integer>builder()
            .put(Blocks.COAL_ORE, 20)
            .put(Blocks.DIAMOND_ORE, 50)
            .put(Blocks.IRON_ORE, 25)
            .put(Blocks.GOLD_ORE, 35)
            .put(Blocks.EMERALD_ORE, 5)
            .put(Blocks.DIAMOND_BLOCK, 250)
            .build();

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        if (!(event.getLevel() instanceof ServerLevel)) {
            return;
        }
        ServerLevel level = (ServerLevel) event.getLevel();
        BlockPos pos = event.getPos();
        BlockState originalState = event.getState();
        Block brokenBlock = originalState.getBlock();
        // Check if the broken block is in our list
        if (Blocks_given_xp.containsKey(brokenBlock)) {

            // Schedule a task to place Bedrock after 1 tick
            blockPlaceTasks.add(new BlockPlaceTask(level, pos, Blocks.BEDROCK.defaultBlockState(), 1));

            // Schedule a task to respawn the original block after 20 ticks
            blockRespawnTasks.add(new BlockRespawnTask(level, pos, originalState, 200));
        }
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (event.level.isClientSide) {
            return;
        }

        // Execute the block place tasks
        blockPlaceTasks.removeIf(BlockPlaceTask::tick);

        // Execute the block respawn tasks
        blockRespawnTasks.removeIf(BlockRespawnTask::tick);
    }

    public static class BlockRespawnTask {
        private final ServerLevel level;
        private final BlockPos pos;
        private final BlockState originalState;
        private int ticksRemaining;

        public BlockRespawnTask(ServerLevel level, BlockPos pos, BlockState originalState, int ticks) {
            this.level = level;
            this.pos = pos;
            this.originalState = originalState;
            this.ticksRemaining = ticks;
        }

        public boolean tick() {
            ticksRemaining--;
            if (ticksRemaining <= 0) {
                level.setBlock(pos, originalState, 3);
                return true;
            }
            return false;
        }
    }

    public static class BlockPlaceTask {
        private final ServerLevel level;
        private final BlockPos pos;
        private final BlockState stateToPlace;
        private int ticksRemaining;

        public BlockPlaceTask(ServerLevel level, BlockPos pos, BlockState stateToPlace, int ticks) {
            this.level = level;
            this.pos = pos;
            this.stateToPlace = stateToPlace;
            this.ticksRemaining = ticks;
        }

        public boolean tick() {
            ticksRemaining--;
            if (ticksRemaining <= 0) {
                level.setBlock(pos, stateToPlace, 3);
                return true;
            }
            return false;
        }
    }
}
