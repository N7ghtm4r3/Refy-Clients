package com.tecknobit.refy.ui.shared.presenters

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.graphics.vector.ImageVector
import com.tecknobit.equinoxcompose.session.screens.EquinoxScreen
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.refy.ui.components.links.LinksGrid
import com.tecknobit.refy.ui.components.links.LinksList
import com.tecknobit.refy.ui.screens.links.data.RefyLink
import com.tecknobit.refy.ui.shared.presentations.BaseLinksScreenViewModel
import org.jetbrains.compose.resources.StringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.add

/**
 * The [BaseLinksScreen] class is the base screen used to display the links list and give the basic
 * behavior of a screen which have to display a list of links
 *
 * @param title The title of the screen
 * @param viewModel The support viewmodel for the screen
 *
 * @param L The type of the link displayed
 * @param V The type of the viewmodel of the screen
 *
 * @author N7ghtm4r3 - Tecknobit
 * @see EquinoxScreen
 * @see RefyScreen
 * @see ItemsScreen
 */
@Structure
abstract class BaseLinksScreen<L : RefyLink, V : BaseLinksScreenViewModel<L>>(
    title: StringResource,
    viewModel: V
) : ItemsScreen<V>(
    title = title,
    viewModel = viewModel
) {

    /**
     * Custom component used to display the items list as grid
     */
    @Composable
    @NonRestartableComposable
    // TODO: ANNOTATE WITH SPECIFIC SizeClass annotations
    override fun ItemsGrid() {
        LinksGrid(
            linksState = viewModel.linksState,
            linkCard = { link ->
                RelatedLinkCard(
                    link = link
                )
            }
        )
    }

    /**
     * Custom component used to display the items list as custom [Column]
     */
    @Composable
    @NonRestartableComposable
    // TODO: ANNOTATE WITH SPECIFIC SizeClass annotations
    override fun ItemsList() {
        LinksList(
            linksState = viewModel.linksState,
            linkCard = { link ->
                RelatedLinkCard(
                    link = link
                )
            }
        )
    }

    /**
     * The custom card used to display the information of the [link]
     *
     * @param link The link to display
     */
    @Composable
    @NonRestartableComposable
    abstract fun RelatedLinkCard(
        link: L,
    )

    /**
     * The representative text of the upsert action
     */
    override fun upsertText(): StringResource {
        return Res.string.add
    }

    /**
     * The representative icon of the upsert action
     */
    override fun upsertIcon(): ImageVector {
        return Icons.Default.AddLink
    }

}