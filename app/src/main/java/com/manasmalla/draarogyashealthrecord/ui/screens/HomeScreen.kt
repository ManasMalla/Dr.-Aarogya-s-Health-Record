package com.manasmalla.draarogyashealthrecord.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.ModeNight
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.PersonAddAlt1
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.manasmalla.draarogyashealthrecord.R
import com.manasmalla.draarogyashealthrecord.ui.theme.DrAarogyasHealthRecordTheme
import com.manasmalla.draarogyashealthrecord.ui.theme.MaterialYouClipper

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(modifier: Modifier = Modifier, homeUiState: HomeUiState) {
//    LaunchedEffect(key1 = true){
//        homeViewModel.getRecords()
//    }
    val homeViewModel: HomeViewModel = viewModel()

    Scaffold(floatingActionButton = {
        FloatingActionButton(
            onClick = { /*TODO*/ }, containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(imageVector = Icons.Rounded.Add, contentDescription = "Add Record")
        }
    }) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            Column(modifier = modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.padding(
                        top = 16.dp, start = 24.dp, end = 24.dp, bottom = 8.dp
                    )
                ) {
                    Image(painter = painterResource(id = R.drawable.mrs_manas_malla),
                        contentDescription = "Profile Picture",
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(align = Alignment.End)
                            .size(64.dp)
                            .clip(MaterialYouClipper())
                            .clickable {
                                homeViewModel.onOpenAccountDialog()
                            })
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Hey there,", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "Aarogya Tatineni",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                when (homeUiState) {
                    is HomeUiState.Empty -> NoRecordsScreen(
                        shouldShowDialog = homeUiState.isAccountDialogOpen,
                        onDismissRequest = homeViewModel::dismissAccountDialog
                    )

                    HomeUiState.Error -> ErrorScreen()
                    HomeUiState.Loading -> LoadingScreen()
                    is HomeUiState.Success -> RecordListScreen()
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DrAarogyasHealthRecordTheme {
        HomeScreen(homeUiState = HomeUiState.Empty())
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreen_nonDyanmicPreview() {
    DrAarogyasHealthRecordTheme(dynamicColor = false) {
        HomeScreen(homeUiState = HomeUiState.Empty())
    }
}

@Composable
fun RecordListScreen() {

}

@Composable
fun LoadingScreen() {
    Text(
        text = "Loading...",
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.Center)
    )
}

@Composable
fun ErrorScreen() {

}

@Composable
fun NoRecordsScreen(shouldShowDialog: Boolean, onDismissRequest: () -> Unit) {
    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxHeight()) {
        Surface(color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp),
            content = {})
        Column(
            verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxHeight()
        ) {
            Text(
                text = "Add a record to get started!",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 8.dp),
                letterSpacing = 4.sp
            )
            Image(
                painter = painterResource(id = R.drawable.vaccination),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .padding(bottom = 120.dp),
                contentScale = ContentScale.FillWidth
            )
        }
    }
    AnimatedVisibility(visible = shouldShowDialog) {
        AccountInfoDialog(onDismissRequest = onDismissRequest)
    }
}

@Preview
@Composable
fun AccountInfoDialog(onDismissRequest: () -> Unit = {}) {
    val userViewModel: UserViewModel = viewModel()
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = RoundedCornerShape(28.dp),
            tonalElevation = 6.dp,
            modifier = Modifier.wrapContentWidth()
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
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
                        userViewModel.updateTheme()
                    }) {
                        Icon(
                            imageVector = if (userViewModel.isDarkTheme.collectAsState().value) Icons.Outlined.ModeNight else Icons.Outlined.LightMode,
                            contentDescription = "Enable Dark Theme"
                        )
                    }
                }
                AccountItem(isPrimary = true)
                Divider()
                Row(modifier = Modifier.padding(16.dp)) {
                    Icon(imageVector = Icons.Rounded.PersonAddAlt1, contentDescription = null)

                    Text(text = "Add another account", modifier = Modifier.padding(start = 16.dp))
                }
                Divider()
                Row(verticalAlignment = Alignment.CenterVertically) {
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
}

@Composable
fun AccountItem(isPrimary: Boolean = false) {
    val modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
    Row(modifier = if (isPrimary) modifier.padding(vertical = 16.dp) else modifier) {
        Image(
            painter = painterResource(id = R.drawable.mrs_manas_malla),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(64.dp)
                .clip(MaterialYouClipper())
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = "Manas Malla", style = MaterialTheme.typography.titleLarge)
            OutlinedButton(onClick = { /*TODO*/ }) {
                Text(text = "Manage your profile")
            }
        }
    }
}
