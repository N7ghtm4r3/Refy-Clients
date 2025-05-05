@file:OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMultiplatform::class,
    ExperimentalComposeApi::class
)

package com.tecknobit.refy.ui.shared.presenters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ExperimentalComposeApi
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcompose.utilities.awaitNullItemLoaded
import com.tecknobit.equinoxcompose.utilities.responsiveMaxWidth
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.refy.navigator
import com.tecknobit.refy.ui.components.links.LinksGrid
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.shared.data.RefyItem
import com.tecknobit.refy.ui.shared.presentations.ItemScreenViewModel
import com.tecknobit.refy.ui.theme.AppTypography
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.links

/**
 * The [ItemScreen] class is useful to display the information of a [RefyItem]
 *
 * @param viewModel The support viewmodel for the screen
 * @param name The name of the item
 *
 * @param I The type of the item displayed
 * @param V The type of the viewmodel of the screen
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 * @see RefyScreen
 */
@Structure
abstract class ItemScreen<I : RefyItem, V : ItemScreenViewModel<I>>(
    viewModel: V,
    private val name: String
) : RefyScreen<V>(
    viewModel = viewModel,
    snackbarHostStateBottomPadding = 0.dp,
    contentBottomPadding = 0.dp
) {

    /**
     *`item` the item displayed
     */
    protected lateinit var item: State<I?>

    /**
     *`itemName` the name of item displayed
     */
    private lateinit var itemName: State<String>

    /**
     * Section related to the back navigation from the current screen to a previous one
     */
    @Composable
    @NonRestartableComposable
    override fun RowScope.NavBackButton() {
        IconButton(
            modifier = Modifier
                .size(25.dp),
            onClick = { navigator.goBack() }
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                contentDescription = null
            )
        }
    }

    /**
     * Method to get the title of the screen
     *
     * @return the title of the screen as [String]
     */
    @Composable
    override fun title(): String {
        return itemName.value
    }

    /**
     * Section related to the filters available for the current screen
     */
    @Composable
    @NonRestartableComposable
    override fun RowScope.Filters() {
    }

    /**
     * The content of the [SubTitleSection]
     */
    @Composable
    @NonRestartableComposable
    override fun SubTitleContent() {
        awaitNullItemLoaded(
            itemToWait = item.value,
            extras = { item.value!!.iAmTheOwner() }
        ) {
            super.SubTitleContent()
        }
    }

    /**
     * The custom content of the screen
     */
    @Composable
    @NonRestartableComposable
    override fun Content() {
        ManagedContent(
            modifier = Modifier
                .fillMaxSize(),
            viewModel = viewModel,
            initialDelay = 500,
            loadingRoutine = { item.value != null },
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier
                            .responsiveMaxWidth()
                    ) {
                        RowItems()
                        LinksSection()
                    }
                }
            }
        )
    }

    /**
     * Custom section used to display the items with a row layout
     */
    @Composable
    @NonRestartableComposable
    protected abstract fun ColumnScope.RowItems()

    /**
     * Section used to display the links attached to the [item] displayed
     */
    @Composable
    @NonRestartableComposable
    private fun LinksSection() {
        LinksHeader()
        LinksGrid(
            linksState = viewModel.linksState,
            linkCard = { link ->
                ItemRelatedLinkCard(
                    link = link
                )
            }
        )
    }

    /**
     * The header of the [LinksSection] component
     */
    @Composable
    @NonRestartableComposable
    private fun LinksHeader() {
        Column(
            modifier = Modifier
                .padding(
                    top = 5.dp,
                    bottom = 10.dp
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionHeaderTitle(
                    header = Res.string.links
                )
                FilterButton()
            }
            FiltersInputField()
        }
    }

    /**
     * Custom card used to display with a properly card the information of a link attached to the [item]
     *
     * @param link The link to display
     */
    @Composable
    @NonRestartableComposable
    protected abstract fun ItemRelatedLinkCard(
        link: RefyLinkImpl
    )

    /**
     * The section of the header title of the screen
     *
     * @param modifier The modifier to apply to the component
     * @param header The header information
     */
    @Composable
    @NonRestartableComposable
    protected fun SectionHeaderTitle(
        modifier: Modifier = Modifier,
        header: StringResource
    ) {
        Text(
            modifier = modifier,
            text = stringResource(header),
            style = AppTypography.headlineLarge
        )
    }

    /**
     * Content to allow the user to attach items to the current [item]
     *
     * @param state The state useful to manage the visibility of the [ModalBottomSheet]
     * @param scope The coroutine useful to manage the visibility of the [ModalBottomSheet]
     */
    @Composable
    protected abstract fun AttachContent(
        state: SheetState,
        scope: CoroutineScope,
    )

    /**
     * Content displayed when the user request to delete the [item]
     *
     * @param delete The state used to manage the visibility of this component
     */
    @Composable
    protected abstract fun DeleteItemContent(
        delete: MutableState<Boolean>,
    )

    /**
     * Method invoked when the [ShowContent] composable has been started
     */
    override fun onStart() {
        super.onStart()
        viewModel.retrieveItem()
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        super.CollectStates()
        item = viewModel.item.collectAsState()
        itemName = viewModel.itemName.collectAsState(
            initial = name
        )
    }

}