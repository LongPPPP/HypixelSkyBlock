package net.swofty.type.skyblockgeneric.item.components;

import lombok.Getter;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.swofty.commons.skyblock.item.ItemType;
import net.swofty.commons.skyblock.item.Rarity;
import net.swofty.commons.text.Text;
import net.swofty.type.skyblockgeneric.entity.PetEntityImpl;
import net.swofty.type.skyblockgeneric.item.SkyBlockItem;
import net.swofty.type.skyblockgeneric.item.SkyBlockItemComponent;
import net.swofty.type.skyblockgeneric.item.handlers.lore.LoreConfig;
import net.swofty.type.skyblockgeneric.user.SkyBlockPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Getter
public class PetSkinComponent extends SkyBlockItemComponent {
    private final String skinName;
    private final ItemType applicablePet;
    private final int gemPrice;
    private final String skullTexture;

    public PetSkinComponent(String skinName, ItemType applicablePet, int gemPrice, String skullTexture) {
        this.skinName = skinName;
        this.applicablePet = applicablePet;
        this.gemPrice = gemPrice;
        this.skullTexture = skullTexture;

        addInheritedComponent(new SkullHeadComponent((item) -> skullTexture));
        addInheritedComponent(new TrackedUniqueComponent());
        addInheritedComponent(new ExtraRarityComponent("COSMETIC"));
        addInheritedComponent(new LoreUpdateComponent(new LoreConfig((item, player) -> getLore(player, item), (item, player) -> {
            Rarity rarity = item.getAttributeHandler().getRarity();
            return Text.of("<color:{}>{}", rarity.getColor(), skinName).serialize();
        }), false));
    }

    public boolean apply(SkyBlockPlayer player, SkyBlockItem skinItem, PetEntityImpl target) {
        if (target.getPlayer() != player) {
            return false;
        }

        SkyBlockItem pet = target.getPet();
        if (pet.getAttributeHandler().getPotentialType() != applicablePet) {
            player.sendMessage("<c>This skin cannot be applied to this pet.");
            return false;
        }

        pet.getAttributeHandler().getPetData().setSkinId(
                skinItem.getAttributeHandler().getPotentialType()
        );

        player.setItemInHand(null);
        player.sendMessage("<a>Your {} <a>has been applied!", skinItem.getDisplayNameText());
        target.refreshTexture();
        player.playSound(Sound.sound()
                .type(Key.key("minecraft", "entity.experience_orb.pickup"))
                .volume(1f)
                .pitch(1f)
                .build());
        return true;
    }

    private List<String> getLore(@Nullable SkyBlockPlayer player, SkyBlockItem item) {
        List<String> lore = new ArrayList<>();
        String petDisplayName = applicablePet.getDisplayName();
        String petName = petDisplayName.endsWith(" Pet")
                ? petDisplayName.substring(0, petDisplayName.length() - " Pet".length())
                : petDisplayName;

        lore.add("<8>Consumed on use");

        lore.add(" ");
        lore.add("<7>Pet skins changes the look and");
        lore.add("<7>particle trail of your pet but only");
        lore.add("<7>one skin can be active at a time");

        lore.add(" ");
        lore.add("<7>This skin can only be applied to");
        lore.add(Text.of("<a>{} <7>pets.", petName).serialize());

        lore.add(" ");
        lore.add("<e>Right-click on your summoned pet to");
        lore.add("<e>apply this skin!");

        return lore;
    }
}
