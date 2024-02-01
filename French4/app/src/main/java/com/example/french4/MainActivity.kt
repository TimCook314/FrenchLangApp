package com.example.french4

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.OpenableColumns
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.french4.ui.theme.French4Theme

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.TabRow
import androidx.compose.material3.Tab
//import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
//import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Alignment
//import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.launch
import java.util.Locale

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay


import com.arthenica.mobileffmpeg.FFmpeg
import java.io.File


private var tts_E_setup = false
private var tts_E_voice by mutableStateOf("undefined")
private lateinit var tts_E1: TextToSpeech
private var ttsE_hasStarted = false
private var ttsE_hasCompleted = false

private var tts_F1_setup = false
private var tts_F2_setup = false
private var tts_F3_setup = false
private var tts_F4_setup = false
private var tts_F5_setup = false
private var tts_F6_setup = false

private var tts_F1_voice by mutableStateOf("undefined")
private var tts_F2_voice by mutableStateOf("undefined")
private var tts_F3_voice by mutableStateOf("undefined")
private var tts_F4_voice by mutableStateOf("undefined")
private var tts_F5_voice by mutableStateOf("undefined")
private var tts_F6_voice by mutableStateOf("undefined")

private var tts_F1_checked by mutableStateOf(false)
private var tts_F2_checked by mutableStateOf(false)
private var tts_F3_checked by mutableStateOf(false)
private var tts_F4_checked by mutableStateOf(false)
private var tts_F5_checked by mutableStateOf(false)
private var tts_F6_checked by mutableStateOf(false)

private lateinit var tts_F1: TextToSpeech
private lateinit var tts_F2: TextToSpeech
private lateinit var tts_F3: TextToSpeech
private lateinit var tts_F4: TextToSpeech
private lateinit var tts_F5: TextToSpeech
private lateinit var tts_F6: TextToSpeech
private var ttsF_hasStarted = false
private var ttsF_hasCompleted = false

private var phrasesFilePathText = mutableStateOf("none")
private var phrasesFileUri = mutableStateOf<Uri?>(null)
private var isSynthesizeAudioButtonEnabled = mutableStateOf(false)


private var genText = mutableStateListOf("App started Version: 9:28 10/21")
private fun reportGenText(txt: String) {
    genText.add(txt)
}




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            French4Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    var tabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Play", "Generate", "Settings")

    Column {
        TabRow(selectedTabIndex = tabIndex) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                )
            }
        }
        when (tabIndex) {
            0 -> PlayScreen()
            1 -> GenerateScreen()
            2 -> SettingsScreen()
        }
    }
}

@Composable
fun PlayScreen() {
    Text(text = "Screen 1", style = MaterialTheme.typography.headlineMedium)
}



@Composable
fun GenerateScreen() {
    val context = LocalContext.current
    //val coroutineScope = rememberCoroutineScope()
    val theFilePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            phrasesFileUri.value = uri
            if (uri == null) {
                phrasesFilePathText.value = "none"
                isSynthesizeAudioButtonEnabled.value = false
                //reportGenText(uri.path())
            } else {
                //val context = LocalContext.current
                val fileName = getUriName(context, uri)
                reportGenText("Phrase file selected: $fileName")
                phrasesFilePathText.value = fileName
                if (tts_E_setup && tts_F1_setup) {
                    isSynthesizeAudioButtonEnabled.value = true
                }
                //readVocalData(this, uri, fileName)
            }
        }
    )

    Column(modifier = Modifier.padding(8.dp)) {
        Text(text = "Hello", style = MaterialTheme.typography.headlineMedium)

        Divider(color = Color.LightGray, thickness = 1.dp)

        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = {
                setupEnglishVoiceWrapped(context)
            }) {
                Text("Setup English Voice")
            }
            Text(tts_E_voice, textAlign = TextAlign.End, modifier = Modifier.align(Alignment.CenterVertically))
        }
        Spacer(modifier = Modifier.height(8.dp))

        Divider(color = Color.LightGray, thickness = 1.dp)

        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = {
                setupFrenchVoicesWrapped(context)
            }) {
                Text("Setup French Voices")
            }
            //Text(tts_E_voice, textAlign = TextAlign.End, modifier = Modifier.align(Alignment.CenterVertically))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider(color = Color.LightGray, thickness = 1.dp)

        Row {
            Checkbox(
                checked = tts_F1_checked,
                onCheckedChange = { isChecked -> tts_F1_checked = isChecked
                    if (isChecked) {
                        speakFrenchVoiceWrapped(context, 1)
                    }
                }
            )
            Text(tts_F1_voice, textAlign = TextAlign.End, modifier = Modifier.align(Alignment.CenterVertically))
            Spacer(modifier = Modifier.width(32.dp))
            Checkbox(
                checked = tts_F2_checked,
                onCheckedChange = { isChecked -> tts_F2_checked = isChecked
                    if (isChecked) {
                        speakFrenchVoiceWrapped(context, 2)
                    }
                }
            )
            Text(tts_F2_voice, textAlign = TextAlign.End, modifier = Modifier.align(Alignment.CenterVertically))
        }
        Row {
            Checkbox(
                checked = tts_F3_checked,
                onCheckedChange = { isChecked -> tts_F3_checked = isChecked
                    if (isChecked) {
                        speakFrenchVoiceWrapped(context, 3)
                    }
                }
            )
            Text(tts_F3_voice, textAlign = TextAlign.End, modifier = Modifier.align(Alignment.CenterVertically))
            Spacer(modifier = Modifier.width(32.dp))
            Checkbox(
                checked = tts_F4_checked,
                onCheckedChange = { isChecked -> tts_F4_checked = isChecked
                    if (isChecked) {
                        speakFrenchVoiceWrapped(context, 4)
                    }
                }
            )
            Text(tts_F4_voice, textAlign = TextAlign.End, modifier = Modifier.align(Alignment.CenterVertically))
        }
        Row {
            Checkbox(
                checked = tts_F5_checked,
                onCheckedChange = { isChecked -> tts_F5_checked = isChecked
                    if (isChecked) {
                        speakFrenchVoiceWrapped(context, 5)
                    }
                }
            )
            Text(tts_F5_voice, textAlign = TextAlign.End, modifier = Modifier.align(Alignment.CenterVertically))
            Spacer(modifier = Modifier.width(32.dp))
            Checkbox(
                checked = tts_F6_checked,
                onCheckedChange = { isChecked -> tts_F6_checked = isChecked
                    if (isChecked) {
                        speakFrenchVoiceWrapped(context, 6)
                    }
                }
            )
            Text(tts_F6_voice, textAlign = TextAlign.End, modifier = Modifier.align(Alignment.CenterVertically))
        }
        Divider(color = Color.LightGray, thickness = 1.dp)
        Button(
            onClick = {
                theFilePicker.launch("text/plain")
            },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Select phrase file")
        }
        Text(
            text = phrasesFilePathText.value,
            //style = MaterialTheme.typography.body1,
            textAlign = TextAlign.End,
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(bottom = 8.dp)
//                    modifier = Modifier
//                        .padding(bottom = 16.dp)
//                        .align(alignment = Alignment.End)
        )
        Divider(color = Color.LightGray, thickness = 1.dp)
        Button(
            onClick = {
                generateAudioFilesWrapped(context)
            },
            enabled = isSynthesizeAudioButtonEnabled.value,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text("Generate Audio Files")
        }
        Divider(color = Color.LightGray, thickness = 1.dp)
        TextListGen()
    }
}

@Composable
fun SettingsScreen() {
    Text(text = "Screen 3", style = MaterialTheme.typography.headlineMedium)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    French4Theme {
        MainScreen()
    }
}

private fun setupEnglishVoiceWrapped(context: Context){
    CoroutineScope(Dispatchers.Main).launch {
        setupEnglishVoice(context)
    }
}

private fun setupFrenchVoicesWrapped(context: Context){
    CoroutineScope(Dispatchers.Main).launch {
        setupFrenchVoices(context)
    }
}

private fun speakFrenchVoiceWrapped(context: Context, count: Int){
    CoroutineScope(Dispatchers.Main).launch {
        speakFrenchVoice(context, count)
    }
}

private suspend fun setupEnglishVoice( context: Context ) {
    reportGenText("Setting up English voice")
    tts_E1 = TextToSpeech(context) { status ->
        if (status == TextToSpeech.SUCCESS) {
            // Set language to English
            tts_E1.language = Locale.ENGLISH
            val allVoices = tts_E1.voices
            val enVoiceNames = mutableListOf<String>()
            for (voice in allVoices) {
                val txt = voice.name
                if (txt.startsWith("en-us") && txt.endsWith("local")) {
                    enVoiceNames.add(txt)
                }
            }
            val n = enVoiceNames.size
            reportGenText("  Number of English voices: $n")
            if (enVoiceNames.isNotEmpty()) {
                val english = Locale("en", "US")
                val txt = enVoiceNames.random()
                reportGenText("  Using English voice: $txt")
                val eVoice = Voice(
                    txt,
                    english,
                    Voice.QUALITY_VERY_HIGH,
                    Voice.LATENCY_NORMAL,
                    false,
                    emptySet()
                )
                tts_E1.voice = eVoice
                tts_E1.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        ttsE_hasStarted = true
                    }

                    override fun onDone(utteranceId: String?) {
                        ttsE_hasCompleted = true
                    }

                    override fun onError(utteranceId: String?, errorCode: Int) {
                        super.onError(utteranceId, errorCode)
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                    }
                })
                tts_E_voice = txt
                tts_E_setup = true
            }
        } else {
            //onResult("Failed to initialize TTS", null)
        }
    }
    var count = 0
    while (count < 100) {
        count++
        delay(100)
        if (tts_E_setup) {
            delay(100)
            tts_E1.speak("I am the english voice", TextToSpeech.QUEUE_FLUSH, null, "")
            do {
                delay(100)
            } while (!ttsE_hasStarted)
            //speaking
            do {
                delay(100)
            } while (!ttsE_hasCompleted)
            break
        }
    }
}

private suspend fun setupFrenchVoices( context: Context ) {
    reportGenText("Setting up French voices")
    val frVoiceNames = mutableListOf<String>()
    var tts = TextToSpeech(context) {}
    tts = TextToSpeech(context) { status ->
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.FRENCH
            // Get a list of available voices in FRENCH
            val allVoices = tts.voices
            for (voice in allVoices) {
                val txt = voice.name
                //if ((txt.startsWith("fr-fr") || txt.startsWith("fr-ca")) && txt.endsWith("local")) {
                if (txt.startsWith("fr-fr") && txt.endsWith("local")) {
                    frVoiceNames.add(txt)
                }
            }
        }
    }
    delay(100)
    val n = frVoiceNames.size
    reportGenText("  Number of French voices: $n")
    var count = 0
    while (count < frVoiceNames.size) {
        val txt = frVoiceNames[count]
        count++
        //reportGenText("  Voice $count called")
        when (count) {
            1 -> setupFVoice1(context, txt)
            2 -> setupFVoice2(context, txt)
            3 -> setupFVoice3(context, txt)
            4 -> setupFVoice4(context, txt)
            5 -> setupFVoice5(context, txt)
            6 -> setupFVoice6(context, txt)
        }
        delay(100)
    }
}



private suspend fun speakFrenchVoice( context: Context, count: Int ) {
    reportGenText("  Voice $count called")
    var ttsF =TextToSpeech(context) {}
    //var txt = "none"
    var ttsSetup  = false
    when (count) {
        1 -> {
            ttsSetup = tts_F1_setup
            //txt = tts_F1_voice
            if (tts_F1_setup) { ttsF = tts_F1 }
        }

        2 -> {
            ttsSetup = tts_F2_setup
            //txt = tts_F2_voice
            if (tts_F2_setup) { ttsF = tts_F2 }
        }

        3 -> {
            ttsSetup = tts_F3_setup
            //txt = tts_F3_voice
            if (tts_F3_setup) { ttsF = tts_F3 }
        }

        4 -> {
            ttsSetup = tts_F4_setup
            //txt = tts_F4_voice
            if (tts_F4_setup) { ttsF = tts_F4 }
        }

        5 -> {
            ttsSetup = tts_F5_setup
            //txt = tts_F5_voice
            if (tts_F5_setup) { ttsF = tts_F5 }
        }

        6 -> {
            ttsSetup = tts_F6_setup
            //txt = tts_F6_voice
            if (tts_F6_setup) { ttsF = tts_F6 }
        }
    }
    if (ttsSetup) {
        if (ttsF_hasStarted) {
            do {
                delay(100)
            } while (!ttsF_hasCompleted)
        }
        @Suppress("SpellCheckingInspection")
        ttsF.speak("Je suis la voix française", TextToSpeech.QUEUE_FLUSH, null, "")
        do {
            delay(100)
        } while (!ttsF_hasStarted)
        //speaking
        do {
            delay(100)
        } while (!ttsF_hasCompleted)
    }
}


@Composable
fun TextListGen() {
    val listState = rememberLazyListState()
    //val coroutineScope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = listState,
        //contentPadding = PaddingValues(16.dp)
    ) {
        items(genText) { item ->
            //Spacer(modifier = Modifier.height(200.dp))
            Text(text = item)
        }
    }
//    ScrollToBottom(
//        onClick = {
//            coroutineScope.launch {
//                // Animate scroll to the first item
//                listState.animateScrollToItem(index = asText.lastIndex)
//            }
//        }
//    )
    //ScrollToBottom(
    //    coroutineScope.launch {
    //        // Animate scroll to the first item
    //        listState.animateScrollToItem(index = asText.lastIndex)
    //    }
    //)
}





// -----------------------------------------------------------------------
private fun setupFVoice1(context: Context, voice: String) {
    //reportGenText("  Setting up French voice: $voice")
    tts_F1 = TextToSpeech(context) { }
    tts_F1 = TextToSpeech(context) { status ->
        if (status == TextToSpeech.SUCCESS) {
            val french = Locale("fr", "FR")
            val aVoice = Voice(
                voice,
                french,
                Voice.QUALITY_VERY_HIGH,
                Voice.LATENCY_NORMAL,
                false,
                emptySet()
            )
            tts_F1.voice = aVoice
            tts_F1.setSpeechRate(0.9f) // 0.9 slight slower
            tts_F1.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    ttsF_hasStarted = true
                }
                override fun onDone(utteranceId: String?) {
                    ttsF_hasCompleted = true
                }
                override fun onError(utteranceId: String?, errorCode: Int) {
                    super.onError(utteranceId, errorCode)
                }
                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                }
            })
            tts_F1_voice = voice
            tts_F1_setup = true
        }
    }
}

private fun setupFVoice2(context: Context, voice: String) {
    //reportGenText("  Setting up French voice: $voice")
    tts_F2 = TextToSpeech(context) { status ->
        if (status == TextToSpeech.SUCCESS) {
            val french = Locale("fr", "FR")
            val aVoice = Voice(
                voice,
                french,
                Voice.QUALITY_VERY_HIGH,
                Voice.LATENCY_NORMAL,
                false,
                emptySet()
            )
            tts_F2.voice = aVoice
            tts_F2.setSpeechRate(0.9f) // 0.9 slight slower
            tts_F2.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    ttsF_hasStarted = true
                }
                override fun onDone(utteranceId: String?) {
                    ttsF_hasCompleted = true
                }
                override fun onError(utteranceId: String?, errorCode: Int) {
                    super.onError(utteranceId, errorCode)
                }
                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                }
            })
            tts_F2_voice = voice
            tts_F2_setup = true
        }
    }
}

private fun setupFVoice3(context: Context, voice: String) {
    //reportGenText("  Setting up French voice: $voice")
    tts_F3 = TextToSpeech(context) { status ->
        if (status == TextToSpeech.SUCCESS) {
            val french = Locale("fr", "FR")
            val aVoice = Voice(
                voice,
                french,
                Voice.QUALITY_VERY_HIGH,
                Voice.LATENCY_NORMAL,
                false,
                emptySet()
            )
            tts_F3.voice = aVoice
            tts_F3.setSpeechRate(0.9f) // 0.9 slight slower
            tts_F3.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    ttsF_hasStarted = true
                }
                override fun onDone(utteranceId: String?) {
                    ttsF_hasCompleted = true
                }
                override fun onError(utteranceId: String?, errorCode: Int) {
                    super.onError(utteranceId, errorCode)
                }
                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                }
            })
            tts_F3_voice = voice
            tts_F3_setup = true
        }
    }
}

private fun setupFVoice4(context: Context, voice: String) {
    //reportGenText("  Setting up French voice: $voice")
    tts_F4 = TextToSpeech(context) { status ->
        if (status == TextToSpeech.SUCCESS) {
            val french = Locale("fr", "FR")
            val aVoice = Voice(
                voice,
                french,
                Voice.QUALITY_VERY_HIGH,
                Voice.LATENCY_NORMAL,
                false,
                emptySet()
            )
            tts_F4.voice = aVoice
            tts_F4.setSpeechRate(0.9f) // 0.9 slight slower
            tts_F4.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    ttsF_hasStarted = true
                }
                override fun onDone(utteranceId: String?) {
                    ttsF_hasCompleted = true
                }
                override fun onError(utteranceId: String?, errorCode: Int) {
                    super.onError(utteranceId, errorCode)
                }
                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                }
            })
            tts_F4_voice = voice
            tts_F4_setup = true
        }
    }
}

private fun setupFVoice5(context: Context, voice: String) {
    //reportGenText("  Setting up French voice: $voice")
    tts_F5 = TextToSpeech(context) { status ->
        if (status == TextToSpeech.SUCCESS) {
            val french = Locale("fr", "FR")
            val aVoice = Voice(
                voice,
                french,
                Voice.QUALITY_VERY_HIGH,
                Voice.LATENCY_NORMAL,
                false,
                emptySet()
            )
            tts_F5.voice = aVoice
            tts_F5.setSpeechRate(0.9f) // 0.9 slight slower
            tts_F5.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    ttsF_hasStarted = true
                }
                override fun onDone(utteranceId: String?) {
                    ttsF_hasCompleted = true
                }
                override fun onError(utteranceId: String?, errorCode: Int) {
                    super.onError(utteranceId, errorCode)
                }
                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                }
            })
            tts_F5_voice = voice
            tts_F5_setup = true
        }
    }
}

private fun setupFVoice6(context: Context, voice: String) {
    //reportGenText("  Setting up French voice: $voice")
    tts_F6 = TextToSpeech(context) { status ->
        if (status == TextToSpeech.SUCCESS) {
            val french = Locale("fr", "FR")
            val aVoice = Voice(
                voice,
                french,
                Voice.QUALITY_VERY_HIGH,
                Voice.LATENCY_NORMAL,
                false,
                emptySet()
            )
            tts_F6.voice = aVoice
            tts_F6.setSpeechRate(0.9f) // 0.9 slight slower
            tts_F6.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    ttsF_hasStarted = true
                }
                override fun onDone(utteranceId: String?) {
                    ttsF_hasCompleted = true
                }
                override fun onError(utteranceId: String?, errorCode: Int) {
                    super.onError(utteranceId, errorCode)
                }
                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String?) {
                }
            })
            tts_F6_voice = voice
            tts_F6_setup = true
        }
    }
}




fun generateSilenceFile(durationInSeconds: Int, outputPath: String) {
    @Suppress("SpellCheckingInspection")
    val command = arrayOf(
        "-f", "lavfi",
        "-i", "anullsrc=r=44100:cl=stereo",
        "-t", durationInSeconds.toString(),
        "-q:a", "9", // Lower quality since it's just silence
        outputPath
    )

    val returnCode = FFmpeg.execute(command)
    if (returnCode == 0) {
        reportGenText("Silence file generated successfully at: $outputPath")
    } else {
        reportGenText("Failed to generate silence file.")
    }

//    FFmpeg.executeAsync(command) { _, returnCode ->
//        if (returnCode == 0) {
//            // Command execution was successful
//            reportGenText("Silence file generated successfully at: $outputPath")
//        } else {
//            // Command execution failed
//            reportGenText("Failed to generate silence file.")
//        }
//    }
}


// =========================================================
private fun getUriName(context: Context, uri: Uri): String {
    var result = "none"
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                result = cursor.getString(nameIndex)
            }
        }
    } else if (uri.toString().startsWith("file:")) {
        val ok: String = uri.path!!
        result = File(ok).name
    }
    return result
}

private fun generateAudioFilesWrapped(context: Context){
    CoroutineScope(Dispatchers.Main).launch {
        generateAudioFiles(context)
    }
}

private fun createDirectoryInDocuments(context: Context, directoryName: String) {
    val resolver: ContentResolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, directoryName)
        put(MediaStore.MediaColumns.MIME_TYPE, "resource/folder")
        put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_DOCUMENTS}/$directoryName")
    }
    val uri: Uri? = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
    if (uri == null) {
        reportGenText("  Failed to created directory: $directoryName in Documents")
        //Log.e("MediaStore", "Failed to create directory: $directoryName in Documents.")
    } else {
        reportGenText("  Created directory: $directoryName in Documents")
        //Log.d("MediaStore", "Successfully created directory: $directoryName in Documents.")
    }
}

fun deleteFolder(folderName: String) {
    reportGenText("Try to delete")
    val documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
    val folder = File(documentsDir, folderName)
    if (folder.exists() && folder.isDirectory) {
        folder.deleteRecursively()
    }
}

fun selectRandomFrenchVoice(list: MutableList<Int>): TextToSpeech? {
    when (list.random()) {
        1 -> return tts_F1
        2 -> return tts_F2
        3 -> return tts_F3
        4 -> return tts_F4
        5 -> return tts_F5
        6 -> return tts_F6
    }
    return null

}


fun createMediaStoreUri(context: Context, folder: String, displayName: String): Uri {
    val values = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        put(MediaStore.MediaColumns.MIME_TYPE, "audio/wav")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/"+ folder)
    }
    return context.contentResolver.insert(MediaStore.Files.getContentUri("external"), values)!!
}

private suspend fun generateAudioFiles(context: Context ) {
    delay(100)
//    val fvName = ttsF.voice.name
//    reportIt("  Using: " + fvName.substring(8, 11) )
    // Get name of phrase file
    val phraseFileName = phrasesFilePathText.value
    //Remove the extension
    val newfolder = "French4Audio/" + File(phraseFileName).nameWithoutExtension
    val tmpfolder = "French4Audio/" + "temp"

    //Delete folder if there
    deleteFolder( newfolder)
    deleteFolder( tmpfolder)

    // Create the folder in Documents
    reportGenText("  Creating directory: $newfolder")
    createDirectoryInDocuments(context, newfolder)
    createDirectoryInDocuments(context, tmpfolder)


    val lines = mutableListOf<String>()
    var count = 0
    val uri = phrasesFileUri.value
    if (uri != null) {
        val ips = context.contentResolver.openInputStream(uri)
        ips?.bufferedReader().use { reader ->
            reader?.forEachLine { line ->
                lines.add(line)
                count++
            }
        }
        ips?.close()
    }
    reportGenText("Read: $count lines")


    val validVoiceList = mutableListOf<Int>()
    if (tts_F1_setup && tts_F1_checked) {validVoiceList.add(1)}
    if (tts_F2_setup && tts_F2_checked) {validVoiceList.add(2)}
    if (tts_F3_setup && tts_F3_checked) {validVoiceList.add(3)}
    if (tts_F4_setup && tts_F4_checked) {validVoiceList.add(4)}
    if (tts_F5_setup && tts_F5_checked) {validVoiceList.add(5)}
    if (tts_F6_setup && tts_F6_checked) {validVoiceList.add(6)}


    val documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
    val folder = File(documentsDir, "$tmpfolder/silence_3.wav")
    generateSilenceFile( 3, folder.absolutePath)
    reportGenText("  Generated!")

    var countValue = 0
    reportGenText("  Generating stuff ")
    lines.forEachIndexed { index, line ->
        val parts = line.split(";")
        if (parts.size >= 4) {
            val temp0 = parts[0].lowercase().trim()
            val tempV = parts[1].trim()
            val tempF = parts[2].trim()
            val tempE = parts[3].trim()
            when (temp0) {
                "phrase" -> {
                    countValue += 1
                    // Create a unique filename for each part
                    val filename1 = "item_$index" + "_fr.wav"
                    val filename2 = "item_$index" + "_en.wav"
                    val filename3 = "item_$index.wav"
                    // Create an output Uri for each file
                    val uri1 = createMediaStoreUri(context, tmpfolder, filename1)
                    val uri2 = createMediaStoreUri(context, tmpfolder, filename2)
                    //val uri3 = createMediaStoreUri(context, tmpfolder, filename3)
                    // Create a ParcelFileDescriptor for each Uri
                    val pfd1 = context.contentResolver.openFileDescriptor(uri1, "w")
                    val pfd2 = context.contentResolver.openFileDescriptor(uri2, "w")
                    // Synthesize the speech to a file
                    val ttsF = selectRandomFrenchVoice( validVoiceList )
                    ttsF!!.synthesizeToFile(tempF, Bundle(), pfd1!!, "ttsF_$index")
                    tts_E1.synthesizeToFile(tempE, Bundle(), pfd2!!, "ttsE_$index")

                    do {
                        delay(50)
                    } while (!ttsE_hasStarted)
                    //speaking
                    do {
                        delay(50)
                    } while (!ttsE_hasCompleted)
                    ttsE_hasStarted = false
                    ttsE_hasCompleted = false
                    do {
                        delay(50)
                    } while (!ttsF_hasStarted)
                    //speaking
                    do {
                        delay(50)
                    } while (!ttsF_hasCompleted)
                    ttsF_hasStarted = false
                    ttsF_hasCompleted = false
                    //reportIt("  Another!.")
                    delay(100)

                    pfd1.close()
                    pfd2.close()

                    //Create one file
                    val fileFr = File(documentsDir, "$tmpfolder/$filename1")
                    val fileEn = File(documentsDir, "$tmpfolder/$filename2")
                    val fileSl = File(documentsDir, "$tmpfolder/silence_3.wav")
                    val fileOt = File(documentsDir, "$newfolder/$filename3")

                    val audioFiles = arrayOf(
                        fileFr.absolutePath,
                        fileSl.absolutePath,
                        fileEn.absolutePath,
                        fileSl.absolutePath,
                        fileFr.absolutePath,
                        fileSl.absolutePath,
                    )
                    concatenateAudios(audioFiles, fileOt.absolutePath)
                    if (countValue % 10 == 0) {
                        reportGenText("  Generated $countValue items")
                    }
                }
            }
        }
    }
    deleteFolder( tmpfolder)
    reportGenText("  Done! Generated $countValue items")
}


suspend fun concatenateAudios(filePaths: Array<String>, outputPath: String) {
//    // Generate the silence file
//    val silenceFilePath = "/path/to/your/silence.wav"
//    generateSilenceFileSynchronously(3, silenceFilePath)

    // Generate the file list content for FFmpeg
//    val fileListContent = filePaths.joinToString(separator = "\n") { "file '$it'" } +
//            "\nfile '$silenceFilePath'\n".repeat(filePaths.size - 1)
    val fileListContent = filePaths.joinToString(separator = "\n") { "file '$it'" }

    // Write the content to a temporary file list
    val tmpfolder = "French4Audio/" + "temp"
    val documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
    val fileListPath = File(documentsDir, "$tmpfolder/file_list.txt")
    //val fileListPath =
    File(fileListPath.absolutePath).writeText(fileListContent)
    delay(50)

    // FFmpeg command to concatenate the files
    val command = arrayOf(
        "-f", "concat",
        "-safe", "0",
        "-i", fileListPath.absolutePath,
        "-c", "copy",
        outputPath
    )

    val returnCode = FFmpeg.execute(command)
    if (returnCode == 0) {
        reportGenText("Generated: $outputPath")
    } else {
        reportGenText("Failed to generate file.")
    }
//    // Execute the FFmpeg command
//    FFmpeg.executeAsync(command) { _, returnCode ->
//        if (returnCode == 0) {
//            // Command execution was successful
//            println("Files concatenated successfully to: $outputPath")
//        } else {
//            // Command execution failed
//            println("Failed to concatenate files.")
//        }
//    }
}
