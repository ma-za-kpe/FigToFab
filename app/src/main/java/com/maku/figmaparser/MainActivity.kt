package com.maku.figmaparser

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.maku.figmaparser.ui.theme.FigmaParserTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FigmaParserTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun MyApp() {
    MaterialTheme {
        Surface {
            FigmaFileScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FigmaFileScreen(
    viewModel: FigmaViewModel = hiltViewModel(),
) {
    val errorMessage by viewModel.errorMessage.collectAsState()
    val figmaErrorMessage by viewModel.figmaErrorMessage.collectAsState()
    val generatedCode by viewModel.generatedCode.collectAsState()
    val figmaFile by viewModel.figmaFile.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Log.d("TAG", "FigmaFileScreennnn: ${figmaFile}")

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Figma Design") })
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Display Figma data (if available)
            generatedCode.let { file ->
//                Log.d("TAG", "FigmaFileScreen ...: ${file}")
                // Render UI based on Figma file content (e.g., nodes, components)
                //                Text("Document Name: ${file.canvases}")
                CodeBlock(
                    code = generatedCode,
                    fetchFigmaDesign = viewModel::fetchFigmaDesign,
                    errorMessage = errorMessage,
                    figmaErrorMessage = figmaErrorMessage,
                    isLoading = isLoading,
                    figmaFile = figmaFile
                )
            }

            // Optional: Button to refresh
            Button(onClick = { viewModel.fetchFigmaDesign()}) {
                Text("Refresh")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeBlock(
    code: String,
    modifier: Modifier = Modifier,
    fetchFigmaDesign: (String, String) -> Unit,
    errorMessage: String?,
    figmaErrorMessage: String?,
    isLoading: Boolean,
    figmaFile: String?
) {
    var showCopySuccess by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var figmaUrl by remember { mutableStateOf("") } // State for the Figma URL input
    var apiKey by remember { mutableStateOf("") } // State for the API key input

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp)
    ) {
        // API Key Input (similar to figmaUrl input)
        OutlinedTextField(
            value = apiKey,
            onValueChange = { apiKey = it },
            label = { Text("Enter API Key") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            shape = RoundedCornerShape(8.dp)
        )

        // Figma URL Input (with styling)
        OutlinedTextField(
            value = figmaUrl,
            onValueChange = { figmaUrl = it },
            label = { Text("Enter Figma Design Key") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            singleLine = true,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            shape = RoundedCornerShape(8.dp) // Add rounded corners
        )

        // Copy Success Message (same as before)
        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Text(text = "Loading ..... ", color = MaterialTheme.colorScheme.error)
        }

        // Button to trigger fetching Figma data (if needed)
        Button(
            onClick = {
            if (apiKey.isNotBlank() && figmaUrl.isNotBlank()) { // Validate input
                fetchFigmaDesign(figmaUrl, apiKey) // Pass API key
            } else {
                Toast.makeText(context, "Please enter both API key and Figma design key", Toast.LENGTH_SHORT).show()
            }
            }, modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Text("Fetch Figma Data")
        }

        // Scrollable Code Display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Make the Box take up available space for scrolling
                .background(MaterialTheme.colorScheme.inverseOnSurface) // Set a dark background
                .padding(8.dp)
        ) {
            if (errorMessage != null) {
                Text(
                    text = figmaErrorMessage.toString(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    color = Color.Red, // Use red for error message
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )
            } else {
                Text(
                    text = figmaFile.toString(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Scrollable Code Display
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Make the Box take up available space for scrolling
                .background(MaterialTheme.colorScheme.inverseOnSurface) // Set a dark background
                .padding(8.dp)
        ) {
            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    color = Color.Red, // Use red for error message
                    modifier = Modifier.verticalScroll(rememberScrollState())
                )
            } else {
                Text(
                    text = code.toString(),
                    fontFamily = FontFamily.Monospace,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Copy Button (same as before)
        Button(onClick = {
            copyToClipboard(context, code)
            showCopySuccess = true
        }) {
            Text("Copy Code")
        }

        // Copy Success Message (same as before)
        AnimatedVisibility(
            visible = showCopySuccess,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Text(text = "Copied!", color = MaterialTheme.colorScheme.error)
        }
    }
}

/**
 * Copies the given text to the clipboard.
 *
 * @param context The context (e.g., obtained from LocalContext.current).
 * @param text The text to copy.
 */
fun copyToClipboard(context: Context, text: String) {
    val clipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
    val clipData = android.content.ClipData.newPlainText("Figma Generated Code", text)
    clipboardManager.setPrimaryClip(clipData)
    Toast.makeText(context, "Code copied to clipboard", Toast.LENGTH_SHORT).show()
}
