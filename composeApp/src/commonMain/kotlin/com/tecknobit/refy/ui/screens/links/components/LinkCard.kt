@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.screens.links.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tecknobit.refy.helpers.shareLink
import com.tecknobit.refy.ui.components.AttachItemButton
import com.tecknobit.refy.ui.components.AttachLink
import com.tecknobit.refy.ui.components.DeleteItemButton
import com.tecknobit.refy.ui.components.DeleteLink
import com.tecknobit.refy.ui.components.ExpandCardButton
import com.tecknobit.refy.ui.components.ItemCardDetails
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.screens.links.presentation.LinksScreenViewModel
import org.jetbrains.compose.resources.painterResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.inserted_on
import refy.composeapp.generated.resources.no_preview_available


// credits to https://cdn.prod.website-files.com/64c7b734b044b679c715bc30/6674b58d32fbc146194c888a_5%20best%20practices%20to%20design%20UI%20Cards%20for%20your%20website%402x.webp
@Composable
@NonRestartableComposable
fun LinkCard(
    modifier: Modifier = Modifier,
    viewModel: LinksScreenViewModel,
    link: RefyLinkImpl
) {
    val expanded = remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(CardDefaults.shape)
            .combinedClickable(
                onClick = {
                    uriHandler.openUri(
                        uri = link.reference
                    )
                },
                onDoubleClick = {
                    viewModel.showSnackbarMessage(
                        message = link.reference
                    )
                }
            )
    ) {
        Column(
            modifier = Modifier
                .padding(
                    all = 10.dp
                )
        ) {
            LinkThumbnail(
                link = link
            )
            ItemCardDetails(
                modifier = Modifier
                    .padding(
                        top = 5.dp,
                        start = 5.dp
                    ),
                expanded = expanded,
                item = link,
                info = Res.string.inserted_on
            )
            LinkBottomBar(
                expanded = expanded,
                viewModel = viewModel,
                link = link
            )
        }
    }
}

@Composable
@NonRestartableComposable
private fun LinkThumbnail(
    link: RefyLinkImpl
) {
    // TODO: CHECK WHETHER USE THE DEDICATED COMPONENT 
    AsyncImage(
        modifier = Modifier
            .padding(
                all = 3.dp
            )
            .clip(
                RoundedCornerShape(7.dp)
            )
            .fillMaxWidth()
            .height(200.dp),
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(link.thumbnailPreview)
            .crossfade(true)
            .crossfade(500)
            .build(),
        contentScale = ContentScale.Crop,
        error = painterResource(Res.drawable.no_preview_available),
        contentDescription = "Reference thumbnail picture"
    )
}

@Composable
@NonRestartableComposable
private fun LinkBottomBar(
    expanded: MutableState<Boolean>,
    viewModel: LinksScreenViewModel,
    link: RefyLinkImpl
) {
    Row(
        modifier = Modifier
            .padding(
                top = 5.dp
            )
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                ExpandCardButton(
                    expanded = expanded
                )
                IconButton(
                    modifier = Modifier
                        .size(28.dp),
                    onClick = {
                        shareLink(
                            viewModel = viewModel,
                            link = link
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = null
                    )
                }
                AttachItemButton(
                    attachItemContent = { state, scope ->
                        AttachLink(
                            state = state,
                            scope = scope,
                            viewModel = viewModel,
                            link = link
                        )
                    }
                )
            }
        }
        DeleteItemButton(
            item = link,
            deleteContent = { delete ->
                DeleteLink(
                    show = delete,
                    viewModel = viewModel,
                    link = link
                )
            }
        )
    }
}