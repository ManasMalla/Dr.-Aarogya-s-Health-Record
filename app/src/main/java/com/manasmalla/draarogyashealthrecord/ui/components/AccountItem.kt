package com.manasmalla.draarogyashealthrecord.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.manasmalla.draarogyashealthrecord.R
import com.manasmalla.draarogyashealthrecord.model.Gender
import com.manasmalla.draarogyashealthrecord.model.User
import com.manasmalla.draarogyashealthrecord.ui.theme.DrAarogyasHealthRecordTheme
import com.manasmalla.draarogyashealthrecord.ui.theme.MaterialYouClipper

@Preview
@Composable
fun AccountItem(
    modifier: Modifier = Modifier,
    user: User = User(
        name = "User",
        age = 0,
        gender = Gender.Other.name,
        metric = listOf()
    ),
    onManageProfile: () -> Unit = {}
) {
    val inheritedModifier = modifier
        .fillMaxWidth()
        .wrapContentHeight()
    Row(
        modifier = if (user.isCurrentUser) inheritedModifier.padding(vertical = 16.dp) else inheritedModifier.padding(
            top = 16.dp
        ), verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(
                id = if (user.gender == Gender.Female.name) R.drawable.mrs_manas_malla else R.drawable.manas_malla
            ),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(if (user.isCurrentUser) 64.dp else 32.dp)
                .clip(MaterialYouClipper())
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = user.name,
                style = if (user.isCurrentUser) MaterialTheme.typography.titleLarge else MaterialTheme.typography.titleMedium,
                fontWeight = if (user.isCurrentUser) null else FontWeight.Normal
            )
            AnimatedVisibility(visible = user.isCurrentUser) {
                OutlinedButton(onClick = onManageProfile) {
                    Text(text = "Manage your profile")
                }
            }
        }
    }
}

@Preview
@Composable
fun NonDefaultUserAccountCardPreview() {
    DrAarogyasHealthRecordTheme {
        AccountItem(
            user = User(
                name = "Non-Default User",
                age = 0,
                gender = Gender.Other.name,
                metric = listOf(),
                isCurrentUser = false
            )
        )
    }
}
