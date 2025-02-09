package com.tecknobit.refy.ui.shared.presenters

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.graphics.vector.ImageVector
import com.tecknobit.equinoxcore.annotations.Structure
import com.tecknobit.refy.ui.components.links.LinksGrid
import com.tecknobit.refy.ui.components.links.LinksList
import com.tecknobit.refy.ui.screens.links.data.RefyLink
import com.tecknobit.refy.ui.shared.presentations.BaseLinksScreenViewModel
import org.jetbrains.compose.resources.StringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.add

@Structure
abstract class BaseLinksScreen<L : RefyLink, V : BaseLinksScreenViewModel<L>>(
    title: StringResource,
    viewModel: V
) : ItemsScreen<V>(
    title = title,
    viewModel = viewModel
) {

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

    @Composable
    @NonRestartableComposable
    abstract fun RelatedLinkCard(
        link: L
    )

    override fun upsertText(): StringResource {
        return Res.string.add
    }

    override fun upsertIcon(): ImageVector {
        return Icons.Default.AddLink
    }

}