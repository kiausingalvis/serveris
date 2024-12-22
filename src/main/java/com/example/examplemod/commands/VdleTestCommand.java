package com.example.examplemod.commands;

import com.example.examplemod.CleaveEffect;
import com.example.examplemod.SkinManager;
import com.example.examplemod.SukunaCleave;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static com.example.examplemod.events.MobSpawner.spawnMob;
import static com.example.examplemod.utils.CircleEffect.startRadius;
import static com.example.examplemod.utils.ClientMessage.*;
import static com.example.examplemod.utils.CustomItemUtil.giveCustomItem;
import static com.example.examplemod.worlds.WorldTeleporter.startF6;

@Mod.EventBusSubscriber
public class VdleTestCommand {

    private static final SuggestionProvider<CommandSourceStack> FEATURE_SUGGESTIONS = (context, builder) -> {
        return suggestFeatureOptions(builder);
    };

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
            Commands.literal("vdletest")
                .then(Commands.argument("feature", StringArgumentType.string())
                    .suggests(FEATURE_SUGGESTIONS)
                    .executes(VdleTestCommand::executeFeatureTest))
        );
    }

    private static int executeFeatureTest(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        String feature = StringArgumentType.getString(context, "feature");
        CommandSourceStack source = context.getSource();

        switch (feature.toLowerCase()) {
            case "terms":
                ServerPlayer player = source.getPlayer();
                Item item = Items.BOW;
                giveCustomItem(player, "TERMINATOR", item, "SPIRITUAL TERMINATOR ✪✪✪✪✪", "Shoots arrows, idk", "TRIPLE ARROWS", "MYTHIC", 350,250,69,400,69,69,10, false, "","", "Smoke", "DragonTracer");
                giveCustomItem(player, "TERMINATOR", item, "SPIRITUAL TERMINATOR §b✪✪✪✪✪", "Shoots arrows, idk", "TRIPLE ARROWS", "PIMPALAS", 350,250,69,400,69,69,10, false, "","", "Notes", "DragonTracer");
                giveCustomItem(player, "TERMINATOR", item, "SPIRITUAL TERMINATOR §b✪✪✪✪✪", "Shoots arrows, idk", "TRIPLE ARROWS", "PIMPALAS", 350,250,69,400,69,69,10, false, "","", "MultiTest", "DragonTracer");
                break;
            case "radius":
                startRadius(source.getEntity(), Blocks.REDSTONE_BLOCK, 10);
                break;
            case "summondrag":
                spawnMob(source.getLevel(), EntityType.ENDER_DRAGON,  source.getPosition(), "ENDER DRAGON", 500, "EDRAG", 100);
                break;
            case "startf6":
                startF6(Objects.requireNonNull(source.getPlayer()));
                break;
            case "aotv":
                giveCustomItem(source.getPlayer(), "PIDERELLA", Items.DIAMOND_SHOVEL, "PIDERELLA", "teleports you x amount of blocks", "teleport a few blocks on right click", "RARE", 2011, 11, 0, 0, 0, 9,40,true, "SHIFT + RIGHT CLICK", "Teleports you 5693 amount of blocks", "","no");
                break;
            case "weapon":
                sendClientMessage(source.getPlayer(), "§6§l[Woah that is a big weapon sirr]");
                break;
            case "weapon_server":
                sendServerMessage(source.getPlayer().getServer(), "§4§l[Woah that is a big weapon sirr]");
                break;
            case "sukuna":
                giveCustomItem(source.getPlayer(), "CLEAVE_STICK", Items.STICK, "CLEAVE STICK", "CLEAVE GO BRR", "CLEAVE", "EPIC", 0, 0, 0, 0, 0, 0,1,false, "", "", "","no");
                giveCustomItem(source.getPlayer(), "CLEAVE_DOMAIN_STICK", Items.STICK, "CLEAVE STICK", "CLEAVE GO BRR", "CLEAVE", "LEGENDARY", 0, 0, 0, 0, 0, 0,1,false, "", "", "","no");
                break;
            case "domain":
                //CleaveEffect.start(source.getLevel(), new Vec3(source.getPlayer().getX(), source.getPlayer().getY(), source.getPlayer().getZ()));
                CleaveEffect.start(source.getLevel(), new Vec3(source.getPlayer().getX(), source.getPlayer().getY(), source.getPlayer().getZ()), source.getPlayer());
                break;
            case "domain_title":
                sendServerTitle(source.getPlayer().getServer(), "§cDOMAIN EXPANSION", "§4DIGGER SHRINE", 10,70,20);
                break;
            case "tnt":
                giveCustomItem(source.getPlayer(), "TNTSTICK", Items.STICK, "TNT", "CLEAVE GO BRR", "CLEAVE", "EPIC", 0, 0, 0, 0, 0, 0,1,false, "", "", "","no");
                break;
            case "firework_wand":
                giveCustomItem(source.getPlayer(), "FIREWORKWAND", Items.END_ROD, "FIREWORK WAND", "Sends fireworks!", "COLORFUL FIREWORKS", "MYTHIC", 0, 0, 0, 0, 0, 0,5,false, "", "", "","no");
            default:
                source.sendFailure(Component.literal("Unknown feature: " + feature));
                break;
        }
        return Command.SINGLE_SUCCESS;
    }


    private static CompletableFuture<Suggestions>suggestFeatureOptions(SuggestionsBuilder builder) {
        builder.suggest("terms");
        builder.suggest("radius");
        builder.suggest("summondrag");
        builder.suggest("startf6");
        builder.suggest("aotv");
        builder.suggest("weapon");
        return builder.buildFuture();
    }
}
