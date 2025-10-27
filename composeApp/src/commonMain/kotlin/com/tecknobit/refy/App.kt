@file:OptIn(ExperimentalStdlibApi::class)

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
import com.tecknobit.equinoxcompose.session.screens.equinoxScreen
import com.tecknobit.equinoxcompose.session.sessionflow.SessionFlowState
import com.tecknobit.equinoxcompose.utilities.generateRandomColor
import com.tecknobit.equinoxcompose.utilities.toHex
import com.tecknobit.equinoxcore.helpers.NAME_KEY
import com.tecknobit.equinoxcore.network.Requester.Companion.toResponseData
import com.tecknobit.equinoxcore.network.sendRequest
import com.tecknobit.equinoxmisc.navigationcomposeutil.clearLastDestinationAllNavData
import com.tecknobit.equinoxmisc.navigationcomposeutil.getDestinationNavData
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
import com.tecknobit.refy.ui.screens.collection.presenter.CollectionScreen
import com.tecknobit.refy.ui.screens.home.presenter.HomeScreen
import com.tecknobit.refy.ui.screens.profile.presenter.ProfileScreen
import com.tecknobit.refy.ui.screens.splashscreen.Splashscreen
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
                val splashscreen = equinoxScreen { Splashscreen() }
                splashscreen.ShowContent()
            }
            composable(
                route = AUTH_SCREEN
            ) {
                val authScreen = equinoxScreen { AuthScreen() }
                authScreen.ShowContent()
            }
            composable(
                route = HOME_SCREEN
            ) {
                navigator.clearLastDestinationAllNavData()
                val homeScreen = equinoxScreen { HomeScreen() }
                homeScreen.ShowContent()
            }
            composable(
                route = UPSERT_LINK_SCREEN
            ) {
                val linkId: String? = navigator.getDestinationNavData(
                    key = LINK_IDENTIFIER_KEY
                )
                val upsertLinkScreen = equinoxScreen {
                    UpsertLinkScreen(
                        linkId = linkId
                    )
                }
                upsertLinkScreen.ShowContent()
            }
            composable(
                route = PROFILE_SCREEN
            ) {
                val profileScreen = equinoxScreen { ProfileScreen() }
                profileScreen.ShowContent()
            }
            composable(
                route = COLLECTION_SCREEN
            ) {
                val collectionId: String = navigator.getDestinationNavData(
                    key = COLLECTION_IDENTIFIER_KEY,
                    defaultValue = ""
                )!!
                val name: String = navigator.getDestinationNavData(
                    key = NAME_KEY,
                    defaultValue = ""
                )!!
                val color: String = navigator.getDestinationNavData(
                    key = COLLECTION_COLOR_KEY,
                    defaultValue = ""
                )!!
                val collectionScreen = equinoxScreen {
                    CollectionScreen(
                        collectionId = collectionId,
                        collectionName = name,
                        collectionColor = color
                    )
                }
                collectionScreen.ShowContent()
            }
            composable(
                route = UPSERT_COLLECTION_SCREEN
            ) {
                val collectionId: String? = navigator.getDestinationNavData(
                    key = COLLECTION_IDENTIFIER_KEY
                )
                val color: String = navigator.getDestinationNavData(
                    key = COLLECTION_COLOR_KEY,
                    defaultValue = generateRandomColor().toHex()
                )!!
                val upsertCollectionScreen = equinoxScreen {
                    UpsertCollectionScreen(
                        collectionId = collectionId,
                        collectionColor = color
                    )
                }
                upsertCollectionScreen.ShowContent()
            }
            composable(
                route = TEAM_SCREEN
            ) {
                val teamId: String = navigator.getDestinationNavData(
                    key = TEAM_IDENTIFIER_KEY,
                    defaultValue = ""
                )!!
                val name: String = navigator.getDestinationNavData(
                    key = NAME_KEY,
                    defaultValue = ""
                )!!
                val teamScreen = equinoxScreen {
                    TeamScreen(
                        teamId = teamId,
                        teamName = name
                    )
                }
                teamScreen.ShowContent()
            }
            composable(
                route = UPSERT_TEAM_SCREEN
            ) {
                val teamId: String? = navigator.getDestinationNavData(
                    key = TEAM_IDENTIFIER_KEY
                )
                val upsertTeamScreen = equinoxScreen {
                    UpsertTeamScreen(
                        teamId = teamId
                    )
                }
                upsertTeamScreen.ShowContent()
            }
            composable(
                route = UPSERT_CUSTOM_LINK_SCREEN
            ) {
                val linkId: String? = navigator.getDestinationNavData(
                    key = LINK_IDENTIFIER_KEY
                )
                val upsertCustomLinkScreen = equinoxScreen {
                    UpsertCustomLinkScreen(
                        linkId = linkId
                    )
                }
                upsertCustomLinkScreen.ShowContent()
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