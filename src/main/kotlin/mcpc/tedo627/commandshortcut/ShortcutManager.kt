package mcpc.tedo627.commandshortcut

class ShortcutManager {

    private val config = CustomConfig()
    private val list: MutableList<Shortcut> = config.load().toMutableList()

    val all: List<Shortcut>
        get() = list.toList()

    fun add() {
        list.add(Shortcut("", "key.keyboard.unknown", "key.keyboard.unknown", "key.keyboard.unknown", true))
    }

    fun remove(shortcut: Shortcut) {
        list.remove(shortcut)
    }

    fun save() {
        config.save(list)
    }
}