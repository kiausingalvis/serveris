package com.example.examplemod.utils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import static com.example.examplemod.ExampleMod.MODID;
import static com.example.examplemod.utils.ClientMessage.sendServerMessage;

@Mod.EventBusSubscriber(modid = MODID)
public class PlayerJoinWelcomeMessage {
    @SubscribeEvent
    public static void WelcomePlayer(PlayerEvent.PlayerLoggedInEvent event) {
        event.setCanceled(true);
        String playerName = null;
        if (event.getEntity() instanceof ServerPlayer player) {
            playerName = player.getName().getString();
        }
        sendServerMessage(event.getEntity().getServer(), "§4§lWelcome: §c§l" + playerName +"§6§l 卐");
    }
}
