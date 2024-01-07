package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
//import com.example.featherandroidtasks.ui.theme.FeatherAndroidTasksTheme

import androidx.compose.runtime.remember


import android.content.Intent
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.Alignment




import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import java.io.File





class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        //    // Request permission
        //    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101)
        //} else {
        //    setContent {
        //        MyApp()
        //    }
        //}


        setContent {
            MyApp()
        }
    }

    //@Deprecated
    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with file operations
                setContent {
                    MyApp()
                }
            } else {
                // Permission denied, handle the denial gracefully
                // For example, you could display an informative message to the user
            }
        }
    }


}

//@Composable
//fun MyApp() {
//    Surface(color = MaterialTheme.colorScheme.background) {
//        Text(text = "Hello, Android with Jetpack Compose!")
//    }
//}

@Composable
fun MyApp() {
    var selectedFile: File? by remember { mutableStateOf(null) }
    var bobCount = 0

    val selectFileLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            selectedFile = File(uri.path!!)
        }
    }

    Surface(color = MaterialTheme.colorScheme.background) {
        Column {
            Button(onClick = { selectFileLauncher.launch(arrayOf("*/*")) }) {
                Text("Select File")
            }
            Button(onClick = {
                if (selectedFile != null) {
                    // Read file and count lines starting with "bob"
                    bobCount = 1234
                    //bobCount = countBobLines(selectedFile!!)
                }
            }) {
                Text("Count Bob Lines")
            }
            Text("Bob lines: $selectedFile")
            //Text("Bob lines: $bobCount")
        }
    }
}

private fun countBobLines(file: File): Int {
    var bobCount = 0
    file.bufferedReader().useLines { lines ->
        lines.forEach {
            if (it.startsWith("bob")) {
                bobCount++
            }
        }
    }
    return bobCount
}

//override fun onCreate(savedInstanceState: Bundle?) {
//    super.onCreate(savedInstanceState)
//
//    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//        // Request permission
//        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101)
//    } else {
//        setContent {
//            MyApp()
//        }
//    }
//}





//
//@Composable
//fun MyApp() {
//    var selectedFileUri by remember { mutableStateOf<String?>(null) }
//
//    val resultCallback = remember { mutableStateOf<(String?) -> Unit> { { } } }
//
//    val fileLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent(),
//        callback = resultCallback.value
//    )     resultCallback.value = { uri ->
//        selectedFileUri = uri
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.Start
//    ) {
//        // Static label "File:"
//        Text(
//            text = "File:",
//            modifier = Modifier.padding(bottom = 4.dp)
//        )
//
//        // Button to select a file
//        Button(
//            onClick = { fileLauncher.launch("text/plain") },
//            modifier = Modifier
//                .padding(bottom = 16.dp)
//                .height(50.dp),
//            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
//        ) {
//            Text("Select File")
//        }
//
//        // Display selected file
//        Text(
//            text = selectedFileUri ?: "No file selected",
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//    }
//}
//
//@Composable
//fun rememberLauncherForActivityResult(
//    contract: ActivityResultContracts.GetContent,
//    callback: (String?) -> Unit
//): ActivityResultLauncher<String> {
//    val resultCallback = remember { mutableStateOf(callback) }
//
//    return rememberLauncherForActivityResult(contract) { uri ->
//        resultCallback.value(uri)
//    }
//}

//@Preview(showBackground = true)
//@Composable
//fun MyAppPreview() {
//    MyApp()
//}
//
//




//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            MyApplicationTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    //Greeting("Android")
//                    MyApp()
//                }
//            }
//        }
//    }
//}
//
////@Composable
////fun Greeting(name: String, modifier: Modifier = Modifier) {
////    Text(
////        text = "Hello $name!",
////        modifier = modifier
////    )
////}
//
//
//
//@Composable
//fun MyApp() {
//
//    var selectedFileUri by remember { mutableStateOf<String?>(null) }
//
//    val fileLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: String? ->
//        selectedFileUri = uri
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.Start
//    ) {
//        // Static label "File:"
//        Text(
//            text = "File:",
//            modifier = Modifier.padding(bottom = 4.dp)
//        )
//
//        // Button to select a file
//        Button(
//            onClick = { fileLauncher.launch("text/plain") },
//            modifier = Modifier
//                .padding(bottom = 16.dp)
//                .height(50.dp),
//            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(8.dp)
//            ) {
//                Icon(Icons.Filled.Upload, contentDescription = null)
//                Text("Select File")
//            }
//        }
//
//        // Display selected file
//        Text(
//            text = selectedFileUri ?: "No file selected",
//            modifier = Modifier.padding(bottom = 16.dp)
//        )
//    }
//}
//
////Column(
////        modifier = Modifier
////            .fillMaxSize()
////            .padding(16.dp),
////        verticalArrangement = Arrangement.Center,
////        horizontalAlignment = Alignment.Start
////    ) {
////        // Static label "File:"
////        Text(
////            text = "File:",
////            modifier = Modifier.padding(bottom = 4.dp)
////        )
////    }
////}
//
//
//@Composable
//fun rememberLauncherForActivityResult(
//    contract: ActivityResultContracts.GetContent,
//    callback: (String?) -> Unit
//): ActivityResultLauncher<String> {
//    val resultCallback = rememberUpdatedState(callback)
//
//    return rememberLauncherForActivityResult(contract) { uri ->
//        resultCallback(uri?.toString())
//    }
//}
//
//
//@Preview(showBackground = true)
//@Composable
//fun MyAppPreview() {
//    MyApplicationTheme {
//        MyApp()
//    }
//}
//


//
//@Composable
//fun MyApp() {
//    Scaffold(
//        content = {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.Start
//            ) {
//                // Static label "File:"
//                Text(
//                    text = "File:",
//                    modifier = Modifier.padding(bottom = 4.dp)
//                )
//            }
//        }
//    )
//}


//@Composable
//fun MyApp() {
//    // Your UI components go here
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Jetpack Compose App") },
//                backgroundColor = MaterialTheme.colorScheme.primary
//            )
//        },
//        content = {
//            // Content of your app
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp),
//                verticalArrangement = Arrangement.Center,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                Text("Hello, Jetpack Compose!")
//            }
//        }
//    )
//}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    MyApplicationTheme {
//        Greeting("Android")
//    }
//}



// Try 1 ------------------------------
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun MyApp() {
//    var selectedFileUri by remember { mutableStateOf<String?>(null) }
//
//    val fileLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: String? ->
//        selectedFileUri = uri
//    }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Jetpack Compose App") },
//                backgroundColor = MaterialTheme.colorScheme.primary
//            )
//        },
//        content = {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp),
//                verticalArrangement = Arrangement.Top,
//                horizontalAlignment = Alignment.Start
//            ) {
//                // Static label "File:"
//                Text(
//                    text = "File:",
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier.padding(bottom = 4.dp)
//                )
//
//                // Display selected file
//                Text(
//                    text = selectedFileUri ?: "No file selected",
//                    modifier = Modifier.padding(bottom = 16.dp)
//                )
//
//                // Button to select a text file
//                Button(
//                    onClick = { fileLauncher.launch("text/plain") },
//                    modifier = Modifier
//                        .padding(bottom = 16.dp)
//                        .height(50.dp),
//                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondary)
//                ) {
//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(8.dp)
//                    ) {
//                        Icon(Icons.Filled.TextSnippet, contentDescription = null)
//                        Text("Select Text File")
//                    }
//                }
//            }
//        }
//    )
//}
//
//@Composable
//fun rememberLauncherForActivityResult(
//    contract: ActivityResultContracts.GetContent,
//    callback: (String?) -> Unit
//): ActivityResultLauncher<String> {
//    val context = LocalContext.current
//    val resultCallback = rememberUpdatedState(callback)
//
//    return rememberLauncherForActivityResult(contract) { uri ->
//        resultCallback(uri?.toString())
//    }
//}
