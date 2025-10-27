@file:OptIn(ExperimentalStdlibApi::class)

package com.tecknobit.refy.helpers

import androidx.navigation.NavHostController
import com.tecknobit.equinoxcompose.annotations.DestinationScreen
import com.tecknobit.equinoxcore.helpers.NAME_KEY
import com.tecknobit.equinoxmisc.navigationcomposeutil.navWithData
import com.tecknobit.refy.ui.screens.collection.presenter.CollectionScreen
import com.tecknobit.refy.ui.screens.customs.data.CustomRefyLink
import com.tecknobit.refy.ui.screens.home.presenter.HomeScreen
import com.tecknobit.refy.ui.screens.profile.presenter.ProfileScreen
import com.tecknobit.refy.ui.screens.splashscreen.Splashscreen
import com.tecknobit.refy.ui.screens.team.presenter.TeamScreen
import com.tecknobit.refy.ui.screens.upsertcollection.presenter.UpsertCollectionScreen
import com.tecknobit.refy.ui.screens.upsertcustomlink.presenter.UpsertCustomLinkScreen
import com.tecknobit.refy.ui.screens.upsertlink.presenter.UpsertLinkScreen
import com.tecknobit.refy.ui.screens.upsertteam.presenter.UpsertTeamScreen
import com.tecknobit.refy.ui.shared.data.LinksCollection
import com.tecknobit.refy.ui.shared.data.RefyLink
import com.tecknobit.refy.ui.shared.data.Team
import com.tecknobit.refycore.COLLECTION_COLOR_KEY
import com.tecknobit.refycore.COLLECTION_IDENTIFIER_KEY
import com.tecknobit.refycore.LINK_IDENTIFIER_KEY
import com.tecknobit.refycore.TEAM_IDENTIFIER_KEY

/**
 * `navigator` the navigator instance is useful to manage the navigation between the screens of the application
 */
lateinit var navigator: NavHostController

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
 * Method used to navigate to the [Splashscreen]
 *
 * @since 1.1.0
 */
@DestinationScreen(Splashscreen::class)
fun navToSplashscreen() {
    navigator.navigate(SPLASHSCREEN)
}

/**
 * Method used to navigate to the [HomeScreen]
 *
 * @since 1.1.0
 */
@DestinationScreen(HomeScreen::class)
fun navToHome() {
    navigator.navigate(HOME_SCREEN)
}

/**
 * Method used to navigate to the [UpsertLinkScreen]
 *
 * @param link The link to edit if not `null`
 *
 * @since 1.1.0
 */
@DestinationScreen(UpsertLinkScreen::class)
fun navToUpsertLinkScreen(
    link: RefyLink? = null,
) {
    navigator.navWithData(
        route = UPSERT_LINK_SCREEN,
        data = buildMap {
            put(LINK_IDENTIFIER_KEY, link?.id)
        }
    )
}

/**
 * Method used to navigate to the [HomeScreen]
 *
 * @param linksCollection The collection to display
 *
 * @since 1.1.0
 */
@DestinationScreen(CollectionScreen::class)
fun navToCollectionScreen(
    linksCollection: LinksCollection,
) {
    navigator.navWithData(
        route = COLLECTION_SCREEN,
        data = buildMap {
            put(COLLECTION_IDENTIFIER_KEY, linksCollection.id)
            put(NAME_KEY, linksCollection.title)
            put(COLLECTION_COLOR_KEY, linksCollection.color)
        }
    )
}

/**
 * Method used to navigate to the [UpsertCollectionScreen]
 *
 * @param linksCollection The collection to edit if not `null`
 *
 * @since 1.1.0
 */
@DestinationScreen(UpsertCollectionScreen::class)
fun navToUpsertLinkCollectionScreen(
    linksCollection: LinksCollection? = null,
) {
    val navData = if (linksCollection == null)
        emptyMap<String, Any>()
    else {
        buildMap {
            put(COLLECTION_IDENTIFIER_KEY, linksCollection.id)
            put(COLLECTION_COLOR_KEY, linksCollection.color)
        }
    }
    navigator.navWithData(
        route = UPSERT_COLLECTION_SCREEN,
        data = navData
    )
}

/**
 * Method used to navigate to the [TeamScreen]
 *
 * @param team The team to display
 *
 * @since 1.1.0
 */
@DestinationScreen(TeamScreen::class)
fun navToTeamScreen(
    team: Team,
) {
    navigator.navWithData(
        route = TEAM_SCREEN,
        data = buildMap {
            put(TEAM_IDENTIFIER_KEY, team.id)
            put(NAME_KEY, team.title)
        }
    )
}

/**
 * Method used to navigate to the [UpsertTeamScreen]
 *
 * @param team The team to edit if not `null`
 *
 * @since 1.1.0
 */
@DestinationScreen(UpsertTeamScreen::class)
fun navToUpsertTeam(
    team: Team? = null,
) {
    navigator.navWithData(
        route = UPSERT_TEAM_SCREEN,
        data = buildMap {
            put(TEAM_IDENTIFIER_KEY, team?.id)
        }
    )
}

/**
 * Method used to navigate to the [UpsertCustomLinkScreen]
 *
 * @param link The link to edit if not `null`
 *
 * @since 1.1.0
 */
@DestinationScreen(UpsertCustomLinkScreen::class)
fun navToUpsertCustomLinkScreen(
    link: CustomRefyLink? = null,
) {
    navigator.navWithData(
        route = UPSERT_CUSTOM_LINK_SCREEN,
        data = buildMap {
            put(LINK_IDENTIFIER_KEY, link?.id)
        }
    )
}

/**
 * Method used to navigate to the [ProfileScreen]
 *
 * @since 1.1.0
 */
@DestinationScreen(ProfileScreen::class)
fun navToProfileScreen() {
    navigator.navigate(PROFILE_SCREEN)
}