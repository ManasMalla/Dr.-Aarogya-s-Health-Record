package com.manasmalla.draarogyashealthrecord.ui.screens.home

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
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
import androidx.compose.ui.graphics.asImageBitmap
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
import com.manasmalla.draarogyashealthrecord.ui.components.AccountInfoDialog
import com.manasmalla.draarogyashealthrecord.ui.components.RecordCard
import com.manasmalla.draarogyashealthrecord.ui.screens.RecordBottomSheetScaffold
import com.manasmalla.draarogyashealthrecord.ui.screens.UserUiState
import com.manasmalla.draarogyashealthrecord.ui.screens.UserViewModel
import com.manasmalla.draarogyashealthrecord.ui.theme.DrAarogyasHealthRecordTheme
import com.manasmalla.draarogyashealthrecord.ui.theme.MaterialYouClipper
import java.io.File
import java.util.Calendar

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
            onSetCurrentUser = { user ->
                userViewModel.setAsCurrentUser(user = user)
                homeViewModel.dismissAccountDialog()
            },
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
        is HomeUiState.Success -> RecordListScreen(records = homeUiState.records)
    }
}

@Composable
fun HomeAppBar(userUiState: UserUiState, onOpenAccountDialog: () -> Unit) {
    Column(
        modifier = Modifier.padding(
            top = 16.dp, start = 24.dp, end = 24.dp, bottom = 8.dp
        )
    ) {
        ProfileImage(imagePath = userUiState.image, gender = userUiState.gender, modifier = Modifier
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
fun ProfileImage(modifier: Modifier = Modifier, imagePath: String?, gender: Gender) {

    AnimatedContent(targetState = imagePath != null) { haveProfilePicture ->
        when (haveProfilePicture) {
            true -> {
                val context = LocalContext.current
                val fileDir = File(context.filesDir, imagePath!!)
                if (fileDir.exists()) {
                    val bitmapData = fileDir.inputStream().readBytes()
                    val imageBitmap = BitmapFactory.decodeByteArray(
                        fileDir.inputStream().readBytes(),
                        0,
                        bitmapData.size
                    ).asImageBitmap()
                    Image(
                        bitmap = imageBitmap,
                        contentDescription = "Profile Picture",
                        modifier = modifier
                    )
                }
            }

            false -> {
                Image(
                    painter = painterResource(id = if (gender == Gender.Female) R.drawable.mrs_manas_malla else R.drawable.manas_malla),
                    contentDescription = "Profile Picture",
                    modifier = modifier
                )
            }
        }

    }
}


@Composable
fun RecordListScreen(records: List<Record>) {
    Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        for (record in records) {
            RecordCard(record = record.record, date = record.date)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecordListPreview() {
    val records = listOf(
        Record(
            record =
            mapOf(
                Metrics.Calories to 1650.0,
                Metrics.BMI to 20.0,
                Metrics.BloodPressure to 120.0,
            ), date = Calendar.getInstance().time, userId = 0
        ), Record(
            record =
            mapOf(
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

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DrAarogyasHealthRecordTheme {

        HomeScreen(
            userViewModel = UserViewModel(userRepository = FakeUserRepository()),
            homeViewModel = HomeViewModel(
                userRepository = FakeUserRepository(), FakeRecordRepository()
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreen_nonDyanmicPreview() {
    DrAarogyasHealthRecordTheme(dynamicColor = false) {

        HomeScreen(
            userViewModel = UserViewModel(userRepository = FakeUserRepository()),
            homeViewModel = HomeViewModel(
                userRepository = FakeUserRepository(), FakeRecordRepository()
            )
        )
    }
}