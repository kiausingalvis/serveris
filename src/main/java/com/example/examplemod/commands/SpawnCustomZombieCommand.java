package com.example.examplemod.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.example.examplemod.Config;

import java.util.Collections;
import java.util.Objects;

@Mod.EventBusSubscriber
public class SpawnCustomZombieCommand {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("spawnzombie")
                .executes(context -> {
                    // Execute command logic here
                    return spawnCustomZombie(context.getSource());
                })
        );
    }

    private static int spawnCustomZombie(CommandSourceStack source) throws CommandSyntaxException {
        ServerLevel world = source.getLevel();
        if (world != null) {
            // Calculate spawn position, for example, 5 blocks in front of the player
            Vec3 spawnPos = source.getPosition().add(source.getEntityOrException().getLookAngle().scale(5.0));

            // Spawn a custom zombie
            Zombie zombie = new Zombie(EntityType.ZOMBIE, world);
            zombie.moveTo(spawnPos.x(), spawnPos.y(), spawnPos.z(), world.random.nextFloat() * 360.0F, 0.0F);

            // Set custom tag
            zombie.getPersistentData().putString("CustomTag", "Level10Zombie");

            // Set custom attributes (e.g., health)
            zombie.getAttribute(net.minecraft.world.entity.ai.attributes.Attributes.MAX_HEALTH).setBaseValue(1000.0D);
            zombie.setHealth(1000.0F);

            // Equip custom head (player skull)
            ItemStack customHead = createCustomSkull("BigGirthy");
            if (customHead != null) {
                customHead.setHoverName(Component.nullToEmpty("Klan Leader Helmet"));
                zombie.setItemSlot(EquipmentSlot.HEAD, customHead);
            }

            // Equip colored leather armor
            ItemStack leatherChestplate = createColoredLeatherArmor(Items.LEATHER_CHESTPLATE);
            ItemStack leatherLeggings = createColoredLeatherArmor(Items.LEATHER_LEGGINGS);
            ItemStack leatherBoots = createColoredLeatherArmor(Items.LEATHER_BOOTS);
            leatherChestplate.getOrCreateTag().putInt("Unbreakable", 1);
            leatherLeggings.getOrCreateTag().putInt("Unbreakable", 1);
            leatherBoots.getOrCreateTag().putInt("Unbreakable", 1);

            ItemStack BaseballBat = Items.TORCH.getDefaultInstance();
            BaseballBat.setHoverName(Component.nullToEmpty("Baseball Bat"));
            EnchantmentHelper.setEnchantments(Collections.singletonMap(Enchantments.SHARPNESS, 5), BaseballBat);

            ItemStack Watermelon = Items.MELON_SLICE.getDefaultInstance();
            Watermelon.setHoverName(Component.nullToEmpty("Watermelon"));



            zombie.setItemSlot(EquipmentSlot.CHEST, leatherChestplate);
            zombie.setItemSlot(EquipmentSlot.LEGS, leatherLeggings);
            zombie.setItemSlot(EquipmentSlot.FEET, leatherBoots);
            zombie.setItemSlot(EquipmentSlot.MAINHAND, BaseballBat);
            zombie.setItemSlot(EquipmentSlot.OFFHAND, Watermelon);
            zombie.setDropChance(EquipmentSlot.HEAD, 0.2f);
            zombie.setDropChance(EquipmentSlot.MAINHAND, 0.5f);
            zombie.setDropChance(EquipmentSlot.OFFHAND, 0.8f);
            zombie.setDropChance(EquipmentSlot.LEGS, 0f);
            zombie.setDropChance(EquipmentSlot.CHEST, 0f);
            zombie.setDropChance(EquipmentSlot.FEET, 0f);

            // Add the zombie to the world
            world.addFreshEntity(zombie);

            // Send success message to the player
            source.sendSuccess(Component.literal("Custom zombie spawned!"), true);
            Config.PlayerBank.addBalance(source.getPlayer(), 200);
            Player player = source.getPlayerOrException();
            source.sendSuccess(Component.literal("Money: "+ Config.PlayerBank.getBalance(player)), false);

            return 1; // Return success code
        }

        return 0; // Return failure code
    }

    private static ItemStack createCustomSkull(String playerName) {
        // Create a player head ItemStack
        ItemStack skull = new ItemStack(Items.PLAYER_HEAD);

        // Set the SkullOwner tag with the player's name
        skull.getOrCreateTag().putString("SkullOwner", playerName);

        return skull;
    }

    private static ItemStack createColoredLeatherArmor(Item item) {
        // Create an ItemStack of the specified leather armor piece
        ItemStack armorPiece = new ItemStack(item);

        // Ensure the item is dyeable leather armor
        if (armorPiece.getItem() instanceof DyeableLeatherItem) {
            // Set the color for the armor piece
            CompoundTag displayTag = armorPiece.getOrCreateTagElement("display");
            displayTag.putInt("color", 16777215);
        }

        return armorPiece;
    }
}
