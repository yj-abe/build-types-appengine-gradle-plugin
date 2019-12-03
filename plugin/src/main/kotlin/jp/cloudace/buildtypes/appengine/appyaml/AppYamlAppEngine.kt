package jp.cloudace.buildtypes.appengine.appyaml

import groovy.lang.Closure
import jp.cloudace.buildtypes.appengine.appyaml.dsl.stage.Stage
import jp.cloudace.buildtypes.appengine.appyaml.task.stage.StageSetupTask
import jp.cloudace.buildtypes.appengine.core.AppEngine
import jp.cloudace.buildtypes.extention.BuildType
import jp.cloudace.buildtypes.extention.BuildTypes
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

open class AppYamlAppEngine(
    project: Project
) : AppEngine(project) {

    val stages: NamedDomainObjectContainer<Stage> = project.container(Stage::class.java) { Stage(it) }

    fun stage(closure: Closure<Stage>) {
        stages.configure(closure)
    }

    override fun onEvaluated() {
        val buildTypes = project.extensions.getByType(BuildTypes::class.java).types
        stages.firstOrNull { buildTypes.findByName(it.name) == null }
            ?.let { throw IllegalStateException("Unknown build type is defined at stage. : \"${it.name}\"") }
    }

    override fun evaluateBuildType(type: BuildType) {
        val stage = stages.findByName(type.name) ?: Stage(type.name)
        val capitalizedName = type.name.capitalize()
        val setupTask = project.tasks.create(
            "setup${capitalizedName}AppEngineStage",
            StageSetupTask::class.java,
            stage
        )

        project.tasks.create("appengine${capitalizedName}Stage").apply {
            dependsOn(setupTask)
            dependsOn("build$capitalizedName")
            dependsOn("appengineStage")
        }

        project.tasks.findByName("appengineStage")?.apply {
            mustRunAfter("build$capitalizedName")
            mustRunAfter(setupTask)
        }

        project.tasks.findByName("appengine${capitalizedName}Deploy")?.apply {
            dependsOn("appengine${capitalizedName}Stage")
        }

    }
}
