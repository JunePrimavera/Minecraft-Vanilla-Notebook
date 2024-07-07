package xyz.sillyjune.notebook.mixin;

import net.minecraft.client.gui.screen.ButtonTextures;
import xyz.sillyjune.notebook.NotebookScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static xyz.sillyjune.notebook.Notebook.MAIN_BUTTON_ICON;
import static xyz.sillyjune.notebook.Notebook.getBookIcon;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenButton extends Screen {
    protected GameMenuScreenButton(Text title) {
        super(title);
    }
    @Inject(at = @At("RETURN"), method="initWidgets")
    private void addCustomButton(CallbackInfo ci) {
        ButtonTextures icon = getBookIcon();
        this.addDrawableChild(new TexturedButtonWidget(this.width / 2 + 104, this.height / 4 + 96 + -16, 20, 20, icon, (button) -> this.client.setScreen(new NotebookScreen())));
    }
}