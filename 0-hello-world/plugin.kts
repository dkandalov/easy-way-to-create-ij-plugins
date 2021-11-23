import liveplugin.show
import liveplugin.whenDisposed

//show("isIdeStartup = $isIdeStartup")
//show("project base path = ${project?.basePath}")
//show("pluginPath = $pluginPath")

show("started")
pluginDisposable.whenDisposed { 
    show("disposed")
}
