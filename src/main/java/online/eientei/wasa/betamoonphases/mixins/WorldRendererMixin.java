package online.eientei.wasa.betamoonphases.mixins;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.level.Level;
import online.eientei.wasa.betamoonphases.interfaces.MoonStuffs;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin implements MoonStuffs {

    @Shadow private Level level;

    private static Minecraft getInstance() {
        return (Minecraft) FabricLoader.getInstance().getGameInstance();
    }
    private static int getTextureID() {
        return getInstance().textureManager.getTextureId("/assets/betamoonphases/moon_phases.png");
    }
    @Override @Unique public int moonTime(long j1) {
        return (int)(j1 / 24000L) % 8;
    }
    @Override @Unique public int getMoonPhase() {
        return this.moonTime(level.getProperties().getTime());
    }

    @Redirect(method = "renderSky", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glBindTexture(II)V", ordinal = 1))
    private void changeMoonTexture(int target, int texture) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, getTextureID());
    }

    @Redirect(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V", ordinal = 4))
    private void removeOldVertex1(Tessellator instance, double e, double f, double g, double h, double v) {
    }
    @Redirect(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V", ordinal = 5))
    private void removeOldVertex2(Tessellator instance, double e, double f, double g, double h, double v) {
    }
    @Redirect(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V", ordinal = 6))
    private void removeOldVertex3(Tessellator instance, double e, double f, double g, double h, double v) {
    }
    @Redirect(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;vertex(DDDDD)V", ordinal = 7))
    private void removeOldVertex4(Tessellator instance, double e, double f, double g, double h, double v) {
    }

    @Inject(method = "renderSky", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Tessellator;draw()V", ordinal = 2))
    private void addNewVertex(float f, CallbackInfo ci) {
        Tessellator render = Tessellator.INSTANCE;
        float size = 20.0F;
        int time = this.getMoonPhase();
        int i2 = time % 4;
        int i3 = (time / 4) % 2;
        float f1 = i2 / 4.0F;
        float f2 = i3 / 2.0F;
        float f3 = (i2 + 1) / 4.0F;
        float f4 = (i3 + 1) / 2.0F;

        render.vertex(-size, -100.0D, size, f3, f4);
        render.vertex(size, -100.0D, size, f1, f4);
        render.vertex(size, -100.0D, -size, f1, f2);
        render.vertex(-size, -100.0D, -size, f3, f2);
    }
}