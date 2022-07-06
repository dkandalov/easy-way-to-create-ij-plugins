@file:Suppress("DialogTitleCapitalization")

import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.dsl.builder.bindItemNullable
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import liveplugin.registerProjectToolWindow
import liveplugin.show

data class PomodoroSettings(
    var duration: Int? = 25,
    var message: String = "Pomodoro is finished!",
    var longBreak: Boolean = false
)

val settings = PomodoroSettings()

val panel: DialogPanel = panel {
    row("Duration:") {
        comboBox(listOf(25, 30, 35, 40))
            .bindItemNullable(settings::duration)
        label("minute(s)")
    }
    row("Message:") {
        textField().bindText(settings::message)
    }
    row("Long break:") {
        checkBox("").bindSelected(settings::longBreak)
    }
}
val mainPanel = panel {
    row {
        cell(panel)
    }
    row {
        button("Save") {
            panel.apply()
            show(settings)
        }
        browserLink(
            text = "See docs for details",
            url = "https://plugins.jetbrains.com/docs/intellij/kotlin-ui-dsl-version-2.html"
        )
    }
}

registerProjectToolWindow(
    toolWindowId = "Pomodoro Settings",
    component = mainPanel
).show()