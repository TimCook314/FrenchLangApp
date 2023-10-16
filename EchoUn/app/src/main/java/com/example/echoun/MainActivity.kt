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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
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
import java.io.InputStreamReader
import java.util.Locale


private var theFilePathText = mutableStateOf("none")
private var thePlayButtonText = mutableStateOf("Start Playing")
private var asText = mutableStateListOf("App started Version: 9:28 9/13")
private var itemsPlayed = 0
private var isPlaying = false
private var reqStop = false

private var fileUri = mutableStateOf<Uri?>(null)

//private var vocabIsLoaded = false

private lateinit var ttsE: TextToSpeech
//private var ttsL = mutableListOf<TextToSpeech>()
private lateinit var tts2: TextToSpeech

//var imageUri by remember {
//    mutableStateOf<Uri?>(null)
//}

//class MyUtteranceProgressListener : UtteranceProgressListener() {
//
//    private var startTime: Long = 0
//
//    override fun onStart(utteranceId: String) {
//        startTime = System.currentTimeMillis()
//        asText.add("A")
//    }
//
//    override fun onDone(utteranceId: String) {
//        val endTime = System.currentTimeMillis()
//        val speakingTime = endTime - startTime
//        asText.add("B")
//        //Log.d("MyUtteranceProgressListener", "Speaking time: $speakingTime ms")
//    }
//
//    override fun onError(utteranceId: String) {
//        //Log.e("MyUtteranceProgressListener", "Error speaking utterance")
//    }
//}

private fun reportIt(txt: String) {
    asText.add(txt)
}

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
                    //reportIt(uri.path())
                } else {
                    reportIt("Got new file, name is:")
                    val itis = getUriName(this, uri)
                    reportIt(itis)
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
        reportIt("setupVoices: 1")
        ttsE = TextToSpeech(this) { status ->
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
                    reportIt("Using English voice: $txt")
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
        }
        var tts1 = TextToSpeech(this) {
            //if (it == TextToSpeech.SUCCESS) { /*tts1.language = Locale.FRENCH */
            //}
        }
        tts1 = TextToSpeech(this) { status ->
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
                if (frVoiceNames.isNotEmpty()) {
                    val french = Locale("fr", "FR")
                    val txt = frVoiceNames.random()
                    reportIt("Using French voice: $txt")
                    val aVoice = Voice(
                        txt,
                        french,
                        Voice.QUALITY_VERY_HIGH,
                        Voice.LATENCY_NORMAL,
                        false,
                        emptySet()
                    )
                    tts1.voice = aVoice
                    //val listener = MyUtteranceProgressListener()
                    //tts1.setOnUtteranceProgressListener(MyUtteranceProgressListener())
                    tts1.setSpeechRate(vocabList.frenchSpeed) // Faster speech
                    //ttsE.setPitch(0.8f) // Lower pitch
                }
            }
        }


        tts2 = tts1
    }
}

@Composable
fun TextList() {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        state = listState,
        //contentPadding = PaddingValues(16.dp)
    ) {
        items(asText) { item ->
            //Spacer(modifier = Modifier.height(200.dp))
            Text(text = item)
        }
    }
    ScrollToBottom(
        onClick = {
            coroutineScope.launch {
                // Animate scroll to the first item
                listState.animateScrollToItem(index = asText.lastIndex)
            }
        }
    )
    //ScrollToBottom(
    //    coroutineScope.launch {
    //        // Animate scroll to the first item
    //        listState.animateScrollToItem(index = asText.lastIndex)
    //    }
    //)

}
@Composable
private fun ScrollToBottom(onClick: () -> Unit = {}) = Unit

//@Composable
//private fun ScrollToBottom()

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
        reportIt("# Stopping play")
    } else {
        reqStop = false
        isPlaying = true
        speakPhrases()
    }
}

private fun speakPhrases() {
    CoroutineScope(Dispatchers.IO).launch {
        //var currentPhrase: String
        reportIt("# Starting to play.")
        thePlayButtonText.value = "Stop Playing"
        //val selectedSeries = vocabList.toMutableList()
        //tts2.setOnUtteranceProgressListener(MyUtteranceProgressListener())

        itemsPlayed = 0
        while (!reqStop) {

            val aPhrasePair =  getThePhrase()
            val phrase = aPhrasePair.fTxt + " --> " + aPhrasePair.eTxt
            reportIt(phrase)

            val audioPattern = vocabList.audioPattern
            for (ch in audioPattern.iterator()) {
                when (ch) {
                    'f' -> {
                        // Start the timer
                        tts2.speak(aPhrasePair.fTxt, TextToSpeech.QUEUE_FLUSH, null, null)
                        val startTime = SystemClock.uptimeMillis()
                        do {
                            delay(100)
                        } while (tts2.isSpeaking)
                        val elapsedTimeF = SystemClock.uptimeMillis() - startTime
                        delay(Math.min(Math.max(3000, (vocabList.frenchPauseFactor*elapsedTimeF).toLong() ), 20000) )
                    }
                    'e' -> {
                        ttsE.speak(aPhrasePair.eTxt, TextToSpeech.QUEUE_FLUSH, null, null)
                        val startTime = SystemClock.uptimeMillis()
                        do {
                            delay(100)
                        } while (ttsE.isSpeaking)
                        val elapsedTimeE = SystemClock.uptimeMillis() - startTime
                        itemsPlayed += 1
                        delay(Math.min(Math.max(2000, (1.0*elapsedTimeE).toLong() ), 20000) )
                    }
                    else -> {
                        //reportIt("*** Error parsing line $line")
                        //code
                    }
                }
            }
        }
        isPlaying = false
        thePlayButtonText.value = "Start Playing"
        reportIt("# Spoke $itemsPlayed phrases")
        reportIt("# Stopped playing")
    }
}


//private fun getdata(myfile: File): String {
//    // on below line creating a variable for file input stream.
//    var fileInputStream: FileInputStream? = null
//    // on below line reading data from file and returning it.
//    try {
//        fileInputStream = FileInputStream(myfile)
//        var i: Int
//        val buffer = StringBuffer()
//        while (fileInputStream.read().also { i = it } != -1) {
//            buffer.append(i.toChar())
//        }
//        return buffer.toString()
//    } catch (e: java.lang.Exception) {
//        e.printStackTrace()
//    } finally {
//        if (fileInputStream != null) {
//            try {
//                fileInputStream.close()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//    }
//    return ""
//}


//@Throws(IOException::class)
//fun readUri(context: Context, uri: Uri?): ByteArray? {
//    val pdf = context.contentResolver.openFileDescriptor(uri!!, "r")!!
//    assert(pdf.statSize <= Int.MAX_VALUE)
//    val data = ByteArray(pdf.statSize.toInt())
//    val fd = pdf.fileDescriptor
//    val fileStream = FileInputStream(fd)
//    fileStream.read(data)
//    return data
//}


private fun getUriName(context: Context, uri: Uri): String {
    var result = "none"
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                reportIt("nameIndex$nameIndex")
                result = cursor.getString(nameIndex)
            }
        }
    } else if (uri.toString().startsWith("file:")) {
        val ok: String = uri.path!!
        result = File(ok).name
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
        reportIt("lines with # is $count")

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
    reportIt("Reading file.")
    // Initialize a count variable
    //val context = LocalContext.current
    // Use a try-catch block to handle any exceptions
    try {
        // Get a BufferedReader from the Uri using the ContentResolver and the openInputStream method
        val bufferedReader =
            BufferedReader(InputStreamReader(context.contentResolver.openInputStream(uri)))

        val linesRead = readIt(bufferedReader)
        // Close the BufferedReader
        bufferedReader.close()
        reportIt("  Lines read is $linesRead")

        //var mActivity: MainActivity
        //mActivity.setupVoices()
        //(activity as MainActivity).setupVoices()

    } catch (e: Exception) {
        reportIt("*** Error accessing file.")
    }
}


//private fun idk(tts: TextToSpeech, uri: Uri, name: String) {
//    //wait for start
//    // Start the timer
//    var startTime = SystemClock.uptimeMillis()
//    while (tts.isSpeaking()) {
//        delay(100)
//    }
//    delay(Math.min(Math.max(3000, (2.5*elapsedTime).toLong() ), 20000) )
//    if (x > 0) {
//        started
//    }
//
//}


// Paste Here --------------------------------------------------------


//Classes -----
enum class Perspective {
    Unset, First, Second, Third,
}

enum class Gender {
    Unset, M, F, Either,
}
enum class Quantity {
    Unset, Single, Plural, Either,
}


class Pronoun {
    var MFSP: String = ""
    var fText: String = ""
    var eText: String = ""
    var freq: Int = 1
}

class Noun {
    var wordGender: Gender = Gender.Unset
    var wordQuantity: Quantity = Quantity.Unset
    var article: String = ""
    var sText: String = ""
    var pText: String = ""
    var eText: String = ""
    //var freq: Int = 1
}

class Adjective {
    var eAdj: String = ""
    var relPos: String = ""
    var masculine: String = ""
    var feminine: String = ""
    var masculinePlural: String = ""
    var femininePlural: String = ""
    var freq: Int = 1
}

class Verb {
    var fInfinitive: String = ""
    var eBase: String = ""
    var e3rdPeronSingular: String = ""
    var Je: String = ""
    var Tu: String = ""
    var Il_Elle: String = ""
    var Nous: String = ""
    var Vous: String = ""
    var Ils_Elles: String = ""
    var freq: Int = 1
}


class Phrase {
    var fPhrase: String = ""
    var ePhrase: String = ""
    var freq: Int = 1
}

class LangData {
    var frenchSpeed: Float = 1.0f
    var frenchPauseFactor: Double = 1.0
    var phrasePatterns = mutableListOf<String>()
    var audioPattern: String = "fef"
    var frenchVoice: String = ""
    var englishVoice: String = ""

    val pronoun = mutableListOf<Pronoun>()
    val nouns = mutableListOf<Noun>()
    val adjectives = mutableListOf<Adjective>()
    val verbs = mutableListOf<Verb>()
    val phrases = mutableListOf<Phrase>()

}

data class FAndEPair(val fTxt: String, val eTxt: String)
data class ANoun(val pqg: PQG, val article: String, val fTxt: String, val eTxt: String)


class PQG {
    var perspective: Perspective = Perspective.Unset
    var gender: Gender = Gender.Unset
    var quantity: Quantity = Quantity.Unset
}

class AnAdjective {
    var eText: String = ""
    var fText: String = ""
    var gender: Gender = Gender.M
    var quantity: Quantity = Quantity.Single
}

class Subject {
    var eText: String = ""
    var fText: String = ""
    var pqg: PQG = PQG()
}

//
//enum class SubjectPerspective {
//    Je_Nous, sdf, WEST, EAST
//}
//
//class SubjectPart {
//    var eTxt: String = ""
//    var fTxt: String = ""
//    var perspective: SubjectPerspective = SubjectPerspective.Je_Nous
//    var quantity: SubjectQuantity = SubjectQuantity.single
//    var gender: SubjectGender = SubjectGender.m
//}


//Data ------
private var vocabList: LangData = LangData()

//Code  ------------




fun readIt(bufferedReader: BufferedReader): Int {
    var linesRead = 0
    try {
        // Use a loop to read each line until the end of the file
        var line: String?
        do {
            line = bufferedReader.readLine()
            linesRead++
            if (line == null) {
                break
            }
            line = line.trim()
            if (line.startsWith("#")) {
                continue
            } else if (line.startsWith("@")) {
                //code it
                val parts = line.split("=")
                val part0 = parts[0].drop(1).trim()
                when (part0) {
                    "frenchSpeed" -> {
                        vocabList.frenchSpeed = parts[1].toFloat()
                    }
                    "frenchPauseFactor" -> {
                        vocabList.frenchPauseFactor = parts[1].toDouble()
                    }
                    "phrasePatterns" -> {
                        vocabList.phrasePatterns.add( parts[1].trim() )
                    }
                    "audioPattern" -> {
                        vocabList.audioPattern = parts[1].trim()
                    }
                    "frenchVoice" -> {
                        vocabList.frenchVoice = parts[1].trim()
                    }
                    "englishVoice" -> {
                        vocabList.englishVoice = parts[1].trim()
                    }
                    else -> {
                        reportIt("*** Error parsing line $line")
                        //code
                    }
                }
            } else {
                val parts = line.split(";")
                val part0 = parts[0].lowercase().trim()
                when (part0) {
                    "pronoun" -> {
                        val the = Pronoun()
                        the.fText = parts[1].trim()
                        the.MFSP = parts[2].trim()
                        the.eText = parts[3].trim()
                        if (parts.size >= (4 + 1)) {
                            the.freq = parts[4].toInt()
                        }
                        vocabList.pronoun.add(the)
                    }

                    "noun" -> {
                        val the = Noun()
                        the.wordGender = stringToGender(parts[1].trim())
                        the.wordQuantity = stringToQuantity(parts[2].trim())
                        the.article = parts[3].trim()
                        val txt1: String = parts[4].trim()
                        val iStart = txt1.indexOf('(')
                        if (iStart < 0) {
                            the.sText = txt1
                            the.pText = txt1
                        } else {
                            val iEnd = txt1.indexOf(')')
                            val sForm = txt1.substring(0,iStart)
                            var pForm = txt1.substring(iStart+1,iEnd)
                            if (pForm.length <= 1) {
                                pForm = sForm + pForm
                            }
                            the.sText = sForm
                            the.pText = pForm
                        }
                        the.eText = parts[5].trim()
                        vocabList.nouns.add(the)
                    }

                    "adjective" -> {
                        val the = Adjective()
                        the.eAdj = parts[1].trim()
                        the.relPos = parts[2].trim()
                        the.masculine = parts[3].trim()
                        the.feminine = parts[4].trim()
                        the.masculinePlural = parts[5].trim()
                        the.femininePlural = parts[6].trim()
                        if (parts.size >= (7 + 1)) {
                            the.freq = parts[7].toInt()
                        }
                        vocabList.adjectives.add(the)
                    }

                    "verb" -> {
                        val the = Verb()
                        the.fInfinitive = parts[1].trim()
                        the.eBase = parts[2].trim()
                        the.e3rdPeronSingular = parts[3].trim()
                        the.Je = parts[4].trim()
                        the.Tu = parts[5].trim()
                        the.Il_Elle = parts[6].trim()
                        the.Nous = parts[7].trim()
                        the.Vous = parts[8].trim()
                        the.Ils_Elles = parts[9].trim()
                        if (parts.size >= (10 + 1)) {
                            the.freq = parts[10].toInt()
                        }
                        vocabList.verbs.add(the)
                    }

                    "phrase" -> {
                        val the = Phrase()
                        the.fPhrase = parts[1].trim()
                        the.ePhrase = parts[2].trim()
                        if (parts.size >= (3 + 1)) {
                            the.freq = parts[3].toInt()
                        }
                        vocabList.phrases.add(the)
                    }

                    else -> {
                        //code
                    }
                }
            }
        } while (true) // (line != null)
    } catch (e: Exception) {
        reportIt("*** Error reading file at about line $linesRead")
    }
    return linesRead
}

private fun getThePhrase(): FAndEPair {
    var fText1 = "Erreur!"
    var eText1 = "Error!"
    //Decide what to do next
    val pattern = vocabList.phrasePatterns.random()
    when (pattern) {
        "phrase" -> {
            val phrase = vocabList.phrases.random()
            fText1 = phrase.fPhrase
            eText1 = phrase.ePhrase
        }
        "noun" -> {
            val aNoun = getAnyNoun()
            fText1 = aNoun.fTxt
            eText1 = aNoun.eTxt
        }
        "the noun" -> {
            val aNoun = getAnyNoun()
            fText1 = if (aNoun.article.endsWith("'")) {
                aNoun.article + aNoun.fTxt
            } else {
                aNoun.article + " " + aNoun.fTxt
            }
            eText1 = "the ${aNoun.eTxt}"
        }
        "Je suis {adjective.Masculine or adjective.Feminine}" -> {
            val subjectGender: Gender = Gender.values().random()
            val subjectQuantity: Quantity = Quantity.Single
            val adj2 = getAnAdjective( subjectGender, subjectQuantity )
            fText1 = "Je suis ${adj2.fText}"
            eText1 = "I am ${adj2.eText}"
        }
        "{pronoun} are {adjective}" -> {
            val pronouns = listOf("Je", "Tu", "Il", "Elle", "Nous", "Vous", "Ils", "Elles")
            val pronoun = pronouns.random()
            when (pronoun) {
                "Je" -> {
                    val subjectGender: Gender = Gender.values().random()
                    val subjectQuantity: Quantity = Quantity.Single
                    val adj2 = getAnAdjective(subjectGender, subjectQuantity)
                    fText1 = "Je suis ${adj2.fText}"
                    eText1 = "I am ${adj2.eText}"
                }

                "Tu" -> {
                    val subjectGender: Gender = Gender.values().random()
                    val subjectQuantity: Quantity = Quantity.Single
                    val adj2 = getAnAdjective(subjectGender, subjectQuantity)
                    fText1 = "Tu es ${adj2.fText}"
                    eText1 = "You are ${adj2.eText}"
                }

                "Il" -> {
                    val subjectGender: Gender = Gender.M
                    val subjectQuantity: Quantity = Quantity.Single
                    val adj2 = getAnAdjective(subjectGender, subjectQuantity)
                    fText1 = "Il est ${adj2.fText}"
                    eText1 = "He is ${adj2.eText}"
                }

                "Elle" -> {
                    val subjectGender: Gender = Gender.F
                    val subjectQuantity: Quantity = Quantity.Single
                    val adj2 = getAnAdjective(subjectGender, subjectQuantity)
                    fText1 = "Elle est ${adj2.fText}"
                    eText1 = "She is ${adj2.eText}"
                }

                "Nous" -> {
                    val subjectGender: Gender = Gender.values().random()
                    val subjectQuantity: Quantity = Quantity.Plural
                    val adj2 = getAnAdjective(subjectGender, subjectQuantity)
                    fText1 = "Nous sommes ${adj2.fText}"
                    eText1 = "We are ${adj2.eText}"
                }

                "Vous" -> {
                    val subjectGender: Gender = Gender.values().random()
                    val subjectQuantity: Quantity = Quantity.values().random()
                    val adj2 = getAnAdjective(subjectGender, subjectQuantity)
                    fText1 = "Vous êtes ${adj2.fText}"
                    eText1 = if (subjectQuantity == Quantity.Single) {
                        "You are ${adj2.eText}"
                    } else {
                        "Y'all are ${adj2.eText}"
                    }
                }

                "Ils" -> {
                    val subjectGender: Gender = Gender.M
                    val subjectQuantity: Quantity = Quantity.Plural
                    val adj2 = getAnAdjective(subjectGender, subjectQuantity)
                    fText1 = "Ils sont ${adj2.fText}"
                    eText1 = "They are ${adj2.eText}"
                }

                "Elles" -> {
                    val subjectGender: Gender = Gender.F
                    val subjectQuantity: Quantity = Quantity.Plural
                    val adj2 = getAnAdjective(subjectGender, subjectQuantity)
                    fText1 = "Elles sont ${adj2.fText}"
                    eText1 = "They are ${adj2.eText}"
                }
            }
        }
        "{pronoun} {want/have/like/go/take} {a/the} {noun}" -> {
            //Get a subject pronoun
            //Get a verb
            //Get a or the
            //Get object (noun for now)

            //val subject = getASubject()
            //val aVerb = getAVerb( PQG )
            //val anObject = getAnObject(  )


            val pronouns = listOf("Je", "Tu", "Il", "Elle", "Nous", "Vous", "Ils", "Elles")
            val pronoun = pronouns.random()
        }
    }
    return FAndEPair(fText1, eText1)
}

private fun getAnAdjective(gender: Gender, quantity: Quantity): AnAdjective {
    val anAdjective = AnAdjective()
    val adj = vocabList.adjectives.random()
    anAdjective.gender = gender
    anAdjective.quantity = quantity
    if ( gender == Gender.M ) {
        if ( quantity == Quantity.Single) {
            anAdjective.eText = adj.eAdj
            anAdjective.fText = adj.masculine
        } else { //quantity == Quantity.plural
            anAdjective.eText = adj.eAdj
            anAdjective.fText = adj.masculinePlural
        }
    } else { //gender == Gender.f ) {
        if ( quantity == Quantity.Single) {
            anAdjective.eText = adj.eAdj
            anAdjective.fText = adj.feminine
        } else { //quantity == Quantity.plural
            anAdjective.eText = adj.eAdj
            anAdjective.fText = adj.femininePlural
        }
    }
    if ((0..4).random() >=4) {
        anAdjective.eText = "very " + anAdjective.eText
        anAdjective.fText = "très " + anAdjective.fText
    }
    return anAdjective
}

//private fun getASubject(): Subject {
//    var aSubject = Subject()
//
//    if ((0..4).random() >= 3) {
//        val noun = vocabList.nouns.random()
//        //TODO: Select if we are going to use a or the in front
//        //TODO: Select if we are going to make it plural
//        aSubject.eText = noun.eText
//        aSubject.fText = noun.fText
//        aSubject.pqg.gender = Gender.M
//        aSubject.pqg.perspective = Perspective.First //TODO: Fix  Need a function to select or get the gender from MFSP
//        aSubject.pqg.quantity = Quantity.Single //TODO: Fix  Need a function to select or get the gender from MFSP
//        aSubject.pqg.gender = Gender.M //TODO: Fix  Need a function to select or get the gender from MFSP
//    } else {
//        val pronoun = vocabList.pronoun.random()
//        aSubject.eText = pronoun.eText
//        aSubject.eText = pronoun.fText
//        aSubject.pqg.perspective = Perspective.First //TODO: Fix  Need a function to select or get the gender from MFSP
//        aSubject.pqg.quantity = Quantity.Single //TODO: Fix  Need a function to select or get the gender from MFSP
//        aSubject.pqg.gender = Gender.M //TODO: Fix  Need a function to select or get the gender from MFSP
//    }
//    return aSubject
//}


private fun getAnyNoun(): ANoun {
    //data class ANoun(val pqg: PQG, val article: String, val fTxt: String, val eTxt: String)
    val pqg: PQG = PQG()
    var article: String = ""
    var fTxt: String = "Erreur"
    var eTxt: String = "Error"
    val noun = vocabList.nouns.random()
    pqg.quantity = noun.wordQuantity
    if (pqg.quantity == Quantity.Either) {
        pqg.quantity = Quantity.Single
        if ((0..2).random() >= 2) {
            pqg.quantity = Quantity.Plural
        }
    }
    if (pqg.quantity == Quantity.Single) {
        pqg.quantity = Quantity.Single
        pqg.gender = noun.wordGender
        pqg.perspective = Perspective.Third
        article = noun.article
        fTxt = noun.sText
        eTxt = noun.eText
    } else if (pqg.quantity == Quantity.Plural) {
        pqg.quantity = Quantity.Plural
        pqg.gender = noun.wordGender
        pqg.perspective = Perspective.Third
        article = "les"
        fTxt = noun.pText
        eTxt = noun.eText + "s"
    }
    return ANoun(pqg, article, fTxt, eTxt)
}
private fun stringToGender(txt: String): Gender {
    val txt2 = txt.trim().lowercase()
    if (txt2 == "m") {
        return Gender.M
    } else if (txt2 == "f") {
        return Gender.F
    }
    return Gender.Unset
}
private fun stringToQuantity(txt: String): Quantity {
    val txt2 = txt.trim().lowercase()
    if (txt2 == "s") {
        return Quantity.Single
    } else if (txt2 == "p") {
        return Quantity.Plural
    } else if (txt2 == "s&p") {
        return Quantity.Either
    } else if (txt2 == "s(p)") {
        return Quantity.Either
    }
    return Quantity.Unset
}
