package com.example.examplemod.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;

public class MobSpawner {

    public static Mob spawnMob(ServerLevel world, EntityType EntityType, Vec3 position, String displayName, double hp, String customTag) {
        Mob mob = ((EntityType<? extends Mob>) EntityType).create(world);
        if (mob != null) {
            mob.setPos(position.x, position.y, position.z);
            if (displayName != null && !displayName.isEmpty()) {
                mob.setCustomName(Component.literal(displayName+hp));
                mob.setCustomNameVisible(true);
            }
                mob.setHealth(200);
            if (customTag != null) {
                CompoundTag tag = new CompoundTag();
                tag.putString("UniqueID", customTag);
                mob.getPersistentData().merge(tag);
            }
            world.addFreshEntity(mob);
        }
        return mob;
    }
}
