package com.elilouve.wingsclassic.item;

import com.elilouve.wingsclassic.WingsTags;
import com.elilouve.wingsclassic.config.WingsConfig;
import com.elilouve.wingsclassic.flight.WingsHelper;
import com.elilouve.wingsclassic.registry.WingsSounds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class WingsItem extends Item implements Equipable {
    private final String name;
    private final int defaultDurability;

    public WingsItem(final String name, final int durability) {
        super(new Item.Properties().durability(durability));
        this.name = name;
        this.defaultDurability = durability;
    }

    @Override
    public int getMaxDamage(final ItemStack stack) {
        return WingsConfig.durability(this.name, this.defaultDurability);
    }

    @Override
    public EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.CHEST;
    }

    /** Un objet listé dans wearObstructions (ex. élytre) empêche de mettre les ailes. */
    private static boolean isObstructed(final Player player) {
        final java.util.List<String> blocked = WingsConfig.wearObstructions();
        if (blocked.isEmpty()) {
            return false;
        }
        final ItemStack chest = player.getItemBySlot(EquipmentSlot.CHEST);
        if (!chest.isEmpty() && blocked.contains(String.valueOf(BuiltInRegistries.ITEM.getKey(chest.getItem())))) {
            return true;
        }
        if (WingsHelper.isCuriosLoaded()) {
            return com.elilouve.wingsclassic.compat.CuriosCompat.hasObstruction(player, blocked);
        }
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(final Level level, final Player player, final InteractionHand hand) {
        if (isObstructed(player)) {
            if (!level.isClientSide) {
                player.displayClientMessage(Component.translatable("message.wingsclassic.obstructed"), true);
            }
            return InteractionResultHolder.fail(player.getItemInHand(hand));
        }
        return this.swapWithEquipmentSlot(this, level, player, hand);
    }

    @Override
    public Holder<SoundEvent> getEquipSound() {
        return WingsSounds.EQUIP_WINGS;
    }

    @Override
    public boolean isValidRepairItem(final ItemStack stack, final ItemStack repairMaterial) {
        return repairMaterial.is(WingsTags.FAIRY_DUST);
    }

    @Override
    public int getEnchantmentValue() {
        return 1;
    }

    // --- Règles de vol (reprises du mod d'origine) ---

    public boolean isUsable(final Player player, final ItemStack stack) {
        return WingsConfig.flightEnabled(this.name) && (!stack.isDamageableItem() || stack.getDamageValue() < stack.getMaxDamage() - 1)
            && player.getFoodData().getFoodLevel() >= WingsConfig.requiredFlightSatiation(this.name);
    }

    public boolean isLandable(final Player player, final ItemStack stack) {
        return player.getFoodData().getFoodLevel() >= WingsConfig.requiredLandSatiation(this.name);
    }

    public void onFlight(final Player player, final Vec3 direction) {
        final int distance = Math.round((float) direction.length() * 100.0F);
        if (distance > 0) {
            player.causeFoodExhaustion(distance * WingsConfig.flyingExertion(this.name));
        }
    }

    public void onLanding(final Player player) {
        player.causeFoodExhaustion(WingsConfig.landingExertion(this.name));
    }
}
