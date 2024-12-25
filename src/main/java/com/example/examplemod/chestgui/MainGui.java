package com.example.examplemod.chestgui;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemStackHandler;

import static com.example.examplemod.commands.VdleTestCommand.createCustomHead;
import static com.example.examplemod.commands.VdleTestCommand.renameItem;

@Mod.EventBusSubscriber
public class MainGui {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("mainmenu")
                .executes(context -> {
                    return mainmenu(context.getSource());
                })
        );
    }

    private static int mainmenu(CommandSourceStack source){
        ItemStack noNameGlass = (renameItem(new ItemStack(Items.CYAN_STAINED_GLASS_PANE), ""));

        ItemStack noNameGlass_black = (renameItem(new ItemStack(Items.GRAY_STAINED_GLASS_PANE), ""));
        ItemStack miningSkillIcon = (renameItem(new ItemStack(Items.STONE_PICKAXE), "§9Mining Skill - future shit (maybe)"));
        ItemStack farmingSkillIcon = (renameItem(new ItemStack(Items.STONE_HOE), "§9Farming Skill - future shit (maybe)"));
        ItemStack combatSkillIcon = (renameItem(new ItemStack(Items.STONE_SWORD), "§9Combat Skill - future shit (maybe)"));
        ItemStack customItemsIcon = renameItem(createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDEwNWM0MTFhZmRjN2YwYTFkOWUxYjI0Y2VhNTMzMjVmNmE0YTExMjNmMDQ1MzYxMDI3MDFkNDc3NDdiZGI4NCJ9fX0="), "§9Custom Items (working on it)");
        ItemStack kingscarlxrdIcon = renameItem(createCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDNkOWExYWJhNmIwZjQ5Yjc1MjU1MjNhNjYxZTRiMWRjNTEwNjI0MjZjZjZmMjBkMTEyMWI3OGIyNjhmZDgxZiJ9fX0="), "§l§4❣ §9KING §l§7SCARL§l§4X§l§7RD §l§9SINCE THE COME UP §4❣");
        kingscarlxrdIcon.getOrCreateTag().putString("UniqueID", "KING_SCAR");
        noNameGlass.getOrCreateTag().putString("UniqueID", "MENU_GLASS");
        noNameGlass_black.getOrCreateTag().putString("UniqueID", "MENU_GLASS");
        combatSkillIcon.getOrCreateTag().putString("UniqueID", "COMBAT_SKILL_MENU");
        ItemStack.TooltipPart[] hideshit = {ItemStack.TooltipPart.ADDITIONAL, ItemStack.TooltipPart.UNBREAKABLE, ItemStack.TooltipPart.CAN_DESTROY, ItemStack.TooltipPart.CAN_PLACE, ItemStack.TooltipPart.DYE, ItemStack.TooltipPart.ENCHANTMENTS, ItemStack.TooltipPart.UNBREAKABLE, ItemStack.TooltipPart.MODIFIERS};
        for(int i=0; i< hideshit.length;i++){
            miningSkillIcon.hideTooltipPart(hideshit[i]);
            combatSkillIcon.hideTooltipPart(hideshit[i]);
            farmingSkillIcon.hideTooltipPart(hideshit[i]);
            customItemsIcon.hideTooltipPart(hideshit[i]);
            kingscarlxrdIcon.hideTooltipPart(hideshit[i]);
        }

        ItemStackHandler chestContents = new ItemStackHandler(54);
        for (int i = 0; i < 54; i++) {
            if (i % 9 == 0 || i % 9 == 8 || i < 9 || i >= 45) {
                chestContents.setStackInSlot(i, noNameGlass);
            }
        }
        chestContents.setStackInSlot(20, miningSkillIcon);
        chestContents.setStackInSlot(22, combatSkillIcon);
        chestContents.setStackInSlot(24, farmingSkillIcon);
        chestContents.setStackInSlot(30 , customItemsIcon);
        chestContents.setStackInSlot(32 , kingscarlxrdIcon);
        for (int i = 0; i < 54; i++) {
            if(chestContents.getStackInSlot(i).is(Items.AIR)){
                chestContents.setStackInSlot(i, noNameGlass_black);
            }
        }
        CustomChestMenu.openChestGUI(source.getPlayer(), chestContents, "§9FUCK YOU MENU");
        return 0;
    }
}
