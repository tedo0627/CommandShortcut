package mcpc.tedo627.commandshortcut.gui

import mcpc.tedo627.commandshortcut.Shortcut
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.button.Button
import net.minecraft.client.util.InputMappings
import net.minecraft.util.text.StringTextComponent

class SelectKeyGui(
    private val beforeScreen: Screen,
    private val shortcut: Shortcut,
    private val keyType: Int
) : Screen(StringTextComponent("select key gui")) {

    override fun init() {
        addButton(Button(width / 4 * 3, height / 4, 40, 20, "reset") {
            val name = "key.keyboard.unknown"
            when (keyType) {
                1 -> shortcut.key1 = name
                2 -> shortcut.key2 = name
                3 -> shortcut.key3 = name
            }
            onClose()
        })
    }

    override fun render(p_render_1_: Int, p_render_2_: Int, p_render_3_: Float) {
        renderBackground()
        font.drawString("Press the key to register", (width / 2 - 50).toFloat(), (height / 2 - 50).toFloat(), 0xFFFFFF)
        font.drawString("Press esc to return", (width / 2 - 50).toFloat(), (height / 2 - 30).toFloat(), 0xFFFFFF)
        super.render(p_render_1_, p_render_2_, p_render_3_)
    }

    override fun keyPressed(key: Int, p_keyPressed_2_: Int, p_keyPressed_3_: Int): Boolean {
        if (key == 256) {
            onClose()
            return true
        }

        val name = InputMappings.Type.KEYSYM.getOrMakeInput(key).translationKey
        when (keyType) {
            1 -> shortcut.key1 = name
            2 -> shortcut.key2 = name
            3 -> shortcut.key3 = name
        }
        onClose()
        return true
    }

    override fun mouseClicked(p_mouseClicked_1_: Double, p_mouseClicked_3_: Double, mouse: Int): Boolean {
        if (super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, mouse)) return true

        val name = InputMappings.Type.MOUSE.getOrMakeInput(mouse).translationKey
        when (keyType) {
            1 -> shortcut.key1 = name
            2 -> shortcut.key2 = name
            3 -> shortcut.key3 = name
        }
        onClose()
        return true
    }

    override fun onClose() {
        this.minecraft?.displayGuiScreen(beforeScreen)
    }
}