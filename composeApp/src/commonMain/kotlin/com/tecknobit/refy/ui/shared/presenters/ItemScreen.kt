@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMultiplatform::class)

package com.tecknobit.refy.ui.shared.presenters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.refy.navigator
import com.tecknobit.refy.ui.components.links.LinksGrid
import com.tecknobit.refy.ui.components.links.LinksList
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import com.tecknobit.refy.ui.shared.data.RefyItem
import com.tecknobit.refy.ui.shared.presentations.ItemScreenViewModel
import com.tecknobit.refy.ui.theme.AppTypography
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.links

@Structure
abstract class ItemScreen<I : RefyItem, V : ItemScreenViewModel<I>>(
    viewModel: V,
    private val name: String
) : RefyScreen<V>(
    viewModel = viewModel,
    snackbarHostStateBottomPadding = 0.dp,
    contentBottomPadding = 0.dp
) {

    protected lateinit var item: State<I?>

    protected lateinit var itemName: State<String>

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

    @Composable
    @NonRestartableComposable
    override fun title(): String {
        return itemName.value
    }

    @Composable
    @NonRestartableComposable
    override fun RowScope.Filters() {
    }

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

    @Composable
    @NonRestartableComposable
    override fun Content() {
        ManagedContent(
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
                            .widthIn(
                                max = MAX_CONTAINER_WIDTH
                            )
                    ) {
                        RowItems()
                        LinksSection()
                    }
                }
            }
        )
    }

    @Composable
    @NonRestartableComposable
    protected abstract fun ColumnScope.RowItems()

    @Composable
    @NonRestartableComposable
    private fun LinksSection() {
        LinksHeader()
        ResponsiveContent(
            onExpandedSizeClass = {
                LinksGrid(
                    linksState = viewModel.linksState,
                    linkCard = { link ->
                        ItemRelatedLinkCard(
                            link = link
                        )
                    }
                )
            },
            onMediumSizeClass = {
                LinksGrid(
                    linksState = viewModel.linksState,
                    linkCard = { link ->
                        ItemRelatedLinkCard(
                            link = link
                        )
                    }
                )
            },
            onCompactSizeClass = {
                LinksList(
                    linksState = viewModel.linksState,
                    linkCard = { link ->
                        ItemRelatedLinkCard(
                            link = link
                        )
                    }
                )
            }
        )
    }

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

    @Composable
    @NonRestartableComposable
    protected abstract fun ItemRelatedLinkCard(
        link: RefyLinkImpl
    )

    @Composable
    @NonRestartableComposable
    @Deprecated("USE THE EQUINOX BUILT-IN")
    // TODO: TO INDICATE IN THE DOCU THAT IS USEFUL WITHOUT AN UI AND FOR THOSE SCENARIOS TO MANAGE
    // OTHER PARTS OF THE UI DEPENDS ON THAT ITEM NOT BE NULL
    protected fun <T> awaitNullItemLoaded(
        itemToWait: T?,
        extras: (T) -> Boolean = { true },
        loadedContent: @Composable (T) -> Unit
    ) {
        var loaded by remember { mutableStateOf(false) }
        LaunchedEffect(itemToWait) {
            loaded = itemToWait != null && extras(itemToWait)
        }
        AnimatedVisibility(
            visible = loaded
        ) {
            loadedContent(itemToWait!!)
        }
    }

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

    // TODO: ANNOTATE WITH SPECIFIC SizeClass annotations
    @Composable
    @NonRestartableComposable
    protected abstract fun AttachContent(
        state: SheetState,
        scope: CoroutineScope
    )

    // TODO: ANNOTATE WITH SPECIFIC SizeClass annotations
    @Composable
    @NonRestartableComposable
    protected abstract fun DeleteItemContent(
        delete: MutableState<Boolean>
    )

    override fun onStart() {
        super.onStart()
        viewModel.retrieveItem()
    }

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