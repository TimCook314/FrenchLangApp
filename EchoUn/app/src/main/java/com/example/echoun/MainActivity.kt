package com.example.echoun

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
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
import androidx.compose.ui.tooling.preview.Preview
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
import java.io.InputStream
import java.io.InputStreamReader


private var theFilePathText = mutableStateOf("none")
private var thePlayButtonText = mutableStateOf("Start Playing")
private var asText = mutableStateListOf<String>("App started")
private var itemsPlayed = 0
private var isPlaying = false
private var reqStop = false

private var fileUri = mutableStateOf<Uri?>(null)

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
                    asText.add("Got file!")
                    asText.add(uri.toString())
                    val itis = getUriName(this, uri)
                    asText.add("Name is:")
                    asText.add(itis)
                    theFilePathText.value = itis
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
                toggleSpeaking()
            }){
                Text(thePlayButtonText.value)
            }
            Divider(color = Color.Blue, thickness = 1.dp)
            TextList()
        }
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

//@Composable
private fun openFilePicker() {
    //theFilePathText.value = "Bushed!"
    asText.add("Pushed!!!")

    //val activity = context as Activity

    // Requesting Permission to access External Storage
    // Requesting Permission to access External Storage
    //ActivityCompat.requestPermissions(
    //    activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 23
    //)

    // getExternalStoragePublicDirectory() represents root of external storage, we are using DOWNLOADS
    // We can use following directories: MUSIC, PODCASTS, ALARMS, RINGTONES, NOTIFICATIONS, PICTURES, MOVIES
    val folder: File =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

    asText.add("folder.name")
    asText.add(folder.name)

    // Storing the data in file with name as geeksData.txt
    val file1 = File(folder, "geeksData.txt")
    //writeTextData(file, message.value, context)
    //message.value = ""
    // displaying a toast message
    //Toast.makeText(context, "Data saved publicly..", Toast.LENGTH_SHORT).show()

    /*
    var data = "It was here!!!"
    var fileOutputStream: FileOutputStream? = null
    try {
        fileOutputStream = FileOutputStream(file1)
        fileOutputStream.write(data.toByteArray())
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        if (fileOutputStream != null) {
            try {
                fileOutputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
     */




    // Accessing the saved data from the downloads folder
    val folder2 =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

    // geeksData represent the file data that is saved publicly
    val file2 = File(folder2, "geeksData.txt")
    val data2: String = getdata(file2)
    if (data2 != null && data2 != "") {
        asText.add(data2)
        //txtMsg.value = data
    } else {
        asText.add("No Data Found")
        //txtMsg.value = "No Data Found"
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


private fun selFile() {


}

private fun getdata(myfile: File): String {
    // on below line creating a variable for file input stream.
    var fileInputStream: FileInputStream? = null
    // on below line reading data from file and returning it.
    try {
        fileInputStream = FileInputStream(myfile)
        var i = -1
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
//
//public fun idk(uri: Uri) {
////Uri uri = data.getData();
//
//    val bob = getResourceAsStream(uri).bufferedReader().readLines()
//    try {
//        InputStream in = getContentResolver().openInputStream(uri);
//
//
//        BufferedReader r = new BufferedReader(new InputStreamReader ( in));
//        StringBuilder total = new StringBuilder();
//        for (String line; (line = r.readLine()) != null; ) {
//            total.append(line).append('\n');
//        }
//
//        String content = total . toString ();
//
//
//    } catch (Exception e) {
//
//    }
//}

private fun getUriName(context: Context, uri: Uri): String {
    var result = "none"
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                asText.add("nameIndex" + nameIndex.toString())
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
            val bufferedReader = BufferedReader(InputStreamReader(context.contentResolver.openInputStream(uri)))

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
            asText.add("lines with # is " + count.toString())

            // Display the count in a Text composable or do something else with it
        //    Text(text = "The number of lines that start with # is $count")

        //} catch (e: Exception) {
            // Handle any exceptions, for example by displaying an error message
        //    Text(text = "An error occurred: ${e.message}")
        //}
    }


    return result
}

//    val txt = uri.toString()
//    if (txt.startsWith("file:")) {
//        return "1: " + File (uri.path).name
//    } else if (txt.startsWith("content:")) {
//        var result: String = "null"
//        if (uri.scheme == "content") {
//            val cursor = context.contentResolver.query (uri, null, null, null, null)
//            cursor?.use {
//                val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
//                result = "2: " + cursor.getString(nameIndex)
////                if (it.moveToFirst ()) {
////                    result = it.getString (it.getColumnIndex (OpenableColumns.DISPLAY_NAME))
////                }
//            }
//        }
//        if (result == null) {
//            //result = uri.path
//            val idk = uri.path
//            if (idk == null) {
//
//            } else {
//                result = idk
//                val cut = result.lastIndexOf ('/')
//                if (cut != -1) {
//                    result = result.substring (cut + 1)
//                }
//
//            }
//        }
//        return "3: " + result
//    }
////    var result: String? = null
////    if (DocumentsContract.isDocumentUri (this, uri)) {
////        val documentId = DocumentsContract.getDocumentId (uri)
////        val splits = documentId.split (":")
////        if (splits.size == 2) {
////            val id = splits [1]
////            val column = arrayOf (MediaStore.Images.Media.DATA)
////            val sel = MediaStore.Images.Media._ID + "=?"
////            val cursor = contentResolver.query (
////                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
////                column, sel, arrayOf (id), null
////            )
////            cursor?.use {
////                if (it.moveToFirst ()) {
////                    result = it.getString (it.getColumnIndexOrThrow (column [0]))
////                }
////            }
////        }
////    } else {
////        result = uri.path
////        val cut = result.lastIndexOf ('/')
////        if (cut != -1) {
////            result = result.substring (cut + 1)
////        }
////    }
////    return result
//    return "4: idk"
//}