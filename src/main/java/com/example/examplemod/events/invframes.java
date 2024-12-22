package com.example.examplemod.events;

import net.minecraft.advancements.critereon.EntityHurtPlayerTrigger;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod.EventBusSubscriber
public class invframes {
    // Event handler for adjusting invulnerability frames for all living entities
    @SubscribeEvent
    public void onLivingEntityHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof LivingEntity) {
            LivingEntity entity = event.getEntity();
            entity.hurtDuration = 0;
            entity.hurtTime = 0;
            entity.invulnerableTime=0;
            entity.setInvulnerable(false);
            entity.setArrowCount(0);
            entity.removeArrowTime=0;
            System.out.println(entity.getAttributes());
        }
    }
}