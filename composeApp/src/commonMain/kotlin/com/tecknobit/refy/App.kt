package com.tecknobit.refy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.compose.LocalPlatformContext
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.CachePolicy
import coil3.request.addLastModifiedToFileCacheKey
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcore.helpers.NAME_KEY
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.refy.helpers.AUTH_SCREEN
import com.tecknobit.refy.helpers.COLLECTION_SCREEN
import com.tecknobit.refy.helpers.HOME_SCREEN
import com.tecknobit.refy.helpers.PROFILE_SCREEN
import com.tecknobit.refy.helpers.RefyLocalUser
import com.tecknobit.refy.helpers.RefyRequester
import com.tecknobit.refy.helpers.SPLASHSCREEN
import com.tecknobit.refy.helpers.TEAM_SCREEN
import com.tecknobit.refy.helpers.UPSERT_COLLECTION_SCREEN
import com.tecknobit.refy.helpers.UPSERT_CUSTOM_LINK_SCREEN
import com.tecknobit.refy.helpers.UPSERT_LINK_SCREEN
import com.tecknobit.refy.helpers.UPSERT_TEAM_SCREEN
import com.tecknobit.refy.helpers.customHttpClient
import com.tecknobit.refy.helpers.navToSplashscreen
import com.tecknobit.refy.helpers.navigator
import com.tecknobit.refy.ui.components.imageLoader
import com.tecknobit.refy.ui.screens.auth.presenter.AuthScreen
import com.tecknobit.refy.ui.screens.home.presenter.HomeScreen
import com.tecknobit.refy.ui.screens.profile.presenter.ProfileScreen
import com.tecknobit.refy.ui.screens.splashscreen.Splashscreen
import com.tecknobit.refy.ui.theme.RefyTheme
import com.tecknobit.refycore.COLLECTION_COLOR_KEY
import com.tecknobit.refycore.COLLECTION_IDENTIFIER_KEY
import com.tecknobit.refycore.LINK_IDENTIFIER_KEY
import com.tecknobit.refycore.TEAM_IDENTIFIER_KEY
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.Font
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
 *`localUser` the helper to manage the local sessions stored locally in
 * the device
 */
val localUser = RefyLocalUser()

/**
 *`requester` the instance to manage the requests with the backend
 */
lateinit var requester: RefyRequester


/**
 * Method to start the `Refy`'s application
 */
@Composable
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
    navigator = rememberNavController()
    RefyTheme {
        NavHost(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .statusBarsPadding(),
            navController = navigator,
            startDestination = SPLASHSCREEN
        ) {
            composable(
                route = SPLASHSCREEN
            ) {
                Splashscreen().ShowContent()
            }
            composable(
                route = AUTH_SCREEN
            ) {
                AuthScreen().ShowContent()
            }
            composable(
                route = HOME_SCREEN
            ) {
                HomeScreen().ShowContent()
            }
            composable(
                route = "$UPSERT_LINK_SCREEN/{$LINK_IDENTIFIER_KEY}?"
            ) { backstackEntry ->
//                val linkId = backstackEntry.path<String>(LINK_IDENTIFIER_KEY)
//                UpsertLinkScreen(
//                    linkId = linkId
//                ).ShowContent()
            }
            composable(
                route = PROFILE_SCREEN
            ) {
                ProfileScreen().ShowContent()
            }
            composable(
                route = "$COLLECTION_SCREEN/{$COLLECTION_IDENTIFIER_KEY}/{$NAME_KEY}/{$COLLECTION_COLOR_KEY}"
            ) { backstackEntry ->
//                val collectionId: String =
//                    backstackEntry.path<String>(COLLECTION_IDENTIFIER_KEY)!!
//                val name: String = backstackEntry.path<String>(NAME_KEY)!!
//                val color: String = backstackEntry.path<String>(COLLECTION_COLOR_KEY)!!
//                CollectionScreen(
//                    collectionId = collectionId,
//                    collectionName = name,
//                    collectionColor = color
//                ).ShowContent()
            }
            composable(
                route = "$UPSERT_COLLECTION_SCREEN/{$COLLECTION_IDENTIFIER_KEY}?/{$COLLECTION_COLOR_KEY}?"
            ) { backstackEntry ->
//                val collectionId = backstackEntry.path<String>(COLLECTION_IDENTIFIER_KEY)
//                val collectionColor = backstackEntry.path<String>(COLLECTION_COLOR_KEY)
//                    ?: generateRandomColor().toHex()
//                UpsertCollectionScreen(
//                    collectionId = collectionId,
//                    collectionColor = collectionColor
//                ).ShowContent()
            }
            composable(
                route = "$TEAM_SCREEN/{$TEAM_IDENTIFIER_KEY}/{$NAME_KEY}"
            ) { backstackEntry ->
//                val teamId: String = backstackEntry.path<String>(TEAM_IDENTIFIER_KEY)!!
//                val name: String = backstackEntry.path<String>(NAME_KEY)!!
//                TeamScreen(
//                    teamId = teamId,
//                    teamName = name
//                ).ShowContent()
            }
            composable(
                route = "$UPSERT_TEAM_SCREEN/{$TEAM_IDENTIFIER_KEY}?"
            ) { backstackEntry ->
//                val teamId = backstackEntry.path<String>(TEAM_IDENTIFIER_KEY)
//                UpsertTeamScreen(
//                    teamId = teamId
//                ).ShowContent()
            }
            composable(
                route = "$UPSERT_CUSTOM_LINK_SCREEN/{$LINK_IDENTIFIER_KEY}?"
            ) { backstackEntry ->
//                val linkId = backstackEntry.path<String>(LINK_IDENTIFIER_KEY)
//                UpsertCustomLinkScreen(
//                    linkId = linkId
//                ).ShowContent()
            }
        }
    }
    SessionFlowState.invokeOnUserDisconnected {
        localUser.clear()
        navToSplashscreen()
    }
}

/**
 * Method to check whether are available any updates for each platform and then launch the application
 * which the correct first screen to display
 *
 */
@Composable
expect fun CheckForUpdatesAndLaunch()

/**
 * Method to init the local session and the related instances then start the user session
 *
 */
fun startSession() {
    requester = RefyRequester(
        host = localUser.hostAddress,
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
                    setUserLanguage()
                },
                onFailure = { setUserLanguage() }
            )
        }
        HOME_SCREEN
    } else
        AUTH_SCREEN
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
expect fun CloseApplicationOnNavBack()