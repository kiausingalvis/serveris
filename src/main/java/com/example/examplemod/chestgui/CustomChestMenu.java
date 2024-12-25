package com.example.examplemod.chestgui;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Objects;

import static com.example.examplemod.utils.ClientMessage.sendClientMessage;

public class CustomChestMenu extends AbstractContainerMenu {
    private final SimpleContainer container;

    protected CustomChestMenu(int id, Inventory playerInventory, SimpleContainer container) {
        super(MenuType.GENERIC_9x6, id); // A 6-row chest
        this.container = container;

        int rows = 6; // Adjust for other sizes if needed
        int containerStartY = 18;
        int playerInventoryStartY = 140;
        int slotSize = 18;

        // Add chest inventory slots (6 rows of 9)
        for (int row = 0; row < rows; ++row) {
            for (int col = 0; col < 9; ++col) {
                int index = col + row * 9;
                this.addSlot(new LockedSlot(container, index, 8 + col * slotSize, containerStartY + row * slotSize));
            }
        }

        // Add player inventory slots
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int index = col + row * 9 + 9;
                this.addSlot(new Slot(playerInventory, index, 8 + col * slotSize, playerInventoryStartY + row * slotSize));
            }
        }

        // Add player hotbar slots
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * slotSize, playerInventoryStartY + 58));
        }
    }

    @Override
    public boolean stillValid(net.minecraft.world.entity.player.Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(net.minecraft.world.entity.player.Player player, int index) {
        return ItemStack.EMPTY; // Prevent shift-clicking behavior
    }

    // Function to open the GUI
    public static void open(ServerPlayer player, String containerName, ItemStackHandler chestContents) {
        // Ensure chestContents has the correct size
        if (chestContents.getSlots() != 54) {
            throw new IllegalArgumentException("Double chest inventory must have exactly 54 slots.");
        }

        SimpleContainer container = new SimpleContainer(54) {
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
            public boolean canPlaceItem(int slot, ItemStack stack) {
                return false; // Prevent placing items
            }
        };

        player.openMenu(new SimpleMenuProvider((id, playerInventory, p) ->
                new CustomChestMenu(id, playerInventory, container),
                Component.literal(containerName)));

    }


    // Custom slot to prevent item pickup and placement
    public static class LockedSlot extends Slot {
        public LockedSlot(SimpleContainer container, int index, int x, int y) {
            super(container, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack) {
            return false; // Prevent placing items
        }

        @Override
        public boolean mayPickup(net.minecraft.world.entity.player.Player player) {
            if(this.getItem().getShareTag().get("UniqueID") == null){
                return false;
            }
            if(this.getItem().getTag().get("UniqueID").getAsString().equals("COMBAT_SKILL_MENU")){
                CustomItems.mainmenu((ServerPlayer) player);
            }
            return false;
        }
    }
    public static void openChestGUI(ServerPlayer player, ItemStackHandler containerContens, String containerName) {

        CustomChestMenu.open(player, containerName, containerContens);
    }
}
