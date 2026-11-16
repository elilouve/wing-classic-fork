package com.elilouve.wingsclassic.client.view;

import com.elilouve.wingsclassic.WingsClassic;
import com.elilouve.wingsclassic.client.anim.AnimatorAvian;
import com.elilouve.wingsclassic.client.anim.AnimatorInsectoid;
import com.elilouve.wingsclassic.client.model.WingsModelAvian;
import com.elilouve.wingsclassic.client.model.WingsModelInsectoid;
import com.elilouve.wingsclassic.registry.WingsItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

/** Associe chaque paire d'ailes à sa forme (créée à la demande, côté client uniquement). */
public final class WingForms {
    private WingForms() {}

    private static Map<Item, WingForm<?>> forms;

    public static WingForm<?> get(final Item item) {
        if (forms == null) {
            forms = build();
        }
        return forms.get(item);
    }

    private static Map<Item, WingForm<?>> build() {
        final WingsModelAvian avian = new WingsModelAvian();
        final WingsModelInsectoid insectoid = new WingsModelInsectoid();
        final Map<Item, WingForm<?>> map = new HashMap<>();
        map.put(WingsItems.ANGEL_WINGS.get(), new WingForm<>(AnimatorAvian::new, avian, texture("angel")));
        map.put(WingsItems.FIRE_WINGS.get(), new WingForm<>(AnimatorAvian::new, avian, texture("fire"), true));
        map.put(WingsItems.BAT_WINGS.get(), new WingForm<>(AnimatorAvian::new, avian, texture("bat")));
        map.put(WingsItems.EVIL_WINGS.get(), new WingForm<>(AnimatorAvian::new, avian, texture("evil")));
        map.put(WingsItems.DRAGON_WINGS.get(), new WingForm<>(AnimatorAvian::new, avian, texture("dragon")));
        map.put(WingsItems.SLIME_WINGS.get(), new WingForm<>(AnimatorInsectoid::new, insectoid, texture("slime")));
        map.put(WingsItems.BLUE_BUTTERFLY_WINGS.get(), new WingForm<>(AnimatorInsectoid::new, insectoid, texture("blue_butterfly")));
        map.put(WingsItems.MONARCH_BUTTERFLY_WINGS.get(), new WingForm<>(AnimatorInsectoid::new, insectoid, texture("monarch_butterfly")));
        map.put(WingsItems.FAIRY_WINGS.get(), new WingForm<>(AnimatorInsectoid::new, insectoid, texture("fairy")));
        return map;
    }

    private static ResourceLocation texture(final String name) {
        return new ResourceLocation(WingsClassic.ID, "textures/entity/wings/" + name + ".png");
    }
}
