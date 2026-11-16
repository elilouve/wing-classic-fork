package com.elilouve.wingsclassic.compat;

import com.elilouve.wingsclassic.item.WingsItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

/**
 * Intégration Curios (optionnelle). Cette classe n'est chargée que si Curios est présent.
 * Les ailes se portent dans l'emplacement « dos » (back), le même que l'élytre d'Elytra Slot.
 */
public final class CuriosCompat {
    private CuriosCompat() {}

    public static ItemStack findWings(final LivingEntity entity) {
        return CuriosApi.getCuriosHelper()
            .findFirstCurio(entity, stack -> stack.getItem() instanceof WingsItem)
            .map(SlotResult::stack)
            .orElse(ItemStack.EMPTY);
    }

    public static boolean hasObstruction(final LivingEntity entity, final List<String> blocked) {
        return CuriosApi.getCuriosHelper()
            .findFirstCurio(entity, stack -> blocked.contains(String.valueOf(BuiltInRegistries.ITEM.getKey(stack.getItem()))))
            .isPresent();
    }
}
