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
import com.tecknobit.equinoxcore.time.TimeFormatter
import com.tecknobit.refy.displayFontFamily
import com.tecknobit.refy.requester
import com.tecknobit.refy.ui.icons.CollapseAll
import com.tecknobit.refy.ui.icons.ExpandAll
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.screens.teams.data.Team
import com.tecknobit.refy.ui.shared.data.RefyItem
import com.tecknobit.refy.ui.shared.data.RefyUser
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
import kotlin.random.Random

const val CHOOSER_PAGES = 2

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
            // TODO: MAKE THE REQUEST THEN
            val teams = listOf(
                Team(
                    id = Random.nextLong().toString(),
                    owner = RefyUser.RefyUserImpl(
                        id = Random.nextLong().toString(),
                        name = "Name",
                        surname = "Name",
                        email = "email@email.com",
                        profilePic = "",
                        tagName = "@prova"
                    ),
                    title = "Tecknobit",
                    date = TimeFormatter.currentTimestamp(),
                    links = emptyList(),
                    members = emptyList(),
                    logoPic = "https://starwalk.space/gallery/images/what-is-space/1920x1080.jpg",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed convallis dolor eu eros tristique gravida. Fusce nec fermentum dui, vitae suscipit eros. Aliquam rhoncus nunc a est sollicitudin blandit. Suspendisse dictum lorem sed vestibulum scelerisque. Morbi ac velit vel lorem molestie venenatis. Donec vitae mauris convallis, efficitur elit ac, hendrerit erat. Suspendisse nisi nunc, faucibus sit amet tellus dignissim, accumsan imperdiet nibh. Duis sapien dolor, dapibus a suscipit in, molestie eu metus. Nam volutpat lacus ut ante auctor sagittis. Donec vitae commodo sem. Curabitur at molestie nunc, quis sodales dui. Vestibulum sed dictum purus. Etiam commodo nibh vitae ex euismod commodo. Morbi egestas massa felis, vitae semper risus rutrum hendrerit. Donec facilisis eget mauris at condimentum.\n" +
                            "\n" +
                            "Pellentesque dignissim tincidunt interdum. Suspendisse egestas risus nec varius rutrum. Cras nisl ligula, consequat vel scelerisque imperdiet, lobortis at diam. Maecenas purus mauris, facilisis hendrerit viverra vel, ullamcorper in mauris. Ut consequat convallis nunc, et molestie lorem auctor sed. Duis consequat placerat sapien, at pharetra ex consectetur a. Sed nulla libero, pretium nec elit vitae, ornare tincidunt augue. Maecenas vel aliquam eros, vel dignissim magna. Proin lacinia, quam eu molestie malesuada, dolor augue tempus lectus, in dictum neque nisi in libero. Phasellus ullamcorper sapien eu nisi hendrerit, sit amet faucibus metus sagittis. Ut vitae nisi at quam venenatis malesuada vitae eu nisi. Vivamus eu elit et purus euismod rutrum. Sed a ipsum semper, facilisis quam non, blandit odio. Pellentesque maximus fringilla hendrerit.\n" +
                            "\n" +
                            "Etiam ac pellentesque nunc. Phasellus sapien elit, consequat non metus ac, viverra hendrerit erat. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Ut malesuada tellus eget eros maximus, a ornare eros vulputate. Nam suscipit tempus lorem, quis ornare enim ornare id. Fusce quis congue ex. Quisque tincidunt enim ut mollis facilisis.\n" +
                            "\n" +
                            "Cras at mauris convallis, porttitor sem euismod, placerat libero. Sed quam mauris, dictum sit amet pharetra ut, rutrum quis nisl. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Aenean condimentum augue venenatis posuere malesuada. Fusce porttitor est quis interdum blandit. In fermentum ante dapibus diam vulputate efficitur. Fusce placerat leo nec ullamcorper sollicitudin. In accumsan scelerisque eros nec feugiat. Duis odio urna, maximus ut leo quis, venenatis euismod est. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Maecenas vel pretium tortor. In suscipit massa laoreet ligula varius, eget lobortis tellus suscipit. Maecenas viverra nibh consectetur, convallis lectus id, dictum tellus.\n" +
                            "\n" +
                            "Donec eros ex, tincidunt sit amet dui et, consequat tempus nisl. Proin justo quam, tempus malesuada libero nec, malesuada pharetra lorem. In ac nisi vel tellus lobortis accumsan. Nulla facilisi. Donec eget lorem viverra, tempus ex congue, finibus orci. In id vehicula sem. Nullam vulputate leo sed sapien dapibus efficitur. Nunc tincidunt, nibh id tincidunt facilisis, nisl nunc sagittis leo, et venenatis eros purus in justo. Donec bibendum sodales efficitur. Donec at odio a velit euismod venenatis in quis tortor. Fusce imperdiet in felis in eleifend. Morbi eget posuere urna. Aliquam interdum vel lacus et suscipit. Cras a suscipit urna, vel dapibus tortor. Duis semper porttitor finibus.",
                )
            )
            appendPage(
                items = if (Random.nextBoolean())
                    listOf()
                else
                    teams, // TODO: TO USE THE REAL DATA,
                nextPageKey = page + 1, // TODO: TO USE THE REAL DATA
                isLastPage = Random.nextBoolean() // TODO: TO USE THE REAL DATA
            )
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