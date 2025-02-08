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
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
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

@Structure
abstract class RefyScreen<V : RefyScreenViewModel>(
    private val title: StringResource? = null,
    viewModel: V,
    private val snackbarHostStateBottomPadding: Dp = 100.dp,
    private val contentBottomPadding: Dp = 79.dp
) : EquinoxScreen<V>(
    viewModel = viewModel
) {

    private lateinit var filtersEnabled: MutableState<Boolean>

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

    @Composable
    @NonRestartableComposable
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
                            supportingContent = {
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
                            },
                            headlineContent = {
                                ScreenTitle()
                            }
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

    @Composable
    @NonRestartableComposable
    protected abstract fun Content()

    @Composable
    @NonRestartableComposable
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

    protected abstract fun upsertIcon(): ImageVector

    protected abstract fun upsertText(): StringResource

    protected abstract fun upsertAction()

    @Composable
    @NonRestartableComposable
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

    @Composable
    @NonRestartableComposable
    protected open fun RowScope.NavBackButton() {
    }

    @Composable
    @NonRestartableComposable
    protected open fun title(): String {
        return if (title != null)
            stringResource(title)
        else
            ""
    }

    @Composable
    @NonRestartableComposable
    protected open fun RowScope.Filters() {
        FilterButton()
    }

    @Composable
    @NonRestartableComposable
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

    @Composable
    @NonRestartableComposable
    // TODO: ANNOTATE WITH SPECIFIC SizeClass annotations
    protected open fun ColumnScope.TrailingContent() {
        ProfilePic(
            size = 75.dp
        )
    }

    @Composable
    @NonRestartableComposable
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
                    placeholder = Res.string.search_by_keywords
                )
            }
        }
    }

    @Composable
    @RequiresSuperCall
    override fun CollectStates() {
        filtersEnabled = remember { mutableStateOf(false) }
        viewModel.keywords = remember { mutableStateOf("") }
    }

}