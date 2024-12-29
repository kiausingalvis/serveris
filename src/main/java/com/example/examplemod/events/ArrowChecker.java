package com.example.examplemod.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static com.example.examplemod.utils.ArrowParticleUtils.arrowsWithParticles;

@Mod.EventBusSubscriber
public class ArrowChecker {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArrowChecker.class);
    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            for (ServerLevel level : event.getServer().getAllLevels()) {
                List<AbstractArrow> arrowsToCheck = new ArrayList<>();
                List<FireworkRocketEntity> fireworktocheck = new ArrayList<>();
                try {
                    for (Entity entity : level.getAllEntities()) {
                        if (entity instanceof AbstractArrow arrow) {
                            arrowsToCheck.add(arrow);
                        }
                    }
                    for (AbstractArrow arrow : arrowsToCheck) {
                        try {
                            CompoundTag nbt = arrow.serializeNBT();
                            int life = nbt.getInt("life");
                            if (life > 1) {
                                arrowsWithParticles.remove(arrow);
                                arrow.discard();
                            }
                        } catch (Exception e) {
                            LOGGER.error("ERROR - Exception while processing arrow NBT: ", e);
                        }
                    }

                } catch (ArrayIndexOutOfBoundsException e) {
                    LOGGER.error("ERROR - ARRAY OUT OF BOUNDS while iterating over entities: ", e);
                } catch (Exception e) {
                    LOGGER.error("ERROR - Unexpected exception: ", e);
                }


                try {
                    for (Entity entity : level.getAllEntities()) {
                        if (entity instanceof FireworkRocketEntity fireworkRocket) {
                            fireworktocheck.add(fireworkRocket);
                        }
                    }
                    for (FireworkRocketEntity fireworkRocket : fireworktocheck) {
                        try {
                            CompoundTag nbt = fireworkRocket.serializeNBT();
                            int life = nbt.getInt("LifeTime");
                            if (life > 0) {
                                //System.out.println(nbt.getAsString());
                            }
                        } catch (Exception e) {
                            LOGGER.error("ERROR - Exception while processing arrow NBT: ", e);
                        }
                    }

                } catch (ArrayIndexOutOfBoundsException e) {
                    LOGGER.error("ERROR - ARRAY OUT OF BOUNDS while iterating over entities: ", e);
                } catch (Exception e) {
                    LOGGER.error("ERROR - Unexpected exception: ", e);
                }
            }
        }
    }
}
