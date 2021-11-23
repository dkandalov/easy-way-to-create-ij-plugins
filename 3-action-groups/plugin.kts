import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.actionSystem.Separator
import com.intellij.openapi.fileEditor.impl.HTMLEditorProvider
import com.intellij.openapi.project.Project
import liveplugin.*
import liveplugin.PluginUtil.openInBrowser
import org.http4k.core.Method.GET
import org.http4k.core.Request

// add-to-classpath $PLUGIN_PATH/libs/*

val action1 = registerAction("Action 1") { show("1 ${it.project}") }
val action2 = registerAction("Action 2") { show("2") }
registerAction(
    id = "Some Actions",
    actionGroupId = ActionGroupIds.EditorPopupMenu,
    action = DefaultActionGroup(action1, action2).also { it.isPopup = true }
)

registerAction("Show Action Group") { event ->
    PopupActionGroup(name = "Some Actions",
        AnAction("LivePlugin on GitHub") {
            openInBrowser("https://github.com/dkandalov/live-plugin")
        },
        AnAction("LivePlugin on GitHub 2") {
            it.project?.openInIdeBrowser("https://github.com/dkandalov/live-plugin")
        },
        AnAction("Open plugin.kts") {
            it.project?.openInEditor("$pluginPath/plugin.kts")
        },
        AnAction("Run a Script") {
            runBackgroundTask(taskTitle = "Running shell script", task = {
                show(runShellScript("""
                    '$pluginPath/hello.sh'
                    sleep 5
                    """.trimIndent()
                ).stdout)
            })
        },
        AnAction("Http Request") {
            val client = org.http4k.client.OkHttp()
            val response = client(Request(GET, "https://duckduckgo.com"))
            show(response.status)
        }
    ).createPopup(event.dataContext)
     .showCenteredInCurrentWindow(event.project!!)
}


fun helloAction(n: Int) =
    AnAction("Action $n") {
        println(n)
        show(n)
    }

fun Project.openInIdeBrowser(url: String, title: String = "") =
    HTMLEditorProvider.openEditor(this, title, url, null)

fun Project.openInEditor(filePath: String) {
    PluginUtil.openUrlInEditor("file://${filePath}", this)
}

registerAction("Show My Popup") { event ->
    PopupActionGroup(
        "Foo",
        AnAction("CI") {
//            PluginUtil.openInBrowser("https://duckduckgo.com?q=asdfsad")
            it.project?.openInIdeBrowser("https://duckduckgo.com?q=asdfsad")
        },
        helloAction(2),
        Separator.getInstance(),
        PopupActionGroup(
            "Bar",
            helloAction(3),
            helloAction(4),
        ),
        AnAction("Run a Script") {
            runBackgroundTask(taskTitle = "Running shell script", task = {
                val result = runShellScript(
                    """
                    cd '$pluginPath'
                    ./hello.sh
                    sleep 4
                    echo "foooooooo"
                    ll asd
                    """.trimIndent()
                )
                show(result)
            })
        },
        AnAction("Edit the Plugin") {
            it.project?.openInEditor("$pluginPath/plugin.kts")
        }
    ).createPopup(event.dataContext)
        .showCenteredInCurrentWindow(event.project!!)
}
