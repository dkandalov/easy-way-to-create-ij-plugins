import com.intellij.execution.ExecutionListener
import com.intellij.execution.ExecutionManager
import com.intellij.execution.filters.ConsoleInputFilterProvider
import com.intellij.execution.filters.InputFilter
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.wm.StatusBar
import liveplugin.show

val provider = ConsoleInputFilterProvider {
    arrayOf(InputFilter { text, _ ->
        show(text)
        null
    })
}
ApplicationManager.getApplication().extensionArea
    .getExtensionPoint(ConsoleInputFilterProvider.INPUT_FILTER_PROVIDERS)
    .registerExtension(provider, pluginDisposable)

ApplicationManager.getApplication().messageBus.connect(pluginDisposable)
    .subscribe(ExecutionManager.EXECUTION_TOPIC, object : ExecutionListener {
        override fun processStarting(executorId: String, env: ExecutionEnvironment) {
            show(executorId)
            show(env)
        }
    })

ApplicationManager.getApplication().messageBus.connect(pluginDisposable)
    .subscribe(Notifications.TOPIC, object: Notifications {
        override fun notify(notification: Notification) {
            println(notification)
        }
    })

ApplicationManager.getApplication().messageBus
    .syncPublisher(Notifications.TOPIC)
    .notify(Notification("aaa", "Aaa", NotificationType.WARNING))


project!!.messageBus
    .syncPublisher(StatusBar.Info.TOPIC)
    .setInfo("Hahah", "aaa")
