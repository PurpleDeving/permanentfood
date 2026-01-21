package net.purple.permfood.client;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.client.gui.elements.ImageData;
import com.cazsius.solcarrot.client.gui.elements.UIElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.awt.*;

import static net.purple.permfood.Constants.*;
import static net.purple.permfood.client.LocalizationHelper.localized;

@OnlyIn(Dist.CLIENT)
public final class PlayerAttributesUIPage extends PageClone {
    private static final ResourceLocation texture = SOLCarrot.resourceLocation("textures/gui/food_book.png");

    static final ImageData armorImage = new ImageData(texture,
            new Rectangle(16, 240, 16, 16),
            8, 8);

    static final ImageData armorThoughnessImage = new ImageData(texture,
            new Rectangle(16, 240, 16, 16),
            8, 8);

    static final ImageData luckImage = new ImageData(texture,
            new Rectangle(16, 240, 16, 16),
            8, 8);

    static final ImageData attackDamageImage = new ImageData(texture,
            new Rectangle(16, 240, 16, 16),
            8, 8);


    public PlayerAttributesUIPage(Player player, Rectangle frame) {
        super(frame, localized("gui", "food_book.player_attributes"));

        /******************************************
         Armor Attribute Modifier
         ******************************************/


        AttributeModifier modifierArmor = player.getAttributes().getInstance(Attributes.ARMOR).getModifier(rLArmorBuff);
        double amountArmor = modifierArmor != null ? modifierArmor.amount() : 0.0;
        String amountStringArmor = String.format("%.2f", amountArmor);

        UIElement ValueStat_Armor = statTextWithIcon(
                armorImage, amountStringArmor,
                localized("gui", "food_book.attributes.food_armor")
        );
        ValueStat_Armor.tooltip = localized("gui", "food_book.attributes.tooltip.food_armor");
        mainStack.addChild(ValueStat_Armor);

        /******************************************
         Armor Thoughness Attribute Modifier
         ******************************************/

        AttributeModifier modifierArmorThoughness = player.getAttributes().getInstance(Attributes.ARMOR_TOUGHNESS).getModifier(rLToughnessBuff);
        double amountArmorThoughness = modifierArmor != null ? modifierArmorThoughness.amount() : 0.0;
        String amountStringArmorThoughness = String.format("%.2f", amountArmorThoughness);

        UIElement ValueStat_ArmorThoughness = statTextWithIcon(
                armorThoughnessImage, amountStringArmorThoughness,
                localized("gui", "food_book.attributes.food_armor_toughness")
        );
        ValueStat_ArmorThoughness.tooltip = localized("gui", "food_book.attributes.tooltip.food_armor_toughness");
        mainStack.addChild(ValueStat_ArmorThoughness);


        /******************************************
         Luck Attribute Modifier
         ******************************************/

        AttributeModifier modLuck = player.getAttributes().getInstance(Attributes.LUCK).getModifier(rLLuckBuff);
        double luckAmount = modLuck != null ? modLuck.amount() : 0.0;
        String luckAmountStr = String.format("%.2f", luckAmount);

        UIElement luckValueStat = statTextWithIcon(
                luckImage, luckAmountStr,
                localized("gui", "food_book.attributes.food_luck")
        );
        luckValueStat.tooltip = localized("gui", "food_book.attributes.tooltip.food_luck");
        mainStack.addChild(luckValueStat);

        /******************************************
         Damage Attribute Modifier
         ******************************************/

        AttributeModifier modAttackDamage = player.getAttributes().getInstance(Attributes.ATTACK_DAMAGE).getModifier(rLAttackDamageBuff);
        double attackDamageAmount = modAttackDamage != null ? modAttackDamage.amount() : 0.0;
        String attackDamageAmountStr = String.format("%.2f", attackDamageAmount);

        UIElement attackValueStat = statTextWithIcon(
                attackDamageImage, attackDamageAmountStr,
                localized("gui", "food_book.attributes.food_attack_damage")
        );
        attackValueStat.tooltip = localized("gui", "food_book.attributes.tooltip.food_attack_damage");

        mainStack.addChild(attackValueStat);

        mainStack.addChild(makeSeparatorLine());

        updateMainStack();
    }
}
