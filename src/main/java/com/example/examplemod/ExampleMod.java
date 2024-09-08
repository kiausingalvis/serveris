package com.example.examplemod;

import com.example.examplemod.commands.Bank;
import com.example.examplemod.commands.CompactCommand;
import com.example.examplemod.commands.RemoveCaneCommand;
import com.example.examplemod.commands.SellHand;
import com.example.examplemod.events.*;
import com.example.examplemod.skills.Mining;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.event.server.ServerStartingEvent;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ExampleMod.MODID)
public class ExampleMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "examplemod";


    public ExampleMod()
    {

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new Mining());
        MinecraftForge.EVENT_BUS.register(ZombieSpawnEvent.class);
        MinecraftForge.EVENT_BUS.register(BossStuff.class);
        MinecraftForge.EVENT_BUS.register(CompactCommand.class);
        MinecraftForge.EVENT_BUS.register(RemoveCaneCommand.class);
        MinecraftForge.EVENT_BUS.register(Bank.class);
        MinecraftForge.EVENT_BUS.register(SellHand.class);
        MinecraftForge.EVENT_BUS.register(BlockBreakEventHandler.class);
        MinecraftForge.EVENT_BUS.register(BlockPlaceEvent.class);
        MinecraftForge.EVENT_BUS.register(RightClickEventHandler.class);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }



    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
    }
    @SubscribeEvent
    public void registerCommands(RegisterCommandsEvent event) {
        RemoveCaneCommand.register(event.getDispatcher());
    }

}
