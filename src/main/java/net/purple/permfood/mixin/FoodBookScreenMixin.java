package net.purple.permfood.mixin;

import com.cazsius.solcarrot.client.gui.FoodBookScreen;
import com.cazsius.solcarrot.client.gui.elements.UIImage;
import org.jline.utils.Log;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.util.List;

import net.purple.permfood.util.PlayerValuesPage;

@Mixin(FoodBookScreen.class)
public abstract class FoodBookScreenMixin {


    @Inject(method = "initPages", at = @At("TAIL"))
    private void onInitPages(CallbackInfo ci) {
        try {

            Field pagesField = FoodBookScreen.class.getDeclaredField("pages");
            pagesField.setAccessible(true);

            Field backgroundField = FoodBookScreen.class.getDeclaredField("background");
            backgroundField.setAccessible(true);

            // Get the List via reflection and allow adding by using a typed List<Object>
            @SuppressWarnings("unchecked")
            List<Object> pages = (List<Object>) pagesField.get(this);
            UIImage background = (UIImage) backgroundField.get(this);



            Log.info("[Food Book Screen] Loading pages");

            Object page = PlayerValuesPage.create(background.frame);
            if (page != null) pages.add(page);

            Log.info(pages.toString());

        } catch (Throwable t) {
            // Fail silently to avoid breaking the book if anything changes
            Log.warn("PermanentFood FoodBook injection failed: " + t);
        }
    }
}
