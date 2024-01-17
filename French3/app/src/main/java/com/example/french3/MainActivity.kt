package com.example.french3

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.french3.ui.theme.French3Theme
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.InputStreamReader
import java.util.Locale

import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Files.createDirectory
import java.nio.file.Path
import java.nio.file.Paths

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues

import android.os.Build
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.speech.tts.UtteranceProgressListener
import androidx.compose.foundation.layout.height
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream


//private var isSynthesizeAudioButtonEnabled by remember { mutableStateOf(false) }
private var isSynthesizeAudioButtonEnabled = mutableStateOf(false)

private var phrasesFilePathText = mutableStateOf("none")
private var phrasesFileUri = mutableStateOf<Uri?>(null)

private var theFELines = mutableListOf<String>()

private var ttsE_setup = false
private var ttsF_setup = false
private lateinit var ttsE: TextToSpeech
private var ttsE_hasStarted = false
private var ttsE_hasCompleted = false
private lateinit var ttsF: TextToSpeech
private var ttsF_hasStarted = false
private var ttsF_hasCompleted = false



//TODO: jetpack compose number picker or slider


private var asText = mutableStateListOf("App started Version: 9:28 10/21")
private fun reportIt(txt: String) {
    asText.add(txt)
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            //var fileName by remember { mutableStateOf("") }

            French3Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Tim (me) Cook")
                }
            }
        }
    }

    private fun setupVoicesWrapped(context: Context){
        //val bob = this
        CoroutineScope(Dispatchers.Main).launch {
            setupVoices(context)
        }
    }
    private fun generateAudioFilesWrapper(){
        val bob = this
        CoroutineScope(Dispatchers.Main).launch {
            generateAudioFiles(bob)
            //delay(1234)
        }
        //generateAudioFiles(this)
    }

    private fun someFunction() {
        //deleteFolder(this, "Sdf")
        deleteFolder(this, "French3Audio/idk/my")
        //val frenchText = "Bonjour"
        // Pass the context to the function
        //generateAudioFile(this, frenchText)
        //generateAudioFiles(this)
    }

    private fun someFunction2() {
        val frenchText = "FrenchAudioFiles3/jim"

        // Pass the context to the function
        //listFiles(this, frenchText)
        createDirectoryInDocuments(this, frenchText)
        writeTextToFile(this, "FrenchAudioFiles2", "bon2.txt", "Hello!")
        val listOfFiles = getListOfFiles(this, "FrenchAudioFiles2")
        echoFilesList(listOfFiles)
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {


        val scope = rememberCoroutineScope()
        val myContext = LocalContext.current

        val theFilePicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent(),
            onResult = { uri ->
                phrasesFileUri.value = uri
                if (uri == null) {
                    phrasesFilePathText.value = "none"
                    isSynthesizeAudioButtonEnabled.value = false
                    //reportIt(uri.path())
                } else {
                    //val context = LocalContext.current
                    reportIt("Got new file, name is:")
                    val fileName = getUriName(this, uri)
                    phrasesFilePathText.value = fileName
                    if (ttsE_setup && ttsF_setup) {
                        isSynthesizeAudioButtonEnabled.value = true
                    }
                    //readVocalData(this, uri, fileName)
                }
            }
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            //horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Hello $name",
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = modifier.padding(12.dp)
            )

            Divider(color = Color.LightGray, thickness = 1.dp)

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                //.height(200.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        setupVoicesWrapped(myContext)
                        //setupVoices(myContext)
                    },
                    modifier = Modifier.padding(8.dp)
                    //.fillMaxWidth()
                ) {
                    Text("Setup voices")
                }

                Button(
                    onClick = {
                        theFilePicker.launch("text/plain")
                    },
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Select phrase file")
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                //.height(60.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Phrase file:",
                    //style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .padding(bottom = 8.dp)
                )

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
            }

            Button(
                onClick = {
                    generateAudioFilesWrapper()




//                    reportIt("calling .....")
//                    scope.launch {
//                        generateAudioFilesWrapper()
////                        val lines = readFile(myContext, uri)
////                        theFELines = lines.toMutableList()
////                        // Do something with the lines
////                        reportIt("done ! with call")
//                    }
//                    reportIt("After call")



                },
                enabled = isSynthesizeAudioButtonEnabled.value,
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth()
            ) {
                Text("Generate Audio Files")
            }




            Divider(color = Color.LightGray, thickness = 1.dp)




            Button(onClick = {
                val uri = phrasesFileUri.value
                if (uri != null) {
                    //val lines = readFile(context, uri)
                    reportIt("Reading lines")
//                    scope.launch {
//                        val lines = readFile(myContext, uri)
//                        theFELines = lines.toMutableList()
//                        // Do something with the lines
//                        reportIt("Read lines")
//                    }
                }
            }) {
                Text("Read File")
            }








            Button(
                onClick = {
                    //setupVoices()
                    //idk()
                    someFunction()
                    //generateAudioFile(this, frenchText)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text("Generate Audio File")
            }



            Button(
                onClick = {
                    someFunction2()
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text("List Files")
            }


            Divider(color = Color.Blue, thickness = 1.dp)
            TextList()
        }
    }











}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    French3Theme {
//        Greeting("Android")
//    }
//}

//
//private fun generateAudioFile(context: Context, frenchText: String): String {
//    var outputDir = Environment.getExternalStorageDirectory()
//    reportIt(outputDir.path)
//
//    // Obtain the output directory within external files
//    outputDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
//    reportIt(outputDir.path)
//
//    // Ensure the output directory exists
//    outputDir?.mkdirs()
//
//    //val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
//    val outputFilePath = File(outputDir, "bonjour.mp3")
//
//
//    val frenchText = "Bonjour"
//    val params = HashMap<String, String>()
//    params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "utteranceId"
//    //val outputFilePath = "bonjour.mp3"
//    ttsF.synthesizeToFile(frenchText, null, outputFilePath, "utteranceId", )
//    //ttsF.synthesizeToFile(frenchText, params, File(outputFilePath), "utteranceId", )
//
//    var result = "none"
//    reportIt("Done")
//
//    val file = File(outputDir, "bonjour.txt")
//    val text = "Hello"
//    try {
//        //val file = File(filePath)
//
//        // Ensure the parent directory exists
//        file.parentFile?.mkdirs()
//
//        // Create FileWriter and BufferedWriter
//        val fileWriter = FileWriter(file)
//        val bufferedWriter2 = BufferedWriter(fileWriter)
//
//        // Write the text to the file
//        bufferedWriter2.write(text)
//
//        // Close the BufferedWriter and FileWriter
//        bufferedWriter2.close()
//        fileWriter.close()
//    } catch (e: IOException) {
//        // Handle IOException, if needed
//        e.printStackTrace()
//    }
//
//    //reportIt(outputDir)
//    return result
//}
//
//private fun idk(): String {
//
//    val outputDir = Environment.getExternalStorageDirectory()
//    reportIt(outputDir.path)
//    //val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
//    val outputFilePath = File(outputDir, "bonjour.mp3")
//
//
//    val frenchText = "Bonjour"
//    val params = HashMap<String, String>()
//    params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "utteranceId"
//    //val outputFilePath = "bonjour.mp3"
//    ttsF.synthesizeToFile(frenchText, null, outputFilePath, "utteranceId", )
//    //ttsF.synthesizeToFile(frenchText, params, File(outputFilePath), "utteranceId", )
//
//    var result = "none"
//    reportIt("Done")
//    //reportIt(outputDir)
//    return result
//}
//

////This reads a file and returns a list of lines
//suspend fun readFile(context: Context, uri: Uri): List<String> {
//    val lines = mutableListOf<String>()
//    var count = 0
//    context.contentResolver.openInputStream(uri)?.bufferedReader().use { reader ->
//        reader?.forEachLine { line ->
//            lines.add(line)
//            count++
//        }
//    }
//    reportIt("Read: $count lines")
//    return lines
//}




private suspend fun setupVoices(myContext: Context) {
    reportIt("Setting up voices")
    ttsE = TextToSpeech(myContext) { status ->
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
                reportIt("  Using English voice: $txt")
                val eVoice = Voice(
                    txt,
                    english,
                    Voice.QUALITY_VERY_HIGH,
                    Voice.LATENCY_NORMAL,
                    false,
                    emptySet()
                )
                ttsE.voice = eVoice
                ttsE.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
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
                ttsE_setup = true
            }
        }
    }
    var tts1 = TextToSpeech(myContext) {
        //if (it == TextToSpeech.SUCCESS) { /*tts1.language = Locale.FRENCH */
        //}
    }
    tts1 = TextToSpeech(myContext) { status ->
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
                reportIt("  Using French voice: $txt")
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
                tts1.setSpeechRate(0.9f) // 0.9 slight slower
                //tts1.setSpeechRate(vocabList.frenchSpeed) // Faster speech
                //ttsE.setPitch(0.8f) // Lower pitch
                tts1.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
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
            }
        }
    }
    ttsF = tts1
    ttsF_setup = true
    delay(1000)
    ttsE.speak("I am the english voice", TextToSpeech.QUEUE_FLUSH, null, "")
    do {
        delay(100)
    } while (!ttsE_hasStarted)
    //speaking
    do {
        delay(100)
    } while (!ttsE_hasCompleted)
    ttsE_hasStarted = false
    ttsE_hasCompleted = false
    delay(1000)
    ttsF.speak("je suis la voix française", TextToSpeech.QUEUE_FLUSH, null, "")
    do {
        delay(100)
    } while (!ttsF_hasStarted)
    //speaking
    do {
        delay(100)
    } while (!ttsF_hasCompleted)
    ttsF_hasStarted = false
    ttsF_hasCompleted = false
    reportIt("  Voice setup complete.")
}



//fun createAudio(lines: List<String>): Int {
//    var count: Int = 0
//    for (line in lines) {
//
//        reportIt(line)
//        if (fileName.startsWith("bob", ignoreCase = true)) {
//            count++
//        }
//    }
//    return count
//}


fun deleteFilesInFolder(context: Context, folderPath: String) {
    val resolver = context.contentResolver
    val selectionArgs = arrayOf("%$folderPath%")
    val cursor = resolver.query(
        MediaStore.Files.getContentUri("external"),
        arrayOf(MediaStore.Files.FileColumns._ID),
        "${MediaStore.Files.FileColumns.DATA} LIKE ?",
        selectionArgs,
        null
    )

    cursor?.use {
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val uri = MediaStore.Files.getContentUri("external", id)
            resolver.delete(uri, null, null)
        }
    }
//    val folder = File(folderPath)
//    if (folder.exists()) {
//        reportIt("Folder exists: $folderPath ")
//        reportIt("  Deleting contents")
//        val resolver: ContentResolver = context.contentResolver
//        val uri: Uri = MediaStore.Files.getContentUri("external")
//        val selection = MediaStore.Files.FileColumns.DATA + " LIKE ?"
//        val selectionArgs = arrayOf("$folderPath/%")
//        // Query the MediaStore to find the files in the folder
//        val cursor = resolver.query(uri, null, selection, selectionArgs, null)
//        cursor?.use {
//            while (it.moveToNext()) {
//                // Get the URI of the file
//                val colIdx = it.getColumnIndex(MediaStore.Files.FileColumns._ID)
//                if (colIdx >= 0) {
//                    val fileUri = ContentUris.withAppendedId(uri, it.getLong(colIdx))
//                    // Delete the file
//                    resolver.delete(fileUri, null, null)
//                }
//            }
//            reportIt("  Done deleting.")
//        }
//    }
}


fun deleteFolder(context: Context, folderName: String) {
    reportIt("Try to delete")
    val documentsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
    //val documentsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    val folder = File(documentsDir, folderName)
    reportIt("Try to ${folder.name}")
    reportIt("Try to ${folder.path}")

    if (folder.exists() && folder.isDirectory) {
        reportIt("Try to delete2")
        folder.deleteRecursively()
    }
}


suspend fun generateAudioFiles(context: Context ) {
    val fvName = ttsF.voice.name
    reportIt("  Using: " + fvName.substring(8, 11) )
    // Get name of phrase file
    val phraseFileName = phrasesFilePathText.value
    //Remove the extension
    val folderName = "French3Audio/" + File(phraseFileName).nameWithoutExtension + "_" + fvName.substring(8, 11)

    //Delete folder if there
    deleteFolder(context, folderName)

    // Create the folder in Documents
    reportIt("  Creating directory: $folderName")
    createDirectoryInDocuments(context, folderName)

    val lines = mutableListOf<String>()
    var count = 0
    val uri = phrasesFileUri.value
    if (uri != null) {
        context.contentResolver.openInputStream(uri)?.bufferedReader().use { reader ->
            reader?.forEachLine { line ->
                lines.add(line)
                count++
            }
        }
    }
    reportIt("Read: $count lines")


    deleteFolder(context, "French3Audio/idk/my")
    //This does not work!!!
//    val uriTemp = createMediaStoreUri(context, folderName, "test.txt")
//    val aPath = uriTemp.path
//    reportIt("aPath: $aPath ")
//    if (aPath != null) {
//        val file = File(aPath)
//        val folderPath = file.parent
//        reportIt("folderPath: $folderPath ")
//        if (folderPath != null) {
//            reportIt("folderPath: $folderPath ")
//            deleteFilesInFolder(context, folderPath)
//        }
//    }

    //return

    //Open the phrase file
    //val uri = phrasesFileUri.value
    reportIt("  Generating stuff ")
    lines.forEachIndexed { index, line ->
        val parts = line.split(";")
        if (parts.size >= 3) {
            val temp0 = parts[0].lowercase().trim()
            val temp1 = parts[1].trim()
            val temp2 = parts[2].trim()
            when (temp0) {
                "phrase" -> {
                    // Create a unique filename for each part
                    val filename1 = "item_$index" + "_fr.wav"
                    val filename2 = "item_$index" + "_en.wav"
                    val filename3 = "item_$index.wav"
                    // Create an output Uri for each file
                    val uri1 = createMediaStoreUri(context, folderName, filename1)
                    val uri2 = createMediaStoreUri(context, folderName, filename2)
                    val uri3 = createMediaStoreUri(context, folderName, filename3)
                    // Create a ParcelFileDescriptor for each Uri
                    val pfd1 = context.contentResolver.openFileDescriptor(uri1, "w")
                    val pfd2 = context.contentResolver.openFileDescriptor(uri2, "w")
                    // Synthesize the speech to a file
                    ttsF.synthesizeToFile(temp1, Bundle(), pfd1!!, "ttsF_$index")
                    ttsE.synthesizeToFile(temp2, Bundle(), pfd2!!, "ttsE_$index")

                    do {
                        delay(100)
                    } while (!ttsE_hasStarted)
                    //speaking
                    do {
                        delay(100)
                    } while (!ttsE_hasCompleted)
                    ttsE_hasStarted = false
                    ttsE_hasCompleted = false
                    do {
                        delay(100)
                    } while (!ttsF_hasStarted)
                    //speaking
                    do {
                        delay(100)
                    } while (!ttsF_hasCompleted)
                    ttsF_hasStarted = false
                    ttsF_hasCompleted = false
                    reportIt("  Another!.")
                    delay(100)




                    val pathE = uri1.path
                    val pathF = uri2.path
                    val path3 = uri3.path
                    if (pathE != null) {
                        if (pathF != null) {
                            if (path3 != null) {
                                joinWavFiles(pathE, pathF, path3, 3)
                            }
                        }
                    }

                }
                else -> {
                    //code
                }
            }
        }
        reportIt("  Generated $index items")
    }
}


//import java.io.*

fun joinWavFiles(file1Path: String, file2Path: String, outputPath: String, silenceDuration: Int) {
    val file1 = File(file1Path)
    val file2 = File(file2Path)
    val output = File(outputPath)

    val silenceAfterFrench = ByteArray(silenceDuration * 44100 * 2) // 44100 samples/second * 2 bytes/sample
    val silenceAfterEnglish = ByteArray(1 * 44100 * 2) // 44100 samples/second * 2 bytes/sample

    val inputStream1 = FileInputStream(file1)
    val inputStream2 = FileInputStream(file2)
    val outputStream = FileOutputStream(output)

    inputStream1.copyTo(outputStream)
    outputStream.write(silenceAfterFrench)
    inputStream2.copyTo(outputStream)
    outputStream.write(silenceAfterEnglish)
    inputStream1.copyTo(outputStream)
    outputStream.write(silenceAfterFrench)

    inputStream1.close()
    inputStream2.close()
    outputStream.close()
}



//    reportIt("  Creating directory ")
//    createDirectoryInDocuments( context, "testing/Bob")
//    val documentsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
////        // Create the "testing/Bob" directory
////        val dir = File(documentsDir, "testing/Bob").apply {
////            if (!exists()) {
////                mkdirs()
////            }
////        }
////    reportIt("  Directory created ")
//    val dir = File(documentsDir, "/testing/Bob")


//
//    reportIt("  Generating stuff ")
//    var count: Int = 0
//    theFELines.forEachIndexed { index, string ->
//        val parts = string.split(";")
//        if (parts.size >= 3) {
//            val temp0 = parts[0].lowercase().trim()
//            val temp1 = parts[1].trim()
//            val temp2 = parts[2].trim()
//            when (temp0) {
//                "phrase" -> {
//                    // Create a unique filename for each part
//                    // Create a unique filename for each part
//                    val filename1 = "part1_$index.wav"
//                    val filename2 = "part2_$index.wav"
//                    // Create an output Uri for each file
//                    val uri1 = createMediaStoreUri(context, "$filename1")
//                    val uri2 = createMediaStoreUri(context, "/testing/Bob/$filename2")
//                    // Create a ParcelFileDescriptor for each Uri
//                    val pfd1 = context.contentResolver.openFileDescriptor(uri1, "w")
//                    val pfd2 = context.contentResolver.openFileDescriptor(uri2, "w")
//
////                    // Create a unique filename for each part
////                    val file1 = File(dir, "part1_$index.wav")
////                    val file2 = File(dir, "part2_$index.wav")
////                    // Create a ParcelFileDescriptor for each file
////                    val pfd1 = ParcelFileDescriptor.open(file1, ParcelFileDescriptor.MODE_WRITE_ONLY)
////                    val pfd2 = ParcelFileDescriptor.open(file2, ParcelFileDescriptor.MODE_WRITE_ONLY)
//
//                    // Synthesize the speech to a file
//                    ttsF.synthesizeToFile(temp1, Bundle(), pfd1!!, "ttsF_$index")
//                    ttsE.synthesizeToFile(temp2, Bundle(), pfd2!!, "ttsE_$index")
//                    count++
//                }
//                else -> {
//                    //code
//                }
//            }
//
//
//        }
//    }
//    reportIt("  Generated $count items ")
//}

//  did have suspend  is this needed!  Yes!  Call from ...
fun createMediaStoreUri(context: Context, folder: String, displayName: String): Uri {
    val values = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        put(MediaStore.MediaColumns.MIME_TYPE, "audio/wav")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/"+ folder)
    }
    return context.contentResolver.insert(MediaStore.Files.getContentUri("external"), values)!!
}

//fun createMediaStoreUri(context: Context, folder: String, displayName: String): Uri {
//    val values = ContentValues().apply {
//        put(MediaStore.MediaColumns.DISPLAY_NAME, relativePath)
//        put(MediaStore.MediaColumns.MIME_TYPE, "audio/wav")
//        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/French3/")
//        //put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS + "/French3/")
//    }
//    return context.contentResolver.insert(MediaStore.Files.getContentUri("external"), values)!!
//}











private fun listFiles(context: Context, dir: String): List<String> {
    val directory2: Path = Paths.get("/storage/emulated/0/FrenchAudioFiles")
    if (!Files.exists(directory2)) {
        Files.createDirectories(directory2)
    }


    createDirectory(Paths.get("/storage/emulated/0/FrenchAudioFiles2"))
    val directory = File(dir)
    val files = directory.listFiles()
    val fileNames = mutableListOf<String>()

    if (files != null) {
        for (file in files) {
            if (file.isFile) {
                fileNames.add(file.name)
            }
        }
    }

    return fileNames
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
        reportIt("  Failed to created directory: $directoryName in Documents")
        //Log.e("MediaStore", "Failed to create directory: $directoryName in Documents.")
    } else {
        reportIt("  Created directory: $directoryName in Documents")
        //Log.d("MediaStore", "Successfully created directory: $directoryName in Documents.")
    }
}






fun getListOfFiles(context: Context, directoryName: String): List<String> {
    val listOfFiles = mutableListOf<String>()

    //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val resolver = context.contentResolver
        val collection: Uri = MediaStore.Files.getContentUri("external")
        val projection = arrayOf(MediaStore.Files.FileColumns.DISPLAY_NAME)
        val selection = "${MediaStore.Files.FileColumns.RELATIVE_PATH} LIKE ?"
        val selectionArgs = arrayOf("%${Environment.DIRECTORY_DOCUMENTS}/$directoryName%")
        val sortOrder = "${MediaStore.Files.FileColumns.DISPLAY_NAME} ASC"

        resolver.query(collection, projection, selection, selectionArgs, sortOrder)?.use { cursor ->
            while (cursor.moveToNext()) {
                val displayNameColumnIndex: Int = cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)
                val displayName: String = cursor.getString(displayNameColumnIndex)
                listOfFiles.add(displayName)
            }
        }
    //} else {
    //    //Log.e("MediaStore", "Cannot get list of files for API < 29.")
    //}

    return listOfFiles
}


fun writeTextToFile(context: Context, directoryName: String, fileName: String, text: String) {
    //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val resolver: ContentResolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "${Environment.DIRECTORY_DOCUMENTS}/$directoryName")
        }

        val uri: Uri? = resolver.insert(MediaStore.Files.getContentUri("external"), contentValues)
        if (uri == null) {
            //Log.e("MediaStore", "Failed to create file: $fileName in $directoryName.")
        } else {
            resolver.openOutputStream(uri)?.use { outputStream: OutputStream ->
                outputStream.write(text.toByteArray())
            }
            //Log.d("MediaStore", "Successfully wrote to file: $fileName in $directoryName.")
        }
    //} else {
    //    //Log.e("MediaStore", "Cannot write to file for API < 29.")
    //}
}



fun echoFilesList(listOfFiles: List<String>): Int {
    var count = 0

    for (fileName in listOfFiles) {
        reportIt(fileName)
        if (fileName.startsWith("bob", ignoreCase = true)) {
            count++
        }
    }

    return count
}




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
    return result
}
private fun getUriName2(context: Context, uri: Uri): String {
    //if (result != "none") {
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
    //}
    return "bob"
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


//
//import android.speech.tts.UtteranceProgressListener
//import java.util.*
//
//// Declare Text-to-Speech outside the Composable function
//val textToSpeech = TextToSpeech(ContextCompat.getSystemService(ContextCompat.getApplicationContext(), TextToSpeech::class.java)!!)
//
//fun generateAudioFile(context: Context, frenchText: String) {
//    val outputFilePath = "path/to/your/output/file.mp3" // Set the desired output file path
//
//    // Configure TTS
//    val params = HashMap<String, String>()
//    params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "utteranceId"
//    textToSpeech.synthesizeToFile(
//        frenchText,
//        params,
//        File(outputFilePath),
//        "utteranceId"
//    )
//
//    // Optional: Set up a UtteranceProgressListener to be notified when synthesis is complete
//    textToSpeech.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
//        override fun onDone(utteranceId: String?) {
//            // Handle completion, if needed
//        }
//
//        override fun onError(utteranceId: String?) {
//            // Handle error, if needed
//        }
//
//        override fun onStart(utteranceId: String?) {
//            // Handle start, if needed
//        }
//    })
//}
