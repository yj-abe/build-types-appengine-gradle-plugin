package jp.cloudace.buildtypes.appengine.core

import groovy.lang.Closure
import jp.cloudace.buildtypes.appengine.core.dsl.deploy.Deploy
import jp.cloudace.buildtypes.appengine.core.task.deploy.DeploySetupTask
import jp.cloudace.buildtypes.extention.BuildType
import jp.cloudace.buildtypes.extention.BuildTypes
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

abstract class AppEngine(
    protected val project: Project
) {

    val deploys: NamedDomainObjectContainer<Deploy> = project.container(Deploy::class.java) { Deploy(it) }

    init {
        project.afterEvaluate { setup() }
    }

    fun deploy(closure: Closure<Deploy>) {
        deploys.configure(closure)
    }

    abstract fun onEvaluated()
    abstract fun evaluateBuildType(type: BuildType)

    private fun setup() {
        checkBuildTypes()
        onEvaluated()
        project.extensions.getByType(BuildTypes::class.java).types.forEach {
            createDeployTask(it)
            evaluateBuildType(it)
        }
    }

    private fun checkBuildTypes() {
        val buildTypes = project.extensions.getByType(BuildTypes::class.java).types
        deploys.firstOrNull { buildTypes.findByName(it.name) == null }
            ?.let { throw IllegalStateException("Unknown build type is defined at deploy. : \"${it.name}\"") }
    }

    private fun createDeployTask(buildType: BuildType) {
        val deploy = deploys.findByName(buildType.name) ?: Deploy(buildType.name)
        val capitalizedName = buildType.name.capitalize()
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

}
