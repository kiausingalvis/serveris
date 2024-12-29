package com.example.examplemod.chestgui;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.ItemStackHandler;

import static com.example.examplemod.commands.VdleTestCommand.renameItem;

public class CombatSkillMenu {
        public static int mainmenu(ServerPlayer player){
            ItemStack noNameGlass = (renameItem(new ItemStack(Items.CYAN_STAINED_GLASS_PANE), ""));
            noNameGlass.getOrCreateTag().putString("UniqueID", "MENU_GLASS");
            ItemStack noNameGlass_black = (renameItem(new ItemStack(Items.GRAY_STAINED_GLASS_PANE), ""));

            ItemStackHandler chestContents = new ItemStackHandler(54);
            for (int i = 0; i < 54; i++) {
                if (i % 9 == 0 || i % 9 == 8 || i < 9 || i >= 45) {
                    chestContents.setStackInSlot(i, noNameGlass);
                }
            }
            for (int i = 0; i < 54; i++) {
                if(chestContents.getStackInSlot(i).is(Items.AIR)){
                    chestContents.setStackInSlot(i, noNameGlass_black);
                }
            }
            CustomChestMenu.openChestGUI(player, chestContents, "§9COMBAT SKILL (please kill me)");
            return 0;
        }
    }
