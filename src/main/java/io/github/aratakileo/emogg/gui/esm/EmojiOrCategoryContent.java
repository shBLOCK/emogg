package io.github.aratakileo.emogg.gui.esm;

import io.github.aratakileo.emogg.emoji.Emoji;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class EmojiOrCategoryContent {
    private final Emoji emoji;
    private final CategoryContent categoryContent;

    private EmojiOrCategoryContent(@Nullable Emoji emoji, @Nullable CategoryContent categoryContent) {
        this.emoji = emoji;
        this.categoryContent = categoryContent;
    }

    public EmojiOrCategoryContent(@NotNull Emoji emoji) {
        this(emoji, null);
    }

    public EmojiOrCategoryContent(@NotNull CategoryContent categoryContent) {
        this(null, categoryContent);
    }

    public boolean isEmoji() {
        return emoji != null;
    }

    public @Nullable Emoji getEmoji() {
        return emoji;
    }

    public boolean isCategoryContent() {
        return categoryContent != null;
    }

    public @Nullable CategoryContent getCategoryContent() {
        return categoryContent;
    }
}
