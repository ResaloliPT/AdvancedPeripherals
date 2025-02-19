package de.srendi.advancedperipherals.common.util.inventory;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.core.apis.TableHelper;
import de.srendi.advancedperipherals.common.util.NBTUtil;
import de.srendi.advancedperipherals.common.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Map;

public class FluidFilter {

    private Fluid fluid = Fluids.EMPTY;
    private TagKey<Fluid> tag = null;
    private CompoundTag nbt = null;
    private int count = 1000;
    private String fingerprint = "";

    private FluidFilter() {
    }

    public static Pair<FluidFilter, String> parse(Map<?, ?> item) {
        FluidFilter itemArgument = empty();
        // If the map is empty, return a filter without any filters
        if (item.size() == 0)
            return Pair.of(itemArgument, null);
        if (item.containsKey("name")) {
            try {
                String name = TableHelper.getStringField(item, "name");
                if (name.startsWith("#")) {
                    itemArgument.tag = TagKey.create(Registry.FLUID_REGISTRY, new ResourceLocation(name.substring(1)));
                } else if ((itemArgument.fluid = ItemUtil.getRegistryEntry(name, ForgeRegistries.FLUIDS)) == null) {
                    return Pair.of(null, "FLUID_NOT_FOUND");
                }
            } catch (LuaException luaException) {
                return Pair.of(null, "NO_VALID_FLUID");
            }
        }
        if (item.containsKey("nbt")) {
            try {
                itemArgument.nbt = NBTUtil.fromText(TableHelper.getStringField(item, "nbt"));
            } catch (LuaException luaException) {
                return Pair.of(null, "NO_VALID_NBT");
            }
        }
        if (item.containsKey("fingerprint")) {
            try {
                itemArgument.fingerprint = TableHelper.getStringField(item, "fingerprint");
            } catch (LuaException luaException) {
                return Pair.of(null, "NO_VALID_FINGERPRINT");
            }
        }
        if (item.containsKey("count")) {
            try {
                itemArgument.count = TableHelper.getIntField(item, "count");
            } catch (LuaException luaException) {
                return Pair.of(null, "NO_VALID_COUNT");
            }
        }

        return Pair.of(itemArgument, null);
    }

    public static FluidFilter fromStack(FluidStack stack) {
        FluidFilter filter = empty();
        filter.fluid = stack.getFluid();
        filter.nbt = stack.hasTag() ? stack.getTag() : null;
        return filter;
    }

    public static FluidFilter empty() {
        return new FluidFilter();
    }

    public boolean isEmpty() {
        return fingerprint.isEmpty() && fluid == Fluids.EMPTY && tag == null && nbt == null;
    }

    public FluidStack toFluidStack() {
        var result = new FluidStack(fluid, count);
        result.setTag(nbt != null ? nbt.copy() : null);
        return result;
    }

    public FluidFilter setCount(int count) {
        this.count = count;
        return this;
    }

    public boolean test(FluidStack stack) {
        if (!fingerprint.isEmpty()) {
            String testFingerprint = FluidUtil.getFingerprint(stack);
            return fingerprint.equals(testFingerprint);
        }

        // If the filter does not have nbt values, a tag or a fingerprint, just test if the items are the same
        if (fluid != Fluids.EMPTY) {
            if (tag == null && nbt == null && fingerprint.isEmpty())
                return stack.getFluid().isSame(fluid);
        }
        if (tag != null && !stack.getFluid().is(tag))
            return false;
        if (nbt != null && !stack.getOrCreateTag().equals(nbt))
            return false;

        return true;
    }

    public int getCount() {
        return count;
    }

    public Fluid getFluid() {
        return fluid;
    }

    public Tag getNbt() {
        return nbt;
    }
}
