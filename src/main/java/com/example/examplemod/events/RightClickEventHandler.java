package com.example.examplemod.events;

import com.example.examplemod.ItemConfigManager;
import com.example.examplemod.SukunaCleave;
import com.example.examplemod.utils.CalculateDamage;
import com.example.examplemod.utils.FireworkUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.example.examplemod.ExampleMod.MODID;
import static com.example.examplemod.utils.AddEtherWarp.teleportIfCrouchRightClick;
import static com.example.examplemod.utils.AddItemCooldown.addCooldownToItem;
import static com.example.examplemod.utils.ArrowShooter.shootArrow;
import static com.example.examplemod.utils.TeleportOnRightClickEvent.TeleportBlocksLookAngle;
import static com.example.examplemod.utils.UNIQUEID_RETRIEVE.getUniqueId;
import static com.example.examplemod.ItemConfigManager.loadItemData;

@Mod.EventBusSubscriber(modid = MODID)
public class RightClickEventHandler {
    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        PrimedTnt tnt = EntityType.TNT.create(event.getLevel());
        ServerPlayer player = event.getEntity().createCommandSourceStack().getPlayer();
        if(getUniqueId(player.getMainHandItem()).equals("PIMPALAS")){
            shootArrow(player, 10f);
        }
        if(getUniqueId(player.getMainHandItem()).equals("TERMINATOR")){
            //shootArrow(player, (float)CalculateDamage.DamageCalculationMelee(loadItemData("TERMINATOR").getDamage(),loadItemData("TERMINATOR").getStrength(),loadItemData("TERMINATOR").getCritDmg()));
        shootArrow(player, 5f);
        }
        if(getUniqueId(player.getMainHandItem()).equals("PIDERELLA")){
            TeleportBlocksLookAngle(player,8);
            addCooldownToItem(player, player.getMainHandItem().getItem(), 2);
            teleportIfCrouchRightClick(player, player.getUsedItemHand());
        }
        if(getUniqueId(player.getMainHandItem()).equals("CLEAVE_STICK")){
            SukunaCleave.triggerCleave(player);
        }
        if(getUniqueId(player.getMainHandItem()).equals("CLEAVE_DOMAIN_STICK")){
            SukunaCleave.ExpansionCleave(player);
        }
        if(getUniqueId(player.getMainHandItem()).equals("TNTSTICK")){
            tnt.setPos(player.getX(), player.getY(), player.getZ());
            tnt.setFuse(0);
            player.getLevel().addFreshEntity(tnt);
        }
        if(getUniqueId(player.getMainHandItem()).equals("FIREWORKWAND")){
            FireworkUtils.spawnInstantFirework(player);
            addCooldownToItem(player, player.getMainHandItem().getItem(), 5);
        }

    }
   @SubscribeEvent
    public static void onBlockRightClick(PlayerInteractEvent.RightClickBlock event){
        ServerPlayer player = (ServerPlayer) event.getEntity();
        if(getUniqueId(player.getMainHandItem()).equals("PIDERELLA")){
            TeleportBlocksLookAngle(player,8);
            addCooldownToItem(player, player.getMainHandItem().getItem(), 2);
            teleportIfCrouchRightClick(player, player.getUsedItemHand());
            event.setCanceled(true);
        }
       if(getUniqueId(player.getMainHandItem()).equals("FIREWORKWAND")){
           event.setCanceled(true);
       }
   }
}
