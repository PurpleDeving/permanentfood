package net.purple.solextended.config;

import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.annotations.Translation;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.util.Translatable;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedSet;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedNumber;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.purple.solextended.Constants;

import static net.purple.solextended.SolExtended.MODID;

//IMPL Names, Prefix, Description etc for everything here
@Translatable.Name("Sol Extended")
@Translation(prefix = MODID + ".solextended_config")
public class SolExtendedConfig extends Config {


    public enum ListMode {
        NONE,
        WHITELIST,
        BLACKLIST
    }

    public SolExtendedConfig() {
        super(Constants.rLSolExtendedConfig);
    }

    @Name("List Mode")
    @Desc("How the item lists below are applied. NONE disables list filtering. Whitelist does overwrite Blacklist entries.")
    public ValidatedEnum<ListMode> listMode = new ValidatedEnum<>(ListMode.NONE, ValidatedEnum.WidgetType.CYCLING);

    @Name("Whitelist")
    @Desc("Items allowed when List Mode is WHITELIST.")
    @RequiresAction(action = Action.RESTART)
    public ValidatedSet<ResourceLocation> whiteList = new ValidatedSet<>(
            java.util.Set.of(),
            ValidatedIdentifier.ofDynamicKey(
                    ResourceLocation.fromNamespaceAndPath("minecraft", "air"),
                    Registries.ITEM,
                    "solextended_item_whitelist",
                    (id, entry) -> entry.value().getFoodProperties(entry.value().getDefaultInstance(), null) != null
            )
    );

    @Name("Blacklist")
    @Desc("Items blocked when List Mode is BLACKLIST.")
    @RequiresAction(action = Action.RESTART)
    public ValidatedSet<ResourceLocation> blackList = new ValidatedSet<>(
            java.util.Set.of(),
            ValidatedIdentifier.ofDynamicKey(
                    ResourceLocation.fromNamespaceAndPath("minecraft", "air"),
                    Registries.ITEM,
                    "solextended_item_whitelist",
                    (id, entry) -> entry.value().getFoodProperties(entry.value().getDefaultInstance(), null) != null
            )
    );

    @Name("Minimum Food Value")
    @Desc("The minimum hunger value foods need to provide in order to count for milestones, in half drumsticks.")
    public ValidatedInt minimumFoodValue = new ValidatedInt(1, 1000, 0, ValidatedNumber.WidgetType.TEXTBOX);


    @Name("Limit Progress To Survival")
    @Desc("When true, progress is only tracked while the player is in Survival mode.")
    public Boolean limitProgressToSurvival = true;

    @Name("Reset Food List On Death")
    @Desc("When true, the tracked food list resets when the player dies.")
    public Boolean resetFoodListOnDeath = false;


}
