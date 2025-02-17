@file:OptIn(ExperimentalMultiplatform::class)

package com.tecknobit.refy.ui.shared.presenters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.unit.dp
import com.tecknobit.equinoxcompose.session.ManagedContent
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcompose.utilities.ResponsiveContent
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.refy.ui.shared.presentations.RefyScreenViewModel
import org.jetbrains.compose.resources.StringResource

/**
 * The [ItemsScreen] class is useful to display the list of the items retrieved from the backend
 *
 * @param title The title of the screen
 * @param viewModel The support viewmodel for the screen
 *
 * @param V The type of the viewmodel of the screen
 *
 * @author N7ghtm4r3 - Tecknobit
 *
 * @see EquinoxScreen
 * @see RefyScreen
 */
@Structure
abstract class ItemsScreen<V : RefyScreenViewModel>(
    title: StringResource,
    override val viewModel: V
) : RefyScreen<V>(
    title = title,
    viewModel = viewModel
) {

    /**
     * The custom content of the screen
     */
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

    /**
     * Custom component used to display the items list as grid
     */
    @Composable
    @NonRestartableComposable
    // TODO: ANNOTATE WITH SPECIFIC SizeClass annotations
    protected abstract fun ItemsGrid()

    /**
     * Custom component used to display the items list as custom [Column]
     */
    @Composable
    @NonRestartableComposable
    // TODO: ANNOTATE WITH SPECIFIC SizeClass annotations
    protected abstract fun ItemsList()

}