package jp.cloudace.buildtypes.appengine.appyaml.task.stage

import com.google.cloud.tools.gradle.appengine.appyaml.AppEngineAppYamlExtension
import jp.cloudace.buildtypes.appengine.appyaml.dsl.stage.Stage
import org.gradle.api.DefaultTask
import javax.inject.Inject

open class StageSetupTask @Inject constructor(
    private val stage: Stage
) : DefaultTask() {

    init {
        description = "Setup AppEngine stage properties for ${stage.name} build type."
        doFirst {
            project.extensions.getByType(AppEngineAppYamlExtension::class.java)
                .let { appengine ->
                    appengine.stage { originalStage ->
                        stage.apply {
                            artifact?.let { originalStage.setArtifact(it) }
                            dockerDirectory?.let { originalStage.setDockerDirectory(it) }
                            appEngineDirectory?.let { originalStage.setAppEngineDirectory(it) }
                            stagingDirectory?.let { originalStage.setStagingDirectory(it) }
                            extraFilesDirectories?.let { originalStage.setExtraFilesDirectories(extraFilesDirectories) }
                        }
                    }
                }
        }
    }

}
