package com.elilouve.wingsclassic.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public final class WingsKeys {
    private WingsKeys() {}

    public static final KeyMapping FLY = new KeyMapping(
        "key.wingsclassic.fly",
        KeyConflictContext.IN_GAME,
        InputConstants.Type.KEYSYM.getOrCreate(GLFW.GLFW_KEY_R),
        "key.categories.wingsclassic"
    );
}
