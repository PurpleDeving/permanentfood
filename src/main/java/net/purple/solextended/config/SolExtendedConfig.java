package net.purple.solextended.config;

import me.fzzyhmstrs.fzzy_config.config.Config;
import me.fzzyhmstrs.fzzy_config.validation.collection.ValidatedSet;
import me.fzzyhmstrs.fzzy_config.validation.minecraft.ValidatedIdentifier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.purple.solextended.Constants;

//IMPL Names, Prefix, Description etc
public class SolExtendedConfig extends Config {


    public SolExtendedConfig() {
        super(Constants.rLSolExtendedConfig);
    }

    public ValidatedSet<ResourceLocation> whiteList = new ValidatedSet<>(
            java.util.Set.of(),
            ValidatedIdentifier.ofDynamicKey(
                    ResourceLocation.fromNamespaceAndPath("minecraft", "air"),
                    Registries.ITEM,
                    "solextended_item_whitelist",
                    (id, entry) -> entry.value().getFoodProperties(entry.value().getDefaultInstance(), null) != null
            )
    );

    public ValidatedSet<ResourceLocation> blackList = new ValidatedSet<>(
            java.util.Set.of(),
            ValidatedIdentifier.ofDynamicKey(
                    ResourceLocation.fromNamespaceAndPath("minecraft", "air"),
                    Registries.ITEM,
                    "solextended_item_whitelist",
                    (id, entry) -> entry.value().getFoodProperties(entry.value().getDefaultInstance(), null) != null
            )
    );


}
