package io.config4k.plugin

import io.config4k.annotation.Properties

data class Config4kContext(
    val defaultVisibility: Properties.PropertiesVisibility = Properties.PropertiesVisibility.SAME_AS_CLASS,
    val defaultGenerateBlocking: Boolean = true,
    val defaultGenerationAsync: Boolean = true,
    val logInfo: Boolean = false,
    val logDebug: Boolean = false,

) {
    init {
        require(defaultVisibility != Properties.PropertiesVisibility.DEFAULT) {
            "the default visibility cannot be DEFAULT, please use SAME_AS_CLASS Instead"
        }
    }
}