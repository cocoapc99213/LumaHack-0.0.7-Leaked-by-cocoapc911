//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.features.modules.combat;

import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.world.World;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketPlayer;
import me.alpha432.oyvey.features.command.Command;
import java.util.HashMap;
import java.util.Map;
import me.alpha432.oyvey.features.setting.Setting;
import me.alpha432.oyvey.features.modules.Module;

public class AutoXP extends Module
{
    private int Ticktimer;
    private int ArmorTimer;
    private boolean isDone;
    private int fixprogress;
    private final Setting<Integer> Tick;
    private final Setting<Integer> Throw;
    private final Setting<Integer> ArmorDelay;
    private final Setting<Integer> FixTo;
    private final Setting<Boolean> FastMode;
    private final Setting<Boolean> Packet;
    private Map<Float, Float> Armor;
    
    public AutoXP() {
        super("AutoXP", "Automatically repairs your armors", Category.COMBAT, true, false, false);
        this.Ticktimer = 0;
        this.ArmorTimer = 0;
        this.isDone = false;
        this.fixprogress = 0;
        this.Tick = (Setting<Integer>)this.register(new Setting("XPDelay", (T)0, (T)0, (T)10));
        this.Throw = (Setting<Integer>)this.register(new Setting("Throw", (T)1, (T)1, (T)5));
        this.ArmorDelay = (Setting<Integer>)this.register(new Setting("ArmorDelay", (T)1, (T)0, (T)10));
        this.FixTo = (Setting<Integer>)this.register(new Setting("FixTo", (T)90, (T)10, (T)100));
        this.FastMode = (Setting<Boolean>)this.register(new Setting("FastMode", (T)true));
        this.Packet = (Setting<Boolean>)this.register(new Setting("Packet", (T)true));
    }
    
    @Override
    public void onEnable() {
        if (AutoXP.mc.player == null) {
            return;
        }
        this.Armor = new HashMap<Float, Float>();
        if (this.findXP() == -1) {
            Command.sendMessage("You don't have XP!");
            this.toggle();
        }
        this.Ticktimer = 0;
        this.isDone = false;
        this.fixprogress = 0;
    }
    
    @Override
    public void onDisable() {
    }
    
    @Override
    public void onUpdate() {
        try {
            AutoXP.mc.player.connection.sendPacket((Packet)new CPacketPlayer.Rotation(0.0f, 90.0f, true));
            if (this.Ticktimer < this.Tick.getValue()) {
                ++this.Ticktimer;
            }
            else if (!this.isDone) {
                if (!this.setNeedFixArmor()) {
                    Command.sendMessage("Done! disabling...");
                    this.isDone = true;
                    return;
                }
                final int slot = this.findXP();
                if (slot == -1) {
                    Command.sendMessage("Missing XP! disabling...");
                    this.isDone = true;
                    return;
                }
                AutoXP.mc.player.inventory.currentItem = slot;
                for (int i = 0; i < this.Throw.getValue(); ++i) {
                    if (this.Packet.getValue()) {
                        AutoXP.mc.player.connection.sendPacket((Packet)new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    }
                    else {
                        AutoXP.mc.playerController.processRightClick((EntityPlayer)AutoXP.mc.player, (World)AutoXP.mc.world, EnumHand.MAIN_HAND);
                    }
                }
                this.Ticktimer = 0;
            }
            if (this.isDone) {
                if (this.ArmorTimer < this.ArmorDelay.getValue()) {
                    ++this.ArmorTimer;
                    return;
                }
                this.ArmorTimer = 0;
                final float armor = (float)this.Armor.keySet().stream().toArray()[this.fixprogress];
                final float temp = (float)this.Armor.values().stream().toArray()[this.fixprogress];
                AutoXP.mc.playerController.windowClick(AutoXP.mc.player.inventoryContainer.windowId, (int)temp, 0, ClickType.PICKUP, (EntityPlayer)AutoXP.mc.player);
                AutoXP.mc.playerController.windowClick(AutoXP.mc.player.inventoryContainer.windowId, (int)armor, 0, ClickType.PICKUP, (EntityPlayer)AutoXP.mc.player);
                if (this.fixprogress == this.Armor.size()) {
                    this.disable();
                }
                else {
                    ++this.fixprogress;
                }
            }
        }
        catch (Exception ex) {
            Command.sendMessage("Error : " + ex.toString());
            this.disable();
        }
    }
    
    public int findXP() {
        for (int i = 0; i < 9; ++i) {
            final ItemStack item = AutoXP.mc.player.inventory.getStackInSlot(i);
            if (item.getItem() == Items.EXPERIENCE_BOTTLE) {
                return i;
            }
        }
        return -1;
    }
    
    public boolean setNeedFixArmor() {
        boolean result = false;
        for (int i = 0; i < AutoXP.mc.player.inventoryContainer.getInventory().size(); ++i) {
            final ItemStack armor = (ItemStack)AutoXP.mc.player.inventoryContainer.getInventory().get(i);
            if (armor.getItem() instanceof ItemArmor && i > 4 && i < 9) {
                final float p = (armor.getMaxDamage() - armor.getItemDamage()) / armor.getMaxDamage() * 100.0f;
                if (p < this.FixTo.getValue()) {
                    result = true;
                }
                else if (this.FastMode.getValue()) {
                    final int slot = this.getfreeslot();
                    AutoXP.mc.playerController.windowClick(AutoXP.mc.player.inventoryContainer.windowId, i, 0, ClickType.PICKUP, (EntityPlayer)AutoXP.mc.player);
                    AutoXP.mc.playerController.windowClick(AutoXP.mc.player.inventoryContainer.windowId, slot, 0, ClickType.PICKUP, (EntityPlayer)AutoXP.mc.player);
                    this.Armor.put(Float.valueOf(i), Float.valueOf(slot));
                }
            }
        }
        return result;
    }
    
    public int getfreeslot() {
        for (int i = 0; i < AutoXP.mc.player.inventoryContainer.getInventory().size(); ++i) {
            if (i > 8 && i < 36 && ((ItemStack)AutoXP.mc.player.inventoryContainer.getInventory().get(i)).getItem() == Items.AIR) {
                return i;
            }
        }
        return -1;
    }
}
