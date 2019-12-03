package jp.cloudace.buildtypes.appengine.standard.dsl.stage

import java.io.File

open class Stage(
    val name: String
) {

    var sourceDirectory: File? = null
    var stagingDirectory: File? = null
    var dockerfile: File? = null
    var enableQuickstart: Boolean? = null
    var disableUpdateCheck: Boolean? = null
    var enableJarSplitting: Boolean? = null
    var jarSplittingExcludes: String? = null
    var compileEncoding: String? = null
    var deleteJsps: Boolean? = null
    var enableJarClasses: Boolean? = null
    var disableJarJsps: Boolean? = null
    var runtime: String? = null

}
