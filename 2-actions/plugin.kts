import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import liveplugin.ActionGroupIds.EditorPopupMenu
import liveplugin.PluginUtil.findAllActions
import liveplugin.registerAction
import liveplugin.show


//show(findAllActions("Bookmark").map { it.javaClass })

registerAction(
    id = "Show Project Name",
    actionGroupId = EditorPopupMenu,
    action = ShowProject()
)

class ShowProject : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        show(event.project?.name)
        ActionManager.getInstance().getAction("CompileDirty").actionPerformed(event)
    }
}


//import com.intellij.openapi.actionSystem.ActionManager
//import com.intellij.openapi.actionSystem.AnAction
//import com.intellij.openapi.actionSystem.AnActionEvent
//import liveplugin.registerAction
//import liveplugin.show
//
//registerAction(
//    id = "Show Project Name",
//    keyStroke = "alt shift .",
//    actionGroupId = liveplugin.ActionGroupIds.EditorPopupMenu,
//    action = ShowProject()
//)
//
//class ShowProject : AnAction() {
//    override fun actionPerformed(event: AnActionEvent) {
//        show(event.project?.name)
//    }
//}
