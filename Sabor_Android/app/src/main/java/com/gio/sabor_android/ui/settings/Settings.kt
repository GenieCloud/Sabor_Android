package com.gio.sabor_android.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.gio.sabor_android.data.repository.AuthRepository

@Composable
fun SettingsScreen(
    onBackClick: () -> Unit = {},
    onLogoutSuccess: () -> Unit = {}
) {
    val authRepository = AuthRepository()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Toolbar matching wireframe 09_Settings
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFFD35400)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Settings",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Account Settings Options Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Account",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFFD35400)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        ProfileSettingRow(title = "Account Information")
                        Spacer(modifier = Modifier.height(12.dp))
                        ProfileSettingRow(title = "Security")
                        Spacer(modifier = Modifier.height(12.dp))
                        ProfileSettingRow(title = "Privacy")
                        Spacer(modifier = Modifier.height(12.dp))
                        ProfileSettingRow(title = "Linked Accounts")

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Notifications",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color(0xFFD35400)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        ProfileSettingRow(title = "Push Notifications")
                        Spacer(modifier = Modifier.height(12.dp))
                        ProfileSettingRow(title = "Subscriptions")
                    }
                }
            }

            // Log Out Button Action
            item {
                Button(
                    onClick = {
                        authRepository.signOut()
                        onLogoutSuccess()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFC0392B)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Log Out",
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Log Out",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun ProfileSettingRow(title: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface)
        Text(text = "›", fontSize = 18.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
    }
}