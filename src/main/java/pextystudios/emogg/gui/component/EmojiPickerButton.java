package pextystudios.emogg.gui.component;

import com.mojang.blaze3d.vertex.PoseStack;
import pextystudios.emogg.emoji.resource.Emoji;
import pextystudios.emogg.handler.EmojiHandler;

public class EmojiPickerButton extends AbstractWidget {
    private Emoji displayableEmoji = null, prevDisplayableEmoji = null;

    public EmojiPickerButton(int x, int y) {
        this(x, y, EmojiHandler.EMOJI_DEFAULT_RENDER_SIZE);
    }

    public EmojiPickerButton(int x, int y, int size) {
        super(
                x,
                y,
                size,
                size
        );

        changeDisplayableEmoji();
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float dt) {
        if (!visible) return;

        var hasBeenHovered = isHovered;
        isHovered = collidePoint(mouseX, mouseY);

        if (!hasBeenHovered && isHovered)
            changeDisplayableEmoji();

        renderButton(poseStack, mouseX, mouseY, dt);
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float dt) {
        if (displayableEmoji == null) {
            disableHint();
            return;
        }

        setHint(displayableEmoji.getCode());

        int renderX = x, renderY = y, renderSize = width;

        if (isHovered()) {
            renderX -= 1;
            renderY -= 1;
            renderSize += 2;
        }

        displayableEmoji.render(renderX, renderY, renderSize, renderSize, poseStack);

        if (isHovered())
            renderToolTip(poseStack, mouseX, mouseY);
    }

    protected void changeDisplayableEmoji() {
        prevDisplayableEmoji = displayableEmoji;

        EmojiHandler.getInstance()
                .getRandomEmoji(true)
                .ifPresent(emoji -> displayableEmoji = emoji);

        if (displayableEmoji == null || prevDisplayableEmoji == null) return;

        while (displayableEmoji.getName().equals(prevDisplayableEmoji.getName()))
            EmojiHandler.getInstance()
                    .getRandomEmoji(true)
                    .ifPresent(emoji -> displayableEmoji = emoji);
    }
}