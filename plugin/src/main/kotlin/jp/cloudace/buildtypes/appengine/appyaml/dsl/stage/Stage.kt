package jp.cloudace.buildtypes.appengine.appyaml.dsl.stage

import org.omg.CORBA.Object
import java.io.File

open class Stage(
    val name: String
) {

    var artifact: File? = null
    var dockerDirectory: File? = null
    var appEngineDirectory: Object? = null
    var stagingDirectory: File? = null
    var extraFilesDirectories: List<File>? = null

}
