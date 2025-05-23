@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.tecknobit.refy.ui.screens.links.presentation.LinksScreenViewModel
import com.tecknobit.refy.ui.shared.data.LinksCollection
import com.tecknobit.refy.ui.shared.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.shared.data.Team
import com.tecknobit.refy.ui.shared.presentations.CollectionsManager
import com.tecknobit.refy.ui.shared.presentations.TeamsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.add_collections
import refy.composeapp.generated.resources.add_links
import refy.composeapp.generated.resources.add_related_collections
import refy.composeapp.generated.resources.share_collection
import refy.composeapp.generated.resources.share_link

/**
 * Component used to attach, so share, the [link] in collections or in teams
 *
 * @param state The state useful to manage the visibility of the [ModalBottomSheet]
 * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
 * @param viewModel The support viewmodel for the screen
 * @param link The link to share
 */
@Composable
@NonRestartableComposable
fun AttachLink(
    state: SheetState,
    scope: CoroutineScope,
    viewModel: LinksScreenViewModel,
    link: RefyLinkImpl
) {
    AttachItem(
        state = state,
        scope = scope,
        pages = arrayOf(
            {
                LinksCollectionsChooser(
                    mainTitle = Res.string.share_link,
                    currentLinksCollectionsAttached = link.collections,
                    confirmAction = { collections ->
                        viewModel.shareLinkWithCollections(
                            link = link,
                            collections = collections,
                            afterShared = {
                                scope.launch {
                                    state.hide()
                                    viewModel.refresh()
                                }
                            }
                        )
                    }
                )
            },
            {
                TeamsChooser(
                    mainTitle = Res.string.share_link,
                    currentTeamsAttached = link.teams,
                    confirmAction = { teams ->
                        viewModel.shareLinkWithTeams(
                            link = link,
                            teams = teams,
                            afterShared = {
                                scope.launch {
                                    state.hide()
                                    viewModel.refresh()
                                }
                            }
                        )
                    }
                )
            }
        )
    )
}

/**
 * Component used to attach links to the [collection] or share it in teams
 *
 * @param state The state useful to manage the visibility of the [ModalBottomSheet]
 * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
 * @param collectionsManager The manager of the collections list data
 * @param collection The collection where share or to share
 */
@Composable
@NonRestartableComposable
fun AttachCollection(
    state: SheetState,
    scope: CoroutineScope,
    collectionsManager: CollectionsManager,
    collection: LinksCollection
) {
    AttachItem(
        state = state,
        scope = scope,
        pages = arrayOf(
            {
                LinksChooser(
                    mainTitle = Res.string.add_links,
                    currentAttachedLinks = collection.links,
                    confirmAction = { links ->
                        collectionsManager.attachLinks(
                            collection = collection,
                            links = links,
                            afterAttached = {
                                scope.launch {
                                    state.hide()
                                }
                            }
                        )
                    }
                )
            },
            {
                TeamsChooser(
                    mainTitle = Res.string.share_collection,
                    currentTeamsAttached = collection.teams,
                    confirmAction = { teams ->
                        collectionsManager.shareWithTeams(
                            collection = collection,
                            teams = teams,
                            afterShared = {
                                scope.launch {
                                    state.hide()
                                }
                            }
                        )
                    }
                )
            }
        )
    )
}

/**
 * Component used to attach links or collections to the [team]
 *
 * @param state The state useful to manage the visibility of the [ModalBottomSheet]
 * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
 * @param teamsManager The manager of the teams list data
 * @param team The team where share the items
 */
@Composable
@NonRestartableComposable
fun AttachTeam(
    state: SheetState,
    scope: CoroutineScope,
    teamsManager: TeamsManager,
    team: Team
) {
    AttachItem(
        state = state,
        scope = scope,
        pages = arrayOf(
            {
                LinksChooser(
                    mainTitle = Res.string.add_links,
                    currentAttachedLinks = team.links,
                    confirmAction = { links ->
                        teamsManager.attachLinks(
                            team = team,
                            links = links,
                            afterAttached = {
                                scope.launch {
                                    state.hide()
                                }
                            }
                        )
                    }
                )
            },
            {
                LinksCollectionsChooser(
                    mainTitle = Res.string.add_collections,
                    subTitle = Res.string.add_related_collections,
                    currentLinksCollectionsAttached = team.collections,
                    confirmAction = { collections ->
                        teamsManager.attachCollections(
                            team = team,
                            collections = collections,
                            afterAttached = {
                                scope.launch {
                                    state.hide()
                                }
                            }
                        )
                    }
                )
            }
        )
    )
}

/**
 * Component used to handle the attach or sharing actions
 *
 * @param state The state useful to manage the visibility of the [ModalBottomSheet]
 * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
 * @param pages The pages to display where the user can select the items to attach or to share
 */
@Composable
private fun AttachItem(
    state: SheetState,
    scope: CoroutineScope,
    vararg pages: @Composable () -> Unit
) {
    if (state.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    state.hide()
                }
            }
        ) {
            val pagerState = rememberPagerState(
                pageCount = { CHOOSER_PAGES }
            )
            HorizontalPager(
                modifier = Modifier.pointerInput(Unit) {
                    detectDragGestures { _, dragAmount ->
                        scope.launch {
                            when {
                                dragAmount.x > 4 -> {
                                    pagerState.animateScrollToPage(
                                        page = pagerState.currentPage - 1
                                    )
                                }

                                dragAmount.x < -4 -> {
                                    pagerState.animateScrollToPage(
                                        page = pagerState.currentPage + 1
                                    )
                                }
                            }
                        }
                    }
                },
                state = pagerState
            ) { index ->
                Column(
                    modifier = Modifier
                        .height(500.dp)
                ) {
                    PagerIndicator(
                        state = pagerState,
                        currentPage = index
                    )
                    pages[index]()
                }
            }
        }
    }
}

/**
 * Component used to indicate the current page displayed by the [AttachItem] component
 *
 * @param state The state of the pager
 * @param currentPage The current page displayed
 */
@Composable
private fun PagerIndicator(
    state: PagerState,
    currentPage: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = 10.dp
            ),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(state.pageCount) { page ->
            Box(
                modifier = Modifier
                    .padding(
                        end = 10.dp
                    )
                    .clip(
                        RoundedCornerShape(
                            size = 2.dp
                        )
                    )
                    .width(85.dp)
                    .height(4.dp)
                    .background(
                        if (currentPage == page)
                            MaterialTheme.colorScheme.primary
                        else
                            Color.LightGray
                    ),
                content = {}
            )
        }
    }
}
