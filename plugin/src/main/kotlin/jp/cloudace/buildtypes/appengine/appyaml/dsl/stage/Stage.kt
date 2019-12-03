package jp.cloudace.buildtypes.appengine.appyaml.dsl.stage

import java.io.File

open class Stage(
    val name: String
) {

    var artifact: File? = null
    var dockerDirectory: File? = null
    var appEngineDirectory: Any? = null
    var stagingDirectory: Any? = null
    var extraFilesDirectories: List<File>? = null

}
