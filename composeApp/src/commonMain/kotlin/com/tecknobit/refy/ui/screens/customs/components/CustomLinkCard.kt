@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.screens.customs.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcore.time.TimeFormatter.toDateString
import com.tecknobit.refy.UPSERT_CUSTOM_LINK_SCREEN
import com.tecknobit.refy.displayFontFamily
import com.tecknobit.refy.navigator
import com.tecknobit.refy.ui.components.DeleteItemButton
import com.tecknobit.refy.ui.components.DeleteLink
import com.tecknobit.refy.ui.components.ItemInfo
import com.tecknobit.refy.ui.components.links.LinkCardContainer
import com.tecknobit.refy.ui.icons.Preview
import com.tecknobit.refy.ui.screens.customs.data.CustomRefyLink
import com.tecknobit.refy.ui.screens.customs.presentation.CustomLinksScreenViewModel
import com.tecknobit.refycore.enums.ExpiredTime.NO_EXPIRATION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.access_method
import refy.composeapp.generated.resources.auth
import refy.composeapp.generated.resources.expired
import refy.composeapp.generated.resources.has_unique_access
import refy.composeapp.generated.resources.link_details
import refy.composeapp.generated.resources.resources
import refy.composeapp.generated.resources.the_link_will_expire_on
import refy.composeapp.generated.resources.this_link_is
import refy.composeapp.generated.resources.this_link_not_expires

@Composable
@NonRestartableComposable
fun CustomLinkCard(
    modifier: Modifier = Modifier,
    viewModel: CustomLinksScreenViewModel,
    link: CustomRefyLink
) {
    val state = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()
    LinkCardContainer(
        modifier = modifier,
        viewModel = viewModel,
        link = link,
        onClick = {
            scope.launch {
                state.show()
            }
        },
        onLongClick = { navigator.navigate("$UPSERT_CUSTOM_LINK_SCREEN/${link.id}") },
        extraInformation = if (link.isExpired()) {
            {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    ItemInfo(
                        info = stringResource(Res.string.this_link_is)
                    )
                    ItemInfo(
                        info = stringResource(Res.string.expired),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        } else
            null,
        extraButton = {
            val uriHandler = LocalUriHandler.current
            IconButton(
                modifier = Modifier
                    .size(30.dp),
                onClick = {
                    uriHandler.openUri(
                        uri = link.previewReference()
                    )
                }
            ) {
                Icon(
                    imageVector = Preview,
                    contentDescription = null
                )
            }
        },
        cancelButton = {
            DeleteItemButton(
                modifier = Modifier
                    .weight(1f),
                item = link,
                deleteContent = { delete ->
                    DeleteLink(
                        show = delete,
                        viewModel = viewModel,
                        link = link
                    )
                }
            )
        }
    )
    CustomLinkDetails(
        state = state,
        scope = scope,
        link = link
    )
}

@Composable
@NonRestartableComposable
private fun CustomLinkDetails(
    state: SheetState,
    scope: CoroutineScope,
    link: CustomRefyLink
) {
    if (state.isVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    state.hide()
                }
            }
        ) {
            Text(
                modifier = Modifier
                    .padding(
                        all = 10.dp
                    ),
                text = stringResource(Res.string.link_details),
                fontSize = 20.sp,
                fontFamily = displayFontFamily,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider()
            Column(
                modifier = Modifier
                    .padding(
                        all = 10.dp
                    )
            ) {
                AccessMethod(
                    link = link
                )
                Auth(
                    link = link
                )
                Spacer(
                    modifier = Modifier
                        .height(5.dp)
                )
                Resources(
                    link = link
                )
            }
        }
    }
}

@Composable
@NonRestartableComposable
private fun AccessMethod(
    link: CustomRefyLink
) {
    Text(
        text = stringResource(Res.string.access_method),
        fontSize = 18.sp,
        fontFamily = displayFontFamily
    )
    if (link.uniqueAccess) {
        ItemInfo(
            info = stringResource(Res.string.has_unique_access)
        )
    }
    val expiredTime = link.expiredTime
    if (expiredTime == NO_EXPIRATION) {
        ItemInfo(
            info = stringResource(Res.string.this_link_not_expires)
        )
    } else {
        ItemInfo(
            info = stringResource(
                resource = Res.string.the_link_will_expire_on,
                link.expirationDate().toDateString()
            )
        )
    }
}

@Composable
@NonRestartableComposable
private fun Auth(
    link: CustomRefyLink
) {
    val fields = link.fields
    if (fields.isNotEmpty()) {
        Text(
            text = stringResource(Res.string.auth),
            fontSize = 18.sp,
            fontFamily = displayFontFamily
        )
        CustomLinkPayload(
            payload = fields
        )
    }
}

@Composable
@NonRestartableComposable
private fun Resources(
    link: CustomRefyLink
) {
    val resources = link.resources
    if (resources.isNotEmpty()) {
        Text(
            text = stringResource(Res.string.resources),
            fontSize = 18.sp,
            fontFamily = displayFontFamily
        )
        CustomLinkPayload(
            payload = resources
        )
    }
}

@Composable
@NonRestartableComposable
private fun CustomLinkPayload(
    payload: Map<String, String>
) {
    val json = Json {
        prettyPrint = true
    }
    Card(
        modifier = Modifier
            .padding(
                top = 5.dp
            )
            .fillMaxWidth()
            .heightIn(
                max = 500.dp
            )
    ) {
        Text(
            modifier = Modifier
                .padding(
                    all = 10.dp
                )
                .verticalScroll(rememberScrollState()),
            text = json.encodeToString(payload)
        )
    }
}