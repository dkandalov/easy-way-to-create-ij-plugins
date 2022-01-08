0) hello world
 - show("Hello world")
 - run, see message in UI, in event log, event log settings
 - imports, syntax highlight, navigate to show() source code
 - script context
     - pluginPath (ctrl/cmd+shift+c to copy file paths)
     - project?.basePath
     - pluginDisposable.whenDisposed
     - isIdeStartup, navigate to its source code
 - compilation error, throw exception, throw from whenDisposed

1) Notifications
 - show(): title, notificationType, groupDisplayId, notificationListener
 - show() with larger message
 - showInConsole()
 - logger.info()/warn()/error(), Logger.getInstance(), Help -> Show Log in Finder
 - println
 - com.intellij.openapi.ui.Messages
    - Messages.showInputDialog
    - How to find other UI dialogs? See https://github.com/JetBrains/intellij-community/blob/master/platform/platform-api/src/com/intellij/openapi/ui/Messages.java
    - Kotlin API on top: https://github.com/JetBrains/intellij-community/blob/master/platform/platform-api/src/com/intellij/openapi/ui/MessageUtil.kt
      com.intellij.openapi.ui.showYesNoDialog(); @DialogTitle, @DialogMessage

2) Actions
 - registerAction("Show project Name"), action reload, action id vs text, Find Action, Keymap
 - pluginDisposable, unload plugin
 - AnActionEvent, project?.name, keyStrokes
 - actionGroupId: ToolsMenu, EditMenu, ToolbarRunGroup
   - to find existing groups use IDE Internal Actions -> UI -> UI Inspector (to enable Help -> Edit Custom Properties: idea.is.internal=true)
   - or find action by text in messages/ActionsBundle.properties
     and find its group as defined in intellij-community/platform/platform-resources/src/idea/LangActions.xml
 - object: AnAction, convert object literal it into a class, AllIcons.Plugins.Rating, event.presentation.isEnabled
 - ActionManager.getInstance().getAction("MainToolBar"), other "managers"
    - https://upsource.jetbrains.com/idea-ce/file/idea-ce-92e0eafbbe481d55f6ccb2ce31a22871b9e442d6/platform/editor-ui-api/src/com/intellij/openapi/actionSystem/AnActionEvent.java
    - https://upsource.jetbrains.com/idea-ce/file/idea-ce-92e0eafbbe481d55f6ccb2ce31a22871b9e442d6/platform/editor-ui-api/src/com/intellij/openapi/actionSystem/AnAction.java
    - https://upsource.jetbrains.com/idea-ce/file/idea-ce-92e0eafbbe481d55f6ccb2ce31a22871b9e442d6/platform/editor-ui-api/src/com/intellij/openapi/actionSystem/ActionManager.java
 - invoking other actions (e.g. ToggleBookmark1), actions are stateless, run on UI thread

3) Action groups
 - PopupActionGroup.createPopup().showCenteredInCurrentWindow()
 - separator, nested PopupActionGroup
 - openInBrowser, openInIdeBrowser, openInEditor (as a bookmark)
 - can't share events between actions, need `createPopup(event.dataContext)`
 - project-specific plugins (".live-plugins" project directory)
 - integration with command line, background tasks
 - external libs (http4k example)
   - // add-to-classpath $PLUGIN_PATH/libs/*
   - curl https://repo1.maven.org/maven2/org/http4k/http4k-core/4.12.0.1/http4k-core-4.12.0.1.jar -o http4k-core-4.12.0.1.jar
   - https://repo1.maven.org/maven2/org/http4k/http4k-client-okhttp/4.12.0.1/http4k-client-okhttp-4.12.0.1.jar

4) Editor, document
 - project?.currentEditor?.selectionModel?.selectionStart/End
 - editor.caretModel.offset/moveToOffset/addCaret
 - project?.currentEditor?.document; show(text); insertString
 - custom to uppercase action
 - threading rules
    - runBackgroundTask { show(selectionModel) }; runWithReadLock
    - runBackgroundTask { caretModel.moveToOffset() }; runOnEdtWithWriteLock

8) Intentions/inspections
 - show the difference in IDE settings
 - intentions
     - registerIntention(object : IntentionAction) with show("Hello")
     - element.containingFile.fileType.defaultExtension == "kt" && element.text.contains("hello")
     - PsiElementBaseIntentionAction which needs "// depends-on-plugin org.jetbrains.kotlin"
        - where to find plugin id? (e.g. https://plugins.jetbrains.com/plugin/6954-kotlin/versions/stable by looking at version info)
     - element.replace(KtPsiFactory(project).createLiteralStringTemplateEntry("aaaa"))
 - inspections
    - AbstractKotlinInspection with expressionVisitor
    - holder.registerProblem (without if; need to edit file to see it all highlighted)
    - expression.text.contains("hello") won't quite work --> expression is KtStringTemplateExpression && expression.text == "\"hello\""
    - HelloWorldQuickFix
 - com.intellij.patterns.ElementPattern

5) Files
 -

6) Extensions and message bus
 - ConsoleInputFilterProvider which just prints console output; registerExtension is @TestOnly 🙄
    - search for other com.intellij.openapi.extensions.ExtensionPointName#create
 - ExecutionListener which prints executorId and env
   - com.intellij.util.messages.MessageBus
   - search for other com.intellij.util.messages.Topic#create()
 - application.messageBus.syncPublisher(Notifications.TOPIC); listener
 - project.messageBus: StatusBar.Info.TOPIC, StatusBarInfo

7) UI
  - registerProjectToolWindow (project/IDE)
  - kotlin UI DSL (https://plugins.jetbrains.com/docs/intellij/kotlin-ui-dsl.html)
     - panel, row, label
     - "UI DSL Debug Mode" internal action
  - widgets

X) Thoughts
 - IDE scripting console
 - all IDE objects are available via reflection, it could smalltalk-like environment
 - in-memory state migration while rerunning plugins
 - serialisation/deserialisation between IDE restarts
 - discoverability of IDE APIs features/classes 💩
    - via text search in IJ codebase
 - some APIs are hard to use or reload (e.g. refactoring)
 - easy to introduce (performance) bugs
 - IDE API evolution (which will break your code, or will be delayed by dependencies from your code)
 - If it's such a good idea, why everyone is not doing it?
    - nobody expects it to be easy
    - not much interest => not a lot of tooling
    - should be a cultural norm (maybe library/project specific plugins)
 - video with samples https://www.youtube.com/watch?v=GcYa4lMRta0 
