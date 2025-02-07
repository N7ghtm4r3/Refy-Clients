@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.refy.ui.shared.presenters

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.FilterListOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.components.EquinoxOutlinedTextField
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcompose.utilities.responsiveAssignment
import com.tecknobit.equinoxcore.annotations.RequiresSuperCall
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.refy.ui.shared.presentations.ItemsScreenViewModel
import org.jetbrains.compose.resources.StringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.search_by_keywords

@Structure
abstract class ItemsScreen<V : ItemsScreenViewModel>(
    title: StringResource,
    override val viewModel: V
) : RefyScreen<V>(
    title = title,
    viewModel = viewModel
) {

    @Composable
    @NonRestartableComposable
    override fun Content() {
        ManagedContent(
            viewModel = viewModel,
            content = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FiltersBar()
                    ResponsiveContent(
                        onExpandedSizeClass = { ItemsGrid() },
                        onMediumSizeClass = { ItemsGrid() },
                        onCompactSizeClass = { ItemsList() }
                    )
                }
            }
        )
    }

    @Composable
    @NonRestartableComposable
    protected abstract fun ItemsGrid()

    @Composable
    @NonRestartableComposable
    protected abstract fun ItemsList()

    @Composable
    @NonRestartableComposable
    override fun RowScope.Filters() {
        IconButton(
            onClick = {
                filtersEnabled.value = !filtersEnabled.value
                if (!filtersEnabled.value) {
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
    private fun FiltersBar() {
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