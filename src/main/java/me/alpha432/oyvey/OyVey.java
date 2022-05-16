// 
// Decompiled by Procyon v0.5.36
// 

package me.alpha432.oyvey;

import org.apache.logging.log4j.LogManager;
import org.lwjgl.opengl.Display;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import me.alpha432.oyvey.manager.SafetyManager;
import me.alpha432.oyvey.manager.PlayerManager;
import me.alpha432.oyvey.manager.TextManager;
import me.alpha432.oyvey.manager.EventManager;
import me.alpha432.oyvey.manager.ServerManager;
import me.alpha432.oyvey.manager.ConfigManager;
import me.alpha432.oyvey.manager.FileManager;
import me.alpha432.oyvey.manager.ReloadManager;
import me.alpha432.oyvey.manager.SpeedManager;
import me.alpha432.oyvey.manager.PositionManager;
import me.alpha432.oyvey.manager.RotationManager;
import me.alpha432.oyvey.manager.PotionManager;
import me.alpha432.oyvey.manager.InventoryManager;
import me.alpha432.oyvey.manager.HoleManager;
import me.alpha432.oyvey.manager.ColorManager;
import me.alpha432.oyvey.manager.PacketManager;
import me.alpha432.oyvey.manager.ModuleManager;
import me.alpha432.oyvey.manager.FriendManager;
import me.alpha432.oyvey.manager.CommandManager;
import org.apache.logging.log4j.Logger;
import net.minecraftforge.fml.common.Mod;

@Mod(modid = "luma", name = "LUMA", version = "0.0.7")
public class OyVey
{
    public static String ClientName;
    public static String ClientVersion;
    public static final String MODID = "luma";
    public static final String MODNAME;
    public static final String MODVER;
    public static final Logger LOGGER;
    public static CommandManager commandManager;
    public static FriendManager friendManager;
    public static ModuleManager moduleManager;
    public static PacketManager packetManager;
    public static ColorManager colorManager;
    public static HoleManager holeManager;
    public static InventoryManager inventoryManager;
    public static PotionManager potionManager;
    public static RotationManager rotationManager;
    public static PositionManager positionManager;
    public static SpeedManager speedManager;
    public static ReloadManager reloadManager;
    public static FileManager fileManager;
    public static ConfigManager configManager;
    public static ServerManager serverManager;
    public static EventManager eventManager;
    public static TextManager textManager;
    public static PlayerManager playerManager;
    public static SafetyManager safetyManager;
    @Mod.Instance
    public static OyVey INSTANCE;
    private static boolean unloaded;
    
    public static void load() {
        OyVey.LOGGER.info("\n\nLoading OyVey by Alpha432");
        OyVey.unloaded = false;
        if (OyVey.reloadManager != null) {
            OyVey.reloadManager.unload();
            OyVey.reloadManager = null;
        }
        OyVey.textManager = new TextManager();
        OyVey.commandManager = new CommandManager();
        OyVey.friendManager = new FriendManager();
        OyVey.moduleManager = new ModuleManager();
        OyVey.rotationManager = new RotationManager();
        OyVey.packetManager = new PacketManager();
        OyVey.eventManager = new EventManager();
        OyVey.speedManager = new SpeedManager();
        OyVey.potionManager = new PotionManager();
        OyVey.inventoryManager = new InventoryManager();
        OyVey.serverManager = new ServerManager();
        OyVey.fileManager = new FileManager();
        OyVey.colorManager = new ColorManager();
        OyVey.positionManager = new PositionManager();
        OyVey.configManager = new ConfigManager();
        OyVey.holeManager = new HoleManager();
        OyVey.playerManager = new PlayerManager();
        OyVey.safetyManager = new SafetyManager();
        OyVey.LOGGER.info("Managers loaded.");
        OyVey.moduleManager.init();
        OyVey.LOGGER.info("Modules loaded.");
        OyVey.configManager.init();
        OyVey.eventManager.init();
        OyVey.LOGGER.info("EventManager loaded.");
        OyVey.textManager.init(true);
        OyVey.moduleManager.onLoad();
        OyVey.LOGGER.info("OyVey successfully loaded!\n");
        RPC.startRPC();
    }
    
    public static void unload(final boolean unload) {
        OyVey.LOGGER.info("\n\nUnloading OyVey by Alpha432");
        if (unload) {
            (OyVey.reloadManager = new ReloadManager()).init((OyVey.commandManager != null) ? OyVey.commandManager.getPrefix() : ".");
        }
        onUnload();
        OyVey.eventManager = null;
        OyVey.friendManager = null;
        OyVey.speedManager = null;
        OyVey.holeManager = null;
        OyVey.positionManager = null;
        OyVey.rotationManager = null;
        OyVey.configManager = null;
        OyVey.commandManager = null;
        OyVey.colorManager = null;
        OyVey.serverManager = null;
        OyVey.fileManager = null;
        OyVey.potionManager = null;
        OyVey.inventoryManager = null;
        OyVey.moduleManager = null;
        OyVey.textManager = null;
        OyVey.LOGGER.info("OyVey unloaded!\n");
    }
    
    public static void reload() {
        unload(false);
        load();
    }
    
    public static void onUnload() {
        if (!OyVey.unloaded) {
            OyVey.eventManager.onUnload();
            OyVey.moduleManager.onUnload();
            OyVey.configManager.saveConfig(OyVey.configManager.config.replaceFirst("oyvey/", ""));
            OyVey.moduleManager.onUnloadPost();
            OyVey.unloaded = true;
        }
    }
    
    @Mod.EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        OyVey.LOGGER.info("I am gona gas you kike - Alpha432");
    }
    
    @Mod.EventHandler
    public void init(final FMLInitializationEvent event) {
        Display.setTitle(OyVey.MODNAME);
        load();
        RPC.startRPC();
    }
    
    public PlayerManager getPlayerManager() {
        return OyVey.playerManager;
    }
    
    static {
        OyVey.ClientName = "LUMAClient";
        OyVey.ClientVersion = "0.0.7";
        MODNAME = OyVey.ClientName;
        MODVER = OyVey.ClientVersion;
        LOGGER = LogManager.getLogger("LUM4");
        OyVey.unloaded = false;
    }
}
