@file:OptIn(ExperimentalResourceApi::class)

package com.tecknobit.refy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import coil3.ImageLoader
import coil3.compose.LocalPlatformContext
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.addLastModifiedToFileCacheKey
import com.tecknobit.ametistaengine.AmetistaEngine
import com.tecknobit.ametistaengine.AmetistaEngine.Companion.FILES_AMETISTA_CONFIG_PATHNAME
import com.tecknobit.equinoxcompose.utilities.generateRandomColor
import com.tecknobit.equinoxcompose.utilities.toHex
import com.tecknobit.equinoxcore.helpers.NAME_KEY
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.refy.helpers.RefyLocalUser
import com.tecknobit.refy.helpers.RefyRequester
import com.tecknobit.refy.helpers.customHttpClient
import com.tecknobit.refy.ui.components.imageLoader
import com.tecknobit.refy.ui.screens.auth.presenter.AuthScreen
import com.tecknobit.refy.ui.screens.collection.presenter.CollectionScreen
import com.tecknobit.refy.ui.screens.home.presenter.HomeScreen
import com.tecknobit.refy.ui.screens.profile.presenter.ProfileScreen
import com.tecknobit.refy.ui.screens.splashscreen.SplashScreen
import com.tecknobit.refy.ui.screens.team.presenter.TeamScreen
import com.tecknobit.refy.ui.screens.upsertcollection.presenter.UpsertCollectionScreen
import com.tecknobit.refy.ui.screens.upsertcustomlink.presenter.UpsertCustomLinkScreen
import com.tecknobit.refy.ui.screens.upsertlink.presenter.UpsertLinkScreen
import com.tecknobit.refy.ui.screens.upsertteam.presenter.UpsertTeamScreen
import com.tecknobit.refy.ui.theme.RefyTheme
import com.tecknobit.refycore.COLLECTION_COLOR_KEY
import com.tecknobit.refycore.COLLECTION_IDENTIFIER_KEY
import com.tecknobit.refycore.LINK_IDENTIFIER_KEY
import com.tecknobit.refycore.TEAM_IDENTIFIER_KEY
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import moe.tlaster.precompose.PreComposeApp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.navigation.path
import moe.tlaster.precompose.navigation.rememberNavigator
import org.jetbrains.compose.resources.ExperimentalResourceApi
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
 *`requester` the instance to manage the requests with the backend
 */
lateinit var requester: RefyRequester

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
 * `UPSERT_LINK_SCREEN` route to navigate to the [com.tecknobit.refy.ui.screens.upsertlink.presenter.UpsertLinkScreen]
 */
const val UPSERT_LINK_SCREEN = "UpsertLinkScreen"

/**
 * `PROFILE_SCREEN` route to navigate to the [com.tecknobit.refy.ui.screens.profile.presenter.ProfileScreen]
 */
const val PROFILE_SCREEN = "ProfileScreen"

/**
 * `COLLECTION_SCREEN` route to navigate to the [com.tecknobit.refy.ui.screens.collection.presenter.CollectionScreen]
 */
const val COLLECTION_SCREEN = "CollectionScreen"

/**
 * `UPSERT_COLLECTION_SCREEN` route to navigate to the [com.tecknobit.refy.ui.screens.upsertcollection.presenter.UpsertCollectionScreen]
 */
const val UPSERT_COLLECTION_SCREEN = "UpsertCollectionScreen"

/**
 * `TEAM_SCREEN` route to navigate to the [com.tecknobit.refy.ui.screens.team.presenter.TeamScreen]
 */
const val TEAM_SCREEN = "TeamScreen"

/**
 * `UPSERT_TEAM_SCREEN` route to navigate to the [com.tecknobit.refy.ui.screens.upsertteam.presenter.UpsertTeamScreen]
 */
const val UPSERT_TEAM_SCREEN = "UpsertTeamScreen"

/**
 * `UPSERT_CUSTOM_LINK_SCREEN` route to navigate to the [com.tecknobit.refy.ui.screens.upsertcustomlink.presenter.UpsertCustomLinkScreen]
 */
const val UPSERT_CUSTOM_LINK_SCREEN = "UpsertCustomLinkScreen"

/**
 * Method to start the `Refy`'s application
 */
@Composable
@Preview
fun App() {
    bodyFontFamily = FontFamily(Font(Res.font.titillium))
    displayFontFamily = FontFamily(Font(Res.font.ubuntu))
    imageLoader = ImageLoader.Builder(LocalPlatformContext.current)
        .components {
            add(
                KtorNetworkFetcherFactory(
                    httpClient = customHttpClient()
                )
            )
        }
        .addLastModifiedToFileCacheKey(true)
        .diskCachePolicy(CachePolicy.ENABLED)
        .networkCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .build()
    InitAmetista()
    PreComposeApp {
        navigator = rememberNavigator()
        RefyTheme {
            NavHost(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .statusBarsPadding(),
                navigator = navigator,
                initialRoute = SPLASHSCREEN
            ) {
                scene(
                    route = SPLASHSCREEN
                ) {
                    SplashScreen().ShowContent()
                }
                scene(
                    route = AUTH_SCREEN
                ) {
                    AuthScreen().ShowContent()
                }
                scene(
                    route = HOME_SCREEN
                ) {
                    HomeScreen().ShowContent()
                }
                scene(
                    route = "$UPSERT_LINK_SCREEN/{$LINK_IDENTIFIER_KEY}?"
                ) { backstackEntry ->
                    val linkId = backstackEntry.path<String>(LINK_IDENTIFIER_KEY)
                    UpsertLinkScreen(
                        linkId = linkId
                    ).ShowContent()
                }
                scene(
                    route = PROFILE_SCREEN
                ) {
                    ProfileScreen().ShowContent()
                }
                scene(
                    route = "$COLLECTION_SCREEN/{$COLLECTION_IDENTIFIER_KEY}/{$NAME_KEY}/{$COLLECTION_COLOR_KEY}"
                ) { backstackEntry ->
                    val collectionId: String =
                        backstackEntry.path<String>(COLLECTION_IDENTIFIER_KEY)!!
                    val name: String = backstackEntry.path<String>(NAME_KEY)!!
                    val color: String = backstackEntry.path<String>(COLLECTION_COLOR_KEY)!!
                    CollectionScreen(
                        collectionId = collectionId,
                        collectionName = name,
                        collectionColor = color
                    ).ShowContent()
                }
                scene(
                    route = "$UPSERT_COLLECTION_SCREEN/{$COLLECTION_IDENTIFIER_KEY}?/{$COLLECTION_COLOR_KEY}?"
                ) { backstackEntry ->
                    val collectionId = backstackEntry.path<String>(COLLECTION_IDENTIFIER_KEY)
                    val collectionColor = backstackEntry.path<String>(COLLECTION_COLOR_KEY)
                        ?: generateRandomColor().toHex()
                    UpsertCollectionScreen(
                        collectionId = collectionId,
                        collectionColor = collectionColor
                    ).ShowContent()
                }
                scene(
                    route = "$TEAM_SCREEN/{$TEAM_IDENTIFIER_KEY}/{$NAME_KEY}"
                ) { backstackEntry ->
                    val teamId: String = backstackEntry.path<String>(TEAM_IDENTIFIER_KEY)!!
                    val name: String = backstackEntry.path<String>(NAME_KEY)!!
                    TeamScreen(
                        teamId = teamId,
                        teamName = name
                    ).ShowContent()
                }
                scene(
                    route = "$UPSERT_TEAM_SCREEN/{$TEAM_IDENTIFIER_KEY}?"
                ) { backstackEntry ->
                    val teamId = backstackEntry.path<String>(TEAM_IDENTIFIER_KEY)
                    UpsertTeamScreen(
                        teamId = teamId
                    ).ShowContent()
                }
                scene(
                    route = "$UPSERT_CUSTOM_LINK_SCREEN/{$LINK_IDENTIFIER_KEY}?"
                ) { backstackEntry ->
                    val linkId = backstackEntry.path<String>(LINK_IDENTIFIER_KEY)
                    UpsertCustomLinkScreen(
                        linkId = linkId
                    ).ShowContent()
                }
            }
        }
    }
}

/**
 * Method to initialize the Ametista system
 */
@Composable
@NonRestartableComposable
private fun InitAmetista() {
    LaunchedEffect(Unit) {
        val ametistaEngine = AmetistaEngine.ametistaEngine
        ametistaEngine.fireUp(
            configData = Res.readBytes(FILES_AMETISTA_CONFIG_PATHNAME),
            host = AmetistaConfig.HOST,
            serverSecret = AmetistaConfig.SERVER_SECRET!!,
            applicationId = AmetistaConfig.APPLICATION_IDENTIFIER!!,
            bypassSslValidation = AmetistaConfig.BYPASS_SSL_VALIDATION,
            debugMode = true // TODO: TO SET ON FALSE
        )
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
    requester = RefyRequester(
        host = localUser.hostAddress,
        userId = localUser.userId,
        userToken = localUser.userToken,
        debugMode = true // TODO: TO REMOVE
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
        HOME_SCREEN
    } else
        AUTH_SCREEN
    setUserLanguage()
    navigator.navigate(route)
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