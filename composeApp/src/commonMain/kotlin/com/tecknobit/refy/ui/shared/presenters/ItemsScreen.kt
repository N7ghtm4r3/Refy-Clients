@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.refy.ui.shared.presenters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.refy.ui.shared.presentations.RefyScreenViewModel
import org.jetbrains.compose.resources.StringResource

@Structure
abstract class ItemsScreen<V : RefyScreenViewModel>(
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
                    FiltersInputField()
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

}