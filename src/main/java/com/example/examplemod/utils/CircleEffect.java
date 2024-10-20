package com.example.examplemod.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

import static com.example.examplemod.utils.CustomItemUtil.giveCustomItem;

@Mod.EventBusSubscriber
public class CircleEffect {

    private static final Logger LOGGER = LogManager.getLogger();
    private static final int PLACEMENT_DELAY = 10; // Delay between placing each radius level (in ticks)
    private static final long REVERT_DELAY = 100L; // Delay before reverting the circle (in ticks)
    private static final Map<Long, CircleData> activeCircles = new HashMap<>();
    private static long currentId = 0;

    public static void startRadius(Entity entity, Block block, int maxRadius) {
        if (!(entity.level instanceof ServerLevel level)) {
            LOGGER.error("Not a server level, cannot proceed.");
            return;
        }



        BlockPos center = new BlockPos(entity.getX(), entity.getY() - 1, entity.getZ());
        LOGGER.info("Starting circle effect at " + center + " with max radius " + maxRadius);

        CircleData circleData = new CircleData(level, center, block, maxRadius);
        activeCircles.put(currentId++, circleData);
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.LevelTickEvent event) {
        if (!(event.level instanceof ServerLevel level) || event.phase != TickEvent.Phase.END) return;

        List<Long> circlesToRemove = new ArrayList<>();

        for (Map.Entry<Long, CircleData> entry : activeCircles.entrySet()) {
            CircleData circleData = entry.getValue();
            LOGGER.debug("Processing circle with ID " + entry.getKey());

            // Ticking to allow time progression
            circleData.tick();

            // If the circle is completed, check for reversion
            if (circleData.isComplete()) {
                if (circleData.shouldRevert()) {
                    LOGGER.info("Reverting circle with ID " + entry.getKey());
                    circleData.revert();
                    circlesToRemove.add(entry.getKey());
                }
                continue;
            }

            // Incrementally place the next radius
            if (circleData.canPlaceNextRadius()) {
                LOGGER.info("Placing next radius for circle with ID " + entry.getKey());
                circleData.placeNextRadius();
            } else {
                LOGGER.debug("Waiting for the next tick to place radius for circle with ID " + entry.getKey());
            }
        }

        // Remove circles that have fully reverted
        for (Long id : circlesToRemove) {
            LOGGER.info("Removing circle with ID " + id + " after reversion.");
            activeCircles.remove(id);
        }
    }

    private static class CircleData {
        private final ServerLevel level;
        private final BlockPos center;
        private final Block block;
        private final int maxRadius;
        private final Map<BlockPos, BlockState> originalBlocks = new HashMap<>();
        private int currentRadius = 0;
        private long ticksSinceLastRadius = 0;
        private long ticksSinceCompletion = 0;
        private boolean circleCompleted = false;

        public CircleData(ServerLevel level, BlockPos center, Block block, int maxRadius) {
            this.level = level;
            this.center = center;
            this.block = block;
            this.maxRadius = maxRadius;
        }

        // Increment the ticks to track the delay
        public void tick() {
            if (!circleCompleted) {
                ticksSinceLastRadius++;
            }
        }

        public boolean canPlaceNextRadius() {
            return ticksSinceLastRadius >= PLACEMENT_DELAY;
        }

        public void placeNextRadius() {
            // Remove the previous radius
            if (currentRadius > 1) {
                int previousRadius = currentRadius - 1;
                for (int x = -previousRadius; x <= previousRadius; x++) {
                    for (int z = -previousRadius; z <= previousRadius; z++) {
                        if (x * x + z * z <= previousRadius * previousRadius) {
                            BlockPos pos = center.offset(x, 0, z);
                            BlockState originalState = originalBlocks.get(pos);
                            if (originalState != null) {
                                level.setBlock(pos, originalState, 3); // Restore original block
                                originalBlocks.remove(pos); // Remove from tracking to avoid reversion later
                            }
                        }
                    }
                }
            }

            // Place the new current radius
            for (int x = -currentRadius; x <= currentRadius; x++) {
                for (int z = -currentRadius; z <= currentRadius; z++) {
                    if (x * x + z * z <= currentRadius * currentRadius) {
                        BlockPos pos = center.offset(x, 0, z);
                        if (!originalBlocks.containsKey(pos)) {
                            originalBlocks.put(pos, level.getBlockState(pos));
                            level.setBlock(pos, block.defaultBlockState(), 3);
                        }
                    }
                }
            }

            ticksSinceLastRadius = 0; // Reset tick counter
            LOGGER.debug("Placed radius " + currentRadius + " and removed previous radius.");

            // Increment radius
            currentRadius++;
            if (currentRadius > maxRadius) {
                circleCompleted = true;
                LOGGER.info("Circle effect completed for center " + center);
            }
        }


        public boolean isComplete() {
            return circleCompleted;
        }

        public boolean shouldRevert() {
            if (circleCompleted) {
                ticksSinceCompletion++;
                return ticksSinceCompletion >= REVERT_DELAY;
            }
            return false;
        }

        public void revert() {
            for (Map.Entry<BlockPos, BlockState> entry : originalBlocks.entrySet()) {
                level.setBlock(entry.getKey(), entry.getValue(), 3);
            }
            LOGGER.info("Reverted circle at center " + center);
        }
    }
}
