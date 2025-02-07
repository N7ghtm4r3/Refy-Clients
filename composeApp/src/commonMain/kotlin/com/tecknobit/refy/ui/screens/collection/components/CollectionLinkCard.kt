package com.tecknobit.refy.ui.screens.collection.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import com.tecknobit.refy.ui.components.links.LinkCardContainer
import com.tecknobit.refy.ui.screens.collection.presentation.CollectionScreenViewModel
import com.tecknobit.refy.ui.screens.collections.data.LinksCollection
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl

@Composable
@NonRestartableComposable
fun CollectionLinkCard(
    modifier: Modifier = Modifier,
    viewModel: CollectionScreenViewModel,
    collection: LinksCollection,
    link: RefyLinkImpl
) {
    LinkCardContainer(
        modifier = modifier,
        viewModel = viewModel,
        link = link,
        cancelButton = {

        }
    )
}