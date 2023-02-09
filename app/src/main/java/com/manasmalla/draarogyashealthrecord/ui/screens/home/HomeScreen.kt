package com.manasmalla.draarogyashealthrecord.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manasmalla.draarogyashealthrecord.R
import com.manasmalla.draarogyashealthrecord.data.fake.FakeUserRepository
import com.manasmalla.draarogyashealthrecord.model.Gender
import com.manasmalla.draarogyashealthrecord.ui.components.AccountInfoDialog
import com.manasmalla.draarogyashealthrecord.ui.screens.RecordBottomSheetScaffold
import com.manasmalla.draarogyashealthrecord.ui.screens.UserUiState
import com.manasmalla.draarogyashealthrecord.ui.screens.UserViewModel
import com.manasmalla.draarogyashealthrecord.ui.theme.DrAarogyasHealthRecordTheme
import com.manasmalla.draarogyashealthrecord.ui.theme.MaterialYouClipper

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    userViewModel: UserViewModel,
    homeViewModel: HomeViewModel,
    onAddUser: () -> Unit = {},
    onManageProfile: () -> Unit = {},
) {
    val users by userViewModel.users.collectAsState()
    val user by homeViewModel.userUiState.collectAsState()
    RecordBottomSheetScaffold(
        recordUiState = homeViewModel.recordUiState,
        onUiStateChanged = homeViewModel::updateRecordUiState,
        onAddRecord = homeViewModel::addRecord
    ) {
        Column {
            HomeAppBar(userUiState = user, onOpenAccountDialog = homeViewModel::showAccountDialog)
            HomeScreenBody(homeUiState = homeViewModel.uiState)
        }
    }
    AnimatedVisibility(visible = homeViewModel.dialogUiState) {
        AccountInfoDialog(
            users = users,
            onAddUser = onAddUser,
            onManageProfile = onManageProfile,
            onSetCurrentUser = userViewModel::setAsCurrentUser,
            updateAccountDialogVisibility = homeViewModel::dismissAccountDialog
        )
    }
}

@Composable
fun HomeScreenBody(homeUiState: HomeUiState) {
    when (homeUiState) {
        HomeUiState.Empty -> NoRecordsScreen()
        HomeUiState.Error -> ErrorScreen()
        HomeUiState.Loading -> LoadingScreen()
        is HomeUiState.Success -> RecordListScreen()
    }
}

@Composable
fun HomeAppBar(userUiState: UserUiState, onOpenAccountDialog: () -> Unit) {
    Column(
        modifier = Modifier.padding(
            top = 16.dp, start = 24.dp, end = 24.dp, bottom = 8.dp
        )
    ) {
        Image(painter = painterResource(id = if (userUiState.gender == Gender.Female) R.drawable.mrs_manas_malla else R.drawable.manas_malla),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.End)
                .size(64.dp)
                .clip(MaterialYouClipper())
                .clickable {
                    onOpenAccountDialog()
                })
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Hey there,", style = MaterialTheme.typography.titleMedium)
        Text(
            text = userUiState.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DrAarogyasHealthRecordTheme {

        HomeScreen(
            userViewModel = UserViewModel(userRepository = FakeUserRepository()),
            homeViewModel = HomeViewModel(userRepository = FakeUserRepository())
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreen_nonDyanmicPreview() {
    DrAarogyasHealthRecordTheme(dynamicColor = false) {

        HomeScreen(
            userViewModel = UserViewModel(userRepository = FakeUserRepository()),
            homeViewModel = HomeViewModel(userRepository = FakeUserRepository())
        )
    }
}

@Composable
fun RecordListScreen() {

}

@Preview(showBackground = true)
@Composable
fun LoadingScreen() {
    Text(
        text = "Loading...",
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(align = Alignment.Center)
    )
}

@Preview(showBackground = true)
@Composable
fun ErrorScreen() {

}

@Preview(showBackground = true)
@Composable
fun NoRecordsScreen() {
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
}
