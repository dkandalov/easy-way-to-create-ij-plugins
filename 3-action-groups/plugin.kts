import com.intellij.openapi.actionSystem.Separator
import liveplugin.*
import liveplugin.PluginUtil.openInBrowser
import org.http4k.core.Method.GET
import org.http4k.core.Request

// add-to-classpath $PLUGIN_PATH/libs/*

registerAction("Show Action Group", keyStroke = "alt shift .") { event ->
    PopupActionGroup(
        name = "Some Actions",
        AnAction("LivePlugin on GitHub") {
            openInBrowser("https://github.com/dkandalov/live-plugin")
        },
        AnAction("Run a Script") {
            runBackgroundTask(taskTitle = "Running shell script", task = {
                val result = runShellScript("""
                    '$pluginPath/hello.sh'
                    sleep 5
                    """.trimIndent()
                )
                show(result.stdout)
            })
        },
        AnAction("Http Request") {
            val client = org.http4k.client.OkHttp()
            val response = client(Request(GET, "https://duckduckgo.com"))
            show(response.bodyString())
        },
        Separator.getInstance(),
        AnAction("Open plugin.kts") {
            it.project?.openInEditor("$pluginPath/plugin.kts")
        }
    ).createPopup(event.dataContext)
        .showCenteredInCurrentWindow(event.project!!)
}
