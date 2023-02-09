package com.manasmalla.draarogyashealthrecord.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material.icons.rounded.QueryStats
import androidx.compose.material.icons.rounded.Scale
import androidx.compose.material3.Card
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
import androidx.compose.ui.layout.Layout
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
import com.manasmalla.draarogyashealthrecord.model.unit
import com.manasmalla.draarogyashealthrecord.ui.components.AccountInfoDialog
import com.manasmalla.draarogyashealthrecord.ui.screens.RecordBottomSheetScaffold
import com.manasmalla.draarogyashealthrecord.ui.screens.UserUiState
import com.manasmalla.draarogyashealthrecord.ui.screens.UserViewModel
import com.manasmalla.draarogyashealthrecord.ui.theme.DrAarogyasHealthRecordTheme
import com.manasmalla.draarogyashealthrecord.ui.theme.MaterialYouClipper
import java.text.SimpleDateFormat
import kotlin.math.ceil

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

@Composable
fun RecordListScreen(records: List<Record>) {
    Column(modifier = Modifier.padding(24.dp)) {
        for (record in records) {
            RecordCard(record)
        }
    }
}

@Composable
fun RecordCard(record: Record) {
    val simpleDateFormat = SimpleDateFormat("dd\nMMM\nyy")
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = simpleDateFormat.format(record.date),
                modifier = Modifier
                    .padding(end = 24.dp)
                    .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            )
            LazyMetricGrid {
                record.record.map { metric ->
                    MetricCard(metric = metric.key, value = metric.value)
                }
            }
        }
    }
}

@Composable
fun MetricCard(metric: Metrics, value: Double) {
    Row(modifier = Modifier.padding(8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Image(
            imageVector = if (metric == Metrics.Weight) Icons.Rounded.Scale else if (metric == Metrics.BloodPressure || metric == Metrics.BloodSugar) Icons.Rounded.MonitorHeart else Icons.Rounded.QueryStats,
            contentDescription = null
        )
        Column(modifier = Modifier.wrapContentHeight()) {
            Text(text = metric.name)
            Text(text = "${value}${metric.unit}")
        }
    }
}

@Composable
fun LazyMetricGrid(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(content = content, modifier = modifier) { measurables, constraints ->
        var x = 0
        var y = 0
        val placeables = measurables.mapIndexed { index, measurable ->
            val placeable = measurable.measure(constraints)
            placeable
        }
        val maxWidth = placeables.maxOf { it.width }.times(2)
        layout(
            maxWidth,
            placeables.maxOf { it.height }.times(
                ceil(placeables.size.toDouble().div(2.0))
            )
                .toInt()
        ) {
            var placeableHeight = 0
            placeables.forEachIndexed { index, placeable ->
                if (index % 2 == 0) {
                    placeable.place(0, y)
                    x = placeable.width
                    placeableHeight = placeable.height
                } else {
                    placeable.place(maxWidth - placeable.width, y)
                    y += maxOf(placeable.height, placeableHeight)
                }
            }
        }

    }
}

@Preview
@Composable
fun LazyMetricGridPreview() {
    LazyMetricGrid {
        mapOf(
            Metrics.Calories to 1650.0,
            Metrics.BMI to 18.0,
            Metrics.BloodPressure to 120.0
        ).map { metric ->
            MetricCard(metric = metric.key, value = metric.value)
        }
    }
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
