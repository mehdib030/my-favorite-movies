package com.e.myfavoritemovies.model

import java.io.Serializable

data class Trailer @JvmOverloads constructor(
    var id: String? = null,
    var name: String? = null,
    var type: String? = null,
    var key: String? = null,
    var size: String? = null,
    var site: String? = null
) : Serializable
