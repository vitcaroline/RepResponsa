package com.example.represponsa.presentation.ui.receipts

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.represponsa.presentation.ui.commons.TopBar
import com.example.represponsa.presentation.ui.receipts.viewModel.UploadReceiptViewModel
import com.example.represponsa.presentation.ui.receipts.viewModel.UploadState

@Composable
fun UploadReceiptScreen(
    onNavigateBack: () -> Unit,
    viewModel: UploadReceiptViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    val uploadState by viewModel.uploadState.collectAsState()

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedFileUri = uri
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Upload de Comprovante",
                onBackClick = onNavigateBack
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.CloudUpload,
                    contentDescription = "Upload",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    "Selecione o comprovante de pagamento (PDF ou imagem)",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )

                Button(
                    onClick = {
                        filePickerLauncher.launch("application/pdf,image/*")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Escolher arquivo")
                }

                selectedFileUri?.let { uri ->
                    Text(
                        "Arquivo selecionado: ${uri.lastPathSegment}",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    when (uploadState) {
                        is UploadState.Loading -> CircularProgressIndicator()
                        is UploadState.Success -> Text("Upload realizado com sucesso!", color = Color.Green)
                        is UploadState.Error -> Text(
                            (uploadState as UploadState.Error).message,
                            color = Color.Red
                        )
                        else -> { /* Idle */ }
                    }

                    Button(
                        onClick = {
                            viewModel.uploadReceipt(uri, context)
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Enviar Comprovante")
                    }
                }
            }
        }
    }
}
