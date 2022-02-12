package mcpc.tedo627.commandshortcut.listener

import com.mojang.brigadier.Command
import mcpc.tedo627.commandshortcut.ShortcutManager
import mcpc.tedo627.commandshortcut.gui.ShortcutSettingGui
import net.minecraft.client.Minecraft
import net.minecraft.client.settings.KeyBinding
import net.minecraft.command.Commands
import net.minecraftforge.client.event.InputEvent
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.event.server.FMLServerStartingEvent

class OpenSettingGuiListener(private val manager: ShortcutManager) {

    private var openScreen = false
    private val key = KeyBinding("open shortcut settings key", -1, "CommandShortcut")

    init {
        ClientRegistry.registerKeyBinding(key)
    }

    @SubscribeEvent
    fun onFMLServerStarting(event: FMLServerStartingEvent) {
        val builder = Commands.literal("shortcut").executes { context ->
            openScreen = true
            Command.SINGLE_SUCCESS
        }
        event.commandDispatcher.register(builder)
    }

    @SubscribeEvent
    fun onClientTick(event: TickEvent.ClientTickEvent) {
        if (event.phase == TickEvent.Phase.END) return
        if (!openScreen) return

        openScreen = false
        Minecraft.getInstance().displayGuiScreen(ShortcutSettingGui(manager))
    }

    @SubscribeEvent
    fun onKeyInput(event: InputEvent.KeyInputEvent) {
        if (event.action != 1) return
        if (event.key != key.key.keyCode) return
        if (Minecraft.getInstance().currentScreen != null) return

        Minecraft.getInstance().displayGuiScreen(ShortcutSettingGui(manager))
    }
}