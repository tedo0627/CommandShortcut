package mcpc.tedo627.commandshortcut.listener

import com.mojang.brigadier.Command
import mcpc.tedo627.commandshortcut.ShortcutManager
import mcpc.tedo627.commandshortcut.gui.ShortcutSettingGui
import net.minecraft.client.Minecraft
import net.minecraft.command.Commands
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.server.FMLServerStartingEvent

class RegisterCommandListener(private val manager: ShortcutManager) {

    private var openScreen = false

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
}