package com.jwg.coord_book.mixin;

import com.jwg.coord_book.screens.menuScreen;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.jwg.coord_book.CoordBook.BOOK_ICON;


@Mixin(GameMenuScreen.class)
public abstract class GameScreenMixin extends Screen {
	int l = this.height / 4 + 48;
	protected GameScreenMixin(Text title) {
		super(title);
	}
	@Inject(at = @At("RETURN"), method="initWidgets")
	private void addCustomButton(CallbackInfo ci) {
		this.addDrawableChild(new TexturedButtonWidget(this.width / 2 + 104, this.height / 4 + 96 + -16, 20, 20, 0, 0, 20, BOOK_ICON, 32, 64, (button) -> {
			//Code is run when the button is clicked
			assert this.client != null;
			this.client.setScreen(new menuScreen(null));
		}, new TranslatableText("jwg.button.bookmenu")));
	}
}
