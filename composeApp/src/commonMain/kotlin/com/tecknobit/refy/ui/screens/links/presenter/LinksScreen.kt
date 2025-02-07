package com.tecknobit.refy.ui.screens.links.presenter

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLink
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.graphics.vector.ImageVector
import com.tecknobit.refy.ui.components.links.LinksGrid
import com.tecknobit.refy.ui.components.links.LinksList
import com.tecknobit.refy.ui.screens.links.components.LinkCard
import com.tecknobit.refy.ui.screens.links.presentation.LinksScreenViewModel
import com.tecknobit.refy.ui.shared.presenters.ItemsScreen
import org.jetbrains.compose.resources.StringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.add
import refy.composeapp.generated.resources.links

class LinksScreen : ItemsScreen<LinksScreenViewModel>(
    title = Res.string.links,
    viewModel = LinksScreenViewModel()
) {

    @Composable
    @NonRestartableComposable
    override fun ItemsGrid() {
        LinksGrid(
            linksState = viewModel.linksState,
            linkCard = { link ->
                LinkCard(
                    viewModel = viewModel,
                    link = link
                )
            }
        )
    }

    @Composable
    @NonRestartableComposable
    override fun ItemsList() {
        LinksList(
            linksState = viewModel.linksState,
            linkCard = { link ->
                LinkCard(
                    viewModel = viewModel,
                    link = link
                )
            }
        )
    }

    override fun upsertAction() {
        // TODO: TO NAV TO CREATE
    }

    override fun upsertText(): StringResource {
        return Res.string.add
    }

    override fun upsertIcon(): ImageVector {
        return Icons.Default.AddLink
    }

}