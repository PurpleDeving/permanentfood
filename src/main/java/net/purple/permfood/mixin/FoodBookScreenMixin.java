package net.purple.permfood.mixin;

import com.cazsius.solcarrot.client.gui.FoodBookScreen;
import com.cazsius.solcarrot.client.gui.elements.UIImage;
import net.purple.permfood.client.PlayerAttributesUIPage;
import org.jline.utils.Log;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.world.entity.player.Player;

import static net.purple.permfood.PermanentFood.LOG;


@Mixin(FoodBookScreen.class)
public abstract class FoodBookScreenMixin {


    @Inject(method = "initPages", at = @At("TAIL"))
    private void onInitPages(CallbackInfo ci) {
        try {

            Field pagesField = FoodBookScreen.class.getDeclaredField("pages");
            pagesField.setAccessible(true);

            Field backgroundField = FoodBookScreen.class.getDeclaredField("background");
            backgroundField.setAccessible(true);

            // Player is private in FoodBookScreen; try to reflect it
            Field playerField = null;
            Player player = null;
            try {
                playerField = FoodBookScreen.class.getDeclaredField("player");
                playerField.setAccessible(true);
                player = (Player) playerField.get(this);
            } catch (NoSuchFieldException ignored) {
                // Things are broken. Skip all injection

                LOG.warn("This should not be reached Part 1");
                return;
            }

            // Get the List via reflection and allow adding by using a typed List<Object>
            @SuppressWarnings("unchecked")
            List<Object> pages = (List<Object>) pagesField.get(this);
            UIImage background = (UIImage) backgroundField.get(this);

            Log.info("[Food Book Screen] Loading pages");

            PlayerAttributesUIPage attributesUIPage = new PlayerAttributesUIPage(player, background.frame);

            pages.add(attributesUIPage);

        } catch (Throwable t) {
            // Fail silently to avoid breaking the book if anything changes
            Log.warn("PermanentFood FoodBook injection failed: " + t);
        }
    }
}
