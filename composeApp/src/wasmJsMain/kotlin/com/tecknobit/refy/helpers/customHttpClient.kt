package com.tecknobit.refy.helpers

import io.ktor.client.HttpClient
import io.ktor.client.engine.js.Js

/**
 * Method to create a custom [HttpClient] for the [com.tecknobit.refy.imageLoader] instance
 */
actual fun customHttpClient(): HttpClient {
    return HttpClient(Js)
}