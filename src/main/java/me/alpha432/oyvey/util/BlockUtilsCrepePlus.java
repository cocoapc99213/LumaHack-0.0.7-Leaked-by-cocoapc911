//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.util;

import net.minecraft.block.BlockAir;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.entity.Entity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.world.IBlockAccess;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.client.Minecraft;

public class BlockUtilsCrepePlus
{
    public static String IlllIlIlllIlIII;
    public double dist;
    protected static Minecraft mc;
    public BlockPos pos;
    public double roty;
    public EnumFacing f;
    public double rotx;
    public int a;
    
    public static BlockUtilsCrepePlus isPlaceable(final BlockPos blockPos, final double d, final boolean bl) {
        final BlockUtilsCrepePlus blockUtils = new BlockUtilsCrepePlus(blockPos, 0, null, d);
        if (!isAir(blockPos)) {
            return null;
        }
        if (!(BlockUtilsCrepePlus.mc.player.inventory.getCurrentItem().getItem() instanceof ItemBlock)) {
            return null;
        }
        final AxisAlignedBB axisAlignedBB = ((ItemBlock)BlockUtilsCrepePlus.mc.player.inventory.getCurrentItem().getItem()).getBlock().getDefaultState().getCollisionBoundingBox((IBlockAccess)BlockUtilsCrepePlus.mc.world, blockPos);
        if (!isAir(blockPos) && BlockUtilsCrepePlus.mc.world.getBlockState(blockPos).getBlock() instanceof BlockLiquid) {
            final Block block = BlockUtilsCrepePlus.mc.world.getBlockState(blockPos.offset(EnumFacing.UP)).getBlock();
            if (block instanceof BlockLiquid) {
                blockUtils.f = EnumFacing.DOWN;
                blockUtils.pos.offset(EnumFacing.UP);
                return blockUtils;
            }
            blockUtils.f = EnumFacing.UP;
            blockUtils.pos.offset(EnumFacing.DOWN);
            return blockUtils;
        }
        else {
            final EnumFacing[] values = EnumFacing.values();
            final int length = values.length;
            int i = 0;
            while (i < length) {
                final EnumFacing enumFacing = values[i];
                if (isAir(new BlockPos(blockPos.getX() - enumFacing.getDirectionVec().getX(), blockPos.getY() - enumFacing.getDirectionVec().getY(), blockPos.getZ() - enumFacing.getDirectionVec().getZ()))) {
                    ++i;
                }
                else {
                    blockUtils.f = enumFacing;
                    if (bl && axisAlignedBB != Block.NULL_AABB && !BlockUtilsCrepePlus.mc.world.checkNoEntityCollision(axisAlignedBB.offset(blockPos), (Entity)null)) {
                        return null;
                    }
                    return blockUtils;
                }
            }
            if (!isRePlaceable(blockPos)) {
                return null;
            }
            blockUtils.f = EnumFacing.UP;
            blockUtils.pos.offset(EnumFacing.UP);
            blockPos.offset(EnumFacing.DOWN);
            if (bl && axisAlignedBB != Block.NULL_AABB && !BlockUtilsCrepePlus.mc.world.checkNoEntityCollision(axisAlignedBB.offset(blockPos), (Entity)null)) {
                return null;
            }
            return blockUtils;
        }
    }
    
    public static boolean doPlace(final BlockUtilsCrepePlus blockUtils, final boolean bl) {
        return blockUtils != null && blockUtils.doPlace(bl);
    }
    
    public boolean doPlace(final boolean bl) {
        final double d = this.pos.getX() + 0.5 - BlockUtilsCrepePlus.mc.player.posX - this.f.getDirectionVec().getX() / 2.0;
        final double d2 = this.pos.getY() + 0.5 - BlockUtilsCrepePlus.mc.player.posY - this.f.getDirectionVec().getY() / 2.0 - BlockUtilsCrepePlus.mc.player.getEyeHeight();
        final double d3 = this.pos.getZ() + 0.5 - BlockUtilsCrepePlus.mc.player.posZ - this.f.getDirectionVec().getZ() / 2.0;
        final double d4 = getDirection2D(d3, d);
        final double d5 = getDirection2D(d2, Math.sqrt(d * d + d3 * d3));
        final Vec3d vec3d = this.getVectorForRotation(-d5, d4 - 90.0);
        this.roty = -d5;
        this.rotx = d4 - 90.0;
        final EnumActionResult enumActionResult = BlockUtilsCrepePlus.mc.playerController.processRightClickBlock(BlockUtilsCrepePlus.mc.player, BlockUtilsCrepePlus.mc.world, new BlockPos(this.pos.getX() - this.f.getDirectionVec().getX(), this.pos.getY() - this.f.getDirectionVec().getY(), this.pos.getZ() - this.f.getDirectionVec().getZ()), this.f, vec3d, EnumHand.MAIN_HAND);
        if (enumActionResult == EnumActionResult.SUCCESS) {
            if (bl) {
                BlockUtilsCrepePlus.mc.player.swingArm(EnumHand.MAIN_HAND);
            }
            return true;
        }
        return false;
    }
    
    public BlockUtilsCrepePlus(final BlockPos blockPos, final int n, final EnumFacing enumFacing, final double d) {
        this.pos = blockPos;
        this.a = n;
        this.f = enumFacing;
        this.dist = d;
    }
    
    public static double getDirection2D(final double d, final double d2) {
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
    
    protected final Vec3d getVectorForRotation(final float f, final float f2) {
        final float f3 = MathHelper.cos(-f2 * 0.017453292f - 3.1415927f);
        final float f4 = MathHelper.sin(-f2 * 0.017453292f - 3.1415927f);
        final float f5 = -MathHelper.cos(-f * 0.017453292f);
        final float f6 = MathHelper.sin(-f * 0.017453292f);
        return new Vec3d((double)(f4 * f5), (double)f6, (double)(f3 * f5));
    }
    
    public static boolean doBreak(final BlockPos blockPos, final EnumFacing enumFacing) {
        return BlockUtilsCrepePlus.mc.playerController.clickBlock(blockPos, enumFacing);
    }
    
    public void doBreak() {
        BlockUtilsCrepePlus.mc.playerController.onPlayerDamageBlock(new BlockPos(this.pos.getX() - this.f.getDirectionVec().getX(), this.pos.getY() - this.f.getDirectionVec().getY(), this.pos.getZ() - this.f.getDirectionVec().getZ()), this.f);
    }
    
    protected final Vec3d getVectorForRotation(final double d, final double d2) {
        final float f = MathHelper.cos((float)(-d2 * 0.01745329238474369 - 3.1415927410125732));
        final float f2 = MathHelper.sin((float)(-d2 * 0.01745329238474369 - 3.1415927410125732));
        final float f3 = -MathHelper.cos((float)(-d * 0.01745329238474369));
        final float f4 = MathHelper.sin((float)(-d * 0.01745329238474369));
        return new Vec3d((double)(f2 * f3), (double)f4, (double)(f * f3));
    }
    
    public static boolean isRePlaceable(final BlockPos blockPos) {
        final Block block = BlockUtilsCrepePlus.mc.world.getBlockState(blockPos).getBlock();
        return block.isReplaceable((IBlockAccess)BlockUtilsCrepePlus.mc.world, blockPos) && !(block instanceof BlockAir);
    }
    
    public static boolean isAir(final BlockPos blockPos) {
        final Block block = BlockUtilsCrepePlus.mc.world.getBlockState(blockPos).getBlock();
        return block instanceof BlockAir;
    }
    
    static {
        BlockUtilsCrepePlus.IlllIlIlllIlIII = "in.";
        BlockUtilsCrepePlus.mc = Minecraft.getMinecraft();
    }
}
