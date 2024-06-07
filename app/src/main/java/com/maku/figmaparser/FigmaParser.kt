package com.maku.figmaparser

import android.content.res.AssetManager
import okio.BufferedSource
import okio.Path
import okio.buffer
import okio.source

class FigmaParser(private val assetManager: AssetManager) {

//    fun parseFigmaFile(fileName: String): ParsedFigmaData {
//        val fileSystem = FileSystem.builder().build()
//        val figFile = fileSystem.source(Path.get(fileName))
//        val figFileContent = figFile.buffer()
//
//        // Parse the binary data from figFileContent
//        // Here you can call your custom parsing function to extract data
//
//        // For example, let's assume you have a function called `parseData`
//        val parsedData = parseData(figFileContent)
//
//        return parsedData
//    }
//
//    private fun parseData(figFileContent: BufferedSource): ParsedFigmaData {
//        // Implement your custom parsing logic here
//        // Extract data from the binary data and return it as a ParsedFigmaData object
//
//        // For demonstration purposes, let's return a dummy object
//        return ParsedFigmaData(
//            frames = listOf(
//                Frame(
//                    name = "Frame 1",
//                    position = Position(x = 0.0, y = 0.0),
//                    size = Size(width = 100.0, height = 100.0),
//                    nodes = listOf(
//                        Node(
//                            type = "RECTANGLE",
//                            properties = Properties(
//                                position = Position(x = 0.0, y = 0.0),
//                                size = Size(width = 100.0, height = 100.0),
//                                color = "#FF0000"
//                            )
//                        )
//                    )
//                )
//            )
//        )
//    }
}

//data class ParsedFigmaData(
//    val frames: List<Frame>,
//)
//
//data class Frame(
//    val name: String,
//    val position: Position,
//    val size: Size,
//    val nodes: List<Node>,
//)
//
//data class Node(
//    val type: String,
//    val properties: Properties,
//)
//
//data class Properties(
//    val position: Position,
//    val size: Size,
//    val color: String,
//)
//
//data class Position(
//    val x: Double,
//    val y: Double,
//)
//
//data class Size(
//    val width: Double,
//    val height: Double,
//)
//
//
//
//package com.maku.figmaparser
//
//import android.os.Bundle
//import android.util.Log
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import com.maku.figmaparser.ui.theme.FigmaParserTheme
//import com.squareup.moshi.JsonClass
//import com.squareup.moshi.JsonDataException
//import com.squareup.moshi.Moshi
//import com.squareup.moshi.Types
//import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import okhttp3.OkHttpClient
//import okhttp3.Request
//import okio.BufferedSource
//import okio.EOFException
//import java.io.IOException
//
//
//@JsonClass(generateAdapter = true)
//data class FigmaDocument(
//    val name: String,
//    val nodes: List<FigmaNode>
//)
//
//@JsonClass(generateAdapter = true)
//data class FigmaNode(
//    val name: String,
//    val type: String,
//    val children: List<FigmaNode>? = null
//)
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            FigmaParserTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    FigmaContentFromUrl("https://www.figma.com/design/E9pFXnK2pUyV36bt92GLAN/HelloNews?node-id=0%3A1&t=8aB34rqmx0iADlgC-1")
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun FigmaContentFromUrl(figmaFileUrl: String) {
//    var figmaData by remember { mutableStateOf<Map<String, Any?>?>(null) } // Map instead of FigmaDocument
//    var isLoading by remember { mutableStateOf(true) }
//    var error by remember { mutableStateOf<String?>(null) }
//
//    LaunchedEffect(Unit) {
//        withContext(Dispatchers.IO) { // Fetch on a background thread
//            try {
//                val client = OkHttpClient()
//                val request = Request.Builder().url(figmaFileUrl).build()
//                val response = client.newCall(request).execute()
//                Log.d("TAG", "FigmaContentFromUrl: ${response}")
//                if (response.isSuccessful) {
//                    response.body?.source()?.buffer()?.use { source ->
//                        figmaData = parseFigmaFile(source)
//                    }
//                } else {
//                    error = "Error fetching Figma file: ${response.code}"
//                }
//            } catch (e: IOException) {
//                error = when (e) {
//                    is EOFException -> "Unexpected end of Figma file data"
//                    else -> "Network Error: ${e.message}"
//                }
//            } finally {
//                isLoading = false // Indicate loading is finished
//            }
//        }
//    }
//
//    // Handle loading and errors (similar to before)
//    if (isLoading) {
//        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
//            CircularProgressIndicator()
//        }
//    } else if (error != null) {
//        Text("Error: $error")
//    } else {
//        figmaData?.let { map ->
//            val documentName = map["name"] as? String ?: "Unknown"
//            Column(modifier = Modifier.padding(16.dp)) {
//                Text("Document Name: $documentName")
//
//
////                val children = map["document"]?.get("children") as? List<Map<String, Any?>>
////                children?.let { FigmaNodeTree(it) }
//            }
//        }
//    }
//}
//
//// Modify FigmaNodeTree to work with the Map representation
//@Composable
//fun FigmaNodeTree(nodes: List<Map<String, Any?>>) {
//    Column {
//        nodes.forEach { node ->
//            val type = node["type"] as? String ?: "UNKNOWN"
//            val name = node["name"] as? String ?: "Unnamed Node"
//            Text(text = "$type: $name")
//
//            val children = node["children"] as? List<Map<String, Any?>>
//            children?.let {
//                Spacer(modifier = Modifier.padding(start = 16.dp))
//                FigmaNodeTree(it)
//            }
//        }
//    }
//}
//
//fun parseFigmaFile(source: BufferedSource): Map<String, Any?>? {
//    return try {
//        val moshi = Moshi.Builder()
//            .add(KotlinJsonAdapterFactory())
//            .build()
//        val adapter = moshi.adapter<Map<String, Any?>>(Types.newParameterizedType(Map::class.java, String::class.java, Any::class.java))
//
//        val figmaJson = source.use { it.readUtf8() }
//        adapter.fromJson(figmaJson)
//
//    } catch (e: Exception) {
//        Log.e("FigmaParser", "Error parsing Figma file: ${e.message}", e)
//        null
//    }
//}
//
////@Composable
////fun FigmaContentFromUrl(figmaFileUrl: String) {
////    var figmaData by remember { mutableStateOf<FigmaDocument?>(null) }
////    var isLoading by remember { mutableStateOf(true) }
////    var error by remember { mutableStateOf<String?>(null) }
////
////    LaunchedEffect(Unit) {
////        withContext(Dispatchers.IO) { // Fetch on a background thread
////            try {
////                val client = OkHttpClient()
////                val request = Request.Builder().url(figmaFileUrl).build()
////                val response = client.newCall(request).execute()
////                Log.d("TAG", "FigmaContentFromUrl: ${response}")
////                if (response.isSuccessful) {
////                    response.body?.source()?.buffer()?.use { source ->
////                        figmaData = parseFigmaFile(source)
////                    }
////                } else {
////                    error = "Error fetching Figma file: ${response.code}"
////                }
////            } catch (e: IOException) {
////                error = "Network Error: ${e}"
////            } finally {
////                isLoading = false // Indicate loading is finished
////            }
////        }
////    }
////
////    if (figmaData == null) {
////        Box(
////            modifier = Modifier.fillMaxSize(),
////            contentAlignment = Alignment.Center
////        ) {
////            CircularProgressIndicator()
////        }
////    } else {
////        Column(modifier = Modifier.padding(16.dp)) {
////            Text(text = "Document: ${figmaData!!.name}")
////            Spacer(modifier = Modifier.height(16.dp))
////            FigmaNodeTree(figmaData!!.nodes)
////        }
////    }
////}
////
////@Composable
////fun FigmaNodeTree(nodes: List<FigmaNode>) {
////    Column {
////        nodes.forEach { node ->
////            Text(text = "${node.type}: ${node.name}")
////            if (node.children != null) {
////                Spacer(modifier = Modifier.padding(start = 16.dp))
////                FigmaNodeTree(node.children)
////            }
////        }
////    }
////}
////
////fun parseFigmaFile(source: okio.BufferedSource): FigmaDocument? { // Return nullable FigmaDocument
////    return try {
////        val moshi = Moshi.Builder()
////            .add(KotlinJsonAdapterFactory())
////            .build()
////        val adapter = moshi.adapter(FigmaDocument::class.java)
////
////        // Read and parse JSON from the source
////        val figmaJson = source.use { it.readUtf8() }
////        adapter.fromJson(figmaJson)
////
////    } catch (e: Exception) {  // Catch all exceptions (IOException, JsonDataException, etc.)
////        Log.e("FigmaParser", "Error parsing Figma file: ${e.message}", e) // Log the error
////        null // Return null to indicate parsing failure
////    }
////}
//
////fun parseFigmaFile(source: okio.BufferedSource): FigmaDocument {
////    val moshi = Moshi.Builder()
////        .add(KotlinJsonAdapterFactory())
////        .build()
////
////    val adapter = moshi.adapter(FigmaDocument::class.java)
////
////    return try {
////        adapter.fromJson(source.use { it.readUtf8() }) ?: FigmaDocument("", emptyList())
////    } catch (e: JsonDataException) {
////        // Handle JSON parsing error
////        // For example, log the error and return a default FigmaDocument or throw a custom exception
////        Log.d("TAG", "parseFigmaFile Error parsing JSON: ${e.message}")
////        FigmaDocument("", emptyList())
////    }
////}
//
////@Preview(showBackground = true)
////@Composable
////fun DefaultPreview() {
////    FigmaParserTheme {
////        FigmaContentFromUrl("https://www.figma.com/design/E9pFXnK2pUyV36bt92GLAN/HelloNews?node-id=0%3A1&t=8aB34rqmx0iADlgC-1")
////    }
////}
//
//// Data Classes (You'll need to define these based on your Figma file structure)
////data class FigmaDocument(val name: String, val children: List<FigmaNode>)
////data class FigmaNode(val id: String, val name: String, val type: String, /* ...other properties */)
////
////class MainActivity : ComponentActivity() {
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////
////        setContent {
////            FigmaParserTheme {
////                Surface(
////                    modifier = Modifier.fillMaxSize(),
////                    color = MaterialTheme.colorScheme.background
////                ) {
////                    FigmaContentFromUrl("https://www.figma.com/design/E9pFXnK2pUyV36bt92GLAN/HelloNews?node-id=0%3A1&t=8aB34rqmx0iADlgC-1")
////                }
////            }
////        }
////    }
////}
////
////@Composable
////fun FigmaContentFromUrl(figmaFileUrl: String) {
////    var figmaData by remember { mutableStateOf<FigmaDocument?>(null) }
////    var isLoading by remember { mutableStateOf(true) }
////    var error by remember { mutableStateOf<String?>(null) }
////
////    LaunchedEffect(Unit) {
////        withContext(Dispatchers.IO) {
////            try {
////                val client = OkHttpClient()
////                val request = Request.Builder().url(figmaFileUrl).build()
////                val response = client.newCall(request).execute()
////
////                if (response.isSuccessful) {
////                    response.body?.source()?.buffer()?.use { source ->
////                        figmaData = parseFigmaFile(source)
////                    }
////                } else {
////                    error = "Error fetching Figma file: ${response.code}"
////                }
////            } catch (e: IOException) {
////                error = "Network Error: ${e}"
////            } finally {
////                isLoading = false // Indicate loading is finished
////            }
////        }
////    }
////
////    Column(modifier = Modifier.padding(16.dp)) {
////        if (isLoading) {
////            CircularProgressIndicator() // Show loading indicator
////        } else if (error != null) {
////            Text("Error: $error") // Show error message
////        } else {
////            figmaData?.let { document ->
////                Text("Document Name: ${document.name}")
////                // Display other extracted data from figmaData here
////            }
////        }
////    }
////}
////
////fun parseFigmaFile(source: BufferedSource): FigmaDocument {
////    val moshi = Moshi.Builder()
////        .add(KotlinJsonAdapterFactory())
////        .build()
////    val adapter = moshi.adapter(FigmaDocument::class.java)
////
////    // Read the JSON string from the BufferedSource
////    val json = source.readUtf8()
////
////    // ... Your custom Figma parsing logic here (replace this comment)
////
////    return adapter.fromJson(json)!!
////}
//
////class MainActivity : ComponentActivity() {
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        setContent {
////            FigmaParserTheme {
////                Surface(
////                    modifier = Modifier.fillMaxSize(),
////                    color = MaterialTheme.colorScheme.background
////                ) {
////                    // Read and display the contents of the Figma file
////                    DisplayFigmaFileContent(applicationContext)
////                }
////            }
////        }
////    }
////}
////
////@Composable
////fun DisplayFigmaFileContent(context: Context, modifier: Modifier = Modifier) {
////    val figmaContent = remember { mutableStateOf("Loading...") }
////
////    // Using LaunchedEffect to read the file in a coroutine
////    LaunchedEffect(Unit) {
////        try {
////            val content = readFigmaFileFromAssets(context,"design.fig")
////            figmaContent.value = content ?: "Failed to read content"
////        } catch (e: IOException) {
////            figmaContent.value = "Error: ${e.message}"
////        }
////    }
////
////    Text(
////        text = figmaContent.value,
////        modifier = modifier.padding(16.dp)
////    )
////}
////
////@Throws(IOException::class)
////fun readFigmaFileFromAssets(context: Context, fileName: String): String? {
////    context.assets.open(fileName).use { inputStream ->
////        val source = inputStream.source()
////        val bufferedSource = source.buffer()
////
////        // Read the entire file into a buffer
////        val buffer = Buffer()
////        bufferedSource.readAll(buffer)
////
////        // Return the contents as a string (or handle as needed)
////        return buffer.readUtf8()
////    }
////}
////
////@Preview(showBackground = true)
////@Composable
////fun GreetingPreview() {
////    FigmaParserTheme {
//////        DisplayFigmaFileContent()
////    }
////}