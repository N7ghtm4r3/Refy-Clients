@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.screens.links.presentation.LinksScreenViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import refy.composeapp.generated.resources.Res
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
                    pages[index]()
                }
            }
        }
    }
}
