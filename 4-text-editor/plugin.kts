import com.intellij.openapi.application.WriteAction
import com.intellij.util.ThrowableRunnable
import kotlin.random.Random
import liveplugin.currentEditor
import liveplugin.document
import liveplugin.editor
import liveplugin.*
import liveplugin.registerAction
import liveplugin.show

//project?.currentEditor?.caretModel?.moveToOffset(0)
//project?.currentEditor?.selectionModel?.setSelection(0, 100)

registerEditorAction("Random Case", "alt shift PERIOD") { editor, caret, _ ->
    val document = editor.document
    val offset = editor.caretModel.offset
    val textAfter = document.text.drop(offset).takeWhile { it.isLetterOrDigit() }
    val textBefore = document.text.take(offset).takeLastWhile { it.isLetterOrDigit() }
    val text = (textBefore + textAfter)
        .map { if (Random.nextBoolean()) it.uppercaseChar() else it.lowercaseChar() }
        .joinToString(separator = "")

    document.replaceString(
        offset - textBefore.length,
        offset + textAfter.length,
        text
    )
}

/*
registerAction("Random Case", "alt shift PERIOD") { event ->
    val document = event.document
    val offset = event.editor?.caretModel?.offset
    if (document != null && offset != null) {
        val textAfter = document.text.drop(offset).takeWhile { it.isLetterOrDigit() }
        val textBefore = document.text.take(offset).takeLastWhile { it.isLetterOrDigit() }
        val text = (textBefore + textAfter)
            .map { if (Random.nextBoolean()) it.uppercaseChar() else it.lowercaseChar() }
            .joinToString(separator = "")

        document.executeCommand(event.project!!, description = "Random Case") {
            document.replaceString(
                offset - textBefore.length,
                offset + textAfter.length,
                text
            )
        }
    }
}
*/
