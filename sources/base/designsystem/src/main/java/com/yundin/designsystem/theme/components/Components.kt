package com.yundin.designsystem.theme.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.yundin.designsystem.R

@ExperimentalComposeUiApi
@Composable
fun SearchField(value: String, onValueChange: (String) -> Unit, label: String) {
    val keyboardController = LocalSoftwareKeyboardController.current
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = label)
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onAny = {
            keyboardController?.hide()
        })
    )
}

data class UIRepository(
    val name: String,
    val description: String?
)

@Composable
fun RepositoryCard(repository: UIRepository, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(8.dp)
        ) {
            Text(
                text = repository.name,
                style = MaterialTheme.typography.h6
            )
            Text(
                text = repository.description ?: stringResource(R.string.no_repository_description),
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
fun AnimatedVisibilityProgressBarItem(visible: Boolean) {
    AnimatedVisibility(visible = visible) {
        ProgressBarItem()
    }
}

@Composable
fun ProgressBarItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun LoadingFailedItem(onRetryClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.loading_failed),
            style = MaterialTheme.typography.body1
        )
        Button(onClick = onRetryClick) {
            Text(
                text = stringResource(R.string.retry_btn),
            )
        }
    }
}
