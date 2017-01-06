package eiteam.esteemedinnovation.tools.steam.upgrades;

import eiteam.esteemedinnovation.api.tool.SteamToolSlot;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.player.PlayerEvent;

import javax.annotation.Nonnull;

import static eiteam.esteemedinnovation.tools.ToolsModule.upgradeResource;

public class ItemChainsawUpgrade extends ItemSteamToolUpgrade {
    private static final Potion SLOWNESS_POTION = Potion.getPotionById(PotionType.getID(PotionTypes.SLOWNESS));

    public ItemChainsawUpgrade() {
        super(SteamToolSlot.SAW_HEAD, upgradeResource("chain"), null, 1);
    }

    @Override
    public float onUpdateBreakSpeedWithTool(PlayerEvent.BreakSpeed event, float newSpeed, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        return (newSpeed == 0F ? event.getOriginalSpeed() : newSpeed) / 1.7F;
    }

    @Override
    public boolean onAttackWithTool(@Nonnull EntityPlayer attacker, @Nonnull EntityLivingBase victim, DamageSource damageSource, @Nonnull ItemStack toolStack, @Nonnull ItemStack thisUpgradeStack) {
        if (!attacker.worldObj.isRemote) {
            victim.attackEntityFrom(DamageSource.generic, 9.0F);
            victim.addPotionEffect(new PotionEffect(SLOWNESS_POTION, 10, 10));
        }
        return true;
    }
}
