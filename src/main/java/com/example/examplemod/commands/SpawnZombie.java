package com.example.examplemod.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.example.examplemod.events.MobSpawner.spawnMob;

@Mod.EventBusSubscriber
public class SpawnZombie {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("spawngay")
                .executes(context -> {
                    return SpawnZombie(context.getSource());
                })
        );
    }
    public static int SpawnZombie(CommandSourceStack source){
        Player player = source.getPlayer();
        Vec3 position = new Vec3(player.getX(), player.getY()+5,player.getZ());
        spawnMob(source.getLevel(), EntityType.WITHER_SKELETON,  position, "GAIDYS", 200, "GAIDYS", 3);
        //spawnMob(source.getLevel(), EntityType.ZOMBIE,  position, "DEBIL", 1000, "DEBIL", 30);
        return 1;
    }

}
