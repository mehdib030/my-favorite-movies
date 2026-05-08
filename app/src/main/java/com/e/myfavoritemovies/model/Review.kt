package com.e.myfavoritemovies.model

import java.io.Serializable

data class Review @JvmOverloads constructor(
    var id: String? = null,
    var author: String? = null,
    var content: String? = null,
    var url: String? = null
) : Serializable
