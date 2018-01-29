package org.loriz.mihomereplacer.core.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

/**
 * Created by loriz on 1/29/18.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
class MiDescriptor(
        @JsonProperty("folder") val folder : Int,
        @JsonProperty("lastplugin") val lastPlugin : String,
        @JsonProperty("oldplugin") val oldPlugin : String,
        @JsonProperty("md5") val md5 : String,
        @JsonProperty("name") val miName : String
) : Serializable
