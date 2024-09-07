package com.example.examplemod.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.meta.When;
import java.util.Collections;
import java.util.Random;


@Mod.EventBusSubscriber
public class BossStuff {
    static int TimesHealed = 0;
    static final int Heal_cooldown = 2; //in ticks
    static int getHeal_cooldown = 0;
    static boolean ShouldBossHeal = false;
    static boolean DidBossHeal = false;
    static int[] SplashAreas = {1,-1,2,3,-1,2,1,-1,-2,2,1,-1,1,2,-2,2,-3,2,1,-2,2,-3};
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        if (!(event.player.level instanceof ServerLevel serverLevel)) {
            return;
        }

        // Iterate through all zombies in the world
        for (Entity entity : serverLevel.getEntities(EntityType.ZOMBIE, entity -> true)) {
            if (entity instanceof Zombie zombie) {
                // Update the custom nametag with current health
                zombie.clearFire();
                if(zombie.isBaby()){
                    zombie.kill();
                }

                CompoundTag data = zombie.getPersistentData();
                String customTag = data.getString("CustomTag");
                if("Level10Zombie".equals(customTag)){
                    String formattedHealth = String.format("%.1f", zombie.getHealth());
                    Component customNametag = Component.literal("§b§l<KLAN LEADER> §e§l"+ formattedHealth + " §4\u2764");
                    zombie.setCustomName(customNametag);
                    zombie.setCustomNameVisible(true);
                    if(zombie.getHealth()<500 && !DidBossHeal && !zombie.isDeadOrDying()){
                        ShouldBossHeal = true;
                        DidBossHeal = true;
                    }
                    if(zombie.isDeadOrDying()){
                        DidBossHeal = false;
                    }
                    if(TimesHealed==20){
                        ShouldBossHeal=false;
                        TimesHealed=0;
                    }
                    if(ShouldBossHeal && TimesHealed <= 40){
                        getHeal_cooldown++;
                        if(getHeal_cooldown == Heal_cooldown){
                            CompoundTag tag = new CompoundTag();
                            ItemStack potion = new ItemStack(Items.SPLASH_POTION);
                            potion.setTag(tag);
                            tag.putString("Potion", "minecraft:strong_harming");
                            ThrownPotion thrownPotion = new ThrownPotion(serverLevel, zombie.position().x + SplashAreas[TimesHealed], zombie.position().y + 3.0, zombie.position().z + SplashAreas[TimesHealed]);
                            thrownPotion.setItem(potion);
                            serverLevel.addFreshEntity(thrownPotion);
                            getHeal_cooldown = 0;
                            TimesHealed++;
                        }
                    }
                }else{
                    String formattedHealth = String.format("%.1f", zombie.getHealth());
                    Component customNametag = Component.literal("§e§l[Lvl §a§l1§e§l] Zombie §4§l" + formattedHealth + " §4§l\u2764");
                    zombie.setCustomName(customNametag);
                    zombie.setCustomNameVisible(true);
                }
               // if(zombie.getHealth()<=500){
                   // ItemStack Whip = Items.BOW.getDefaultInstance();
                   // Whip.setHoverName(Component.nullToEmpty("Whip"));
                   /// EnchantmentHelper.setEnchantments(Collections.singletonMap(Enchantments.PUNCH_ARROWS,5), Whip);
                  //  zombie.setItemSlot(EquipmentSlot.MAINHAND, Whip);
              //  }

            }
        }
    }
}
///give @p minecraft:player_head{SkullOwner:{Id:[I;-74212603,-1804381175,-1596149326,-1701137274],Properties:{textures:[{Value:"e3RleHR1cmVzOntTS0lOOnt1cmw6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTQ5YmEyOWZkODk0MDEzZGZkMTM0YjRkYjkwN2JlNGE1NTdlZDllZDU1YzhlN2QyZWVmMjA3NzI5MjJmY2E1MyJ9fX0="}]}}}