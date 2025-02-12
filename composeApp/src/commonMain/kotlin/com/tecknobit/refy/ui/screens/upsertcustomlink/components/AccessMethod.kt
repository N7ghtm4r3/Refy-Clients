package com.tecknobit.refy.ui.screens.upsertcustomlink.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecknobit.refy.ui.screens.upsertcustomlink.presentation.UpsertCustomLinkScreenViewModel
import com.tecknobit.refycore.enums.ExpiredTime
import com.tecknobit.refycore.enums.ExpiredTime.FIFTEEN_MINUTES
import com.tecknobit.refycore.enums.ExpiredTime.NO_EXPIRATION
import com.tecknobit.refycore.enums.ExpiredTime.ONE_DAY
import com.tecknobit.refycore.enums.ExpiredTime.ONE_HOUR
import com.tecknobit.refycore.enums.ExpiredTime.ONE_MINUTE
import com.tecknobit.refycore.enums.ExpiredTime.ONE_WEEK
import com.tecknobit.refycore.enums.ExpiredTime.THIRTY_MINUTES
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import refy.composeapp.generated.resources.Res
import refy.composeapp.generated.resources.expirations_day
import refy.composeapp.generated.resources.expirations_hour
import refy.composeapp.generated.resources.expirations_minute
import refy.composeapp.generated.resources.expirations_week
import refy.composeapp.generated.resources.expires
import refy.composeapp.generated.resources.expires_in
import refy.composeapp.generated.resources.unique_access

@Composable
@NonRestartableComposable
fun UniqueAccessCheckBox(
    viewModel: UpsertCustomLinkScreenViewModel
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = viewModel.uniqueAccess.value,
            onCheckedChange = { viewModel.uniqueAccess.value = it }
        )
        Text(
            text = stringResource(Res.string.unique_access)
        )
    }
}

@Composable
@NonRestartableComposable
fun ExpirationTimeCheckBox(
    viewModel: UpsertCustomLinkScreenViewModel
) {
    var expires by remember { mutableStateOf(viewModel.expirationTime.value != NO_EXPIRATION) }
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = expires,
            onCheckedChange = {
                expires = it
                viewModel.expirationTime.value = if (!expires)
                    NO_EXPIRATION
                else
                    ONE_MINUTE
            }
        )
        Text(
            text = stringResource(Res.string.expires)
        )
        AnimatedVisibility(
            visible = expires
        ) {
            Row(
                modifier = Modifier
                    .padding(
                        start = 5.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.expires_in)
                )
                ExpiredTimeText(
                    modifier = Modifier
                        .padding(
                            start = 5.dp
                        ),
                    expiredTime = viewModel.expirationTime.value
                )
                Column {
                    val expanded = remember { mutableStateOf(false) }
                    IconButton(
                        modifier = Modifier
                            .size(28.dp),
                        onClick = { expanded.value = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null
                        )
                    }
                    ExpiredTimesMenu(
                        expanded = expanded,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
@NonRestartableComposable
private fun ExpiredTimesMenu(
    expanded: MutableState<Boolean>,
    viewModel: UpsertCustomLinkScreenViewModel
) {
    DropdownMenu(
        expanded = expanded.value,
        onDismissRequest = { expanded.value = false }
    ) {
        ExpiredTime.entries.forEach { expiredTime ->
            if (expiredTime != NO_EXPIRATION) {
                DropdownMenuItem(
                    onClick = {
                        expanded.value = false
                        viewModel.expirationTime.value = expiredTime
                    },
                    text = {
                        ExpiredTimeText(
                            expiredTime = expiredTime
                        )
                    }
                )
            }
        }
    }
}

@Composable
@NonRestartableComposable
private fun ExpiredTimeText(
    modifier: Modifier = Modifier,
    expiredTime: ExpiredTime
) {
    val timeValue = expiredTime.timeValue
    Text(
        modifier = modifier,
        text = pluralStringResource(
            resource = expiredTime.asText(),
            quantity = timeValue,
            timeValue
        )
    )
}

@Composable
@NonRestartableComposable
private fun ExpiredTime.asText(): PluralStringResource {
    return when (this) {
        ONE_MINUTE, FIFTEEN_MINUTES, THIRTY_MINUTES -> Res.plurals.expirations_minute
        ONE_HOUR -> Res.plurals.expirations_hour
        ONE_DAY -> Res.plurals.expirations_day
        ONE_WEEK -> Res.plurals.expirations_week
        else -> Res.plurals.expirations_minute
    }
}