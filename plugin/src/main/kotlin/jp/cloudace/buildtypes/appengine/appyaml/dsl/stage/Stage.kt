package jp.cloudace.buildtypes.appengine.appyaml.dsl.stage

import java.io.File

open class Stage(
    val name: String
) {

    var artifact: File? = null
    var dockerDirectory: File? = null
    var appEngineDirectory: File? = null
    var stagingDirectory: File? = null
    var extraFilesDirectories: List<File>? = null

}
