//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.util;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.EnumHand;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.EnumActionResult;
import net.minecraft.entity.Entity;
import net.minecraft.block.BlockObsidian;
import net.minecraft.util.EnumFacing;
import net.minecraft.block.BlockEmptyDrops;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.client.Minecraft;

public class CrystalUtilsCrepePlus
{
    protected static Minecraft mc;
    public static final AxisAlignedBB CRYSTAL_AABB;
    
    public static boolean canPlace(final BlockPos blockPos) {
        return CrystalUtilsCrepePlus.mc.world.getBlockState(blockPos.offset(EnumFacing.DOWN)).getBlock() instanceof BlockEmptyDrops && CrystalUtilsCrepePlus.mc.world.getBlockState(blockPos.offset(EnumFacing.DOWN)).getBlock() instanceof BlockObsidian && CrystalUtilsCrepePlus.mc.world.checkNoEntityCollision(new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 2.0, 1.0).offset(blockPos), (Entity)null);
    }
    
    public static EnumActionResult doPlace(final BlockPos blockPos) {
        final double d = blockPos.getX() + 0.5 - CrystalUtilsCrepePlus.mc.player.posX;
        final double d2 = blockPos.getY() - 1 + 0.5 - CrystalUtilsCrepePlus.mc.player.posY - 0.5 - CrystalUtilsCrepePlus.mc.player.getEyeHeight();
        final double d3 = blockPos.getZ() + 0.5 - CrystalUtilsCrepePlus.mc.player.posZ;
        final double d4 = getDirection2D(d3, d);
        final double d5 = getDirection2D(d2, Math.sqrt(d * d + d3 * d3));
        final Vec3d vec3d = getVectorForRotation(-d5, d4 - 90.0);
        return CrystalUtilsCrepePlus.mc.playerController.processRightClickBlock(CrystalUtilsCrepePlus.mc.player, CrystalUtilsCrepePlus.mc.world, blockPos.offset(EnumFacing.DOWN), EnumFacing.UP, vec3d, CrystalUtilsCrepePlus.mc.player.getActiveHand());
    }
    
    public static EnumActionResult placeCrystal(final BlockPos blockPos) {
        blockPos.offset(EnumFacing.DOWN);
        final double d = blockPos.getX() + 0.5 - CrystalUtilsCrepePlus.mc.player.posX;
        final double d2 = blockPos.getY() + 0.5 - CrystalUtilsCrepePlus.mc.player.posY - 0.5 - CrystalUtilsCrepePlus.mc.player.getEyeHeight();
        final double d3 = blockPos.getZ() + 0.5 - CrystalUtilsCrepePlus.mc.player.posZ;
        final double d4 = getDirection2D(d3, d);
        final double d5 = getDirection2D(d2, Math.sqrt(d * d + d3 * d3));
        final Vec3d vec3d = getVectorForRotation(-d5, d4 - 90.0);
        if (((ItemStack)CrystalUtilsCrepePlus.mc.player.inventory.offHandInventory.get(0)).getItem().getClass().equals(Item.getItemById(426).getClass())) {
            return CrystalUtilsCrepePlus.mc.playerController.processRightClickBlock(CrystalUtilsCrepePlus.mc.player, CrystalUtilsCrepePlus.mc.world, blockPos.offset(EnumFacing.DOWN), EnumFacing.UP, vec3d, EnumHand.OFF_HAND);
        }
        if (InventoryUtilsCrepePlus.pickItem(426) != -1) {
            InventoryUtilsCrepePlus.setSlot(InventoryUtilsCrepePlus.pickItem(426));
            return CrystalUtilsCrepePlus.mc.playerController.processRightClickBlock(CrystalUtilsCrepePlus.mc.player, CrystalUtilsCrepePlus.mc.world, blockPos.offset(EnumFacing.DOWN), EnumFacing.UP, vec3d, EnumHand.MAIN_HAND);
        }
        return EnumActionResult.FAIL;
    }
    
    protected static final double getDirection2D(final double d, final double d2) {
        double d3;
        if (d2 == 0.0) {
            d3 = ((d > 0.0) ? 90.0 : -90.0);
        }
        else {
            d3 = Math.atan(d / d2) * 57.2957796;
            if (d2 < 0.0) {
                d3 = ((d > 0.0) ? (d3 += 180.0) : ((d < 0.0) ? (d3 -= 180.0) : 180.0));
            }
        }
        return d3;
    }
    
    protected static final Vec3d getVectorForRotation(final double d, final double d2) {
        final float f = MathHelper.cos((float)(-d2 * 0.01745329238474369 - 3.1415927410125732));
        final float f2 = MathHelper.sin((float)(-d2 * 0.01745329238474369 - 3.1415927410125732));
        final float f3 = -MathHelper.cos((float)(-d * 0.01745329238474369));
        final float f4 = MathHelper.sin((float)(-d * 0.01745329238474369));
        return new Vec3d((double)(f2 * f3), (double)f4, (double)(f * f3));
    }
    
    static {
        CrystalUtilsCrepePlus.mc = Minecraft.getMinecraft();
        CRYSTAL_AABB = new AxisAlignedBB(0.0, 0.0, 0.0, 1.0, 2.0, 1.0);
    }
}
