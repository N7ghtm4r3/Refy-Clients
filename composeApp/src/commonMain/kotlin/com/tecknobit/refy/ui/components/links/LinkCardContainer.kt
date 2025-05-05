@file:OptIn(ExperimentalFoundationApi::class)

package com.tecknobit.refy.ui.components.links

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tecknobit.equinoxcompose.viewmodels.EquinoxViewModel
import com.tecknobit.refy.UPSERT_LINK_SCREEN
import com.tecknobit.refy.helpers.shareLink
import com.tecknobit.refy.navigator
import com.tecknobit.refy.ui.components.ExpandCardButton
import com.tecknobit.refy.ui.components.ItemCardDetails
import com.tecknobit.refy.ui.components.ProfilePic
import com.tecknobit.refy.ui.screens.links.data.RefyLink
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.shared.data.RefyUser
import org.jetbrains.compose.resources.painterResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.inserted_on
import refy.composeapp.generated.resources.no_preview_available

/**
 * Container used to display a custom card with the details of a [RefyLink].
 *
 * Design inspired by [this](https://cdn.prod.website-files.com/64c7b734b044b679c715bc30/6674b58d32fbc146194c888a_5%20best%20practices%20to%20design%20UI%20Cards%20for%20your%20website%402x.webp)
 *
 * @param modifier The modifier to apply to the component
 * @param viewModel The support viewmodel for the screen
 * @param link The link to display
 * @param onClick The action to execute when the card has been clicked
 * @param onLongClick The action to execute when the card has been long clicked
 * @param showOwnerData Whether show the data of the owner of the link, for example when it is shared
 * in a team
 * @param extraInformation Extra content to display
 * @param extraButton Extra button to handle actions
 * @param cancelButton The button used to remove or delete a link
 */
@Composable
fun LinkCardContainer(
    modifier: Modifier = Modifier,
    viewModel: EquinoxViewModel,
    link: RefyLink,
    onClick: (UriHandler) -> Unit = { uriHandler -> uriHandler.openUri(link.reference) },
    onLongClick: () -> Unit = {
        navigator.navigate("$UPSERT_LINK_SCREEN/${link.id}")
    },
    showOwnerData: Boolean = false,
    extraInformation: @Composable (() -> Unit)? = null,
    extraButton: @Composable (() -> Unit)? = null,
    cancelButton: @Composable RowScope.() -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    val descriptionLines = remember { mutableIntStateOf(0) }
    val uriHandler = LocalUriHandler.current
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clip(CardDefaults.shape)
            .combinedClickable(
                onClick = { onClick.invoke(uriHandler) },
                onDoubleClick = {
                    viewModel.showSnackbarMessage(
                        message = link.reference
                    )
                },
                onLongClick = if (link.iAmTheOwner()) {
                    {
                        onLongClick()
                    }
                } else
                    null
            )
    ) {
        if (showOwnerData) {
            OwnerData(
                owner = link.owner
            )
        }
        Column(
            modifier = Modifier
                .padding(
                    all = 10.dp
                )
        ) {
            if (link is RefyLinkImpl) {
                LinkThumbnail(
                    link = link
                )
            }
            ItemCardDetails(
                modifier = Modifier
                    .padding(
                        top = 5.dp,
                        start = 5.dp
                    ),
                expanded = expanded,
                item = link,
                info = Res.string.inserted_on,
                extraInformation = extraInformation,
                descriptionLines = descriptionLines
            )
            LinkBottomBar(
                expanded = expanded,
                viewModel = viewModel,
                link = link,
                extraButton = extraButton,
                cancelButton = cancelButton,
                descriptionLines = descriptionLines
            )
        }
    }
}

/**
 * The data of the owner of the [LinkCardContainer]
 *
 * @param owner The owner of the link
 */
@Composable
@NonRestartableComposable
private fun OwnerData(
    owner: RefyUser
) {
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        ),
        overlineContent = {
            Text(
                text = owner.tagName,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingContent = {
            ProfilePic(
                profilePic = owner.profilePic,
                size = 40.dp
            )
        },
        headlineContent = {
            Text(
                text = owner.completeName(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
    HorizontalDivider(
        color = MaterialTheme.colorScheme.primary
    )
}

/**
 * The thumbnail of the link fetched from the **og:image** metadata
 *
 * @param link The link from fetch its **og:image**
 */
@Composable
@NonRestartableComposable
private fun LinkThumbnail(
    link: RefyLinkImpl
) {
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

/**
 * The bottom bar of the [LinkCardContainer]
 *
 * @param expanded Whether the card is expanded
 * @param viewModel The support viewmodel for the screen
 * @param link The link to display
 * in a team
 * @param extraButton Extra button to handle actions
 * @param cancelButton The button used to remove or delete a link
 * @param descriptionLines The number of the lines occupied by the link description
 */
@Composable
@NonRestartableComposable
private fun LinkBottomBar(
    expanded: MutableState<Boolean>,
    viewModel: EquinoxViewModel,
    link: RefyLink,
    extraButton: @Composable (() -> Unit)? = null,
    cancelButton: @Composable RowScope.() -> Unit,
    descriptionLines: MutableState<Int>
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
                    descriptionLines = descriptionLines,
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
                extraButton?.let {
                    extraButton()
                }
            }
        }
        cancelButton()
    }
}