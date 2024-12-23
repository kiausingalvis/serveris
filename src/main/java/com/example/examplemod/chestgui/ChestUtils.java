package com.example.examplemod.chestgui;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber
public class ChestUtils {
    public static void openDoubleChest(ServerPlayer player, ItemStackHandler chestContents, String containerName) {
        // Ensure chestContents has the correct size
        if (chestContents.getSlots() != 54) {
            throw new IllegalArgumentException("Double chest inventory must have exactly 54 slots.");
        }

        // Create the double chest menu provider
        SimpleMenuProvider containerProvider = new SimpleMenuProvider(
                (id, playerInventory, playerEntity) -> {
                    return ChestMenu.sixRows(id, playerInventory, new SimpleContainer(54) {
                        @Override
                        public int getContainerSize() {
                            return chestContents.getSlots();
                        }

                        @Override
                        public ItemStack getItem(int slot) {
                            return chestContents.getStackInSlot(slot);
                        }

                        @Override
                        public void setItem(int slot, ItemStack stack) {
                            chestContents.setStackInSlot(slot, stack);
                        }

                        @Override
                        public boolean canPlaceItem(int slot, @NotNull ItemStack stack) {
                            return false;
                        }
                    });
                },
                Component.literal(containerName)
        );

        // Open the container for the player
        player.openMenu(containerProvider);

    }
}
