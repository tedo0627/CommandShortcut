package mcpc.tedo627.commandshortcut

import mcpc.tedo627.commandshortcut.listener.KeyInputListener
import mcpc.tedo627.commandshortcut.listener.RegisterCommandListener
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext


@Mod(CommandShortcut.MOD_ID)
class CommandShortcut {

    companion object {
        const val MOD_ID = "commandshortcut"
    }

    lateinit var shortcut: ShortcutManager
        private set

    init {
        FMLJavaModLoadingContext.get().modEventBus.addListener { event: FMLLoadCompleteEvent -> complete(event) }
    }

    private fun complete(event: FMLLoadCompleteEvent) {
        shortcut = ShortcutManager()
        MinecraftForge.EVENT_BUS.register(KeyInputListener(shortcut))
        MinecraftForge.EVENT_BUS.register(RegisterCommandListener(shortcut))
    }
}