package com.tecknobit.refy.ui.screens.links.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.tecknobit.refy.displayFontFamily
import com.tecknobit.refy.ui.screens.links.data.RefyLink.RefyLinkImpl
import org.jetbrains.compose.resources.painterResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.logo

//https://cdn.prod.website-files.com/64c7b734b044b679c715bc30/6674b58d32fbc146194c888a_5%20best%20practices%20to%20design%20UI%20Cards%20for%20your%20website%402x.webp
@Composable
@NonRestartableComposable
fun LinkCard(
    modifier: Modifier = Modifier,
    link: RefyLinkImpl
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    all = 10.dp
                )
        ) {
            LinkThumbnail(
                link = link
            )
            Text(
                modifier = Modifier
                    .padding(
                        top = 5.dp,
                        start = 5.dp
                    ),
                text = link.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Bold,
                fontFamily = displayFontFamily
            )
        }
    }
}

@Composable
@NonRestartableComposable
private fun LinkThumbnail(
    link: RefyLinkImpl
) {
    // TODO: CHECK WHETHER USE THE DEDICATED COMPONENT 
    AsyncImage(
        modifier = Modifier
            .padding(
                all = 3.dp
            )
            .clip(
                RoundedCornerShape(7.dp)
            )
            .fillMaxWidth()
            .height(200.dp),
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(link.thumbnailPreview)
            .crossfade(true)
            .crossfade(500)
            .build(),
        contentScale = ContentScale.Crop,
        error = painterResource(Res.drawable.logo),
        contentDescription = "Reference thumbnail picture"
    )
}