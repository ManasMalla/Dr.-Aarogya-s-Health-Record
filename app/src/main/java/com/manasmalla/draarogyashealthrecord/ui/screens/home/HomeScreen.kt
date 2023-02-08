package com.manasmalla.draarogyashealthrecord.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.manasmalla.draarogyashealthrecord.R
import com.manasmalla.draarogyashealthrecord.ui.theme.DrAarogyasHealthRecordTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
) {


}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    DrAarogyasHealthRecordTheme {
        HomeScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreen_nonDyanmicPreview() {
    DrAarogyasHealthRecordTheme(dynamicColor = false) {
        HomeScreen()
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
