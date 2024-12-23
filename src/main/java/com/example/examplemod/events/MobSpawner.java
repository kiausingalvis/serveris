package com.example.examplemod.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import static com.example.examplemod.utils.CircleEffect.startRadius;

public class MobSpawner {

    public static Mob spawnMob(ServerLevel world, EntityType EntityType, Vec3 position, String displayName, double hp, String customTag, int MobLevel) {
        Mob mob = ((EntityType<? extends Mob>) EntityType).create(world);
        if (mob != null) {
            mob.setPos(position.x, position.y, position.z);
            if (displayName != null && !displayName.isEmpty()) {
                mob.setCustomName(Component.literal("§b§l"+displayName+"§9§l(Lvl"+MobLevel+") §4§l"+(int)hp*1000+"§c♥"));
                mob.setCustomNameVisible(true);
            }
            AttributeInstance maxHealthAttr = mob.getAttribute(Attributes.MAX_HEALTH);
            if (maxHealthAttr != null) {
                maxHealthAttr.setBaseValue(hp);
            }

            mob.setHealth((float) hp);
            if (customTag != null) {
                CompoundTag tag = new CompoundTag();
                tag.putString("UniqueID", customTag);
                mob.getPersistentData().merge(tag);
            }
            //mob.setNoAi(true);
            world.addFreshEntity(mob);

        }
        return mob;
    }
}
