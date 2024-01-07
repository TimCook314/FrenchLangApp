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
import android.content.ContentValues

import android.os.Build
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import java.io.FileOutputStream
import java.io.OutputStream


private var theFilePathText = mutableStateOf("none")
private var fileUri = mutableStateOf<Uri?>(null)

private var theFELines = mutableListOf<String>()

private lateinit var ttsE: TextToSpeech
private lateinit var ttsF: TextToSpeech






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


    private fun someFunction() {
        val frenchText = "Bonjour"

        // Pass the context to the function
        generateAudioFile(this, frenchText)
        generateAudioFiles(this)
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
                fileUri.value = uri
                if (uri == null) {
                    //reportIt(uri.path())
                } else {
                    //val context = LocalContext.current
                    //reportIt("Got new file, name is:")
                    val itis = getUriName(this, uri)
                    //reportIt(itis)
                    theFilePathText.value = itis
                    //readVocalData(this, uri, itis)
                }
            }
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Hello $name!",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = modifier.padding(18.dp)
            )

            Button(onClick = { theFilePicker.launch("text/plain") }) {
                Text("Vocab File Picker")
            }

            Text(
                text = "Phrase file:",
                //style = MaterialTheme.typography.h6,
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = theFilePathText.value,
                //style = MaterialTheme.typography.body1,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(alignment = Alignment.End)
            )

            Button(onClick = {
                val uri = fileUri.value
                if (uri != null) {
                    //val lines = readFile(context, uri)
                    reportIt("Reading lines")
                    scope.launch {
                        val lines = readFile(myContext, uri)
                        theFELines = lines.toMutableList()
                        // Do something with the lines
                        reportIt("Read lines")
                    }
                }
            }) {
                Text("Read File")
            }



            Button(
                onClick = {
                    setupVoices(myContext)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text("Setup voices")
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


private fun generateAudioFile(context: Context, frenchText: String): String {
    var outputDir = Environment.getExternalStorageDirectory()
    reportIt(outputDir.path)

    // Obtain the output directory within external files
    outputDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
    reportIt(outputDir.path)

    // Ensure the output directory exists
    outputDir?.mkdirs()

    //val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
    val outputFilePath = File(outputDir, "bonjour.mp3")


    val frenchText = "Bonjour"
    val params = HashMap<String, String>()
    params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "utteranceId"
    //val outputFilePath = "bonjour.mp3"
    ttsF.synthesizeToFile(frenchText, null, outputFilePath, "utteranceId", )
    //ttsF.synthesizeToFile(frenchText, params, File(outputFilePath), "utteranceId", )

    var result = "none"
    reportIt("Done")

    val file = File(outputDir, "bonjour.txt")
    val text = "Hello"
    try {
        //val file = File(filePath)

        // Ensure the parent directory exists
        file.parentFile?.mkdirs()

        // Create FileWriter and BufferedWriter
        val fileWriter = FileWriter(file)
        val bufferedWriter2 = BufferedWriter(fileWriter)

        // Write the text to the file
        bufferedWriter2.write(text)

        // Close the BufferedWriter and FileWriter
        bufferedWriter2.close()
        fileWriter.close()
    } catch (e: IOException) {
        // Handle IOException, if needed
        e.printStackTrace()
    }

    //reportIt(outputDir)
    return result
}

private fun idk(): String {

    val outputDir = Environment.getExternalStorageDirectory()
    reportIt(outputDir.path)
    //val outputDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC)
    val outputFilePath = File(outputDir, "bonjour.mp3")


    val frenchText = "Bonjour"
    val params = HashMap<String, String>()
    params[TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID] = "utteranceId"
    //val outputFilePath = "bonjour.mp3"
    ttsF.synthesizeToFile(frenchText, null, outputFilePath, "utteranceId", )
    //ttsF.synthesizeToFile(frenchText, params, File(outputFilePath), "utteranceId", )

    var result = "none"
    reportIt("Done")
    //reportIt(outputDir)
    return result
}





//This reads a file and returns a list of lines
suspend fun readFile(context: Context, uri: Uri): List<String> {
    val lines = mutableListOf<String>()
    var count = 0
    context.contentResolver.openInputStream(uri)?.bufferedReader().use { reader ->
        reader?.forEachLine { line ->
            lines.add(line)
            count++
        }
    }
    reportIt("Read: $count lines")
    return lines
}

private fun setupVoices(myContext: Context) {
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
            }
        }
    }
    ttsF = tts1
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


fun generateAudioFiles(context: Context ) {
    reportIt("  Creating directory ")
    createDirectoryInDocuments( context, "testing/Bob")
    val documentsDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
//        // Create the "testing/Bob" directory
//        val dir = File(documentsDir, "testing/Bob").apply {
//            if (!exists()) {
//                mkdirs()
//            }
//        }
//    reportIt("  Directory created ")
    val dir = File(documentsDir, "/testing/Bob")

    reportIt("  Generating stuff ")
    var count: Int = 0
    theFELines.forEachIndexed { index, string ->
        val parts = string.split(";")
        if (parts.size >= 3) {
            val temp0 = parts[0].lowercase().trim()
            val temp1 = parts[1].trim()
            val temp2 = parts[2].trim()
            when (temp0) {
                "phrase" -> {
                    // Create a unique filename for each part
                    // Create a unique filename for each part
                    val filename1 = "part1_$index.wav"
                    val filename2 = "part2_$index.wav"
                    // Create an output Uri for each file
                    val uri1 = createMediaStoreUri(context, "$filename1")
                    val uri2 = createMediaStoreUri(context, "/testing/Bob/$filename2")
                    // Create a ParcelFileDescriptor for each Uri
                    val pfd1 = context.contentResolver.openFileDescriptor(uri1, "w")
                    val pfd2 = context.contentResolver.openFileDescriptor(uri2, "w")

//                    // Create a unique filename for each part
//                    val file1 = File(dir, "part1_$index.wav")
//                    val file2 = File(dir, "part2_$index.wav")
//                    // Create a ParcelFileDescriptor for each file
//                    val pfd1 = ParcelFileDescriptor.open(file1, ParcelFileDescriptor.MODE_WRITE_ONLY)
//                    val pfd2 = ParcelFileDescriptor.open(file2, ParcelFileDescriptor.MODE_WRITE_ONLY)

                    // Synthesize the speech to a file
                    ttsF.synthesizeToFile(temp1, Bundle(), pfd1!!, "ttsF_$index")
                    ttsE.synthesizeToFile(temp2, Bundle(), pfd2!!, "ttsE_$index")
                    count++
                }
                else -> {
                    //code
                }
            }


        }
    }
    reportIt("  Generated $count items ")
}

//  did have suspend  is this needed!  Yes!  Call from coetc...
fun createMediaStoreUri(context: Context, relativePath: String): Uri {
    val values = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, relativePath)
        put(MediaStore.MediaColumns.MIME_TYPE, "audio/wav")
        put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/testing/Bob/")
    }
    return context.contentResolver.insert(MediaStore.Files.getContentUri("external"), values)!!
}












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

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
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
    } else {
        //Log.e("MediaStore", "Cannot get list of files for API < 29.")
    }

    return listOfFiles
}


fun writeTextToFile(context: Context, directoryName: String, fileName: String, text: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
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
    } else {
        //Log.e("MediaStore", "Cannot write to file for API < 29.")
    }
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
