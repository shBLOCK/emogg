package pextystudios.emogg.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public final class RenderUtil {
    public static class Rect2i extends net.minecraft.client.renderer.Rect2i {
        public Rect2i(int x, int y, int size) {
            this(x, y, size, size);
        }

        public Rect2i(int x, int y, int width, int height) {
            super(x, y, width, height);
        }

        public Rect2i move(int x, int y) {
            return new Rect2i(xPos + x, yPos + y, width, height);
        }

        public Rect2i expand(int horizontal, int vertical) {
            return new Rect2i(xPos, yPos, width + horizontal, height + vertical);
        }
    }

    public final static float DEFAULT_Z_LEVEL = 0;

    public static void renderTexture(PoseStack poseStack, ResourceLocation resourceLocation, Rect2i rect2i) {
        renderTexture(poseStack, resourceLocation, rect2i.getX(), rect2i.getY(), rect2i.getWidth(), rect2i.getHeight());
    }

    public static void renderTexture(PoseStack poseStack, ResourceLocation resourceLocation, int x, int y, int width, int height) {
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.setShaderTexture(0, resourceLocation);
        RenderSystem.enableBlend();
        GuiComponent.blit(poseStack, x, y, 0f, 0f, width, height, width, height);
        RenderSystem.disableBlend();
    }

    public static void drawRect(int x, int y, int width, int height, int color, int strokeThickness, int strokeColor) {
        drawRect(x, y, width, height, color, strokeThickness, strokeColor, DEFAULT_Z_LEVEL);
    }

    public static void drawRect(int x, int y, int width, int height, int color, int strokeThickness, int strokeColor, float zLevel) {
        drawRect(x, y, width, height, color, zLevel);
        drawRectStroke(x, y, width, height, strokeThickness, strokeColor, zLevel);
    }

    public static void drawRect(Rect2i rect2i, int color) {
        drawRect(rect2i.getX(), rect2i.getY(), rect2i.getWidth(), rect2i.getHeight(), color);
    }

    public static void drawRect(int x, int y, int width, int height, int color) {
        fillSquad(x, y, x, y + height, x + width, y + height, x + width, y, color, DEFAULT_Z_LEVEL);
    }

    public static void drawRect(int x, int y, int width, int height, int color, float zLevel) {
        fillSquad(x, y, x, y + height, x + width, y + height, x + width, y, color, zLevel);
    }

    public static void drawRectStroke(int x, int y, int width, int height, int strokeThickness, int strokeColor) {
        drawRectStroke(x, y, width, height, strokeThickness, strokeColor, DEFAULT_Z_LEVEL);
    }

    public static void drawRectStroke(int x, int y, int width, int height, int strokeThickness, int strokeColor, float zLevel) {
        drawRect(x, y, strokeThickness, height, strokeColor, zLevel);                                                                                // left
        drawRect(x + width - strokeThickness, y, strokeThickness, height, strokeColor, zLevel);                                                   // right
        drawRect(x + strokeThickness, y, width - 2 * strokeThickness, strokeThickness, strokeColor, zLevel);                                // top
        drawRect(x + strokeThickness, y + height - strokeThickness, width - 2 * strokeThickness, strokeThickness, strokeColor, zLevel);  // bottom
    }

    public static void fillSquad(float x0, float y0, float x1, float y1, float x2, float y2, float x3, float y3, int alphaColor, float zLevel) {
        int a = (alphaColor >> 24 & 255);
        int r = (alphaColor >> 16 & 255);
        int g = (alphaColor >> 8 & 255);
        int b = (alphaColor & 255);

        setupFillMode();

        final var tesselator = Tesselator.getInstance();
        final var builder = tesselator.getBuilder();

        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

        builder.vertex(x0, y0, zLevel).color(r, g, b, a).endVertex();
        builder.vertex(x1, y1, zLevel).color(r, g, b, a).endVertex();
        builder.vertex(x2, y2, zLevel).color(r, g, b, a).endVertex();
        builder.vertex(x3, y3, zLevel).color(r, g, b, a).endVertex();

        tesselator.end();

        disassembleFillMode();
    }

    public static void setupFillMode() {
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
    }

    public static void disassembleFillMode() {
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }
}