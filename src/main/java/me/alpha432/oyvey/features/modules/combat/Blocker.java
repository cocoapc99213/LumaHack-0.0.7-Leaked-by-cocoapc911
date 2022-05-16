//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.combat;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.block.Block;
import java.util.Comparator;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.Entity;
import me.alpha432.oyvey.util.BlockUtil;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3i;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class Blocker extends Module
{
    private Setting<Boolean> piston;
    private Setting<Boolean> cev;
    private Setting<Float> range;
    private Setting<Boolean> packetPlace;
    private BlockPos b_piston;
    private BlockPos b_cev;
    
    public Blocker() {
        super("Blocker", "Block gs attack lmao", Category.COMBAT, true, false, false);
        this.piston = (Setting<Boolean>)this.register(new Setting("Piston", (T)true));
        this.cev = (Setting<Boolean>)this.register(new Setting("CevBreaker", (T)true));
        this.range = (Setting<Float>)this.register(new Setting("Range", (T)6.1f, (T)1.0f, (T)10.0f));
        this.packetPlace = (Setting<Boolean>)this.register(new Setting("PacketPlace", (T)true));
        this.b_piston = null;
        this.b_cev = null;
    }
    
    @Override
    public void onTick() {
        if (Blocker.mc.player == null) {
            return;
        }
        try {
            final int ob = this.findMaterials(Blocks.OBSIDIAN);
            if (ob == -1) {
                return;
            }
            final BlockPos p_pos = new BlockPos(Blocker.mc.player.posX, Blocker.mc.player.posY, Blocker.mc.player.posZ);
            if (this.piston.getValue()) {
                final BlockPos[] offset = { new BlockPos(2, 1, 0), new BlockPos(-2, 1, 0), new BlockPos(0, 1, 2), new BlockPos(0, 1, -2) };
                for (int y = 0; y < 4; ++y) {
                    for (int i = 0; i < offset.length; ++i) {
                        final BlockPos pre_piston = p_pos.add((Vec3i)offset[i].add(0, y, 0));
                        if (this.getBlock(pre_piston).getBlock() == Blocks.PISTON || this.getBlock(pre_piston).getBlock() == Blocks.STICKY_PISTON) {
                            this.b_piston = pre_piston;
                        }
                    }
                }
                if (this.b_piston != null) {
                    if (this.getBlock(this.b_piston).getBlock() == Blocks.AIR) {
                        if (Blocker.mc.player.getDistance((double)this.b_piston.getX(), (double)this.b_piston.getY(), (double)this.b_piston.getZ()) > this.range.getValue()) {
                            return;
                        }
                        final int oldslot = Blocker.mc.player.inventory.currentItem;
                        Blocker.mc.player.inventory.currentItem = ob;
                        Blocker.mc.playerController.updateController();
                        BlockUtil.placeBlock(this.b_piston, EnumHand.MAIN_HAND, true, this.packetPlace.getValue(), false);
                        Blocker.mc.player.inventory.currentItem = oldslot;
                        Blocker.mc.playerController.updateController();
                    }
                    if (this.getBlock(this.b_piston).getBlock() == Blocks.OBSIDIAN || Blocker.mc.player.getDistance((double)this.b_piston.getX(), (double)this.b_piston.getY(), (double)this.b_piston.getZ()) > this.range.getValue()) {
                        this.b_piston = null;
                    }
                }
            }
            if (this.cev.getValue()) {
                final BlockPos p_player = new BlockPos(Blocker.mc.player.posX, Blocker.mc.player.posY, Blocker.mc.player.posZ);
                final Entity crystal = (Entity)Blocker.mc.world.loadedEntityList.stream().filter(e -> e instanceof EntityEnderCrystal).filter(c -> c.posY > p_player.getY()).filter(c -> Blocker.mc.player.getDistance(c.posX, Blocker.mc.player.posY, c.posZ) < 1.0).min(Comparator.comparing(c -> Blocker.mc.player.getDistanceSq(c.posX, c.posY, c.posZ))).orElse(null);
                if (this.getBlock(new BlockPos(p_player.getX(), p_player.getY() + 2, p_player.getZ())).getBlock() == Blocks.OBSIDIAN && crystal != null) {
                    this.b_cev = new BlockPos(crystal.posX, crystal.posY, crystal.posZ);
                }
                if (this.b_cev != null && this.getBlock(this.b_cev).getBlock() == Blocks.AIR) {
                    if (Blocker.mc.player.getDistance((double)this.b_cev.getX(), (double)this.b_cev.getY(), (double)this.b_cev.getZ()) > this.range.getValue()) {
                        return;
                    }
                    if (crystal == null && new BlockPos(Blocker.mc.player.posX, (double)this.b_cev.getY(), Blocker.mc.player.posZ).getDistance(this.b_cev.getX(), this.b_cev.getY(), this.b_cev.getZ()) < 1.0) {
                        final int oldslot2 = Blocker.mc.player.inventory.currentItem;
                        Blocker.mc.player.inventory.currentItem = ob;
                        Blocker.mc.playerController.updateController();
                        BlockUtil.placeBlock(this.b_cev.add(0, -1, 0), EnumHand.MAIN_HAND, true, false, false);
                        BlockUtil.placeBlock(this.b_cev, EnumHand.MAIN_HAND, true, false, false);
                        Blocker.mc.player.inventory.currentItem = oldslot2;
                        Blocker.mc.playerController.updateController();
                        this.b_cev = null;
                    }
                }
            }
        }
        catch (Exception ex) {
            this.b_cev = null;
            this.b_piston = null;
        }
    }
    
    private int findMaterials(final Block b) {
        for (int i = 0; i < 9; ++i) {
            if (Blocker.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock && ((ItemBlock)Blocker.mc.player.inventory.getStackInSlot(i).getItem()).getBlock() == b) {
                return i;
            }
        }
        return -1;
    }
    
    public BlockPos getPistonPos() {
        return this.b_piston;
    }
    
    private IBlockState getBlock(final BlockPos o) {
        return Blocker.mc.world.getBlockState(o);
    }
}
