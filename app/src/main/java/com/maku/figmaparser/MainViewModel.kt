package com.maku.figmaparser

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.GenerateContentResponse
import com.google.ai.client.generativeai.type.GoogleGenerativeAIException
import com.google.ai.client.generativeai.type.InvalidAPIKeyException
import com.google.ai.client.generativeai.type.InvalidStateException
import com.google.ai.client.generativeai.type.PromptBlockedException
import com.google.ai.client.generativeai.type.QuotaExceededException
import com.google.ai.client.generativeai.type.RequestTimeoutException
import com.google.ai.client.generativeai.type.ResponseStoppedException
import com.google.ai.client.generativeai.type.SerializationException
import com.google.ai.client.generativeai.type.ServerException
import com.google.ai.client.generativeai.type.TextPart
import com.google.ai.client.generativeai.type.UnknownException
import com.google.ai.client.generativeai.type.UnsupportedUserLocationException
import com.google.ai.client.generativeai.type.content
import com.maku.figmaparser.model.finali.FigmaFile
import com.maku.figmaparser.model.finali.Node
import com.maku.figmaparser.model.finali.NodeType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import kotlin.math.pow

@HiltViewModel
class FigmaViewModel @Inject constructor(
    private val figmaRepository: FigmaRepository,
    var sharedPreferences: SharedPreferences
) : ViewModel() {

    private val _figmaFile = MutableStateFlow<String?>(null)
    val figmaFile: StateFlow<String?> = _figmaFile
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage
    private val _figmaErrorMessage = MutableStateFlow<String?>(null)
    val figmaErrorMessage: StateFlow<String?> = _figmaErrorMessage
    private val _generatedCode = MutableStateFlow("")
    val generatedCode: StateFlow<String> = _generatedCode
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    val apiKeySha = sharedPreferences.getString("apiKey", null)
    val figmaId = ""

    init {
        var figmaId = extractFigmaKey("https://www.figma.com/design/E9pFXnK2pUyV36bt92GLAN/HelloNews?node-id=0-1&t=QR8RraKlzziwgH05-0")
        Log.d("TAG", "fetchFigmaDesign: figmaId ${figmaId}")
        figmaId = figmaId
    }

    // https://www.figma.com/design/E9pFXnK2pUyV36bt92GLAN/HelloNews?node-id=0-1&t=QR8RraKlzziwgH05-0
    fun fetchFigmaDesign(
        figmaUrl: String = "",
        apiKey: String = ""
    ) {
        viewModelScope.launch {
            figmaRepository.getFigmaResponse(figmaUrl).collect { result ->
                when (result) {
                    is Result.Loading -> _isLoading.value = true
                    is Result.Success -> {
                        _isLoading.value = false
                        val preprocessedFile = preprocessFigmaFile(result.data, apiKey)
                        _figmaFile.value = preprocessedFile.toString()
                    }

                    is Result.Error -> {
                        Log.d("TAG", "fetchFigmaDesign: running 4 ${result}")
                        _isLoading.value = false
                        _figmaErrorMessage.value = "Error: ${result.exception.message}"
                    }
                }
            }
        }
    }

    /**
     * flattenGroupsAndFrames: This function recursively flattens nested groups and frames, merging their child nodes into a single list.
     * filterIrrelevantNodes: This function removes nodes that you don't want to include in the code generation (e.g., hidden layers, non-visible elements).
     * extractDesignTokens: (Optional) This function extracts color and typography information from the document to create a designTokens object for use in your Compose theming.
     * */

    private fun preprocessFigmaFile(figmaFile: FigmaFile, apiKey: String): Node {
        val flattenedDocument = flattenGroupsAndFrames(figmaFile.document)
        println("flattenedDocument ${flattenedDocument}")
        // process and send visible nodes to the AI model.
        val visibleNodes = flattenedDocument.children?.filter { it.visible } ?: emptyList()
        println("visibleNodes ${visibleNodes}")

        val nodeChunks = visibleNodes.chunked(5)
        println("nodeChunks ${nodeChunks}")

        runBlocking {
            processNodeChunk(nodeChunks, apiKey, figmaFile.document)// Start a new coroutine scope
//            nodeChunks?.map { chunk ->
//                launch { // Launch a new coroutine for each chunk
//                    processNodeChunk(chunk, apiKey)
//                }
//            }?.joinAll() // Wait for all coroutines to complete
        }

        return flattenedDocument
    }

    private suspend fun processNodeChunk(chunk: List<List<Node>>?, apiKey: String, document: Node) {
        _isLoading.value = true
        print("chunk ${chunk?.size}")
        print("chunk ${chunk?.chunked(5).toString()}")

        val prompt = """
            Transform the following Figma node structure into Jetpack Compose code:
            - The structure includes nodes like Canvas, ComponentSet, Component, and Frame, with various properties such as visibility, type, fills, strokes, strokeWeight, cornerRadius, characters, styles, and children nodes."
            - If you cannot generate code for all visible nodes in a chunk due to resource constraints, prioritize the most significant visible elements first.
            - Dont add any explanations
            Nodes:
            ${document}

            Considerations:
            * Focus on visible UI elements (Text, Rectangle, Image, etc.)
            * Prioritize layout accuracy (Rows, Columns, Box, etc.)
            * Include basic styling (Modifiers, Colors, Fonts)
            * Use Material Design components where applicable
            * If applicable, infer design tokens (Colors, Typography) from the nodes
        """.trimIndent()

//        val prompt = """
//                        You are an expert Android developer proficient in converting Figma designs to Jetpack Compose code. Your task is to transform the following Figma node structure into accurate and concise Jetpack Compose code.
//
//                        Context:
//                        - The Figma node structure includes various elements such as Canvas, ComponentSet, Component, and Frame.
//                        - Each node has properties like visibility, type, fills, strokes, strokeWeight, cornerRadius, characters, styles, and children nodes.
//
//                        Instructions:
//                        1. Focus on visible UI elements (Text, Rectangle, Image, etc.).
//                        2. Prioritize layout accuracy using Rows, Columns, Box, etc.
//                        3. Include basic styling using Modifiers, Colors, Fonts.
//                        4. Use Material Design components where applicable.
//                        5. Infer design tokens (Colors, Typography) from the nodes if applicable.
//                        6. Provide only the Jetpack Compose code and nothing else.
//
//                        Nodes:
//                        ${chunk?.chunked(5).toString()}
//
//                        Example:
//                        // Given a Figma node structure like:
//                        // [
//                        //   { "type": "TEXT", "characters": "Hello", "styles": { "fontSize": 16, "fontWeight": "bold" } },
//                        //   { "type": "RECTANGLE", "fills": [{ "color": "#FF0000" }], "cornerRadius": 4 }
//                        // ]
//                        // Generate Jetpack Compose code:
//                        // Text(text = "Hello", fontSize = 16.sp, fontWeight = FontWeight.Bold)
//                        // Box(modifier = Modifier.background(Color.Red).cornerRadius(4.dp))
//
//                        Generate similar code for the provided nodes.
//                    """.trimIndent()


        // Retry mechanism for QuotaExceededException
        var retryCount = 0
        val maxRetries = 3

        while (retryCount < maxRetries) {
            try {
                val aiResponse = GenerativeModel(
                    "gemini-1.5-pro",
                    apiKey,
                    systemInstruction = content { text("You are an expert Android developer proficient in converting Figma designs to Jetpack Compose code. Understand the Figma node structure, including types, properties (visibility, layout, styling), and relationships. Generate concise, accurate Compose code using best practices.") },
                ).generateContentStream(prompt)
                viewModelScope.launch {
                    aiResponse.collect { response ->
                        when (response) {
                            is GenerateContentResponse -> {
                                for (candidate in response.candidates) {
                                    val generatedCode =
                                        candidate.content.parts.joinToString("") {
                                            if (it is TextPart) it.text else "" // Access the text property of TextPart
                                        }
                                    _generatedCode.value += generatedCode
                                    println("Generated Code: $generatedCode")
                                    _isLoading.value = false
                                }
                            }

                            else -> {
                                _errorMessage.value = "Unexpected response from AI"
                                println("Unexpected response")
                                _isLoading.value = false
                            }
                        }
                    }
                }
                break  // Exit retry loop if successful
            } catch (e: QuotaExceededException) {
                _isLoading.value = false
                retryCount++
                val delayMillis = 1000L * 2.0.pow(retryCount).toLong()  // Exponential backoff
                println("Quota exceeded. Retrying in ${delayMillis / 1000} seconds...")
                delay(delayMillis)
            }
        }
        if (retryCount == maxRetries) {
            _errorMessage.value = "Quota exceeded after multiple retries."
            println("Quota exceeded after maximum retries.")
        }

        // Introduce a delay between requests to avoid rate limiting
        delay(2000) // Adjust delay as needed
    }


//    private fun preprocessFigmaFile(figmaFile: FigmaFile, apiKey: String): Node {
//        val flattenedDocument = flattenGroupsAndFrames(figmaFile.document)
//        val nodeChunks = flattenedDocument.children?.chunked(10) // Adjust the chunk size as needed
//        nodeChunks?.forEach { chunk ->
//            println("chunk ${chunk}")
//
//            val prompt = """
//            Transform the following Figma node structure into Jetpack Compose code:
//            - The structure includes nodes like Canvas, ComponentSet, Component, and Frame, with various properties such as visibility, type, fills, strokes, strokeWeight, cornerRadius, characters, styles, and children nodes."
//            - Provide only the code and nothing else.
//            Nodes:
//            ${chunk}
//
//            Considerations:
//            * Focus on visible UI elements (Text, Rectangle, Image, etc.)
//            * Prioritize layout accuracy (Rows, Columns, Box, etc.)
//            * Include basic styling (Modifiers, Colors, Fonts)
//            * Use Material Design components where applicable
//            * If applicable, infer design tokens (Colors, Typography) from the nodes
//            """
//            val aiResponse = GenerativeModel(
//                "gemini-1.5-pro",
//                apiKey,
//                systemInstruction = content { text("You are an expert Android developer proficient in converting Figma designs to Jetpack Compose code. Understand the Figma node structure, including types, properties (visibility, layout, styling), and relationships. Generate concise, accurate Compose code using best practices.") },
//            ).generateContentStream(prompt)
//            viewModelScope.launch {
//                aiResponse.collect {
////                println("response ${it}")
//                    try {
//                        aiResponse.collect { response ->
//                            when (response) {
//                                is GenerateContentResponse -> {
//                                    for (candidate in response.candidates) {
//                                        val generatedCode =
//                                            candidate.content.parts.joinToString("") {
//                                                if (it is TextPart) it.text else "" // Access the text property of TextPart
//                                            }
//                                        _generatedCode.value += generatedCode
//                                        println("Generated Code: $generatedCode")
//                                    }
//                                }
//
//                                else -> {
//                                    _errorMessage.value = "Unexpected response from AI"
//                                    println("Unexpected response")
//                                }
//                            }
//                            // Process the response
////                        println("response ${response}")
////                        println("response response.candidates[0]${response.candidates[0]}")
////                        println("response.candidates[0].content ${response.candidates[0].content}")
////                        _generatedCode.value += response.candidates[0].content
//                        }
//                    } catch (e: ResponseStoppedException) {
//                        // Handle the exception
//                        val finishReason = e.response.candidates.firstOrNull()?.finishReason
//                        _errorMessage.value =
//                            "Error: Content generation stopped. Reason: ${finishReason ?: "Unknown"}"
//                    } catch (e: QuotaExceededException) {
//                        // Handle other Google Generative AI exceptions
//                        _errorMessage.value = handleGoogleGenerativeAIException(e)
//                    } catch (e: Exception) { // Catch any other unexpected exceptions
//                        _errorMessage.value = "An unexpected error occurred: ${e.message}"
//                    }
//                }
//                // Optional: Add delay between batches to avoid hitting rate limits
//                delay(1000) // Delay in milliseconds, adjust as needed
//            }
//        }
//
//        return flattenedDocument
//    }


    // Helper function to check if all child nodes have been processed
    private fun allChildrenProcessed(node: Node?): Boolean {
        if (node == null) return true
        return node.children?.all { allChildrenProcessed(it) } ?: true // Check children recursively
    }

    private fun handleGoogleGenerativeAIException(exception: Throwable): String? {
        val googleAIException = GoogleGenerativeAIException.from(exception)
        return when (googleAIException) {
            is SerializationException -> "Error deserializing response: ${googleAIException.message}"
            is ServerException -> "Server error: ${googleAIException.message}"
            is InvalidAPIKeyException -> "Invalid API key: ${googleAIException.message}"
            is PromptBlockedException -> "Prompt blocked: ${googleAIException.response.promptFeedback?.blockReason?.name}"
            is UnsupportedUserLocationException -> "Unsupported user location"
            is InvalidStateException -> "Invalid state: ${googleAIException.message}"
            is ResponseStoppedException -> "Content generation stopped: ${googleAIException.response.candidates.first().finishReason?.name}"
            is RequestTimeoutException -> "Request timeout: ${googleAIException.message}"
            is QuotaExceededException -> "Quota exceeded: ${googleAIException.message}"
            is UnknownException -> "Unknown error: ${googleAIException.message}"
        }
    }

    // The main task of the flattenGroupsAndFrames function is to flatten nested groups and frames by merging their child nodes into a single list.
    private fun flattenGroupsAndFrames(node: Node): Node {
        print("running 2")
        return if (node.type == NodeType.GROUP.name || node.type == NodeType.FRAME.name) {
            node.copy(children = node.children?.flatMap { flattenGroupsAndFrames(it).children.orEmpty() }
                .orEmpty())
        } else {
            node.copy(children = node.children?.map { flattenGroupsAndFrames(it) }.orEmpty())
        }
    }

    private fun <T> List<T>.chunked(size: Int): List<List<T>> {
        val chunks = mutableListOf<List<T>>()
        var index = 0
        while (index < this.size) {
            chunks.add(this.subList(index, (index + size).coerceAtMost(this.size)))
            index += size
        }
        return chunks
    }

    fun extractFigmaKey(url: String): String? {
        // Regex Pattern for the key in the Figma URL
        val pattern = """https://www\.figma\.com/design/([A-Za-z0-9]+)/"""

        val matchResult = Regex(pattern).find(url)
        return matchResult?.groups?.get(1)?.value // Return the matched key
    }
}



