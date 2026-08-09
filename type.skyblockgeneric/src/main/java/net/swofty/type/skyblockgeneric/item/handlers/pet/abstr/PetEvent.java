package net.swofty.type.skyblockgeneric.item.handlers.pet.abstr;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import net.minestom.server.entity.damage.DamageType;
import net.minestom.server.item.Material;
import net.minestom.server.registry.RegistryKey;
import net.swofty.type.skyblockgeneric.entity.mob.SkyBlockMob;
import net.swofty.type.skyblockgeneric.fishing.catches.CatchPayload;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public sealed interface PetEvent {
    SkyBlockPlayer player();

    enum XpType {
        SKILL, SLAYER, HOTM, HOTF
    }

    record Kill(SkyBlockPlayer player, SkyBlockItem pet, SkyBlockMob mob) implements PetEvent {
    }

    record Jump(SkyBlockPlayer player, SkyBlockItem pet) implements PetEvent {
    }

    record PetInteract(SkyBlockPlayer player, SkyBlockItem pet) implements PetEvent {
    }

    record AbilityCast(SkyBlockPlayer player, SkyBlockItem pet) implements PetEvent {
    }

    @Getter
    @Accessors(fluent = true)
    final class ManaCost implements PetEvent {
        private final SkyBlockPlayer player;
        private final SkyBlockItem pet;
        private final double cost;
        @Setter
        private boolean free;

        public ManaCost(SkyBlockPlayer player, SkyBlockItem pet, double cost) {
            this.player = player;
            this.pet = pet;
            this.cost = cost;
        }
    }

    @Getter
    @Accessors(fluent = true)
    final class AbilityCooldown implements PetEvent {
        private final SkyBlockPlayer player;
        private final SkyBlockItem pet;
        private final SkyBlockItem item;
        @Setter
        private double cooldown;  // millis, modified by handlers

        public AbilityCooldown(SkyBlockPlayer player, SkyBlockItem pet, SkyBlockItem item, double cooldown) {
            this.player = player;
            this.pet = pet;
            this.item = item;
            this.cooldown = cooldown;
        }
    }

    @Getter
    @Accessors(fluent = true)
    non-sealed class Damaged implements PetEvent {
        private final SkyBlockPlayer player;
        private final SkyBlockItem pet;
        @Nullable
        private final RegistryKey<@NotNull DamageType> type;
        @Setter
        private double damage;

        public Damaged(SkyBlockPlayer player, SkyBlockItem pet, @Nullable RegistryKey<@NotNull DamageType> type, double damage) {
            this.player = player;
            this.pet = pet;
            this.type = type;
            this.damage = damage;
        }
    }

    @Getter
    @Accessors(fluent = true)
    final class DamagedByMob extends Damaged {
        private final SkyBlockMob mob;

        public DamagedByMob(SkyBlockPlayer player, SkyBlockItem pet, SkyBlockMob mob, double damage) {
            super(player, pet, DamageType.MOB_ATTACK, damage);
            this.mob = mob;
        }
    }

    @Getter
    @Accessors(fluent = true)
    final class FallDamage extends Damaged {
        public FallDamage(SkyBlockPlayer player, SkyBlockItem pet, double damage) {
            super(player, pet, DamageType.FALL, damage);
        }
    }

    @Getter
    @Accessors(fluent = true)
    final class XpGain implements PetEvent {
        private final SkyBlockPlayer player;
        private final SkyBlockItem pet;
        private final XpType type;
        @Nullable
        private final SkyBlockMob mob;
        @Setter
        private double amount;

        public XpGain(SkyBlockPlayer player, SkyBlockItem pet, XpType type, @Nullable SkyBlockMob mob, double amount) {
            this.player = player;
            this.pet = pet;
            this.type = type;
            this.mob = mob;
            this.amount = amount;
        }
    }


    @Getter
    @Accessors(fluent = true)
    final class FishCaught implements PetEvent {
        private final SkyBlockPlayer player;
        private final SkyBlockItem pet;
        @Setter
        private CatchPayload payload;
        @Nullable
        private final String regionId;

        public FishCaught(SkyBlockPlayer player, SkyBlockItem pet, CatchPayload payload, @Nullable String regionId) {
            this.player = player;
            this.pet = pet;
            this.payload = payload;
            this.regionId = regionId;
        }
    }

    @Getter
    @Accessors(fluent = true)
    final class CropHarvested implements PetEvent {
        private final SkyBlockPlayer player;
        private final SkyBlockItem pet;
        private final Material material;
        @Setter
        private int crops;

        public CropHarvested(SkyBlockPlayer player, SkyBlockItem pet, Material material, int crops) {
            this.player = player;
            this.pet = pet;
            this.material = material;
            this.crops = crops;
        }
    }
}
