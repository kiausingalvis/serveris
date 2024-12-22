package com.example.examplemod.events;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "examplemod")
public class TickEvent {
    @SubscribeEvent
    public static void CheckPlayerHand(LivingEntityUseItemEvent event){
        if(event.getItem().is(Items.BOW)){
            event.setCanceled(true);
        }
    }
}
