package xyz.sillyjune.notebook.mixin;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.sillyjune.notebook.NotebookScreen;

import static xyz.sillyjune.notebook.Notebook.getBookIcon;

@Mixin(TitleScreen.class)
public abstract class TitleScreenButton extends Screen {
    protected TitleScreenButton(Text title) {
        super(title);
    }
    @Inject(at = @At("RETURN"), method="init")
    private void addCustomButton(CallbackInfo ci) {
        TexturedButtonWidget b = new TexturedButtonWidget(
                this.width / 2 + 104,
                this.height / 4 + 96,
                20,
                20,
                getBookIcon(),
                (button) -> this.client.setScreen(new NotebookScreen())
        );
        b.setMessage(Text.translatable("key.notebook.open"));
        this.addDrawableChild(b);
    }
}