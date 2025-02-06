package com.tecknobit.refy.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FolderOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.ChameleonText
import com.tecknobit.equinoxcompose.components.EmptyListUI
import com.tecknobit.equinoxcompose.utilities.generateRandomColor
import com.tecknobit.equinoxcompose.utilities.toColor
import com.tecknobit.equinoxcompose.utilities.toHex
import com.tecknobit.equinoxcore.pagination.PaginatedResponse.Companion.DEFAULT_PAGE
import com.tecknobit.refy.ui.icons.CollapseAll
import com.tecknobit.refy.ui.icons.ExpandAll
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.shared.data.RefyItem
import com.tecknobit.refy.ui.shared.data.RefyUser
import io.github.ahmad_hamwi.compose.pagination.PaginatedLazyColumn
import io.github.ahmad_hamwi.compose.pagination.PaginationState
import io.github.ahmad_hamwi.compose.pagination.rememberPaginationState
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.collection_color
import refy.composeapp.generated.resources.no_collections_yet
import kotlin.random.Random

@Composable
@NonRestartableComposable
fun LinksCollectionsChooser(
    mainItem: RefyItem,
    currentLinksCollectionsAttached: List<LinksCollection>
) {
    val linksCollectionsState = rememberPaginationState(
        initialPageKey = DEFAULT_PAGE,
        onRequestPage = { page ->
            loadOwnedLinksCollections(
                page = page
            )
        }
    )
    PaginatedLazyColumn(
        modifier = Modifier
            .animateContentSize(),
        paginationState = linksCollectionsState,
        firstPageProgressIndicator = { FirstPageProgressIndicator() },
        newPageProgressIndicator = { NewPageProgressIndicator() },
        firstPageEmptyIndicator = {
            EmptyListUI(
                containerModifier = Modifier
                    .heightIn(
                        max = 500.dp
                    ),
                icon = Icons.Default.FolderOff,
                subText = Res.string.no_collections_yet
            )
        }
    ) {
        items(
            items = linksCollectionsState.allItems!!,
            key = { collection -> collection.id }
        ) { collection ->
            LinksCollectionItem(
                collection = collection
            )
        }
    }
}

private fun PaginationState<Int, LinksCollection>.loadOwnedLinksCollections(
    page: Int
) {
    // TODO: MAKE THE REQUEST THEN
    val linksCollection = listOf(
        LinksCollection(
            id = Random.nextLong().toString(),
            owner = RefyUser.RefyUserImpl(
                id = Random.nextLong().toString(),
                name = "Name",
                surname = "Name",
                email = "email@email.com",
                profilePic = "",
                tagName = "@prova"
            ),
            color = generateRandomColor().toHex(),
            title = "gag",
            links = emptyList(),
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
        items = linksCollection, // TODO: TO USE THE REAL DATA
        nextPageKey = page + 1, // TODO: TO USE THE REAL DATA
        isLastPage = Random.nextBoolean() // TODO: TO USE THE REAL DATA
    )
}

@Composable
@NonRestartableComposable
private fun LinksCollectionItem(
    collection: LinksCollection
) {
    val expanded = remember { mutableStateOf(false) }
    ListItem(
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        ),
        leadingContent = {
            IconButton(
                onClick = {

                }
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null
                )
            }
        },
        overlineContent = {
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
        },
        headlineContent = {
            Text(
                text = collection.title
            )
        },
        supportingContent = {
            Text(
                modifier = Modifier
                    .heightIn(
                        max = 150.dp
                    )
                    .verticalScroll(rememberScrollState())
                    .animateContentSize(),
                text = collection.description,
                minLines = 3,
                maxLines = if (expanded.value)
                    Int.MAX_VALUE
                else
                    3
            )
        },
        trailingContent = {
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
    )
}