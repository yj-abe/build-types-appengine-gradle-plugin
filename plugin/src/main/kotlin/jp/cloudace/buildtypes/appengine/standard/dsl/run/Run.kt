package jp.cloudace.buildtypes.appengine.standard.dsl.run

import java.io.File

class Run(
    val name: String
) {

    var environment: Map<String, String>? = null
    var jvmFlags: List<String>? = null
    var port: Int? = null
    var host: String? = null
    var serverVersion: String? = null
    var services: MutableList<File>? = null
    var startSuccessTimeout: Int? = null
    var projectId: String = name

}
