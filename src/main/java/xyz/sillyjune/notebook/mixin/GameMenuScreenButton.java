package xyz.sillyjune.notebook.mixin;

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

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenButton extends Screen {
    int l = this.height / 4 + 48;
    protected GameMenuScreenButton(Text title) {
        super(title);
    }
    @Inject(at = @At("RETURN"), method="initWidgets")
    private void addCustomButton(CallbackInfo ci) {
        int l = this.height / 4 + 48;
        this.addDrawableChild(new TexturedButtonWidget(this.width / 2 + 104, this.height / 4 + 96 + -16, 20, 20, MAIN_BUTTON_ICON, (button) -> {
            //Code is run when the button is clicked
            assert this.client != null;
            this.client.setScreen(new NotebookScreen());
        }, Text.translatable("jwg.button.bookmenu")));
    }
}