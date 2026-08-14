package net.swofty.type.skyblockgeneric.item.handlers.pet.abilities.frog;

import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.commons.skyblock.statistics.ItemStatistic;
import net.swofty.commons.skyblock.statistics.ItemStatistics;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.handlers.pet.PetHandler;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbility;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetAbilityRegistration;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetEvent;
import net.swofty.type.skyblockgeneric.item.handlers.pet.abstr.PetEventHandler;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import net.swofty.type.skyblockgeneric.utility.RarityValue;

import java.util.List;

import static net.swofty.commons.StringUtility.commaify;

@PetAbilityRegistration(pet = PetHandler.FROG, minimumRarity = Rarity.EPIC, order = 1,
        implemented = false, notImplementedReason = "logic complete; awaits dispatch(PetEvent.Jump) in the Y-rise branch of ActionPlayerFall")
public final class HopAbility implements PetAbility {
    private static final long BUFF_DURATION_MILLIS = 20_000;
    private long buffUntil;
    private static final RarityValue<Double> FORAGING_FORTUNE_PER_LEVEL =
            new RarityValue<>(0.0, 0.0, 0.0, 0.79, 0.79, 0.79, 0.0);

    @Override
    public String getName() {
        return "Hop";
    }

    @Override
    public List<String> getDescription(SkyBlockItem pet) {
        Rarity rarity = pet.getAttributeHandler().getRarity();
        int level = pet.getAttributeHandler().getPetData().getAsLevel(rarity);
        String value = commaify(1 + FORAGING_FORTUNE_PER_LEVEL.getForRarity(rarity) * level);

        return List.of(
                "<7>Grants <6>" + value + " <stat:foraging_fortune> <7>for",
                "<e>20 <7>seconds every time you jump."
        );
    }

    @Override
    public ItemStatistics getStatistics(SkyBlockPlayer player, SkyBlockItem pet) {
        if (buffUntil <= System.currentTimeMillis()) return ItemStatistics.empty();
        return computeStatistics(player, pet);
    }

    private long buffDuration(SkyBlockItem pet) {
        return BUFF_DURATION_MILLIS;
    }

    private ItemStatistics computeStatistics(SkyBlockPlayer player, SkyBlockItem pet) {
        Rarity rarity = pet.getAttributeHandler().getRarity();
        int level = pet.getAttributeHandler().getPetData().getAsLevel(rarity);

        return ItemStatistics.builder()
                .withBase(ItemStatistic.FORAGING_FORTUNE, 1 + FORAGING_FORTUNE_PER_LEVEL.getForRarity(rarity) * level)
                .build();
    }

    @PetEventHandler
    public void onJump(PetEvent.Jump event) {
        buffUntil = System.currentTimeMillis() + buffDuration(event.pet());
    }
}
