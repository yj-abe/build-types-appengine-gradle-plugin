package jp.cloudace.buildtypes.appengine.task.run

import com.google.cloud.tools.gradle.appengine.standard.AppEngineStandardExtension
import jp.cloudace.buildtypes.appengine.dsl.run.Run
import org.gradle.api.DefaultTask
import javax.inject.Inject

open class RunSetupTask @Inject constructor(
    private val run: Run
) : DefaultTask() {

    init {
        description = "Setup AppEngine run properties for ${run.name} build type."
        doFirst {
            project.extensions.getByType(AppEngineStandardExtension::class.java)
                .let { appengine ->
                    appengine.run { originalRun ->
                        run.apply {
                            environment?.let { originalRun.environment = it }
                            jvmFlags?.let { originalRun.jvmFlags = it }
                            port?.let { originalRun.port = it }
                            host?.let { originalRun.host = it }
                            serverVersion?.let { originalRun.serverVersion = it }
                            services?.let { originalRun.setServices(it) }
                            startSuccessTimeout?.let { originalRun.startSuccessTimeout = it }
                            originalRun.projectId = projectId // 何故かローカル実行でもprojectIdが必須なので、設定する。
                        }
                    }
                }
        }

    }

}
