package com.example.examplemod;

import com.example.examplemod.commands.Bank;
import com.example.examplemod.events.*;
import com.example.examplemod.skills.Mining;
import com.example.examplemod.utils.AddEtherWarp;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
@Mod(ExampleMod.MODID)
public class ExampleMod
{
    public static final String MODID = "examplemod";
    public ExampleMod()
    {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new Mining());
        MinecraftForge.EVENT_BUS.register(Bank.class);
        MinecraftForge.EVENT_BUS.register(RightClickEventHandler.class);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AddEtherWarp.Config.SPEC);
    }
}
