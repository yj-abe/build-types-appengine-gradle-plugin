package jp.cloudace.buildtypes.appengine.dsl

import groovy.lang.Closure
import jp.cloudace.buildtypes.appengine.dsl.deploy.Deploy
import jp.cloudace.buildtypes.appengine.dsl.run.Run
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project


open class AppEngine(
    project: Project
) {

    val deploys: NamedDomainObjectContainer<Deploy> = project.container(Deploy::class.java) { Deploy(it) }
    val runs: NamedDomainObjectContainer<Run> = project.container(Run::class.java) { Run(it) }

    fun deploy(closure: Closure<Deploy>) {
        deploys.configure(closure)
    }

    fun run(closure: Closure<Run>) {
        runs.configure(closure)
    }


}
