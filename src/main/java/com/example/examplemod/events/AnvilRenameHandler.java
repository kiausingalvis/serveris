package com.example.examplemod.events;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

import static com.example.examplemod.ExampleMod.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class AnvilRenameHandler {

    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        event.setCanceled(true);
    }
        @SubscribeEvent
        public static void onLivingEntityHurt(LivingHurtEvent event) {
                LivingEntity entity = event.getEntity();
                entity.hurtDuration = 0;
                entity.hurtTime = 0;
                entity.hurtDir=0;
                entity.hurtMarked=false;
                entity.invulnerableTime=0;
                entity.swing(InteractionHand.MAIN_HAND);
                entity.yHeadRot=20;
                entity.setYRot(20);
                entity.setDiscardFriction(true);
        }
}
