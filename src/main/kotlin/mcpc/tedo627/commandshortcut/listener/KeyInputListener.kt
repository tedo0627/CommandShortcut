package mcpc.tedo627.commandshortcut.listener

import mcpc.tedo627.commandshortcut.ShortcutManager
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.InputEvent
import net.minecraftforge.eventbus.api.SubscribeEvent

class KeyInputListener(private val manager: ShortcutManager) {

    private val pressKeys = mutableListOf(-1)

    @SubscribeEvent
    fun onKeyInput(event: InputEvent.KeyInputEvent) {
        press(event.key, event.action)
    }

    @SubscribeEvent
    fun onMouseInput(event: InputEvent.MouseInputEvent) {
        press(event.button, event.action)
    }

    private fun press(key: Int, action: Int) {
        if (Minecraft.getInstance().currentScreen != null) return

        if (action == 0) pressKeys.remove(key)
        if (action != 1) return

        pressKeys.add(key)
        val list = manager.all
        for (shortcut in list) {
            if (!shortcut.enabled) continue
            if (shortcut.command == "") continue
            if (shortcut.key1 == "key.keyboard.unknown") continue

            shortcut.pressKey(key, pressKeys)
        }
    }
}