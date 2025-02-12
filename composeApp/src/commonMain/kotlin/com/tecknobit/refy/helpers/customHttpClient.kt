package com.tecknobit.refy.helpers

import io.ktor.client.HttpClient

/**
 * Method to create a custom [HttpClient] for the [com.tecknobit.refy.imageLoader] instance
 */
expect fun customHttpClient(): HttpClient