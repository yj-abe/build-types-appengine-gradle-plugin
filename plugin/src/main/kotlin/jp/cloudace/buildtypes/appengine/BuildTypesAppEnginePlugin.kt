package jp.cloudace.buildtypes.appengine

import jp.cloudace.buildtypes.BuildTypesPlugin
import jp.cloudace.buildtypes.appengine.dsl.AppEngine
import jp.cloudace.buildtypes.appengine.dsl.deploy.Deploy
import jp.cloudace.buildtypes.appengine.dsl.run.Run
import jp.cloudace.buildtypes.appengine.task.deploy.DeploySetupTask
import jp.cloudace.buildtypes.appengine.task.run.RunSetupTask
import jp.cloudace.buildtypes.extention.BuildTypes
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware

open class BuildTypesAppEnginePlugin : Plugin<Project> {

    override fun apply(project: Project) {
        if (!project.plugins.hasPlugin(BuildTypesPlugin::class.java)) {
            BuildTypesPlugin().apply(project)
        }
        val appEngine = project.extensions.getByType(BuildTypes::class.java)
            .let { it as ExtensionAware }
            .let { it.extensions.create("appengine", AppEngine::class.java, project) }

        project.afterEvaluate { createTasks(project, appEngine) }
    }

    private fun createTasks(project: Project, appEngine: AppEngine) {
        checkBuildTypes(project, appEngine)
        project.extensions.getByType(BuildTypes::class.java).types
            .map { it.name }
            .forEach {
                createDeployTask(project, appEngine, it)
                createRunTask(project, appEngine, it)
            }
    }

    private fun checkBuildTypes(project: Project, appEngine: AppEngine) {
        val buildTypes = project.extensions.getByType(BuildTypes::class.java).types
        appEngine.deploys
            .firstOrNull { buildTypes.findByName(it.name) == null }
            ?.let { throw IllegalStateException("Unknown build type is defined at deploy. : \"${it.name}\"") }
        appEngine.runs
            .firstOrNull { buildTypes.findByName(it.name) == null }
            ?.let { throw IllegalStateException("Unknown build type is defined at run. : \"${it.name}\"") }
    }

    private fun createDeployTask(project: Project, appEngine: AppEngine, buildTypeName: String) {
        val deploy = appEngine.deploys.findByName(buildTypeName) ?: Deploy(buildTypeName)
        val capitalizedName = buildTypeName.capitalize()
        val setupTask = project.tasks.create(
            "setup${capitalizedName}AppEngineDeploy",
            DeploySetupTask::class.java,
            deploy
        )

        project.tasks.create("appengine${capitalizedName}Deploy").apply {
            dependsOn(setupTask)
            dependsOn("build$capitalizedName")
            dependsOn("appengineDeploy")
        }

        project.tasks.findByName("appengineDeploy")?.apply {
            mustRunAfter("build$capitalizedName")
            mustRunAfter(setupTask)
        }
    }

    private fun createRunTask(project: Project, appEngine: AppEngine, buildTypeName: String) {
        val run = appEngine.runs.findByName(buildTypeName) ?: Run(buildTypeName)
        val capitalizedName = buildTypeName.capitalize()
        val setupTask = project.tasks.create(
            "setup${capitalizedName}AppEngineRun",
            RunSetupTask::class.java,
            run
        )

        project.tasks.create("appengine${capitalizedName}Run").apply {
            dependsOn(setupTask)
            dependsOn("build$capitalizedName")
            dependsOn("appengineRun")
        }

        project.tasks.findByName("appengineRun")?.apply {
            mustRunAfter("build$capitalizedName")
            mustRunAfter(setupTask)
        }
    }


}
