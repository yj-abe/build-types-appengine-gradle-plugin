package jp.cloudace.buildtypes.appengine

import com.google.cloud.tools.gradle.appengine.AppEnginePlugin
import com.google.cloud.tools.gradle.appengine.standard.AppEngineStandardExtension
import jp.cloudace.buildtypes.BuildTypesPlugin
import jp.cloudace.buildtypes.appengine.appyaml.AppYamlAppEngine
import jp.cloudace.buildtypes.appengine.standard.StandardAppEngine
import jp.cloudace.buildtypes.extention.BuildTypes
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware

open class BuildTypesAppEnginePlugin : Plugin<Project> {

    override fun apply(project: Project) {

        if (!project.plugins.hasPlugin(AppEnginePlugin::class.java)) {
            throw java.lang.IllegalStateException("App Engine plugins must be applied before this plugin.")
        }

        if (!project.plugins.hasPlugin(BuildTypesPlugin::class.java)) {
            BuildTypesPlugin().apply(project)
        }

        val isStandard = project.extensions.findByType(AppEngineStandardExtension::class.java) != null

        project.extensions.getByType(BuildTypes::class.java)
            .let { it as ExtensionAware }
            .extensions
            .create(
                "appengine",
                if (isStandard) StandardAppEngine::class.java else AppYamlAppEngine::class.java,
                project
            )
    }
}
