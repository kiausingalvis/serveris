package com.example.examplemod.events;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(Dist.DEDICATED_SERVER) // or Dist.CLIENT for client-side
public class Telekinesis {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;

        // Only run on the server side and at the end phase of the tick
        if (player.level.isClientSide || event.phase != TickEvent.Phase.END) {
            return;
        }

        // Check if the player is holding an item with the Telekinesis lore
        ItemStack heldItem = player.getMainHandItem();
        if (!hasTelekinesisLore(heldItem)) {
            return;
        }

        // Define the search radius
        double radius = 5.0;

        // Find nearby items within the specified radius
        List<ItemEntity> nearbyItems = player.level.getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(radius));

        for (ItemEntity itemEntity : nearbyItems) {
            // Attempt to add the item to the player's inventory
            boolean added = player.getInventory().add(itemEntity.getItem());

            // Check if the item was successfully added
            if (added) {
                // Remove the item entity from the world
                itemEntity.discard();
            }
        }
    }

    private static boolean hasTelekinesisLore(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return false;
        }

        var displayTag = itemStack.getTagElement("display");
        if (displayTag != null) {
            var lore = displayTag.getList("Lore", 8);
            for (int i = 0; i < lore.size(); i++) {
                var loreEntry = lore.getString(i);
                if (loreEntry.contains("Telekinesis")) {
                    return true;
                }
            }
        }

        return false;
    }
}
