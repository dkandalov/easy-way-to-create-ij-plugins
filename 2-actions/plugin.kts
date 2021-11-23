import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.Constraints.*
import liveplugin.registerAction
import liveplugin.show

registerAction("Invoke Another Action") { event ->
    val action = ActionManager.getInstance().getAction("ToggleBookmark1")
    action.actionPerformed(event)
}

registerAction(id = "Show Project Name", action = ShowProjectName())

class ShowProjectName : AnAction(AllIcons.Ide.Gift) {
    override fun actionPerformed(event: AnActionEvent) {
        show(event.project?.name)
    }
    override fun update(event: AnActionEvent) {
        event.presentation.isEnabled = true
    }
}

//val actionManager = com.intellij.openapi.actionSystem.ActionManager.getInstance()
//actionManager.registerAction("action id", action)
