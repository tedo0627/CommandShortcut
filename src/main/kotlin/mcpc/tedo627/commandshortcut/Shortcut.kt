package mcpc.tedo627.commandshortcut

import net.minecraft.client.Minecraft
import net.minecraft.client.util.InputMappings

class Shortcut(
    var command: String,
    var key1: String,
    var key2: String,
    var key3: String,
    var enabled: Boolean
) {
    fun pressKey(key: Int, pressedKeys: List<Int>) {
        val code1 = InputMappings.getInputByName(key1).keyCode
        val code2 = InputMappings.getInputByName(key2).keyCode
        val code3 = InputMappings.getInputByName(key3).keyCode

        val pressed1 = code1 == key && pressedKeys.contains(code2) && pressedKeys.contains(code3)
        val pressed2 = code2 == key && pressedKeys.contains(code1) && pressedKeys.contains(code3)
        val pressed3 = code3 == key && pressedKeys.contains(code1) && pressedKeys.contains(code2)

        if (pressed1 || pressed2 || pressed3) {
            Minecraft.getInstance().player?.sendChatMessage("/$command")
        }
    }
}