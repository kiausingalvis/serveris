package com.example.examplemod.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import com.example.examplemod.ItemConfigManager;

public class CustomItemUtil {

    public static void giveCustomItem(ServerPlayer player, String ID, Item ITEM, String DisplayName, String Description, String ItemAbility, String rarity,int Damage, int Strength, int AttackSpeed, int CritDmg, int CritChance,int Intelligence, int ItemCooldown, boolean AddSpecialAbility,String SpecialAbilityCombo, String specialAbilityDescription, String rune, String DragonTracer) {
        Item item = ITEM;
        Component displayName = Component.literal(DisplayName).withStyle(getDisplayNameColor(rarity));
        String uniqueId = ID;
        ItemStack customStack = ItemUtils.addCustomNBTData(item, displayName, uniqueId, rune, DragonTracer);
        CompoundTag tag = customStack.getOrCreateTag();
        tag.putInt("HideFlags", 254);
        customStack.setTag(tag);
        List<Component> lore = new ArrayList<>();

        lore.add(Component.literal("§7Damage: +"+Damage));
        lore.add(Component.literal("§7Strength: +"+Strength));
        lore.add(Component.literal("§7Attack Speed: +"+AttackSpeed));
        lore.add(Component.literal("§7Crit Damage: +"+CritDmg+"%"));
        lore.add(Component.literal("§7Crit Chance: +"+CritChance));
        lore.add(Component.literal("§7Intelligence: +"+Intelligence));
        lore.add(Component.literal("§7Item Cooldown: "+(double)ItemCooldown/20+"s"));
        lore.add(Component.literal(""));
        lore.add(Component.literal("§eITEM DESCRIPTION"));
        lore.add(Component.literal("§7"+Description));
        lore.add(Component.literal(""));
        lore.add(Component.literal("§cITEM ABILITY"));
        lore.add(Component.literal("§7"+ItemAbility));
        lore.add(Component.literal(""));
        if(AddSpecialAbility){
            lore.add(Component.literal("§4SPECIAL ABILITY - §c" + SpecialAbilityCombo));
            lore.add(Component.literal("§7"+specialAbilityDescription));
            lore.add(Component.literal(""));
        }
        if(!rune.isEmpty()){
            lore.add(Component.literal("§r§8Rune: §7"+rune));
        }
        lore.add(getRarityComponent(rarity));
        ItemUtils.setItemLore(customStack, lore);
        ItemUtils.giveCustomItemToPlayer(player, customStack);
        giveCustomItem(player, ID, item, DisplayName, Description, ItemAbility,rarity, Damage, Strength,AttackSpeed,CritDmg,CritChance,Intelligence);
    }
    public static void giveCustomArmor(ServerPlayer player, String ID, Item ITEM, String DisplayName, String Description, String rarity,int Health, int Defense, int Speed,  int Strength, int AttackSpeed, int CritDmg, int CritChance,int Intelligence) {
        Item item = ITEM;
        Component displayName = Component.translatable(DisplayName).withStyle(getDisplayNameColor(rarity));
        String uniqueId = ID;
        ItemStack customStack = ItemUtils.addCustomNBTData(item, displayName, uniqueId, "", "");
        List<Component> lore = new ArrayList<>();

        lore.add(Component.literal("§7Health: +"+Health));
        lore.add(Component.literal("§7Defense: +"+Defense));
        lore.add(Component.literal("§7Strength: +"+Strength));
        lore.add(Component.literal("§7Attack Speed: +"+AttackSpeed));
        lore.add(Component.literal("§7Crit Damage: +"+CritDmg+"%"));
        lore.add(Component.literal("§7Crit Chance: +"+CritChance));
        lore.add(Component.literal("§7Intelligence: +"+Intelligence));
        lore.add(Component.literal("§7Speed: +"+Speed));
        lore.add(Component.literal(""));
        lore.add(Component.literal("§eITEM DESCRIPTION"));
        lore.add(Component.literal("§7"+Description));
        lore.add(Component.literal(""));
        lore.add(getRarityComponent(rarity));
        ItemUtils.setItemLore(customStack, lore);
        ItemUtils.giveCustomItemToPlayer(player, customStack);
    }
    private static Component getRarityComponent(String rarity) {
        ChatFormatting color = switch (rarity.toUpperCase()) {
            case "COMMON" -> ChatFormatting.DARK_GRAY;
            case "UNCOMMON" -> ChatFormatting.GREEN;
            case "RARE" -> ChatFormatting.BLUE;
            case "EPIC" -> ChatFormatting.DARK_PURPLE;
            case "LEGENDARY" -> ChatFormatting.GOLD;
            case "MYTHIC" -> ChatFormatting.LIGHT_PURPLE;
            case "DIVINE" -> ChatFormatting.AQUA;
            case "PIMPALAS" -> ChatFormatting.DARK_RED;
            default -> ChatFormatting.WHITE;
        };
        return Component.literal(rarity).withStyle(color);
    }
    private static ChatFormatting getDisplayNameColor(String rarity) {
        ChatFormatting color = switch (rarity.toUpperCase()) {
            case "COMMON" -> ChatFormatting.DARK_GRAY;
            case "UNCOMMON" -> ChatFormatting.GREEN;
            case "RARE" -> ChatFormatting.BLUE;
            case "EPIC" -> ChatFormatting.DARK_PURPLE;
            case "LEGENDARY" -> ChatFormatting.GOLD;
            case "MYTHIC" -> ChatFormatting.LIGHT_PURPLE;
            case "DIVINE" -> ChatFormatting.AQUA;
            case "PIMPALAS" -> ChatFormatting.DARK_RED;
            default -> ChatFormatting.WHITE;
        };
        return color;
    }
    public static void giveCustomItem(ServerPlayer player, String ID, Item ITEM, String DisplayName, String Description, String ItemAbility, String rarity, int Damage, int Strength, int AttackSpeed, int CritDmg, int CritChance, int Intelligence) {
        Item item = ITEM;
        Component displayName = Component.literal(DisplayName).withStyle(getDisplayNameColor(rarity));
        String uniqueId = ID;
        ItemStack customStack = ItemUtils.addCustomNBTData(item, displayName, uniqueId, "", "");
        ItemConfigManager.saveItemData(uniqueId, Damage, Strength, AttackSpeed, CritDmg, CritChance, Intelligence);
    }

}
