package jp.cloudace.buildtypes.appengine.standard.task.stage

import com.google.cloud.tools.gradle.appengine.standard.AppEngineStandardExtension
import jp.cloudace.buildtypes.appengine.standard.dsl.stage.Stage
import org.gradle.api.DefaultTask
import javax.inject.Inject

open class StageSetupTask @Inject constructor(
    private val stage: Stage
) : DefaultTask() {

    init {
        description = "Setup AppEngine stage properties for ${stage.name} build type."
        doFirst {
            project.extensions.getByType(AppEngineStandardExtension::class.java)
                .let { appengine ->
                    appengine.stage { originalStage ->
                        stage.apply {
                            sourceDirectory?.let { originalStage.setSourceDirectory(it) }
                            stagingDirectory?.let { originalStage.setStagingDirectory(it) }
                            dockerfile?.let { originalStage.setDockerfile(it) }
                            enableQuickstart?.let { originalStage.enableQuickstart = it }
                            disableUpdateCheck?.let { originalStage.disableUpdateCheck = it }
                            enableJarSplitting?.let { originalStage.enableJarSplitting = it }
                            jarSplittingExcludes?.let { originalStage.jarSplittingExcludes = it }
                            compileEncoding?.let { originalStage.compileEncoding = it }
                            deleteJsps?.let { originalStage.deleteJsps = it }
                            enableJarClasses?.let { originalStage.enableJarClasses = it }
                            disableJarJsps?.let { originalStage.disableJarJsps = it }
                            runtime?.let { originalStage.runtime = it }
                        }
                    }
                }
        }
    }

}
