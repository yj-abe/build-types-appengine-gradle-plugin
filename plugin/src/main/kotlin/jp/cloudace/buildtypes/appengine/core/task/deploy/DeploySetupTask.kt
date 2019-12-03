package jp.cloudace.buildtypes.appengine.core.task.deploy

import com.google.cloud.tools.gradle.appengine.standard.AppEngineStandardExtension
import jp.cloudace.buildtypes.appengine.core.dsl.deploy.Deploy
import org.gradle.api.DefaultTask
import javax.inject.Inject

open class DeploySetupTask @Inject constructor(
    private val deploy: Deploy
) : DefaultTask() {

    init {
        description = "Setup AppEngine deploy properties for ${deploy.name} build type."
        doFirst {
            project.extensions.getByType(AppEngineStandardExtension::class.java)
                .let { appengine ->
                    appengine.deploy { originalDeploy ->
                        originalDeploy.apply {
                            projectId = deploy.projectId
                            version = deploy.version
                            promote = deploy.promote
                            deploy.appEngineDirectory?.let { setAppEngineDirectory(it) }
                            deploy.bucket?.let { bucket = it }
                            deploy.imageUrl?.let { imageUrl = it }
                            deploy.server?.let { server = it }
                            deploy.stopPreviousVersion?.let { stopPreviousVersion = it }
                        }
                    }
                }
        }
    }

}
