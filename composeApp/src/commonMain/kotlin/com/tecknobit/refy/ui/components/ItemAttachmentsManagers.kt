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
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.screens.links.presentation.LinksScreenViewModel
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.screens.teams.presentation.TeamsScreenViewModel
import com.tecknobit.refy.ui.shared.presentations.CollectionsManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.add_collections
import refy.composeapp.generated.resources.add_links
import refy.composeapp.generated.resources.add_related_collections
import refy.composeapp.generated.resources.share_collection
import refy.composeapp.generated.resources.share_link

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
                            linksCollection = collections,
                            afterShared = {
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
                    mainTitle = Res.string.share_link,
                    currentTeamsAttached = link.teams,
                    confirmAction = { teams ->
                        viewModel.shareLinkWithTeams(
                            link = link,
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

@Composable
@NonRestartableComposable
fun AttachTeam(
    state: SheetState,
    scope: CoroutineScope,
    viewModel: TeamsScreenViewModel,
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
                        viewModel.attachLinks(
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
                        viewModel.attachCollections(
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

@Composable
@NonRestartableComposable
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

@Composable
@NonRestartableComposable
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
