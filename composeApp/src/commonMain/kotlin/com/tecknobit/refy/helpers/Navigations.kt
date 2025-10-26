package com.tecknobit.refy.helpers

import androidx.navigation.NavHostController
import com.tecknobit.equinoxcompose.annotations.DestinationScreen
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

// TODO: TO DOCU SINCE 
@DestinationScreen(Splashscreen::class)
fun navToSplashscreen() {
    navigator.navigate(SPLASHSCREEN)
}

@DestinationScreen(HomeScreen::class)
fun navToHome() {
    navigator.navigate(HOME_SCREEN)
}

@DestinationScreen(CollectionScreen::class)
fun navToCollectionScreen(
    linkCollection: LinksCollection,
) {
    navigator.navigate(COLLECTION_SCREEN)
}

@DestinationScreen(TeamScreen::class)
fun navToTeamScreen(
    team: Team,
) {
    navigator.navigate(TEAM_SCREEN)
}

@DestinationScreen(UpsertLinkScreen::class)
fun navToUpsertLinkScreen(
    link: RefyLink? = null,
) {
    navigator.navigate(UPSERT_LINK_SCREEN)
}

@DestinationScreen(UpsertCustomLinkScreen::class)
fun navToUpsertCustomLinkScreen(
    link: CustomRefyLink? = null,
) {
    navigator.navigate(UPSERT_CUSTOM_LINK_SCREEN)
}

@DestinationScreen(UpsertCollectionScreen::class)
fun navToUpsertLinkCollectionScreen(
    linksCollection: LinksCollection? = null,
) {
    navigator.navigate(UPSERT_COLLECTION_SCREEN)
}

@DestinationScreen(UpsertTeamScreen::class)
fun navToUpsertTeam(
    team: Team? = null,
) {
    navigator.navigate(UPSERT_TEAM_SCREEN)
}

@DestinationScreen(ProfileScreen::class)
fun navToProfileScreen() {
    navigator.navigate(PROFILE_SCREEN)
}