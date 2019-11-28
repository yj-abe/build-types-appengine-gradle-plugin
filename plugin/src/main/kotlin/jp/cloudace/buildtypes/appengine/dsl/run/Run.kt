package jp.cloudace.buildtypes.appengine.dsl.run

import java.io.File

class Run(
    val name: String
) {

    var environment: Map<String, String>? = null
    var jmvFlags: List<String>? = null
    var port: Int? = null
    var host: String? = null
    var serviceVersion: String? = null
    var services: MutableList<File>? = null
    var startSuccessTimeout: Int? = null

}
