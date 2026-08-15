package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities.hermit_crab;

import net.minestom.server.instance.EntityTracker;
import net.minestom.server.instance.Instance;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.PetHandler;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbilityRegistration;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.utility.RarityValue;

import java.util.List;

import static net.swofty.commons.StringUtility.decimalify;

@PetAbilityRegistration(pet = PetHandler.HERMIT_CRAB, minimumRarity = Rarity.LEGENDARY, order = 2)
public final class CrabRaveAbility implements PetAbility {
    private static final int MAX_PLAYERS = 5;
    private static final double RADIUS_BLOCKS = 30;
    private static final RarityValue<Double> TREASURE_CHANCE_PER_LEVEL =
            new RarityValue<>(0.0, 0.0, 0.0, 0.0, 0.002, 0.002, 0.0);

    @Override
    public String getName() {
        return "Crab Rave";
    }

    @Override
    public List<String> getDescription(SkyBlockItem pet) {
        Rarity rarity = pet.getAttributeHandler().getRarity();
        int level = pet.getAttributeHandler().getPetData().getAsLevel(rarity);
        String treasureChance = decimalify(TREASURE_CHANCE_PER_LEVEL.getForRarity(rarity) * level, 3);

        return List.of(
                "<7>Grants <6>+" + treasureChance + " <stat:treasure_chance> <7>for",
                "<7>each player with a <a>Hermit Crab Pet<7> within",
                "<a>30 <7>blocks, up to <a>" + MAX_PLAYERS + " <7>players."
        );
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockPlayer player, SkyBlockItem pet) {
        int crabCount = countNearbyCrabPets(player);
        if (crabCount == 0) return ItemStatistics.empty();

        Rarity rarity = pet.getAttributeHandler().getRarity();
        int level = pet.getAttributeHandler().getPetData().getAsLevel(rarity);
        double treasureChance = TREASURE_CHANCE_PER_LEVEL.getForRarity(rarity) * level * crabCount;

        return ItemStatistics.builder()
                .withBase(ItemStatistic.TREASURE_CHANCE, treasureChance)
                .build();
    }

    private static int countNearbyCrabPets(SkyBlockPlayer player) {
        Instance instance = player.getInstance();
        if (instance == null) return 0;

        int[] count = {0};
        instance.getEntityTracker().nearbyEntities(player.getPosition(), RADIUS_BLOCKS,
                EntityTracker.Target.PLAYERS, other -> {
                    if (count[0] >= MAX_PLAYERS) return;
                    if (other == player) return;
                    if (!(other instanceof SkyBlockPlayer skyPlayer)) return;

                    SkyBlockItem enabledPet = skyPlayer.getPetData().getEnabledPet();
                    if (enabledPet == null) return;
                    if (enabledPet.getAttributeHandler().getPotentialType() == ItemType.HERMIT_CRAB_PET) count[0]++;
                });
        return count[0];
    }
}
