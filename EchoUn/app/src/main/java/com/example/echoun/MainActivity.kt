package com.example.echoun

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.SystemClock
import android.provider.OpenableColumns
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.echoun.ui.theme.EchoUnTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.util.Locale


private var theFilePathText = mutableStateOf("none")
private var thePlayButtonText = mutableStateOf("Start Playing")
private var asText = mutableStateListOf<String>("App started")
private var itemsPlayed = 0
private var isPlaying = false
private var reqStop = false

private var fileUri = mutableStateOf<Uri?>(null)

private var vocabIsLoaded = false
private var vocabList = mutableListOf<Vocab>()

private lateinit var ttsE: TextToSpeech
private var ttsL = mutableListOf<TextToSpeech>()
private lateinit var tts2: TextToSpeech

//var imageUri by remember {
//    mutableStateOf<Uri?>(null)
//}

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


    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {

        val theFilePicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                fileUri.value = uri
                if (uri == null) {
                    //asText.add(uri.path())
                } else {
                    asText.add("Got new file, name is:")
                    val itis = getUriName(this, uri)
                    asText.add(itis)
                    theFilePathText.value = itis
                    readVocalData(this, uri, itis)
                }
            }
        )

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
//            Text(
//                text = theFilePathText.value,
//                //fontSize = 36.sp,
//                modifier = Modifier
//                    .padding(16.dp)
//                    .align(alignment = Alignment.End)
//            )
//            Button(onClick = { openFilePicker() }) {
//                Text("Select File")
//            }

            Button(onClick = { theFilePicker.launch("text/plain") }) {
                Text("Vocab File Picker")
            }


            Text(
                text = "File:"
            )
            Text(
                text = theFilePathText.value,
                //fontSize = 36.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .align(alignment = Alignment.End)
            )
            Button(onClick = {
                if (!isPlaying) {setupVoices()}
                toggleSpeaking()
            }) {
                Text(thePlayButtonText.value)
            }
            Divider(color = Color.Blue, thickness = 1.dp)
            TextList()
        }
    }


    private fun setupVoices() {
        asText.add("setupVoices: 1")
        ttsE = TextToSpeech(this, TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsE.language = Locale.ENGLISH
                // Get a list of available voices in English
                val allVoices = ttsE.voices
                val enVoiceNames = mutableListOf<String>()
                for (voice in allVoices) {
                    val txt = voice.name
                    if (txt.startsWith("en-us") && txt.endsWith("local")) {
                        enVoiceNames.add(txt)
                    }
                }
                if (enVoiceNames.isNotEmpty()) {
                    val english = Locale("en", "US")
                    val txt = enVoiceNames.random()
                    asText.add("Using English voice: $txt")
                    val eVoice = Voice(
                        txt,
                        english,
                        Voice.QUALITY_VERY_HIGH,
                        Voice.LATENCY_NORMAL,
                        false,
                        emptySet()
                    )
                    ttsE.voice = eVoice
                }
            }
        })
        var tts1 = TextToSpeech(this) {
            //if (it == TextToSpeech.SUCCESS) { /*tts1.language = Locale.FRENCH */
            //}
        }
        tts1 = TextToSpeech(this, TextToSpeech.OnInitListener { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts1.language = Locale.FRENCH
                // Get a list of available voices in FRENCH
                val allVoices = tts1.voices
                val frVoiceNames = mutableListOf<String>()
                for (voice in allVoices) {
                    val txt = voice.name
                    //if ((txt.startsWith("fr-fr") || txt.startsWith("fr-ca")) && txt.endsWith("local")) {
                    if (txt.startsWith("fr-fr") && txt.endsWith("local")) {
                        frVoiceNames.add(txt)
                    }
                }
                if (frVoiceNames.count() > 0) {
                    val french = Locale("fr", "FR")
                    val txt = frVoiceNames.random()
                    asText.add("Using French voice: " + txt)
                    val aVoice = Voice(
                        txt,
                        french,
                        Voice.QUALITY_VERY_HIGH,
                        Voice.LATENCY_NORMAL,
                        false,
                        emptySet()
                    )
                    tts1.setVoice(aVoice)
                    //ttsE.setSpeechRate(1.5f) // Faster speech
                    //ttsE.setPitch(0.8f) // Lower pitch
                }
            }
        })


        tts2 = tts1
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


//@Preview(showBackground = true,
//    showSystemUi = true)
//@Composable
//fun GreetingPreview() {
//    EchoUnTheme {
//        Greeting("Tim me")
//    }
//}


private fun toggleSpeaking() {
    if (isPlaying) {
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
        //var currentPhrase: String
        asText.add("# Starting to play.")
        thePlayButtonText.value = "Stop Playing"

        itemsPlayed = 0
        while (reqStop == false) {
            val aVocab = vocabList.random()
            val phrase = aVocab.french + " --> " + aVocab.english
            asText.add(phrase)
            //ttsL.random().speak(aVocab.french, TextToSpeech.QUEUE_FLUSH, null, null)

            // Start the timer
            var startTime = SystemClock.uptimeMillis()
            tts2.speak(aVocab.french, TextToSpeech.QUEUE_FLUSH, null, null)
            var elapsedTime = SystemClock.uptimeMillis() - startTime
            delay(Math.min(Math.max(3000, (2.5*elapsedTime).toLong() ), 20000) )

            startTime = SystemClock.uptimeMillis()
            ttsE.speak(aVocab.english, TextToSpeech.QUEUE_FLUSH, null, null)
            elapsedTime = SystemClock.uptimeMillis() - startTime
            itemsPlayed += 1
            delay(Math.min(Math.max(3000, (2.5*elapsedTime).toLong() ), 20000) )
        }
        isPlaying = false
        thePlayButtonText.value = "Start Playing"
        asText.add("# Spoke $itemsPlayed phrases")
        asText.add("# Stopped playing")
    }
}


private fun selFile() {


}

private fun getdata(myfile: File): String {
    // on below line creating a variable for file input stream.
    var fileInputStream: FileInputStream? = null
    // on below line reading data from file and returning it.
    try {
        fileInputStream = FileInputStream(myfile)
        var i: Int
        val buffer = StringBuffer()
        while (fileInputStream.read().also { i = it } != -1) {
            buffer.append(i.toChar())
        }
        return buffer.toString()
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    } finally {
        if (fileInputStream != null) {
            try {
                fileInputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    return ""
}


@Throws(IOException::class)
fun readUri(context: Context, uri: Uri?): ByteArray? {
    val pdf = context.contentResolver.openFileDescriptor(uri!!, "r")!!
    assert(pdf.statSize <= Int.MAX_VALUE)
    val data = ByteArray(pdf.statSize.toInt())
    val fd = pdf.fileDescriptor
    val fileStream = FileInputStream(fd)
    fileStream.read(data)
    return data
}


private fun getUriName(context: Context, uri: Uri): String {
    var result = "none"
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                asText.add("nameIndex$nameIndex")
                result = cursor.getString(nameIndex)
            }
        }
    } else if (uri.toString().startsWith("file:")) {
        result = File(uri.path).name
    }

    if (result != "none") {
        // Initialize a count variable
        var count = 0
        //val context = LocalContext.current
        // Use a try-catch block to handle any exceptions
        //try {
        // Get a BufferedReader from the Uri using the ContentResolver and the openInputStream method
        val bufferedReader =
            BufferedReader(InputStreamReader(context.contentResolver.openInputStream(uri)))

        // Use a loop to read each line until the end of the file
        var line = bufferedReader.readLine()
        while (line != null) {
            // Check if the line starts with "#"
            if (line.startsWith("#")) {
                // Increment the count variable
                count++
            }
            // Read the next line
            line = bufferedReader.readLine()
        }

        // Close the BufferedReader
        bufferedReader.close()
        asText.add("lines with # is $count")

        // Display the count in a Text composable or do something else with it
        //    Text(text = "The number of lines that start with # is $count")

        //} catch (e: Exception) {
        // Handle any exceptions, for example by displaying an error message
        //    Text(text = "An error occurred: ${e.message}")
        //}
    }


    return result
}

private fun readVocalData(context: Context, uri: Uri, name: String) {
    if (name == "none") {
        return
    }
    asText.add("Reading file.")
    // Initialize a count variable
    var linesRead = 0
    var count = 0
    //val context = LocalContext.current
    // Use a try-catch block to handle any exceptions
    try {
        // Get a BufferedReader from the Uri using the ContentResolver and the openInputStream method
        val bufferedReader =
            BufferedReader(InputStreamReader(context.contentResolver.openInputStream(uri)))

        // Use a loop to read each line until the end of the file
        var line = bufferedReader.readLine()
        while (line != null) {
            linesRead++
            val parts = line.split(";")
            // Check if the line starts with "#"
            if (line.startsWith("#")) {
                // Increment the count variable
                count++
            } else if (line.startsWith("@")) {
                //Read data
            } else if ((parts.size >= 3) && parts[0].lowercase().startsWith("phrase")) {
                //Parse the phrase
                val mine = Vocab()
                mine.french = parts[1]
                mine.english = parts[2]
                mine.group = 1
                mine.frequency = 1
//                if (parts.size >= 4) {
//                    mine.group = parts[3].toInt()
//                }
//                if (parts.size >= 5) {
//                    mine.frequency = parts[4].toInt()
//                }
                vocabIsLoaded = true
                vocabList.add(mine)
            }
            // Read the next line
            line = bufferedReader.readLine()
        }
        // Close the BufferedReader
        bufferedReader.close()
        asText.add("  Lines read is $linesRead")
        asText.add("  Lines with # is $count")

        //var mActivity: MainActivity
        //mActivity.setupVoices()
        //(activity as MainActivity).setupVoices()

    } catch (e: Exception) {
        asText.add("*** Error reading file at about line $linesRead")
    }
}


class VoicesOptions {
    var speed: Double = 1.0
    var pauseFactor: Double = 1.0
    var voice1: String = ""
    var voice2: String = ""
}

//This class is for the data structure
class Vocab {
    var lang: String = ""
    var french: String = ""
    var english: String = ""
    var group: Int = 1
    var frequency: Int = 1
}

