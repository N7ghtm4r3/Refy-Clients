package com.tecknobit.refy

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.text.font.FontFamily
import com.tecknobit.refy.helpers.RefyLocalUser
import com.tecknobit.refy.ui.screens.home.presenter.HomeScreen
import com.tecknobit.refy.ui.screens.splashscreen.SplashScreen
import com.tecknobit.refy.ui.theme.RefyTheme
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.rememberNavigator
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.ui.tooling.preview.Preview
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.titillium
import refy.composeapp.generated.resources.ubuntu

/**
 * `bodyFontFamily` the Refy's body font family
 */
lateinit var bodyFontFamily: FontFamily

/**
 * `displayFontFamily` the Refy's font family
 */
lateinit var displayFontFamily: FontFamily

/**
 * `navigator` the navigator instance is useful to manage the navigation between the screens of the application
 */
lateinit var navigator: Navigator

/**
 *`localUser` the helper to manage the local sessions stored locally in
 * the device
 */
val localUser = RefyLocalUser()

/**
 * `SPLASHSCREEN` route to navigate to the [com.tecknobit.refy.ui.screens.splashscreen.Splashscreen]
 */
const val SPLASHSCREEN = "Splashscreen"

/**
 * `AUTH_SCREEN` route to navigate to the [com.tecknobit.refy.ui.screens.auth.presenter.AuthScreen]
 */
const val AUTH_SCREEN = "AuthScreen"

/**
 * `HOME_SCREEN` route to navigate to the [com.tecknobit.refy.ui.screens.home.presenter.HomeScreen]
 */
const val HOME_SCREEN = "HomeScreen"

/**
 * `PROFILE_SCREEN` route to navigate to the [com.tecknobit.refy.ui.screens.profile.presenter.ProfileScreen]
 */
const val PROFILE_SCREEN = "ProfileScreen"

/**
 * Method to start the `Refy`'s application
 */
@Composable
@Preview
fun App() {
    bodyFontFamily = FontFamily(Font(Res.font.titillium))
    displayFontFamily = FontFamily(Font(Res.font.ubuntu))
    // TODO: TO SET
    /*imageLoader = ImageLoader.Builder(LocalPlatformContext.current)
        .components {
            add(
                OkHttpNetworkFetcherFactory {
                    OkHttpClient.Builder()
                        .sslSocketFactory(sslContext.socketFactory,
                            validateSelfSignedCertificate()[0] as X509TrustManager
                        )
                        .hostnameVerifier { _: String?, _: SSLSession? -> true }
                        .connectTimeout(5, TimeUnit.SECONDS)
                        .build()
                }
            )
        }
        .addLastModifiedToFileCacheKey(true)
        .diskCachePolicy(CachePolicy.ENABLED)
        .networkCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()*/
    PreComposeApp {
        navigator = rememberNavigator()
        RefyTheme {
            NavHost(
                navigator = navigator,
                initialRoute = SPLASHSCREEN
            ) {
                scene(
                    route = SPLASHSCREEN
                ) {
                    SplashScreen().ShowContent()
                }
                scene(
                    route = HOME_SCREEN
                ) {
                    HomeScreen().ShowContent()
                }
            }
        }
    }
}

/**
 * Method to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
@NonRestartableComposable
expect fun CheckForUpdatesAndLaunch()

/**
 * Method to init the local session and the related instances then start the user session
 *
 */
fun startSession() {
    // TODO: MAKE THE REAL NAVIGATION
    /*requester = NeutronRequester(
        host = localUser.hostAddress ?: "",
        userId = localUser.userId,
        userToken = localUser.userToken
    )
    val route = if (localUser.isAuthenticated) {
        MainScope().launch {
            requester.sendRequest(
                request = {
                    getDynamicAccountData()
                },
                onSuccess = { response ->
                    localUser.updateDynamicAccountData(
                        dynamicData = response.toResponseData()
                    )
                },
                onFailure = {}
            )
        }
        REVENUES_SCREEN
    } else
        AUTH_SCREEN*/
    setUserLanguage()
    navigator.navigate(HOME_SCREEN)
}

/**
 * Method to set locale language for the application
 *
 */
expect fun setUserLanguage()

/**
 * Method to manage correctly the back navigation from the current screen
 *
 */
@Composable
@NonRestartableComposable
expect fun CloseApplicationOnNavBack()