package com.example.examplemod;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber
public class CleaveEffect {
    private static final double CLEAVE_RADIUS = 20.0;
    private static final float DAMAGE_AMOUNT = 0.1f;
    private static final int DURATION_TICKS = 200;

    private static final List<CleaveInstance> activeCleaveEffects = new ArrayList<>();

    public static void start(ServerLevel world, Vec3 location, ServerPlayer player) {
        System.out.println("[DEBUG] Cleave effect started at: " + location);
        activeCleaveEffects.add(new CleaveInstance(world, location, DURATION_TICKS, player));
    }

    @SubscribeEvent
    public static void onServerTick(TickEvent.ServerTickEvent event) {
        // Process cleave effects each tick
        Iterator<CleaveInstance> iterator = activeCleaveEffects.iterator();

        while (iterator.hasNext()) {
            CleaveInstance cleave = iterator.next();

            // Damage entities and show particles
            cleave.damageEntities();
            cleave.spawnOutlineParticles();

            // Reduce remaining ticks, remove if expired
            if (cleave.decrementAndCheckExpired()) {
                System.out.println("[DEBUG] Cleave effect ended.");
                iterator.remove();
            }
        }
    }

    // Represents a single cleave effect instance
    private static class CleaveInstance {
        private final ServerLevel world;
        private final Vec3 location;
        private int remainingTicks;
        private final ServerPlayer player;

        public CleaveInstance(ServerLevel world, Vec3 location, int duration, ServerPlayer player) {
            this.world = world;
            this.location = location;
            this.remainingTicks = duration;
            this.player = player;
        }


        public void damageEntities() {
            AABB damageArea = new AABB(
                    location.x - CLEAVE_RADIUS, location.y - CLEAVE_RADIUS, location.z - CLEAVE_RADIUS,
                    location.x + CLEAVE_RADIUS, location.y + CLEAVE_RADIUS, location.z + CLEAVE_RADIUS
            );

            List<LivingEntity> entities = world.getEntitiesOfClass(LivingEntity.class, damageArea);

            for (LivingEntity entity : entities) {
                if (entity.isAlive() && !entity.getName().getString().equals(player.getName().getString())) {

                    entity.hurt(DamageSource.GENERIC, DAMAGE_AMOUNT);
                    entity.setDeltaMovement(Vec3.ZERO);
                    System.out.println("[DEBUG] Damaged entity: " + entity.getName());
                }
            }
        }

        public void spawnOutlineParticles() {
            for (int theta = 0; theta < 360; theta+=20) {
                for (int phi = -90; phi <= 90; phi+=15){
                    double radTheta = Math.toRadians(theta);
                    double radPhi = Math.toRadians(phi);

                    double x = location.x + CLEAVE_RADIUS * Math.cos(radPhi) * Math.cos(radTheta);
                    double y = location.y + CLEAVE_RADIUS * Math.sin(radPhi);
                    double z = location.z + CLEAVE_RADIUS * Math.cos(radPhi) * Math.sin(radTheta);



                    world.sendParticles(
                            ParticleTypes.GLOW,
                            x, y, z,
                            1, // Count
                            5, 5, 5, // Delta
                            0.01 // Speed
                    );
                    world.sendParticles(
                        ParticleTypes.SCRAPE,
                            x, y, z,
                            1, // Count
                            0, 0, 0, // Delta
                            0.01 // Speed
                    );
                }
            }
        }

        public boolean decrementAndCheckExpired() {
            remainingTicks--;
            return remainingTicks <= 0;
        }
    }
}
