package xyz.sillyjune.notebook.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import xyz.sillyjune.notebook.NotebookScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static xyz.sillyjune.notebook.Notebook.*;


@Environment(EnvType.CLIENT)
@Mixin(TitleScreen.class)
public abstract class TitleScreenButton extends Screen {
    protected TitleScreenButton(Text title) {
        super(title);
    }
    @Inject(at = @At("TAIL"), method="initWidgetsNormal")
    private void addCustomButton(int y, int spacingY, CallbackInfo ci) {
        TexturedButtonWidget b = new TexturedButtonWidget(
                (this.width / 2 + 104),
                y + spacingY + CONFIG.button_offset(),
                20,
                20,
                getBookIcon(),
                (button) -> this.client.setScreen(new NotebookScreen())
        );
        b.setMessage(Text.translatable("key.notebook.open"));
        this.addDrawableChild(b);
    }
}

