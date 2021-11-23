import com.intellij.icons.AllIcons
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.ui.Messages.showInputDialog
import com.intellij.openapi.ui.showYesNoDialog
import liveplugin.show

//show(
//    message = "Hello <a href=''>world</a>",
//    title = "Foo",
//    groupDisplayId = "foo",
//    notificationListener = { notification, hyperlinkEvent ->
//        show("Hi ${notification.content} ${hyperlinkEvent.sourceElement}")
//    }
//)
//
//project?.showInConsole(
//    message = "a really long message, or maybe even a stacktrace",
//    consoleTitle = "title",
//    contentType = ERROR_OUTPUT
//)

val isYes = showYesNoDialog(
    "Dialog Title",
    "And this is a question?",
    project
)
show(if (isYes) "yes" else "no 😿")

val userInput = com.intellij.openapi.ui.Messages.showInputDialog(
    project,
    "Please enter something",
    "Dialog Title",
    AllIcons.Ide.Gift,
    "initial value",
    null
)
show("userInput = $userInput")
//show(if (yes) "yes" else "no")

val logger = Logger.getInstance("MyPlugin")
logger.info("info message")
logger.warn("warn message")
logger.error("error message")

println("hello stdout")