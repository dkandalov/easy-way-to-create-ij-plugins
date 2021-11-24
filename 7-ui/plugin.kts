import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.layout.GrowPolicy.MEDIUM_TEXT
import com.intellij.ui.layout.panel
import javax.swing.DefaultComboBoxModel
import liveplugin.registerProjectToolWindow
import liveplugin.show

data class Config(
    var foo: Boolean = true,
    var bar: String = "barrrr",
    var quix: Int = 1
)

val config = Config()

// https://plugins.jetbrains.com/docs/intellij/kotlin-ui-dsl.html

val panel: DialogPanel = panel {
    row {
        label("Sample text")
        link("foo") { show("Clicked foo") }
        browserLink("aaaaaaaa", "https://foo.com")
    }
    row {
        label("Sample text")
        checkBox("fooo", config::foo)
        textField(config::bar, columns = 20).growPolicy(MEDIUM_TEXT)
    }
    row {
        label("Sample text")
        comboBox(DefaultComboBoxModel(arrayOf(1, 2, 3)), config::quix)
    }
}
val mainPanel = panel {
    row {
        component(panel)
    }
    row {
        button("Submit") {
            panel.apply()
            show("Config: $config")
        }
    }
}
registerProjectToolWindow(
    "My Toolwindow",
    component = mainPanel
).show()