package n11client.cosmetics.hats;

import n11client.cosmetics.CosmeticBase;
import n11client.cosmetics.CosmeticController;
import n11client.cosmetics.CosmeticModelBase;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class CosmeticTopHat extends CosmeticBase {

    private final ModelTopHat TopHat;
    private static final ResourceLocation Texture = new ResourceLocation("n11client/tophat.png");

    public CosmeticTopHat(RenderPlayer player) {
        super(player);
        TopHat = new ModelTopHat(player);
    }

    @Override
    public void render(AbstractClientPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float headYaw, float headPitch, float scale) {

        if (CosmeticController.shouldRenderTopHat(player)) {
            GlStateManager.pushMatrix();
            this.player.bindTexture(Texture);
            if (player.isSneaking()) GL11.glTranslated(0, 0.225D, 0);
            float[] color = CosmeticController.getTopHatColor(player);
            GL11.glColor3f(color[0], color[1], color[2]);
            TopHat.render(player, limbSwing, limbSwingAmount, ageInTicks, headYaw, headPitch, scale);
            GL11.glColor3f(1, 1, 1);
            GlStateManager.popMatrix();
        }

    }

    private class ModelTopHat extends CosmeticModelBase {

        private ModelRenderer rim, top;

        public ModelTopHat(RenderPlayer player) {
            super(player);
            rim = new ModelRenderer(playerModel, 0, 0);
            rim.addBox(-5.5f, -9f, -5.5f, 11, 2, 11);
            top = new ModelRenderer(playerModel, 0, 13);
            top.addBox(-3.5f, -17f, -3.5f, 7, 8, 7);
        }

        @Override
        public void render(Entity player, float limbSwing, float limbSwingAmount, float ageInTicks, float headYaw, float headPitch, float scale) {
            rim.rotateAngleX = playerModel.bipedHead.rotateAngleX;
            rim.rotateAngleY = playerModel.bipedHead.rotateAngleY;
            rim.rotationPointX = playerModel.bipedHead.rotationPointX;
            rim.rotationPointY = playerModel.bipedHead.rotationPointY;
            rim.render(scale);
            top.rotateAngleX = playerModel.bipedHead.rotateAngleX;
            top.rotateAngleY = playerModel.bipedHead.rotateAngleY;
            top.rotationPointX = playerModel.bipedHead.rotationPointX;
            top.rotationPointY = playerModel.bipedHead.rotationPointY;
            top.render(scale);
        }
    }

}
