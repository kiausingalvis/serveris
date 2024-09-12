package com.example.examplemod.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ArrowChecker {
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
            for (ServerLevel level : event.getServer().getAllLevels()) {
                for (Entity entity : level.getEntities().getAll()) {
                    if (entity instanceof AbstractArrow arrow) {
                        CompoundTag nbt = arrow.serializeNBT();
                        int life = nbt.getInt("life");
                        if (life>40) {
                            arrow.discard();
                        }
                    }
                }
        }
    }
}
