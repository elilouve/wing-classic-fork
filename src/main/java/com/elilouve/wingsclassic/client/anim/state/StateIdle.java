package com.elilouve.wingsclassic.client.anim.state;

import com.elilouve.wingsclassic.client.anim.Animator;
import com.elilouve.wingsclassic.flight.Flight;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class StateIdle extends State {
    public StateIdle() {
        super(Animator::beginIdle);
    }

    @Override
    protected State createIdle() {
        return this;
    }

    @Override
    protected State getDescent(final Flight flight, final Player player, final ItemStack wings) {
        final BlockPos below = BlockPos.containing(player.getX(), player.getY() - 0.25D, player.getZ());
        if (player.level().isEmptyBlock(below) && player.level().isEmptyBlock(below.below())) {
            return super.getDescent(flight, player, wings);
        }
        return this.createIdle();
    }
}
