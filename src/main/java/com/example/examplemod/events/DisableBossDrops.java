package com.example.examplemod.events;

import com.example.examplemod.ExampleMod;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExampleMod.MODID)
public class DisableBossDrops {

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        Entity entity = event.getEntity();
        DamageSource source = event.getSource();

        if (entity instanceof Zombie && entity.getPersistentData().getString("CustomTag").equals("Level10Zombie")) {
            event.getDrops().removeIf(drop -> drop.getItem().getItem() == Items.ROTTEN_FLESH);
        }
    }
}
