package jp.cloudace.buildtypes.appengine.standard

import groovy.lang.Closure
import jp.cloudace.buildtypes.appengine.core.AppEngine
import jp.cloudace.buildtypes.appengine.standard.dsl.run.Run
import jp.cloudace.buildtypes.appengine.standard.dsl.stage.Stage
import jp.cloudace.buildtypes.appengine.standard.task.run.RunSetupTask
import jp.cloudace.buildtypes.appengine.standard.task.stage.StageSetupTask
import jp.cloudace.buildtypes.extention.BuildType
import jp.cloudace.buildtypes.extention.BuildTypes
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project


open class StandardAppEngine(
    project: Project
) : AppEngine(project) {

    val runs: NamedDomainObjectContainer<Run> = project.container(Run::class.java) { Run(it) }
    val stages: NamedDomainObjectContainer<Stage> = project.container(Stage::class.java) { Stage(it) }

    fun run(closure: Closure<Run>) {
        runs.configure(closure)
    }

    fun stage(closure: Closure<Stage>) {
        stages.configure(closure)
    }

    override fun onEvaluated() {
        val buildTypes = project.extensions.getByType(BuildTypes::class.java).types
        runs.firstOrNull { buildTypes.findByName(it.name) == null }
            ?.let { throw IllegalStateException("Unknown build type is defined at run. : \"${it.name}\"") }
        stages.firstOrNull { buildTypes.findByName(it.name) == null }
            ?.let { throw IllegalStateException("Unknown build type is defined at stage. : \"${it.name}\"") }
    }

    override fun evaluateBuildType(type: BuildType) {
        createRunTasks(type)
        createStageTasks(type)
    }

    private fun createRunTasks(type: BuildType) {
        val run = runs.findByName(type.name) ?: Run(type.name)
        val capitalizedName = type.name.capitalize()
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

    private fun createStageTasks(type: BuildType) {
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
