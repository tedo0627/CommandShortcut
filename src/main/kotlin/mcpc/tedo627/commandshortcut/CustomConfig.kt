package mcpc.tedo627.commandshortcut

import com.electronwill.nightconfig.core.Config
import com.electronwill.nightconfig.core.file.CommentedFileConfig
import com.electronwill.nightconfig.core.io.WritingMode
import java.io.File

class CustomConfig {

    private val config = CommentedFileConfig
        .builder(File("config/CommandShortcut.toml"))
        .sync()
        .autosave()
        .writingMode(WritingMode.REPLACE)
        .build()

    fun load(): List<Shortcut> {
        config.load()

        val list = config.get<List<Config>>("shortcut")?.toMutableList() ?: mutableListOf()
        val result = mutableListOf<Shortcut>()
        for (config in list) {
            val command = config.get<String>("command") ?: ""
            val key1 = config.get<String>("key1") ?: "key.keyboard.unknown"
            val key2 = config.get<String>("key2") ?: "key.keyboard.unknown"
            val key3 = config.get<String>("key3") ?: "key.keyboard.unknown"
            val enabled = config.get<Boolean>("enabled") ?: true
            result.add(Shortcut(command, key1, key2, key3, enabled))
        }

        return result
    }

    fun save(shortcuts: List<Shortcut>) {
        val list = mutableListOf<Config>()
        for (shortcut in shortcuts) {
            val map = mutableMapOf<String, Any>()
            map["command"] = shortcut.command
            map["key1"] = shortcut.key1
            map["key2"] = shortcut.key2
            map["key3"] = shortcut.key3
            map["enabled"] = shortcut.enabled

            list.add(Config.wrap(map, config.configFormat()))
        }

        config.set<Any?>("shortcut", list)
        config.createSubConfig()
        config.save()
    }
}