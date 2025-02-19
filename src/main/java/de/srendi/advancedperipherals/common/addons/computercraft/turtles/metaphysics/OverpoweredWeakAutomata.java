package de.srendi.advancedperipherals.common.addons.computercraft.turtles.metaphysics;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleSide;
import de.srendi.advancedperipherals.common.addons.computercraft.peripheral.metaphysics.OverpoweredWeakAutomataCorePeripheral;
import de.srendi.advancedperipherals.lib.turtle.ClockwiseAnimatedTurtleUpgrade;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class OverpoweredWeakAutomata extends ClockwiseAnimatedTurtleUpgrade<OverpoweredWeakAutomataCorePeripheral> {

    public OverpoweredWeakAutomata(ResourceLocation id, ItemStack stack) {
        super(id, stack);
    }

    @Override
    protected ModelResourceLocation getLeftModel() {
        return null;
    }

    @Override
    protected ModelResourceLocation getRightModel() {
        return null;
    }

    @Override
    protected OverpoweredWeakAutomataCorePeripheral buildPeripheral(@NotNull ITurtleAccess turtle, @NotNull TurtleSide side) {
        return new OverpoweredWeakAutomataCorePeripheral(turtle, side);
    }
}
