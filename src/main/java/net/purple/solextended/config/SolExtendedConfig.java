package net.purple.solextended.config;

import me.fzzyhmstrs.fzzy_config.annotations.Action;
import me.fzzyhmstrs.fzzy_config.annotations.RequiresAction;
import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedSet;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import me.fzzyhmstrs.fzzy_config.validation.misc.ValidatedEnum;
import me.fzzyhmstrs.fzzy_config.validation.number.ValidatedInt;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.purple.solextended.Constants;

//IMPL Names, Prefix, Description etc for everything here
public class SolExtendedConfig extends Config {

    public enum ListMode {
        NONE,
        WHITELIST,
        BLACKLIST
    }

    public SolExtendedConfig() {
        super(Constants.rLSolExtendedConfig);
    }

    @Desc("How the item lists below are applied. NONE disables list filtering.")
    public ValidatedEnum<ListMode> listMode = new ValidatedEnum<>(ListMode.NONE, ValidatedEnum.WidgetType.CYCLING);

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

    @Desc("The minimum hunger value foods need to provide in order to count for milestones, in half drumsticks.")
    public ValidatedInt minimumFoodValue = new ValidatedInt(1, 0, 1000);


}
