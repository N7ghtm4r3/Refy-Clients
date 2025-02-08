@file:OptIn(ExperimentalMaterial3Api::class)

package com.tecknobit.refy.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tecknobit.equinoxcore.time.TimeFormatter.toDateString
import com.tecknobit.refy.displayFontFamily
import com.tecknobit.refy.ui.icons.CollapseAll
import com.tecknobit.refy.ui.icons.ExpandAll
import com.tecknobit.refy.ui.shared.data.RefyItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

private const val MINIMUM_DESCRIPTION_LINES = 3

@Composable
@NonRestartableComposable
fun ItemCardDetails(
    modifier: Modifier = Modifier,
    expanded: MutableState<Boolean>,
    item: RefyItem,
    info: StringResource,
    descriptionLines: MutableState<Int>
) {
    Column(
        modifier = modifier
            .heightIn(
                max = 200.dp
            )
    ) {
        ItemTitle(
            item = item
        )
        Text(
            text = stringResource(
                resource = info,
                item.date.toDateString()
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 15.sp
        )
        Text(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .animateContentSize(),
            onTextLayout = { layout -> descriptionLines.value = layout.lineCount },
            text = item.description,
            minLines = MINIMUM_DESCRIPTION_LINES,
            maxLines = if (expanded.value)
                Int.MAX_VALUE
            else
                MINIMUM_DESCRIPTION_LINES,
            overflow = TextOverflow.Ellipsis,
            fontSize = 14.sp
        )
    }
}

@Composable
@NonRestartableComposable
fun ItemTitle(
    item: RefyItem
) {
    Text(
        text = item.title,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontWeight = FontWeight.Bold,
        fontFamily = displayFontFamily
    )
}

@Composable
@NonRestartableComposable
fun ExpandCardButton(
    expanded: MutableState<Boolean>,
    descriptionLines: MutableState<Int>
) {
    if (descriptionLines.value >= MINIMUM_DESCRIPTION_LINES) {
        IconButton(
            modifier = Modifier
                .size(30.dp),
            onClick = { expanded.value = !expanded.value }
        ) {
            Icon(
                imageVector = if (expanded.value)
                    CollapseAll
                else
                    ExpandAll,
                contentDescription = null
            )
        }
    }
}

@Composable
@NonRestartableComposable
fun AttachItemButton(
    attachItemContent: @Composable() (SheetState, CoroutineScope) -> Unit
) {
    val state = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val scope = rememberCoroutineScope()
    IconButton(
        modifier = Modifier
            .size(30.dp),
        onClick = {
            scope.launch {
                state.show()
            }
        }
    ) {
        Icon(
            imageVector = Icons.Default.Attachment,
            contentDescription = null
        )
    }
    attachItemContent(state, scope)
}

@Composable
@NonRestartableComposable
fun RemoveItemButton(
    removeAction: () -> Unit
) {
    IconButton(
        modifier = Modifier
            .size(32.dp),
        onClick = removeAction
    ) {
        Icon(
            imageVector = Icons.Default.Cancel,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
@NonRestartableComposable
fun RowScope.DeleteItemButton(
    item: RefyItem,
    deleteContent: @Composable() (MutableState<Boolean>) -> Unit
) {
    if (item.iAmTheOwner()) {
        val deleteItem = remember { mutableStateOf(false) }
        Column(
            modifier = Modifier
                .weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            IconButton(
                modifier = Modifier
                    .size(32.dp),
                onClick = { deleteItem.value = true }
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
        deleteContent(deleteItem)
    }
}