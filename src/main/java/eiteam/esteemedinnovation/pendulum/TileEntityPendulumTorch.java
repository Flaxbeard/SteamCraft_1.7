package eiteam.esteemedinnovation.pendulum;

import eiteam.esteemedinnovation.api.tile.TileEntityTickableSafe;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;

public class TileEntityPendulumTorch extends TileEntityTickableSafe {
    private int timer;
    private int numStrings;
    private int requiredTicks;
    private int maximumTicks;
    private boolean initialized;

    private void init() {
        BlockPos.MutableBlockPos mutPos = new BlockPos.MutableBlockPos(pos.up());
        while (worldObj.getBlockState(mutPos).getBlock() == PendulumModule.PENDULUM_STRING) {
            numStrings++;
            mutPos.move(EnumFacing.UP);
        }
        requiredTicks = numStrings * 20;
        maximumTicks = requiredTicks + 20;
        initialized = true;
    }

    @Override
    public boolean canUpdate(IBlockState target) {
        return target.getBlock() == PendulumModule.PENDULUM_TORCH;
    }

    @Override
    public void safeUpdate() {
        if (!initialized) {
            init();
        }

        if (timer > maximumTicks) {
            timer = 0;
        }
        // FIXME: This might be bad for performance. Test.
        worldObj.checkLightFor(EnumSkyBlock.BLOCK, pos);
        worldObj.notifyNeighborsOfStateChange(pos, worldObj.getBlockState(pos).getBlock());
        timer++;
    }

    boolean canProvideWeakPower() {
        return timer >= requiredTicks && timer <= maximumTicks;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        timer = compound.getInteger("Timer");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        compound = super.writeToNBT(compound);
        compound.setInteger("Timer", timer);
        return compound;
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound access = getUpdateTag();
        access.setInteger("Timer", timer);
        return new SPacketUpdateTileEntity(pos, 1, access);
    }


    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        NBTTagCompound access = pkt.getNbtCompound();
        timer = access.getInteger("Timer");
    }
}