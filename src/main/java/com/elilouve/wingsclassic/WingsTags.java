package com.elilouve.wingsclassic;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class WingsTags {
    private WingsTags() {}

    public static final TagKey<Item> FAIRY_DUST = ItemTags.create(new ResourceLocation("forge", "dusts/fairy"));
}
