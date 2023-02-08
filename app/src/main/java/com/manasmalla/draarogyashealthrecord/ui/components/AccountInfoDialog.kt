package com.manasmalla.draarogyashealthrecord.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ModeNight
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.PersonAddAlt1
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.manasmalla.draarogyashealthrecord.model.Gender
import com.manasmalla.draarogyashealthrecord.model.User


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AccountInfoDialog(
    users: List<User>,
    updateAccountDialogVisibility: (Boolean) -> Unit = {},
    onManageProfile: () -> Unit = {},
    onAddUser: () -> Unit = {},
    onSetCurrentUser: (User) -> Unit = {}
) {

    Dialog(
        onDismissRequest = {
            updateAccountDialogVisibility(false)
        }, properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        AccountInfoCard(
            modifier = Modifier.padding(horizontal = 24.dp),
            users = users,
            onManageProfile = onManageProfile,
            onAddUser = onAddUser,
            onDismissRequest = {
                updateAccountDialogVisibility(false)
            },
            onSetCurrentUser = onSetCurrentUser
        )
    }
}


@Preview(showBackground = true)
@Composable
fun AccountInfoCard(
    modifier: Modifier = Modifier,
    users: List<User> = listOf(
        User(name = "User", age = 0, gender = Gender.Female.name, metric = listOf()), User(
            uId = 1,
            name = "User",
            age = 0,
            gender = Gender.Male.name,
            metric = listOf(),
            isCurrentUser = false
        )
    ),
    onManageProfile: () -> Unit = {},
    onAddUser: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
    onSetCurrentUser: (User) -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(28.dp),
        tonalElevation = 6.dp,
        modifier = modifier.wrapContentWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            DialogHeader(onDismissRequest = onDismissRequest)
            AccountsList(
                users = users,
                onManageProfile = onManageProfile,
                onSetCurrentUser = onSetCurrentUser
            )
            AddAnotherAccountButton(onAddUser = onAddUser)
            Divider()
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentWidth(Alignment.CenterHorizontally)
            ) {
                TextButton(onClick = { /*TODO*/ }) {
                    Text(
                        text = "Privacy Policy",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                Text(text = " • ")
                TextButton(onClick = { /*TODO*/ }) {
                    Text(
                        text = "Terms of Service",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
fun DialogHeader(onDismissRequest: () -> Unit = {}) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(onClick = onDismissRequest) {
            Icon(imageVector = Icons.Rounded.Close, contentDescription = "Close Dialog")
        }
        Column {
            Text(
                text = "Dr. Aarogya's",
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "Health Record",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium
            )
        }
        IconButton(onClick = {
            //TODO: UpdateTheme
            //userViewModel.updateTheme()
        }) {
            Icon(
                imageVector = Icons.Outlined.ModeNight, contentDescription = "Enable Dark Theme"
            )
        }
    }
}

@Composable
fun AddAnotherAccountButton(onAddUser: () -> Unit = {}) {
    Row(modifier = Modifier
        .padding(
            16.dp
        )
        .clickable {
            onAddUser()
        }) {
        Icon(imageVector = Icons.Rounded.PersonAddAlt1, contentDescription = null)

        Text(text = "Add another account", modifier = Modifier.padding(start = 16.dp))
    }
}

@Composable
fun AccountsList(
    users: List<User> = listOf(),
    onManageProfile: () -> Unit = {},
    onSetCurrentUser: (User) -> Unit = {}
) {
    LazyColumn {
        items(users.filter { it.isCurrentUser }, { it.uId }) { user ->
            AccountItem(user = user, onManageProfile = onManageProfile)
        }
        item {
            Divider()
        }
        items(users.filter { !it.isCurrentUser }, { it.uId }) { user ->
            AccountItem(modifier = Modifier.clickable { onSetCurrentUser(user) },user = user,
                onManageProfile = onManageProfile)
        }
    }

}
