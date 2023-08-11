package pextystudios.emogg.resource;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pextystudios.emogg.Emogg;
import pextystudios.emogg.handler.EmojiHandler;
import pextystudios.emogg.util.EmojiUtil;
import pextystudios.emogg.util.StringUtil;


public class Emoji {
    protected final String name;
    protected final ResourceLocation resourceLocation;
    protected final String category;
    protected int width = -1, height = -1;

    protected Emoji(@NotNull String name, @NotNull ResourceLocation resourceLocation, @NotNull String category) {
        this.name = name;
        this.resourceLocation = resourceLocation;
        this.category = category;
    }

    public String getName() {return name;}

    public String getCategory() {return category;}

    public String getCode() {
        return ":" + name + ':';
    }

    public String getEscapedCode() {return '\\' + getCode();}

    public ResourceLocation getResourceLocation() {return resourceLocation;}

    public ResourceLocation getRenderResourceLocation() {return  resourceLocation;}

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    protected boolean load() {
        try {
            var resource = Minecraft.getInstance().getResourceManager().getResource(resourceLocation);
            var bufferedImage = NativeImage.read(resource.get().open());

            width = bufferedImage.getWidth();
            height = bufferedImage.getHeight();

            return width > 0 && height > 0;
        } catch (Exception e) {
            Emogg.LOGGER.error("Failed to load " + StringUtil.repr(resourceLocation), e);
        }

        return false;
    }

    public static @Nullable Emoji from(String name, ResourceLocation resourceLocation) {
        name = EmojiUtil.normalizeNameOrCategory(name);

        var category = resourceLocation.getPath().substring(EmojiHandler.EMOJIS_PATH_PREFIX.length() + 1);
        if (category.contains("/")) {
            var splitPath = category.split("/");

            category = splitPath[splitPath.length - 2];
        } else
            category = EmojiHandler.CATEGORY_DEFAULT;

        category = EmojiUtil.normalizeNameOrCategory(category);

        final var emoji = resourceLocation.getPath().endsWith(EmojiHandler.ANIMATED_EMOJI_EXTENSION) ?
                new AnimatedEmoji(name, resourceLocation, category) : new Emoji(name, resourceLocation, category);

        if (emoji.load())
            return emoji;

        return null;
    }
}