package mcpc.tedo627.commandshortcut.gui

import mcpc.tedo627.commandshortcut.Shortcut
import mcpc.tedo627.commandshortcut.ShortcutManager
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.TextFieldWidget
import net.minecraft.client.gui.widget.Widget
import net.minecraft.client.gui.widget.button.Button
import net.minecraft.client.resources.I18n
import net.minecraft.client.util.InputMappings
import net.minecraft.util.text.StringTextComponent

class ShortcutSettingGui(private val manager: ShortcutManager) : Screen(StringTextComponent("shortcut settings gui")) {

    companion object {
        const val VISIBLE_COUNT = 6
    }

    private var scrollStep = 0
    private val scrolls = mutableListOf<ScrollItem>()

    override fun init() {
        scrolls.clear()

        val list = manager.all
        for (shortcut in list) addScroll(ScrollItem(this, font, width, height, manager, shortcut))

        addButton(Button(width / 2 - 20, height / 5 + 200, 40, 20, "add") {
            manager.add()
            this@ShortcutSettingGui.scrollStep = this@ShortcutSettingGui.scrolls.size + 1 - VISIBLE_COUNT
            init()
        })
    }

    private fun addScroll(item: ScrollItem) {
        val buttons = item.buttons
        for (i in 0 until buttons.size) addButton(buttons[i])
        scrolls.add(item)
    }

    override fun render(arg1: Int, arg2: Int, arg3: Float) {
        renderBackground()

        font.drawString("command", (width / 10).toFloat(), 50f, 0xFFFFFF)
        font.drawString("key1", (width / 10 + 120).toFloat(), 50f, 0xFFFFFF)
        font.drawString("key2", (width / 10 + 240).toFloat(), 50f, 0xFFFFFF)
        font.drawString("key3", (width / 10 + 360).toFloat(), 50f, 0xFFFFFF)
        font.drawString("enable", (width / 10 + 485).toFloat(), 50f, 0xFFFFFF)
        val size = scrolls.size
        for (i in 0 until size) {
            if (VISIBLE_COUNT >= size) {
                scrolls[i].render(i, i)
                scrolls[i].visible()
                continue
            }

            if (scrollStep <= i && i < scrollStep + VISIBLE_COUNT) {
                scrolls[i].render(i - scrollStep, i)
                scrolls[i].visible()
                continue
            }

            scrolls[i].invisible()
        }
        super.render(arg1, arg2, arg3)
    }

    override fun mouseScrolled(arg1: Double, arg2: Double, arg3: Double): Boolean {
        scrollStep -= arg3.toInt()
        if (scrollStep > scrolls.size - VISIBLE_COUNT) scrollStep = scrolls.size - VISIBLE_COUNT
        if (scrollStep < 0) scrollStep = 0
        return super.mouseScrolled(arg1, arg2, arg3)
    }

    override fun onClose() {
        manager.save()
        super.onClose()
    }

    class ScrollItem(
        private val screen: Screen,
        private val font: FontRenderer,
        private val width: Int,
        private val height: Int,
        private val manager: ShortcutManager,
        private val shortcut: Shortcut
    ) {

        val buttons = mutableListOf<Widget>()

        init {
            buttons.add(TextFieldWidget(font, width / 10, height / 5, 100, 20, "").run {
                text = shortcut.command
                setValidator {
                    shortcut.command = it
                    true
                }
                this
            })
            buttons.add(Button(width / 10 + 120, height / 5, 100, 20, getKeyName(shortcut.key1)) {
                Minecraft.getInstance().displayGuiScreen(SelectKeyGui(screen, shortcut, 1))
            })
            buttons.add(Button(width / 10 + 240, height / 5, 100, 20, getKeyName(shortcut.key2)) {
                Minecraft.getInstance().displayGuiScreen(SelectKeyGui(screen, shortcut, 2))
            })
            buttons.add(Button(width / 10 + 360, height / 5, 100, 20, getKeyName(shortcut.key3)) {
                Minecraft.getInstance().displayGuiScreen(SelectKeyGui(screen, shortcut, 3))
            })
            buttons.add(Button(width / 10 + 480, height / 5, 50, 20, if (shortcut.enabled) "§aenabled" else "§cdisabled") {
                shortcut.enabled = !shortcut.enabled
                it.message = if (shortcut.enabled) "§aenabled" else "§cdisabled"
            })
            buttons.add(Button(width / 10 + 540, height / 5, 20, 20, "§4x") {
                manager.remove(shortcut)
                Minecraft.getInstance().displayGuiScreen(screen)
            })
        }

        fun visible() {
            for (i in 0 until buttons.size) {
                buttons[i].visible = true
            }
        }

        fun invisible() {
            for (i in 0 until buttons.size) {
                buttons[i].visible = false
            }
        }

        fun render(step: Int, index: Int) {
            for (i in 0 until buttons.size) {
                val button = buttons[i]
                button.y = (height / 5) + (30 * step)
            }
            font.drawString("${index + 1}:", (width / 10 - 20).toFloat(), ((height / 5) + (30 * step) + 6).toFloat(), 0xFFFFFF)
            font.drawString("+", (width / 10 + 230 - 2).toFloat(), ((height / 5) + (30 * step) + 6).toFloat(), 0xFFFFFF)
            font.drawString("+", (width / 10 + 350 - 2).toFloat(), ((height / 5) + (30 * step) + 6).toFloat(), 0xFFFFFF)
        }

        private fun getKeyName(name: String): String {
            val input = InputMappings.getInputByName(name)
            val keyName = InputMappings.getKeynameFromKeycode(input.keyCode)
            if (keyName != null) return keyName
            if (I18n.hasKey(name)) return I18n.format(name)

            return I18n.format("key.mouse", name.removePrefix("key.mouse."))
        }
    }
}