package jp.cloudace.buildtypes.appengine.core.dsl.deploy

import java.io.File

open class Deploy(
    val name: String
) {

    var projectId: String = "GCLOUD_CONFIG"
    var promote: Boolean = false
    var version: String = "GCLOUD_CONFIG"
    var appEngineDirectory: File? = null
    var bucket: String? = null
    var imageUrl: String? = null
    var server: String? = null
    var stopPreviousVersion: Boolean? = null

}
