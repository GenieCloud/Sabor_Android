package com.gio.sabor_android.ui.post

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.gio.sabor_android.data.repository.PostRepository
import com.gio.sabor_android.ui.theme.AvocadoGreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    onNavigateBack: () -> Unit,
    onPostSuccessfullyCreated: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Pass context into PostRepository so it can encode the image to Base64
    val postRepository = remember { PostRepository(context) }

    var caption by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }

    // Temporary file for camera capture
    val photoFile = remember {
        File(context.cacheDir, "sabor_camera_${System.currentTimeMillis()}.jpg")
    }
    val cameraImageUri = remember {
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", photoFile)
    }

    // Gallery Picker Launcher
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            android.util.Log.d("CreatePostDebug", "Gallery Image Selected: $it")
        }
    }

    // Camera Capture Launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            selectedImageUri = cameraImageUri
            android.util.Log.d("CreatePostDebug", "Camera Capture Success: $cameraImageUri")
        }
    }

    // Camera Permission Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(cameraImageUri)
        } else {
            Toast.makeText(context, "Camera permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Create Sabor Post", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AvocadoGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Caption Text Field
                OutlinedTextField(
                    value = caption,
                    onValueChange = { caption = it },
                    label = { Text("What's cooking? Write a caption...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp),
                    shape = RoundedCornerShape(12.dp)
                )

                // Media Selection Buttons ([SCRUM-14](https://giojones.atlassian.net/browse/SCRUM-14))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { galleryLauncher.launch("image/*") },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Icon(imageVector = Icons.Default.PhotoLibrary, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Gallery")
                    }

                    Button(
                        onClick = { permissionLauncher.launch(android.Manifest.permission.CAMERA) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                    ) {
                        Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Camera")
                    }
                }

                // Image Preview Box (Displays actual image if selected)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Selected Post Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text(
                            text = "No Image Selected",
                            color = Color.Gray
                        )
                    }
                }
            }

            // Submit Button ([SCRUM-15](https://giojones.atlassian.net/browse/SCRUM-15) & [SCRUM-16](https://giojones.atlassian.net/browse/SCRUM-16))
            Button(
                onClick = {
                    if (selectedImageUri != null && caption.isNotBlank()) {
                        isUploading = true
                        coroutineScope.launch(Dispatchers.IO) {
                            try {
                                val result = postRepository.createPost(caption, selectedImageUri!!)
                                withContext(Dispatchers.Main) {
                                    isUploading = false
                                    if (result.isSuccess) {
                                        Toast.makeText(context, "Post uploaded successfully!", Toast.LENGTH_SHORT).show()
                                        onPostSuccessfullyCreated()
                                    } else {
                                        val errorMsg = result.exceptionOrNull()?.localizedMessage ?: "Unknown error"
                                        android.util.Log.e("CreatePostDebug", "Upload failed: $errorMsg", result.exceptionOrNull())
                                        Toast.makeText(context, "Upload failed: $errorMsg", Toast.LENGTH_LONG).show()
                                    }
                                }
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    isUploading = false
                                    android.util.Log.e("CreatePostDebug", "Exception during upload: ${e.message}", e)
                                    Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isUploading && selectedImageUri != null && caption.isNotBlank(),
                colors = ButtonDefaults.buttonColors(containerColor = AvocadoGreen)
            ) {
                if (isUploading) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Share Recipe Post", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}