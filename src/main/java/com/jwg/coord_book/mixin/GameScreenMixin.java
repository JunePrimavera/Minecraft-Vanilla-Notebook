package com.jwg.coord_book.mixin;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.advancement.AdvancementsScreen;
import net.minecraft.client.gui.screen.option.AccessibilityOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;



@Mixin(GameMenuScreen.class)
public abstract class GameScreenMixin extends Screen {
	int l = this.height / 4 + 48;
	private static final Identifier ACCESSIBILITY_ICON_TEXTURE = new Identifier("textures/gui/accessibility.png");
	protected GameScreenMixin(Text title) {
		super(title);
	}
	@Inject(at = @At("RETURN"), method="initWidgets")
	private void addCustomButton(CallbackInfo ci) {
		this.addDrawableChild(new TexturedButtonWidget(this.width / 2 + 104, l + 72 + 12, 20, 20, 0, 0, 20, ACCESSIBILITY_ICON_TEXTURE, 32, 64, (button) -> {
			//Code is run when the button is clicked
			//this.client.setScreen(new AccessibilityOptionsScreen(this, this.client.options));
		}, Text.translatable("jwg.button.bookmenu")));
	}
}
