package com.elilouve.wingsclassic.flight;

import com.elilouve.wingsclassic.compat.CuriosCompat;
import com.elilouve.wingsclassic.item.WingsItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModList;

/** Retrouve les ailes portées : emplacement Curios « dos » d'abord, puis emplacement de plastron. */
public final class WingsHelper {
    private WingsHelper() {}

    private static Boolean curiosLoaded;

    public static boolean isCuriosLoaded() {
        if (curiosLoaded == null) {
            curiosLoaded = ModList.get() != null && ModList.get().isLoaded("curios");
        }
        return curiosLoaded;
    }

    public static ItemStack findWings(final LivingEntity entity) {
        if (isCuriosLoaded()) {
            final ItemStack curio = CuriosCompat.findWings(entity);
            if (!curio.isEmpty()) {
                return curio;
            }
        }
        final ItemStack chest = entity.getItemBySlot(EquipmentSlot.CHEST);
        return chest.getItem() instanceof WingsItem ? chest : ItemStack.EMPTY;
    }
}
