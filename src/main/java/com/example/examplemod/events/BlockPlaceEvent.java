package com.example.examplemod.events;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockPlaceEvent {
    private static final Block[] Blocks_with_xp={
            Blocks.COAL_ORE,
            Blocks.DIAMOND_ORE,
            Blocks.IRON_ORE,
            Blocks.GOLD_ORE,
            Blocks.EMERALD_ORE,
            Blocks.DIAMOND_BLOCK,
            Blocks.TNT
    };

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.EntityPlaceEvent event) {
        for(int i = 1;i<=Blocks_with_xp.length;i++){
            if(event.getPlacedBlock().getBlock() == Blocks_with_xp[i-1]){
                event.setCanceled(true);
            }
        }
    }
}
