package com.manasmalla.draarogyashealthrecord.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manasmalla.draarogyashealthrecord.R
import com.manasmalla.draarogyashealthrecord.data.fake.FakeRecordRepository
import com.manasmalla.draarogyashealthrecord.data.fake.FakeUserRepository
import com.manasmalla.draarogyashealthrecord.model.Gender
import com.manasmalla.draarogyashealthrecord.model.Metrics
import com.manasmalla.draarogyashealthrecord.model.Record
import com.manasmalla.draarogyashealthrecord.ui.ProfileUiState
import com.manasmalla.draarogyashealthrecord.ui.components.AccountInfoDialog
import com.manasmalla.draarogyashealthrecord.ui.components.RecordCard
import com.manasmalla.draarogyashealthrecord.ui.screens.UserUiState
import com.manasmalla.draarogyashealthrecord.ui.screens.UserViewModel
import com.manasmalla.draarogyashealthrecord.ui.screens.record.RecordBottomSheetScaffold
import com.manasmalla.draarogyashealthrecord.ui.theme.DrAarogyasHealthRecordTheme
import com.manasmalla.draarogyashealthrecord.ui.theme.MaterialYouClipper
import java.util.Calendar

@OptIn(ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    userViewModel: UserViewModel,
    homeViewModel: HomeViewModel,
    onAddUser: () -> Unit = {},
    onManageProfile: () -> Unit = {},
    onRecordPressed: (Int) -> Unit = {},
    onToggleTheme: () -> Unit = {}
) {
    val users by userViewModel.users.collectAsState()
    val user by homeViewModel.userUiState.collectAsState()
    val context = LocalContext.current
    RecordBottomSheetScaffold(
        recordUiState = homeViewModel.recordUiState,
        onUiStateChanged = homeViewModel::updateRecordUiState,
        onAddRecord = homeViewModel::addRecord,
        onAddPastRecord = {
            homeViewModel.addPastRecord(context)
        },
    ) {
        AnimatedContent(targetState = homeViewModel.uiState) {
            when (it) {
                HomeUiState.Empty -> NoRecordsScreen(
                    user = user,
                    imageUiState = homeViewModel.profileUiState,
                    onOpenAccountDialog = homeViewModel::showAccountDialog
                )

                HomeUiState.Error -> ErrorScreen()
                HomeUiState.Loading -> LoadingScreen()
                is HomeUiState.Success -> HomeScreenBody(
                    homeUiState = it,
                    user = user,
                    homeViewModel = homeViewModel,
                    onRecordPressed = onRecordPressed
                )
            }
        }
    }
    AnimatedVisibility(visible = homeViewModel.dialogUiState) {
        AccountInfoDialog(
            users = users,
            profileUiStates = userViewModel.usersProfileUiState,
            onAddUser = onAddUser,
            onManageProfile = onManageProfile,
            onSetCurrentUser = { user ->
                userViewModel.setAsCurrentUser(user = user)
                homeViewModel.dismissAccountDialog()
            },
            onToggleTheme = onToggleTheme,
            updateAccountDialogVisibility = homeViewModel::dismissAccountDialog
        )
    }
}

@Composable
fun HomeScreenBody(
    homeUiState: HomeUiState.Success,
    onRecordPressed: (Int) -> Unit = {},
    user: UserUiState,
    homeViewModel: HomeViewModel
) {
    LazyColumn {

        item {
            HomeAppBar(
                userUiState = user,
                onOpenAccountDialog = homeViewModel::showAccountDialog,
                imageUiState = homeViewModel.profileUiState
            )
        }
        item {
            RecordListScreen(
                records = homeUiState.records,
                onRecordPressed = onRecordPressed
            )
        }

    }
}

@Composable
fun HomeAppBar(
    userUiState: UserUiState,
    imageUiState: ProfileUiState = ProfileUiState.Loading,
    onOpenAccountDialog: () -> Unit
) {
    Column(
        modifier = Modifier.padding(
            top = 16.dp, start = 24.dp, end = 24.dp, bottom = 8.dp
        )
    ) {
        ProfileImage(imageUiState = imageUiState,
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ProfileImage(modifier: Modifier = Modifier, imageUiState: ProfileUiState) {
    AnimatedContent(targetState = imageUiState) { haveProfilePicture ->
        when (haveProfilePicture) {
            is ProfileUiState.Default -> {
                Image(
                    painter = painterResource(id = if (haveProfilePicture.gender == Gender.Female) R.drawable.mrs_manas_malla else R.drawable.manas_malla),
                    contentDescription = "Profile Picture",
                    modifier = modifier
                )
            }

            is ProfileUiState.Storage -> {
                Image(
                    bitmap = haveProfilePicture.bitmap,
                    contentDescription = "Profile Picture",
                    modifier = modifier, contentScale = ContentScale.Crop
                )
            }

            ProfileUiState.Loading -> {
                Surface(tonalElevation = 8.dp, modifier = modifier) {

                }
            }
        }
    }
}


@Composable
fun RecordListScreen(records: List<Record>, onRecordPressed: (Int) -> Unit = {}) {
    Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        for (record in records) {
            RecordCard(
                record = record.record,
                date = record.date,
                modifier = Modifier.clickable { onRecordPressed(record.id) })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecordListPreview() {
    val records = listOf(
        Record(
            record = mapOf(
                Metrics.Calories to 1650.0,
                Metrics.BMI to 20.0,
                Metrics.BloodPressure to 120.0,
            ), date = Calendar.getInstance().time, userId = 0
        ), Record(
            record = mapOf(
                Metrics.Calories to 1650.0,
                Metrics.BMI to 20.0,
                Metrics.Weight to 60.0,
            ), date = Calendar.getInstance().time, userId = 0
        )
    )
    RecordListScreen(records = records)
}

@Preview(showBackground = true)
@Composable
fun LoadingScreen() {

    CircularProgressIndicator(
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
fun NoRecordsScreen(
    user: UserUiState = UserUiState("User", "", Gender.Male),
    imageUiState: ProfileUiState = ProfileUiState.Loading,
    onOpenAccountDialog: () -> Unit = {}
) {
    Column {
        HomeAppBar(
            userUiState = user,
            onOpenAccountDialog = onOpenAccountDialog,
            imageUiState = imageUiState
        )
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
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DrAarogyasHealthRecordTheme {
        val context = LocalContext.current
        HomeScreen(
            userViewModel = UserViewModel(userRepository = FakeUserRepository(), context),
            homeViewModel = HomeViewModel(
                userRepository = FakeUserRepository(), FakeRecordRepository(),
                context
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreen_nonDyanmicPreview() {
    DrAarogyasHealthRecordTheme(dynamicColor = false) {
        val context = LocalContext.current
        HomeScreen(
            userViewModel = UserViewModel(userRepository = FakeUserRepository(), context),
            homeViewModel = HomeViewModel(
                userRepository = FakeUserRepository(), FakeRecordRepository(), context
            )
        )
    }
}