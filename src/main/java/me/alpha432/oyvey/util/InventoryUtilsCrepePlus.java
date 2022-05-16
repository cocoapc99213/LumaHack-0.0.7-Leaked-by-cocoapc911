//Deobfuscated with https://github.com/SimplyProgrammer/Minecraft-Deobfuscator3000 using mappings "E:\desktop\Minecraft-Deobfuscator3000-1.2.3\Minecraft-Deobfuscator3000-1.2.3\1.12 stable mappings"!

// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey.util;

import java.util.Comparator;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;

public class InventoryUtilsCrepePlus
{
    protected static Minecraft mc;
    
    private static int lambda$getPlaceableItem$0(final Object o, final Object o1) {
        return 0;
    }
    
    public static int pickItem(final int n) {
        final ArrayList<Object> arrayList = new ArrayList<Object>();
        for (int i = 0; i < 9; ++i) {
            if (Item.getIdFromItem(((ItemStack)InventoryUtilsCrepePlus.mc.player.inventory.mainInventory.get(i)).getItem()) == n) {
                arrayList.add(InventoryUtilsCrepePlus.mc.player.inventory.mainInventory.get(i));
            }
        }
        if (arrayList.size() >= 1) {
            return InventoryUtilsCrepePlus.mc.player.inventory.mainInventory.indexOf(arrayList.get(0));
        }
        return -1;
    }
    
    public static void setSlot(final int n) {
        if (n > 8 || n < 0) {
            return;
        }
        InventoryUtilsCrepePlus.mc.player.inventory.currentItem = n;
    }
    
    public static int getPlaceableItem() {
        final ArrayList<Object> arrayList = new ArrayList<Object>();
        for (int i = 0; i < 9; ++i) {
            if (((ItemStack)InventoryUtilsCrepePlus.mc.player.inventory.mainInventory.get(i)).getItem() instanceof ItemBlock) {
                arrayList.add(InventoryUtilsCrepePlus.mc.player.inventory.mainInventory.get(i));
            }
        }
        arrayList.sort(InventoryUtilsCrepePlus::lambda$getPlaceableItem$0);
        if (arrayList.size() >= 1) {
            return InventoryUtilsCrepePlus.mc.player.inventory.mainInventory.indexOf(arrayList.get(0));
        }
        return -1;
    }
    
    private static int lambda$getPlaceableItem$0(final ItemStack itemStack, final ItemStack itemStack2) {
        return itemStack2.getCount() - itemStack.getCount();
    }
    
    public static int getSlot() {
        return InventoryUtilsCrepePlus.mc.player.inventory.currentItem;
    }
    
    static {
        InventoryUtilsCrepePlus.mc = Minecraft.getMinecraft();
    }
}
