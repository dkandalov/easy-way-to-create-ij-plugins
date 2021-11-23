import kotlin.random.Random
import liveplugin.currentEditor
import liveplugin.document
import liveplugin.editor
import liveplugin.executeCommand
import liveplugin.registerAction

//show(project?.currentEditor)
//show(project?.currentDocument)
//show(project?.currentFile)

//val editor = project?.currentEditor ?: error("No editor")
//editor.caretModel.moveToOffset(0)
//editor.selectionModel.setSelection(0, editor.document.textLength)

//show(project?.currentEditor?.caretModel?.logicalPosition?.line)
//
//val selection = project?.currentEditor?.selectionModel ?: error("")
//show(selection.selectionStart..selection.selectionEnd)

project?.currentEditor?.selectionModel?.let { selection ->
//    runBackgroundTask {
//        runWithReadLock {
//            show(selection.selectionStart..selection.selectionEnd)
//        }
//    }
}

//project?.currentEditor?.caretModel?.let { caretModel ->
////    caretModel.moveToOffset(0)
//    runBackgroundTask {
////        show(caretModel.offset)
//        runOnEdtWithWriteLock {
//            caretModel.moveToOffset(0)
//        }
//    }
////     it.addCaret(VisualPosition(1, 0))
//}
//
//project?.currentEditor?.document?.let { document ->
////    show(it.text)
////    executeCommand(document, project, "Foo") {
////        it.insertString(0, "// hello\n")
////    }
//}

registerAction("Random Case", "ctrl alt shift PERIOD") { event ->
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

