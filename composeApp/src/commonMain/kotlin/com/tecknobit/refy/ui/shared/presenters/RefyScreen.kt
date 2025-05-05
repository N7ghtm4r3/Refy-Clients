@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.refy.ui.shared.presenters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FilterListOff
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcompose.annotations.ScreenCoordinator
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcompose.utilities.CompactClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.EXPANDED_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClass.MEDIUM_CONTENT
import com.tecknobit.equinoxcompose.utilities.ResponsiveClassComponent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.refy.displayFontFamily
import com.tecknobit.refy.ui.components.ProfilePic
import com.tecknobit.refy.ui.shared.presentations.RefyScreenViewModel
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.search_by_keywords

/**
 * The [RefyScreen] class is useful to provides the basic behavior of a Refy's UI screen
 *
 * @param title The title of the screen
 * @param viewModel The support viewmodel for the screen
 * @param snackbarHostStateBottomPadding The padding to apply from the bottom of the screen to place
 * the [SnackbarHost]
 * @param contentBottomPadding The padding to apply from the bottom of the screen
 *
 * @param V The type of the viewmodel of the screen
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 */
@Structure
@ScreenCoordinator
abstract class RefyScreen<V : RefyScreenViewModel>(
    private val title: StringResource? = null,
    viewModel: V,
    private val snackbarHostStateBottomPadding: Dp = 100.dp,
    private val contentBottomPadding: Dp = 79.dp
) : EquinoxScreen<V>(
    viewModel = viewModel
) {

    /**
     *`filtersEnabled` the state used to enable or disable the filtering
     */
    private lateinit var filtersEnabled: MutableState<Boolean>

    /**
     * Method to arrange the content of the screen to display
     */
    @Composable
    override fun ArrangeScreenContent() {
        Scaffold(
            snackbarHost = {
                SnackbarHost(
                    modifier = Modifier
                        .padding(
                            bottom = responsiveAssignment(
                                onExpandedSizeClass = { 0.dp },
                                onMediumSizeClass = { 0.dp },
                                onCompactSizeClass = { snackbarHostStateBottomPadding }
                            )
                        ),
                    hostState = viewModel.snackbarHostState!!
                )
            },
            floatingActionButton = {
                ResponsiveContent(
                    onExpandedSizeClass = { ExtendedFAB() },
                    onMediumSizeClass = { ExtendedFAB() },
                    onCompactSizeClass = {}
                )
            }
        ) {
            Column {
                TopBar()
                Column(
                    modifier = Modifier
                        .padding(
                            all = 16.dp
                        )
                        .padding(
                            bottom = responsiveAssignment(
                                onExpandedSizeClass = { 0.dp },
                                onMediumSizeClass = { 0.dp },
                                onCompactSizeClass = { contentBottomPadding }
                            )
                        )
                        .navigationBarsPadding()
                ) {
                    Content()
                }
            }
        }
    }

    /**
     * Custom top bar of the screen to display the information about the screen or to execute
     * any actions related to the screen
     */
    @Composable
    protected fun TopBar() {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(113.dp)
                    .padding(
                        horizontal = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ResponsiveContent(
                    onExpandedSizeClass = { ScreenTitle() },
                    onMediumSizeClass = { ScreenTitle() },
                    onCompactSizeClass = {
                        ListItem(
                            modifier = Modifier
                                .weight(2f),
                            headlineContent = { ScreenTitle() },
                            supportingContent = { SubTitleSection() }
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f),
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.Center
                        ) {
                            TrailingContent()
                        }
                    }
                )
            }
            HorizontalDivider(
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    /**
     * Custom [ExtendedFloatingActionButton] used to edit the item where needed
     */
    @Composable
    @ResponsiveClassComponent(
        classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
    )
    protected open fun ExtendedFAB() {
        ExtendedFloatingActionButton(
            onClick = { upsertAction() }
        ) {
            Text(
                text = stringResource(upsertText())
            )
            Icon(
                modifier = Modifier
                    .padding(
                        start = 5.dp
                    ),
                imageVector = upsertIcon(),
                contentDescription = null
            )
        }
    }

    /**
     * The custom content of the screen
     */
    @Composable
    protected abstract fun Content()

    /**
     * The representative icon of the upsert action
     */
    protected abstract fun upsertIcon(): ImageVector

    /**
     * The representative text of the upsert action
     */
    protected abstract fun upsertText(): StringResource

    /**
     * The action to execute to update or insert an item
     */
    protected abstract fun upsertAction()

    /**
     * The title of the screen section
     */
    @Composable
    @ResponsiveClassComponent(
        classes = [EXPANDED_CONTENT, MEDIUM_CONTENT]
    )
    private fun ScreenTitle() {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavBackButton()
            Text(
                text = title(),
                fontSize = 28.sp,
                color = MaterialTheme.colorScheme.primary,
                fontFamily = displayFontFamily,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Filters()
        }
    }

    /**
     * The subtitle of the screen section
     */
    @Composable
    @NonRestartableComposable
    protected fun SubTitleSection() {
        Column {
            SubTitleContent()
        }
    }

    /**
     * The content of the [SubTitleSection]
     */
    @Composable
    protected open fun SubTitleContent() {
        Row(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        size = 5.dp
                    )
                )
                .clickable { upsertAction() }
                .padding(
                    horizontal = 4.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = upsertIcon(),
                contentDescription = null
            )
            Text(
                text = stringResource(upsertText())
            )
        }
    }

    /**
     * Section related to the back navigation from the current screen to a previous one
     */
    @Composable
    @NonRestartableComposable
    protected open fun RowScope.NavBackButton() {
    }

    /**
     * Method to get the title of the screen
     *
     * @return the title of the screen as [String]
     */
    @Composable
    protected open fun title(): String {
        return if (title != null)
            stringResource(title)
        else
            ""
    }

    /**
     * Section related to the filters available for the current screen
     */
    @Composable
    @NonRestartableComposable
    protected open fun RowScope.Filters() {
        FilterButton()
    }

    /**
     * Custom [IconButton] used to filter a list displayed in the current screen
     */
    @Composable
    protected fun FilterButton() {
        IconButton(
            onClick = {
                filtersEnabled.value = !filtersEnabled.value
                if (!filtersEnabled.value && viewModel.keywords.value.isNotEmpty()) {
                    viewModel.keywords.value = ""
                    viewModel.refresh()
                }
            }
        ) {
            Icon(
                imageVector = if (filtersEnabled.value)
                    Icons.Default.FilterListOff
                else
                    Icons.Default.FilterList,
                contentDescription = null
            )
        }
    }

    /**
     * Custom trailing content to display in the [TopBar] component
     */
    @Composable
    @CompactClassComponent
    @NonRestartableComposable
    protected open fun ColumnScope.TrailingContent() {
        ProfilePic(
            size = 75.dp
        )
    }

    /**
     * The [EquinoxOutlinedTextField] used to allow the user to insert the data of the filters
     * to apply
     */
    @Composable
    protected fun FiltersInputField() {
        AnimatedVisibility(
            visible = filtersEnabled.value
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = responsiveAssignment(
                    onExpandedSizeClass = { Alignment.CenterHorizontally },
                    onMediumSizeClass = { Alignment.CenterHorizontally },
                    onCompactSizeClass = { Alignment.Start }
                )
            ) {
                EquinoxOutlinedTextField(
                    shape = RoundedCornerShape(
                        size = 12.dp
                    ),
                    value = viewModel.keywords,
                    onValueChange = {
                        viewModel.keywords.value = it
                        viewModel.refresh()
                    },
                    placeholder = Res.string.search_by_keywords,
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                viewModel.keywords.value = ""
                                viewModel.refresh()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null
                            )
                        }
                    }
                )
            }
        }
    }

    /**
     * Method to collect or instantiate the states of the screen
     */
    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        filtersEnabled = remember { mutableStateOf(false) }
        viewModel.keywords = remember { mutableStateOf("") }
    }

}