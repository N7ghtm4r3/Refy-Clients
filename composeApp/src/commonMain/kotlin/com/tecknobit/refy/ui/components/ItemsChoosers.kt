package com.tecknobit.refy.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.components.ChameleonText
import com.tecknobit.equinoxcompose.utilities.toColor
import com.tecknobit.equinoxcore.annotations.Wrapper
import com.tecknobit.equinoxcore.mergeIfNotContained
import com.tecknobit.equinoxcore.network.Requester.Companion.sendPaginatedRequest
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.refy.displayFontFamily
import com.tecknobit.refy.requester
import com.tecknobit.refy.ui.icons.CollapseAll
import com.tecknobit.refy.ui.icons.ExpandAll
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.shared.data.RefyItem
import com.tecknobit.refy.ui.theme.AppTypography
import com.tecknobit.refy.ui.theme.green
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import io.github.ahmad_hamwi.compose.pagination.rememberPaginationState
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.add_related_links
import refy.composeapp.generated.resources.collection_color
import refy.composeapp.generated.resources.confirm
import refy.composeapp.generated.resources.with_collections
import refy.composeapp.generated.resources.with_teams

/**
 * `CHOOSER_PAGES` constant value of the number of the pages displayed in the choosers
 */
const val CHOOSER_PAGES = 2

/**
 * Custom component used to choose the links to share in collections or teams
 *
 * @param currentAttachedLinks The list of the current links attached in collections or teams
 * @param linksAddedSupportList The list used to contains the links added to be attached
 */
@Wrapper
@Composable
@NonRestartableComposable
fun LinksChooser(
    currentAttachedLinks: List<RefyLinkImpl>,
    linksAddedSupportList: SnapshotStateList<RefyLinkImpl>
) {
    LinksChooser(
        lazyColumSize = 250.dp,
        mainTitle = null,
        currentAttachedLinks = currentAttachedLinks,
        linksAddedSupportList = linksAddedSupportList,
        confirmAction = null
    )
}

/**
 * Custom component used to choose the links to share in collections or teams
 *
 * @param lazyColumSize The size of the [PaginatedLazyColumn] used to display the links
 * @param mainTitle The main title of the chooser
 * @param currentAttachedLinks The list of the current links attached in collections or teams
 * @param linksAddedSupportList The list used to contains the links added to be attached
 * @param confirmAction The action to execute when the user confirm the choose
 */
@Wrapper
@Composable
@NonRestartableComposable
fun LinksChooser(
    lazyColumSize: Dp = (-1).dp,
    mainTitle: StringResource?,
    currentAttachedLinks: List<RefyLinkImpl>,
    linksAddedSupportList: SnapshotStateList<RefyLinkImpl> = mutableStateListOf(),
    confirmAction: ((List<RefyLinkImpl>) -> Unit)?
) {
    val linksState = rememberPaginationState(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            MainScope().launch {
                requester.sendPaginatedRequest(
                    request = {
                        getLinks(
                            ownedOnly = true,
                            page = page
                        )
                    },
                    serializer = RefyLinkImpl.serializer(),
                    onSuccess = { paginatedResponse ->
                        appendPage(
                            items = paginatedResponse.data,
                            nextPageKey = paginatedResponse.nextPage,
                            isLastPage = paginatedResponse.isLastPage
                        )
                    },
                    onFailure = { }
                )
            }
        }
    )
    ItemsChooser(
        lazyColumSize = lazyColumSize,
        mainTitle = mainTitle,
        subTitle = Res.string.add_related_links,
        currentItemsAttached = currentAttachedLinks,
        itemsAddedSupportList = linksAddedSupportList,
        itemsState = linksState,
        confirmAction = if (confirmAction != null) {
            { links ->
                confirmAction.invoke(links)
            }
        } else
            null,
        pageEmptyIndicator = { EmptyLinks() },
        overlineContent = { link ->
            Text(
                text = link.reference,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}

/**
 * Custom component used to choose the collections where share the links or to share in teams
 *
 * @param mainTitle The main title of the chooser
 * @param subTitle The subtitle title of the chooser
 * @param currentLinksCollectionsAttached The list of the current collections attached or shared
 * @param confirmAction The action to execute when the user confirm the choose
 */
@Wrapper
@Composable
@NonRestartableComposable
fun LinksCollectionsChooser(
    mainTitle: StringResource,
    subTitle: StringResource = Res.string.with_collections,
    currentLinksCollectionsAttached: List<LinksCollection>,
    confirmAction: (List<LinksCollection>) -> Unit
) {
    val linksCollectionState = rememberPaginationState(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            MainScope().launch {
                requester.sendPaginatedRequest(
                    request = {
                        getCollections(
                            ownedOnly = true,
                            page = page
                        )
                    },
                    serializer = LinksCollection.serializer(),
                    onSuccess = { paginatedResponse ->
                        appendPage(
                            items = paginatedResponse.data,
                            nextPageKey = paginatedResponse.nextPage,
                            isLastPage = paginatedResponse.isLastPage
                        )
                    },
                    onFailure = {}
                )
            }
        }
    )
    ItemsChooser(
        mainTitle = mainTitle,
        subTitle = subTitle,
        currentItemsAttached = currentLinksCollectionsAttached,
        itemsState = linksCollectionState,
        confirmAction = { collections ->
            confirmAction.invoke(collections)
        },
        pageEmptyIndicator = { EmptyCollections() },
        overlineContent = { collection ->
            val collectionColor = collection.color.toColor()
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = collectionColor
                ),
                shape = RoundedCornerShape(
                    size = 5.dp
                )
            ) {
                ChameleonText(
                    modifier = Modifier
                        .padding(
                            horizontal = 8.dp
                        ),
                    text = stringResource(Res.string.collection_color),
                    backgroundColor = collectionColor
                )
            }
        }
    )
}

/**
 * Custom component used to choose the teams where share links or collections
 *
 * @param mainTitle The main title of the chooser
 * @param currentTeamsAttached The list of the current teams attached
 * @param confirmAction The action to execute when the user confirm the choose
 */
@Wrapper
@Composable
@NonRestartableComposable
fun TeamsChooser(
    mainTitle: StringResource,
    currentTeamsAttached: List<Team>,
    confirmAction: (List<Team>) -> Unit
) {
    val teamsState = rememberPaginationState(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            MainScope().launch {
                requester.sendPaginatedRequest(
                    request = {
                        getTeams(
                            ownedOnly = true,
                            page = page
                        )
                    },
                    serializer = Team.serializer(),
                    onSuccess = { paginatedResponse ->
                        appendPage(
                            items = paginatedResponse.data,
                            nextPageKey = paginatedResponse.nextPage,
                            isLastPage = paginatedResponse.isLastPage
                        )
                    },
                    onFailure = {}
                )
            }
        }
    )
    ItemsChooser(
        mainTitle = mainTitle,
        subTitle = Res.string.with_teams,
        currentItemsAttached = currentTeamsAttached,
        itemsState = teamsState,
        confirmAction = { teams ->
            confirmAction.invoke(teams)
        },
        pageEmptyIndicator = { EmptyTeams() },
        overlineContent = { team ->
            TeamLogo(
                team = team
            )
        }
    )
}

/**
 * The header of the [ItemsChooser]
 *
 * @param mainTitle The main title of the chooser
 * @param subTitle The subtitle title of the chooser
 * @param confirmAction The action to execute when the user confirm the choose
 */
@Composable
@NonRestartableComposable
private fun ChooserHeader(
    mainTitle: StringResource,
    subTitle: StringResource,
    confirmAction: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(
                    start = 16.dp,
                    bottom = 5.dp
                )
        ) {
            Text(
                text = stringResource(mainTitle),
                fontSize = 20.sp,
                fontFamily = displayFontFamily,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(subTitle),
                style = AppTypography.labelLarge
            )
        }
        Button(
            modifier = Modifier
                .padding(
                    end = 12.dp,
                    bottom = 5.dp
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = green(),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(
                size = 12.dp
            ),
            onClick = confirmAction
        ) {
            Text(
                text = stringResource(Res.string.confirm)
            )
        }
    }
    HorizontalDivider()
}

/**
 * Custom component used to choose the items to share or the items where share
 *
 * @param lazyColumSize The size of the [PaginatedLazyColumn] used to display the links
 * @param mainTitle The main title of the chooser
 * @param subTitle The subtitle title of the chooser
 * @param currentItemsAttached The list of the current items attached
 * @param itemsAddedSupportList The list used to contains the items added to be attached or shared
 * @param itemsState The state used to handle the pagination of the items list
 * @param confirmAction The action to execute when the user confirm the choose
 * @param pageEmptyIndicator The indicator to use when the items are no available
 * @param overlineContent Custom content to display in the overline section
 */
@Composable
@NonRestartableComposable
private fun <T : RefyItem> ItemsChooser(
    lazyColumSize: Dp = (-1).dp,
    mainTitle: StringResource? = null,
    subTitle: StringResource? = null,
    currentItemsAttached: List<T>,
    itemsAddedSupportList: SnapshotStateList<T> = mutableStateListOf(),
    itemsState: PaginationState<Int, T>,
    confirmAction: ((List<T>) -> Unit)?,
    pageEmptyIndicator: @Composable () -> Unit,
    overlineContent: @Composable ((T) -> Unit)? = null
) {
    val itemsAdded = remember { itemsAddedSupportList }
    mainTitle?.let {
        ChooserHeader(
            mainTitle = mainTitle,
            subTitle = subTitle!!,
            confirmAction = { confirmAction!!.invoke(itemsAdded) }
        )
    }
    LaunchedEffect(Unit) {
        itemsAdded.mergeIfNotContained(
            collectionToMerge = currentItemsAttached
        )
    }
    PaginatedLazyColumn(
        modifier = Modifier
            .then(
                if (lazyColumSize == (-1).dp)
                    Modifier.fillMaxSize()
                else {
                    Modifier
                        .heightIn(
                            max = lazyColumSize
                        )
                }
            )
            .animateContentSize(),
        paginationState = itemsState,
        firstPageProgressIndicator = { FirstPageProgressIndicator() },
        newPageProgressIndicator = { NewPageProgressIndicator() },
        firstPageEmptyIndicator = pageEmptyIndicator
    ) {
        items(
            items = itemsState.allItems!!,
            key = { item -> item.id }
        ) { item ->
            ChooserItem(
                itemsAdded = itemsAdded,
                mainItem = item,
                overlineContent = overlineContent
            )
        }
    }
}

/**
 * The chooser of the item to share or where attach
 *
 * @param itemsAdded The list used to contains the items added to be attached or shared
 * @param mainItem The item where attach or share the [itemsAdded]
 * @param overlineContent Custom content to display in the overline section
 */
@Composable
@NonRestartableComposable
private fun <T : RefyItem> ChooserItem(
    itemsAdded: SnapshotStateList<T>,
    mainItem: T,
    overlineContent: @Composable ((T) -> Unit)? = null
) {
    val expanded = remember { mutableStateOf(false) }
    var contained by remember {
        mutableStateOf(itemsAdded.any { itemAdded -> itemAdded.id == mainItem.id })
    }
    var descriptionLines by remember { mutableIntStateOf(0) }
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        ),
        leadingContent = {
            IconButton(
                onClick = {
                    contained = !contained
                    if (contained)
                        itemsAdded.add(mainItem)
                    else
                        itemsAdded.removeAll { item -> item.id == mainItem.id }
                }
            ) {
                Icon(
                    imageVector = if (contained)
                        Icons.Default.Cancel
                    else
                        Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (contained)
                        MaterialTheme.colorScheme.error
                    else
                        green()
                )
            }
        },
        overlineContent = if (overlineContent != null) {
            {
                overlineContent(mainItem)
            }
        } else
            null,
        headlineContent = {
            Text(
                text = mainItem.title
            )
        },
        supportingContent = {
            Text(
                modifier = Modifier
                    .heightIn(
                        max = 500.dp
                    )
                    .verticalScroll(rememberScrollState())
                    .animateContentSize(),
                text = mainItem.description,
                textAlign = TextAlign.Justify,
                onTextLayout = { descriptionLines = it.lineCount },
                minLines = 3,
                maxLines = if (expanded.value)
                    Int.MAX_VALUE
                else
                    3
            )
        },
        trailingContent = if (descriptionLines >= MINIMUM_DESCRIPTION_LINES) {
            {
                IconButton(
                    modifier = Modifier
                        .size(30.dp),
                    onClick = { expanded.value = !expanded.value }
                ) {
                    Icon(
                        imageVector = if (expanded.value)
                            CollapseAll
                        else
                            ExpandAll,
                        contentDescription = null
                    )
                }
            }
        } else
            null
    )
}