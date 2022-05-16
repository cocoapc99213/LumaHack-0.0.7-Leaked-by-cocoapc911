//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.combat;

import net.minecraft.block.state.IBlockState;
import java.util.ArrayList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketPlayer;
import me.alpha432.oyvey.util.MathUtil;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.Vec3d;
import net.minecraft.network.Packet;
import net.minecraft.util.EnumFacing;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import me.alpha432.oyvey.features.modules.misc.AutoGG;
import me.alpha432.oyvey.util.BlockUtil;
import net.minecraft.util.EnumHand;
import me.alpha432.oyvey.OyVey;
import java.util.Comparator;
import me.alpha432.oyvey.features.command.Command;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import java.util.List;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.player.EntityPlayer;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class PistonCrystal extends Module
{
    private Setting<redstone> redstoneType;
    private Setting<Integer> start_delay;
    private Setting<Integer> place_delay;
    private Setting<Integer> crystal_delay;
    private Setting<Integer> break_delay;
    private Setting<Integer> break_attempts;
    private Setting<types> target_type;
    private Setting<Integer> MaxY;
    private Setting<Float> range;
    private Setting<mode> trap;
    private Setting<Boolean> packetPlace;
    private Setting<arm> swingArm;
    private Setting<Boolean> antiweakness;
    private Setting<Boolean> toggle;
    public EntityPlayer target;
    private boolean r_redstone;
    private int b_stage;
    private BlockPos b_crystal;
    private BlockPos b_piston;
    private BlockPos b_redStone;
    private boolean p_crystal;
    private boolean p_piston;
    private boolean p_redstone;
    private boolean s_crystal;
    private boolean u_crystal;
    private int attempts;
    private int crystalId;
    private int trapprogress;
    private int timer;
    private boolean autoGG;
    private int debug_stage;
    private List<BlockPos> c_crystal;
    private List<BlockPos> c_piston;
    private List<BlockPos> c_redStone;
    private boolean isTorch;
    
    public PistonCrystal() {
        super("PistonCrystal", "Sleeping... :3", Category.COMBAT, true, false, false);
        this.redstoneType = (Setting<redstone>)this.register(new Setting("Redstone", redstone.Both));
        this.start_delay = (Setting<Integer>)this.register(new Setting("Start Delay", 1, 0, 10));
        this.place_delay = (Setting<Integer>)this.register(new Setting("Place Delay", 1, 0, 10));
        this.crystal_delay = (Setting<Integer>)this.register(new Setting("Crystal Delay", 1, 0, 10));
        this.break_delay = (Setting<Integer>)this.register(new Setting("Break Delay", 1, 0, 10));
        this.break_attempts = (Setting<Integer>)this.register(new Setting("Break Attempts", 2, 1, 10));
        this.target_type = (Setting<types>)this.register(new Setting("Target", types.Looking));
        this.MaxY = (Setting<Integer>)this.register(new Setting("MaxY", 2, 1, 5));
        this.range = (Setting<Float>)this.register(new Setting("Range", 5.2f, 1.0f, 15.0f));
        this.trap = (Setting<mode>)this.register(new Setting("Trap Mode", mode.Smart));
        this.packetPlace = (Setting<Boolean>)this.register(new Setting("PacketPlace", false));
        this.swingArm = (Setting<arm>)this.register(new Setting("Swing Arm", arm.MainHand));
        this.antiweakness = (Setting<Boolean>)this.register(new Setting("AntiWeakness", false));
        this.toggle = (Setting<Boolean>)this.register(new Setting("Toggle", true));
        this.target = null;
        this.r_redstone = false;
        this.b_stage = 0;
        this.b_crystal = null;
        this.b_piston = null;
        this.b_redStone = null;
        this.p_crystal = false;
        this.p_piston = false;
        this.p_redstone = false;
        this.s_crystal = false;
        this.u_crystal = false;
        this.attempts = 0;
        this.crystalId = 0;
        this.trapprogress = 0;
        this.timer = 0;
        this.autoGG = false;
        this.debug_stage = -1;
        this.c_crystal = null;
        this.c_piston = null;
        this.c_redStone = null;
        this.isTorch = false;
    }
    
    @Override
    public void onEnable() {
        this.Init();
        this.autoGG = false;
    }
    
    @Override
    public void onTick() {
        try {
            final int oldslot = PistonCrystal.mc.player.inventory.currentItem;
            final int pickaxe = this.findItem(Items.DIAMOND_PICKAXE);
            final int crystal = this.findItem(Items.END_CRYSTAL);
            int piston = this.findMaterials((Block)Blocks.PISTON);
            if (piston == -1) {
                piston = this.findMaterials((Block)Blocks.STICKY_PISTON);
            }
            int redstone = this.findMaterials(Blocks.REDSTONE_TORCH);
            this.isTorch = true;
            if (this.redstoneType.getValue() == PistonCrystal.redstone.Block || (this.redstoneType.getValue() == PistonCrystal.redstone.Both && redstone == -1)) {
                redstone = this.findMaterials(Blocks.REDSTONE_BLOCK);
                this.isTorch = false;
            }
            final int obsidian = this.findMaterials(Blocks.OBSIDIAN);
            int sword = this.findItem(Items.DIAMOND_SWORD);
            if (this.antiweakness.getValue() && sword == -1) {
                sword = pickaxe;
            }
            if (pickaxe == -1 || crystal == -1 || piston == -1 || redstone == -1 || obsidian == -1) {
                Command.sendMessage("Missing Materials! disabling...");
                this.disable();
                return;
            }
            this.debug_stage = 0;
            if (this.target == null) {
                if (this.target_type.getValue() == types.Nearest) {
                    this.target = (EntityPlayer)PistonCrystal.mc.world.playerEntities.stream().filter(p -> p.getEntityId() != PistonCrystal.mc.player.getEntityId()).min(Comparator.comparing(p -> PistonCrystal.mc.player.getDistance(p))).orElse(null);
                }
                if (this.target_type.getValue() == types.Looking) {}
                if (this.target == null) {
                    this.disable();
                    return;
                }
            }
            this.debug_stage = 1;
            if (this.b_crystal == null || this.b_piston == null || this.b_redStone == null) {
                this.searchSpace();
                if (this.b_crystal == null || this.b_piston == null || this.b_redStone == null) {
                    if (this.toggle.getValue()) {
                        Command.sendMessage("Not found space! disabling...");
                        this.disable();
                    }
                    return;
                }
            }
            this.debug_stage = 2;
            if (this.getRange(this.b_crystal) > this.range.getValue() || this.getRange(this.b_piston) > this.range.getValue() || this.getRange(this.b_redStone) > this.range.getValue()) {
                Command.sendMessage("Out of range! disabling...");
                if (this.toggle.getValue()) {
                    this.disable();
                }
                return;
            }
            this.debug_stage = 3;
            final boolean doTrap = this.trap.getValue() == mode.Force || (this.trap.getValue() == mode.Smart && OyVey.holeManager.isSafe(PistonCrystal.mc.player.getPosition()) && this.b_piston.getY() == PistonCrystal.mc.player.getPosition().getY() + 1);
            if (doTrap && PistonCrystal.mc.world.getBlockState(new BlockPos(this.target.posX, this.target.posY + 2.0, this.target.posZ)).getBlock() == Blocks.AIR) {
                if (this.timer < this.place_delay.getValue()) {
                    ++this.timer;
                    return;
                }
                this.timer = 0;
                PistonCrystal.mc.player.inventory.currentItem = obsidian;
                PistonCrystal.mc.playerController.updateController();
                final BlockPos first = new BlockPos((Math.floor(this.target.posX) - this.b_crystal.getX()) * 1.0 + this.target.posX, (double)this.b_piston.getY(), (Math.floor(this.target.posZ) - this.b_crystal.getZ()) * 1.0 + this.target.posZ);
                if (this.trapprogress == 0 || this.trapprogress == 1) {
                    BlockPos pos = first;
                    if (this.trapprogress == 1) {
                        pos = new BlockPos(first.getX(), first.getY() + 1, first.getZ());
                    }
                    BlockUtil.placeBlock(pos, EnumHand.MAIN_HAND, true, this.packetPlace.getValue(), false);
                }
                else {
                    BlockUtil.placeBlock(new BlockPos(this.target.posX, this.target.posY + 2.0, this.target.posZ), EnumHand.MAIN_HAND, true, this.packetPlace.getValue(), false);
                }
                ++this.trapprogress;
            }
            else {
                this.debug_stage = 4;
                OyVey.moduleManager.getModuleByClass(AutoGG.class).addTargetedPlayer(this.target.getName());
                this.debug_stage = 5;
                if (this.getBlock(this.b_piston.add(0, -1, 0)).getBlock() == Blocks.AIR) {
                    PistonCrystal.mc.player.inventory.currentItem = obsidian;
                    PistonCrystal.mc.playerController.updateController();
                    if (this.timer < this.place_delay.getValue()) {
                        ++this.timer;
                        return;
                    }
                    this.timer = 0;
                    BlockUtil.placeBlock(this.b_piston.add(0, -1, 0), EnumHand.MAIN_HAND, true, this.packetPlace.getValue(), false);
                }
                else if (this.getBlock(this.b_redStone.add(0, -1, 0)).getBlock() == Blocks.AIR && this.isTorch) {
                    PistonCrystal.mc.player.inventory.currentItem = obsidian;
                    PistonCrystal.mc.playerController.updateController();
                    if (this.timer < this.place_delay.getValue()) {
                        ++this.timer;
                        return;
                    }
                    this.timer = 0;
                    BlockUtil.placeBlock(this.b_redStone.add(0, -1, 0), EnumHand.MAIN_HAND, true, this.packetPlace.getValue(), false);
                }
                else {
                    this.debug_stage = 6;
                    if (this.r_redstone) {
                        if (this.getBlock(this.b_redStone).getBlock() == Blocks.AIR) {
                            this.r_redstone = false;
                            this.b_stage = 0;
                            this.p_crystal = false;
                            this.p_redstone = false;
                            return;
                        }
                        PistonCrystal.mc.player.inventory.currentItem = pickaxe;
                        PistonCrystal.mc.playerController.updateController();
                        if (this.b_stage == 0) {
                            PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, this.b_redStone, EnumFacing.DOWN));
                            this.b_stage = 1;
                        }
                        else if (this.b_stage == 1) {
                            PistonCrystal.mc.playerController.onPlayerDamageBlock(this.b_redStone, EnumFacing.DOWN);
                        }
                    }
                    else {
                        this.debug_stage = 7;
                        if (!this.p_piston) {
                            if (this.timer < this.place_delay.getValue()) {
                                ++this.timer;
                                return;
                            }
                            this.timer = 0;
                            PistonCrystal.mc.player.inventory.currentItem = piston;
                            PistonCrystal.mc.playerController.updateController();
                            final float[] angle = MathUtil.calcAngle(new Vec3d((Vec3i)this.b_piston), new Vec3d((Vec3i)this.b_crystal));
                            PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(angle[0] + 180.0f, angle[1], true));
                            BlockUtil.placeBlock(this.b_piston, EnumHand.MAIN_HAND, false, this.packetPlace.getValue(), false);
                            this.p_piston = true;
                        }
                        this.debug_stage = 8;
                        if (!this.p_crystal) {
                            if (this.timer < this.crystal_delay.getValue()) {
                                ++this.timer;
                                return;
                            }
                            this.timer = 0;
                            if (crystal != 999) {
                                PistonCrystal.mc.player.inventory.currentItem = crystal;
                            }
                            PistonCrystal.mc.playerController.updateController();
                            AutoCrystal.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItemOnBlock(this.b_crystal, EnumFacing.UP, (PistonCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND, 0.0f, 0.0f, 0.0f));
                            this.p_crystal = true;
                        }
                        this.debug_stage = 9;
                        if (!this.p_redstone) {
                            if (this.timer < this.place_delay.getValue()) {
                                ++this.timer;
                                return;
                            }
                            this.timer = 0;
                            PistonCrystal.mc.player.inventory.currentItem = redstone;
                            PistonCrystal.mc.playerController.updateController();
                            BlockUtil.placeBlock(this.b_redStone, EnumHand.MAIN_HAND, true, this.packetPlace.getValue(), false);
                            this.p_redstone = true;
                        }
                        this.debug_stage = 10;
                        if (this.p_crystal && this.p_piston && this.p_redstone && !this.u_crystal) {
                            if (this.timer < this.break_delay.getValue()) {
                                ++this.timer;
                                return;
                            }
                            this.timer = 0;
                            final Entity t_crystal = (Entity)PistonCrystal.mc.world.loadedEntityList.stream().filter(p -> p instanceof EntityEnderCrystal).min(Comparator.comparing(c -> this.target.getDistance(c))).orElse(null);
                            if (t_crystal == null) {
                                if (this.attempts < this.break_attempts.getValue()) {
                                    ++this.attempts;
                                    return;
                                }
                                this.attempts = 0;
                                Command.sendMessage("Not found crystal! retrying...");
                                this.r_redstone = true;
                                this.b_stage = 0;
                                return;
                            }
                            else {
                                this.crystalId = t_crystal.getEntityId();
                                if (this.antiweakness.getValue()) {
                                    PistonCrystal.mc.player.inventory.currentItem = sword;
                                    PistonCrystal.mc.playerController.updateController();
                                }
                                PistonCrystal.mc.player.connection.sendPacket((Packet)new CPacketUseEntity(t_crystal));
                                if (this.swingArm.getValue() != arm.None) {
                                    PistonCrystal.mc.player.swingArm((this.swingArm.getValue() == arm.MainHand) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
                                }
                                this.u_crystal = true;
                            }
                        }
                        this.debug_stage = 11;
                        if (this.u_crystal) {
                            if (this.timer < this.break_delay.getValue()) {
                                ++this.timer;
                                return;
                            }
                            this.timer = 0;
                            this.Init();
                        }
                        else {
                            PistonCrystal.mc.player.inventory.currentItem = oldslot;
                            PistonCrystal.mc.playerController.updateController();
                        }
                    }
                }
            }
        }
        catch (Exception ex) {
            Command.sendMessage("Has Error! : " + ex.toString());
            Command.sendMessage("Stage : " + this.debug_stage);
            Command.sendMessage("Trying to init...");
            this.Init();
        }
    }
    
    private int findMaterials(final Block b) {
        for (int i = 0; i < 9; ++i) {
            if (PistonCrystal.mc.player.inventory.getStackInSlot(i).getItem() instanceof ItemBlock && ((ItemBlock)PistonCrystal.mc.player.inventory.getStackInSlot(i).getItem()).getBlock() == b) {
                return i;
            }
        }
        return -1;
    }
    
    private int findItem(final Item item) {
        if (item == Items.END_CRYSTAL && PistonCrystal.mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
            return 999;
        }
        for (int i = 0; i < 9; ++i) {
            if (PistonCrystal.mc.player.inventory.getStackInSlot(i).getItem() == item) {
                return i;
            }
        }
        return -1;
    }
    
    private void searchSpace() {
        final BlockPos floored_pos = new BlockPos(this.target.posX, this.target.posY, this.target.posZ);
        final BlockPos[] offset = { new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1) };
        this.debug_stage = -2;
        for (int y = 0; y < this.MaxY.getValue() + 1; ++y) {
            for (int i = 0; i < offset.length; ++i) {
                this.sp(offset[i], y, floored_pos);
            }
        }
        this.debug_stage = -3;
        this.b_crystal = this.c_crystal.stream().min(Comparator.comparing(b -> PistonCrystal.mc.player.getDistance((double)b.getX(), (double)b.getY(), (double)b.getZ()))).orElse(null);
        this.b_piston = this.c_piston.stream().min(Comparator.comparing(b -> this.b_crystal.distanceSq(b))).orElse(null);
        if (this.b_piston != null) {
            this.b_redStone = this.c_redStone.stream().filter(b -> this.b_piston.getDistance(b.getX(), b.getY(), b.getZ()) < 2.0).min(Comparator.comparing(b -> this.b_crystal.distanceSq(b))).orElse(null);
        }
        this.debug_stage = -4;
        if (this.b_crystal == null) {
            return;
        }
        if (this.getBlock(this.b_crystal.add(0, 1, 0)).getBlock() == Blocks.PISTON_HEAD && this.getBlock(this.b_redStone).getBlock() == this.getRedStoneBlock()) {
            this.r_redstone = true;
            this.b_stage = 0;
        }
        this.debug_stage = -5;
    }
    
    private void sp(final BlockPos offset, final int offset_y, final BlockPos enemy_pos) {
        final BlockPos mypos = new BlockPos(PistonCrystal.mc.player.posX, PistonCrystal.mc.player.posY, PistonCrystal.mc.player.posZ);
        boolean v_crystal = false;
        boolean v_piston = false;
        boolean v_redstone = false;
        final BlockPos pre_crystal = new BlockPos(enemy_pos.getX() + offset.getX(), enemy_pos.getY() + offset.getY() + offset_y, enemy_pos.getZ() + offset.getZ());
        final BlockPos pre_piston = new BlockPos(enemy_pos.getX() + offset.getX() * 2, enemy_pos.getY() + offset.getY() + offset_y + 1, enemy_pos.getZ() + offset.getZ() * 2);
        BlockPos pre_redstone = new BlockPos(enemy_pos.getX() + offset.getX() * 3, enemy_pos.getY() + offset.getY() + offset_y + 1, enemy_pos.getZ() + offset.getZ() * 3);
        if (this.checkBlock(this.getBlock(pre_crystal).getBlock()) && this.isAir(this.getBlock(pre_crystal.add(0, 1, 0)).getBlock()) && this.isAir(this.getBlock(pre_crystal.add(0, 2, 0)).getBlock())) {
            v_crystal = true;
        }
        if (this.isAir(this.getBlock(pre_piston).getBlock())) {
            v_piston = true;
        }
        if (this.isTorch) {
            final BlockPos[] t_offset = { new BlockPos(1, 0, 0), new BlockPos(-1, 0, 0), new BlockPos(0, 0, 1), new BlockPos(0, 0, -1) };
            final List<BlockPos> pre_redstone_place = new ArrayList<BlockPos>();
            final BlockPos tmp = null;
            for (int o = 0; o < t_offset.length; ++o) {
                final BlockPos pre_redstone_offset = pre_piston.add((Vec3i)t_offset[o]);
                if (this.isAir(this.getBlock(pre_redstone_offset).getBlock()) && (pre_redstone_offset.getX() != mypos.getX() || (pre_redstone_offset.getY() != mypos.getY() && pre_redstone_offset.getY() != mypos.getY() + 1) || pre_redstone_offset.getZ() != mypos.getZ()) && pre_crystal.getDistance(pre_redstone_offset.getX(), pre_redstone_offset.getY(), pre_redstone_offset.getZ()) > 1.0) {
                    pre_redstone_place.add(pre_redstone_offset);
                }
            }
            if (this.getBlock(new BlockPos(enemy_pos.getX() + offset.getX() * 3, enemy_pos.getY() + offset.getY() + offset_y + 2, enemy_pos.getZ() + offset.getZ() * 3)).getBlock() == Blocks.AIR && this.getBlock(new BlockPos(enemy_pos.getX() + offset.getX() * 3, enemy_pos.getY() + offset.getY() + offset_y + 1, enemy_pos.getZ() + offset.getZ() * 3)).getBlock() == Blocks.OBSIDIAN) {
                pre_redstone_place.add(new BlockPos(enemy_pos.getX() + offset.getX() * 3, enemy_pos.getY() + offset.getY() + offset_y + 2, enemy_pos.getZ() + offset.getZ() * 3));
            }
            if (this.getBlock(new BlockPos(enemy_pos.getX() + offset.getX() * 2, enemy_pos.getY() + offset.getY() + offset_y + 2, enemy_pos.getZ() + offset.getZ() * 2)).getBlock() == Blocks.AIR && this.getBlock(new BlockPos(enemy_pos.getX() + offset.getX() * 3, enemy_pos.getY() + offset.getY() + offset_y + 2, enemy_pos.getZ() + offset.getZ() * 3)).getBlock() == Blocks.OBSIDIAN) {
                pre_redstone_place.add(new BlockPos(enemy_pos.getX() + offset.getX() * 2, enemy_pos.getY() + offset.getY() + offset_y + 2, enemy_pos.getZ() + offset.getZ() * 2));
            }
            pre_redstone = pre_redstone_place.stream().min(Comparator.comparing(b -> PistonCrystal.mc.player.getDistance((double)b.getX(), (double)b.getY(), (double)b.getZ()))).orElse(null);
            if (pre_redstone != null) {
                v_redstone = true;
            }
        }
        else if (this.isAir(this.getBlock(pre_redstone).getBlock())) {
            v_redstone = true;
        }
        if (pre_piston.getX() == mypos.getX() && (pre_piston.getY() == mypos.getY() || pre_piston.getY() == mypos.getY() + 1) && pre_piston.getZ() == mypos.getZ()) {
            v_piston = false;
        }
        if (pre_redstone != null && pre_redstone.getX() == mypos.getX() && (pre_redstone.getY() == mypos.getY() || pre_redstone.getY() == mypos.getY() + 1) && pre_redstone.getZ() == mypos.getZ()) {
            v_redstone = false;
        }
        if (mypos.getDistance(pre_piston.getX(), mypos.getY(), pre_piston.getZ()) < 3.1 && pre_piston.getY() > mypos.getY() + 1) {
            v_piston = false;
        }
        if (this.getBlock(pre_crystal.add(0, 1, 0)).getBlock() == Blocks.PISTON_HEAD && (this.getBlock(pre_redstone).getBlock() == Blocks.REDSTONE_BLOCK || this.getBlock(pre_redstone).getBlock() == Blocks.REDSTONE_TORCH)) {
            v_piston = true;
            v_crystal = true;
            v_redstone = true;
        }
        if (v_crystal && v_piston && v_redstone) {
            this.c_crystal.add(pre_crystal);
            this.c_piston.add(pre_piston);
            this.c_redStone.add(pre_redstone);
        }
    }
    
    private void Init() {
        this.target = null;
        this.b_crystal = null;
        this.b_piston = null;
        this.b_redStone = null;
        this.c_crystal = new ArrayList<BlockPos>();
        this.c_piston = new ArrayList<BlockPos>();
        this.c_redStone = new ArrayList<BlockPos>();
        this.p_crystal = false;
        this.p_piston = false;
        this.p_redstone = false;
        this.u_crystal = false;
        this.attempts = 0;
        this.r_redstone = false;
        this.b_stage = 0;
        this.trapprogress = 0;
        this.timer = 0;
        this.crystalId = 0;
        this.debug_stage = -1;
    }
    
    private float getRange(final BlockPos t) {
        return (float)PistonCrystal.mc.player.getDistance((double)t.getX(), (double)t.getY(), (double)t.getZ());
    }
    
    private boolean isAir(final Block b) {
        return b == Blocks.AIR;
    }
    
    private boolean checkBlock(final Block b) {
        return b == Blocks.OBSIDIAN || b == Blocks.BEDROCK;
    }
    
    private IBlockState getBlock(final BlockPos o) {
        return PistonCrystal.mc.world.getBlockState(o);
    }
    
    private double f(final double v) {
        return Math.floor(v);
    }
    
    private Block getRedStoneBlock() {
        return this.isTorch ? Blocks.REDSTONE_TORCH : Blocks.REDSTONE_BLOCK;
    }
    
    private enum types
    {
        Nearest, 
        Looking;
    }
    
    private enum arm
    {
        MainHand, 
        OffHand, 
        None;
    }
    
    private enum redstone
    {
        Block, 
        Torch, 
        Both;
    }
    
    private enum mode
    {
        Smart, 
        Force, 
        None;
    }
}
