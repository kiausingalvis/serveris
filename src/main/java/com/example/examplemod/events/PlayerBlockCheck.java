package com.example.examplemod.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "examplemod", bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerBlockCheck {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (!(event.player instanceof ServerPlayer)) {
            return;
        }

        ServerPlayer player = (ServerPlayer) event.player;
        ServerLevel level = player.getLevel();
        BlockPos playerPos = player.blockPosition();
        BlockPos blockBelowPos = playerPos.below();
        BlockState blockBelowState = level.getBlockState(blockBelowPos);

        // Check if the block below is air, meaning there's no block
        if (blockBelowState.isAir()) {
            // Place a stone block below the player
            //level.setBlockAndUpdate(blockBelowPos, Blocks.STONE.defaultBlockState());
        }
    }
}
