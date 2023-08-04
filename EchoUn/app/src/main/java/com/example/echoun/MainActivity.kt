package com.example.echoun

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echoun.ui.theme.EchoUnTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


private var theFilePathText = mutableStateOf(" none ")
private var thePlayButtonText = mutableStateOf("Start Playing")
private var asText = mutableStateListOf<String>("App started")
private var itemsPlayed = 0
private var isPlaying = false
private var reqStop = false

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EchoUnTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Tim me")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier.padding(8.dp)
    ) {
        Text(
            text = "Hello $name!",
            fontSize = 24.sp,
            //lineHeight = 116.sp,
            textAlign = TextAlign.Center,
            modifier = modifier.padding(24.dp)
        )
        Text(
            text = theFilePathText.value,
            //fontSize = 36.sp,
            modifier = Modifier
                .padding(16.dp)
                .align(alignment = Alignment.End)
        )
        Button(onClick = { openFilePicker() }) {
            Text("Select File")
        }
        Text(
            text = "File:"
        )
        Text(
            text = theFilePathText.value,
            //fontSize = 36.sp,
            modifier = Modifier
                .padding(8.dp)
                .align(alignment = Alignment.Start)
        )
        Button(onClick = {
            toggleSpeaking()
        }){
            Text(thePlayButtonText.value)
        }
        Divider(color = Color.Blue, thickness = 1.dp)
        TextList()
    }
}


@Composable
fun TextList() {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        //contentPadding = PaddingValues(16.dp)
    ) {
        items(asText) { item ->
            //Spacer(modifier = Modifier.height(200.dp))
            Text(text = item)
        }
    }
}


private fun openFilePicker() {
    theFilePathText.value = "Bushed!"
    asText.add("Pushed!!!")
}

@Preview(showBackground = true,
    showSystemUi = true)
@Composable
fun GreetingPreview() {
    EchoUnTheme {
        Greeting("Tim me")
    }
}


private fun toggleSpeaking() {
    if (isPlaying == true) {
        // Set flag to stop playing
        reqStop = true
        thePlayButtonText.value = "Stopping"
        asText.add("# Stopping play")
    } else {
        reqStop = false
        isPlaying = true
        speakPhrases()
    }
}

private fun speakPhrases() {
    CoroutineScope(Dispatchers.IO).launch {

        asText.add("# Starting to play.")
        thePlayButtonText.value = "Stop Playing"

        itemsPlayed = 0
        while ( reqStop == false) {
            val phrase = "again."
            asText.add(phrase)
            itemsPlayed += 1
            delay(3000)
        }
        isPlaying = false
        thePlayButtonText.value = "Start Playing"
        asText.add("# Spoke $itemsPlayed phrases")
        asText.add("# Stopped playing")
    }
}