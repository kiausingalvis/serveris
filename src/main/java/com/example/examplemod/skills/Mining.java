package com.example.examplemod.skills;

import com.example.examplemod.Config;
import com.example.examplemod.ExampleMod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID)
public class Mining {
    private static final Block[] Blocks_with_xp={
            Blocks.COAL_ORE,
            Blocks.DIAMOND_ORE,
            Blocks.IRON_ORE,
            Blocks.GOLD_ORE,
            Blocks.EMERALD_ORE,
            Blocks.DIAMOND_BLOCK
    };
    private static final int[] Blocks_given_xp={
            20, //coal
            50, //diamond
            25, //iron
            35, //gold
            5, //emerald
            250 //dmd block

    };
    private static float MINING_LEVEL = 0.0f;
    private static final int[] MINING_LEVEL_XP = {
      0, 3000, 6000, 9000, 12000, 15000, 18000, 21000, 24000, 27000, 30000, 33000, 36000, 39000, 42000
    };
    private static float MINING_DROP_RATE =0f; // 1500%


    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        int get_mining_xp = Config.MiningSkill.getMiningXp(event.getPlayer());
        for(int a = 1; a<=MINING_LEVEL_XP.length;a++){
            if(get_mining_xp >= MINING_LEVEL_XP[a-1]){
                MINING_LEVEL = a;
                MINING_DROP_RATE = (float) (MINING_LEVEL*0.5);
            }
        }
        for(int i = 1; i<=Blocks_with_xp.length;i++) {
            if (event.getState().getBlock() == Blocks_with_xp[i - 1]) {
                Config.MiningSkill.addMiningXp(event.getPlayer(), Blocks_given_xp[i - 1]);
                System.out.println(event.getPlayer() + " mining xp: " + Config.MiningSkill.getMiningXp(event.getPlayer())+ " Mining Level: " + MINING_LEVEL);
                if (!event.getPlayer().isCreative() && !event.getLevel().isClientSide()) {
                    ServerLevel world = (ServerLevel) event.getLevel();
                    BlockPos pos = event.getPos();
                    BlockState state = event.getState();
                    List<ItemStack> drops = Block.getDrops(state, world, pos, null, event.getPlayer(), event.getPlayer().getMainHandItem());

                    if (!drops.isEmpty()) {
                        ItemStack drop = drops.get(0).copy();
                        int totalDrops = (int) Math.floor(MINING_DROP_RATE);
                        float extraDropChance = MINING_DROP_RATE - totalDrops;

                        // Always drop the default count
                        for (int t = 0; t < totalDrops; t++) {
                            Block.popResource(world, pos, drop.copy());
                        }

                        // Calculate the extra drop based on the fractional part of the mining drop rate
                        if (Math.random() < extraDropChance) {
                            Block.popResource(world, pos, drop.copy());
                        }
                    }
                }
            }

        }

    }
}
