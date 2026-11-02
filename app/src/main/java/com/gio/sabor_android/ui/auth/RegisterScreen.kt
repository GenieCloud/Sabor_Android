package com.gio.sabor_android.ui.auth

import android.util.Patterns
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel // Add this import
import com.gio.sabor_android.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel = viewModel(), // Added authViewModel parameter here
    onRegisterSuccess: () -> Unit,
    onGoogleSignUpClick: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Date of Birth states & Picker visibility
    var dateOfBirth by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // Gender Dropdown states (Optional)
    var gender by remember { mutableStateOf("") }
    var genderExpanded by remember { mutableStateOf(false) }
    val genderOptions = listOf("Female", "Male", "Non-binary", "Prefer not to say")

    var termsAccepted by remember { mutableStateOf(false) }

    // Password visibility toggle states
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    // Error message state for validation (Combines local validation & ViewModel errors)
    var localErrorMessage by remember { mutableStateOf<String?>(null) }
    val displayError = localErrorMessage ?: authViewModel.errorMessage

    // Date Picker Dialog component
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).apply {
                                timeZone = TimeZone.getTimeZone("UTC")
                            }
                            val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
                                timeInMillis = millis
                            }
                            dateOfBirth = formatter.format(calendar.time)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK", color = NarangaOrange)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false }
                ) {
                    Text("Cancel", color = SaborBlack.copy(alpha = 0.6f))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = SaborWhite
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title Header
            Text(
                text = "Create your account",
                color = SaborBlack,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Navigate to Login prompt
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account? ",
                    color = SaborBlack.copy(alpha = 0.6f),
                    fontSize = 14.sp
                )
                TextButton(
                    onClick = { onNavigateToLogin() },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = "Log in",
                        color = NarangaOrange,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // First Name Field (Required)
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NarangaOrange,
                    unfocusedBorderColor = SaborBlack.copy(alpha = 0.3f),
                    focusedLabelColor = NarangaOrange,
                    unfocusedLabelColor = AvocadoGreen.copy(alpha = 0.7f),
                    focusedTextColor = SaborBlack,
                    unfocusedTextColor = AvocadoGreen
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Last Name Field (Required)
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NarangaOrange,
                    unfocusedBorderColor = SaborBlack.copy(alpha = 0.3f),
                    focusedLabelColor = NarangaOrange,
                    unfocusedLabelColor = AvocadoGreen.copy(alpha = 0.7f),
                    focusedTextColor = SaborBlack,
                    unfocusedTextColor = AvocadoGreen
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Email Field (Required)
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email *") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NarangaOrange,
                    unfocusedBorderColor = SaborBlack.copy(alpha = 0.3f),
                    focusedLabelColor = NarangaOrange,
                    unfocusedLabelColor = AvocadoGreen.copy(alpha = 0.7f),
                    focusedTextColor = SaborBlack,
                    unfocusedTextColor = AvocadoGreen
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Password Field with Visibility Toggle (Required)
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password *") },
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = description, tint = AvocadoGreen.copy(alpha = 0.7f))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NarangaOrange,
                    unfocusedBorderColor = SaborBlack.copy(alpha = 0.3f),
                    focusedLabelColor = NarangaOrange,
                    unfocusedLabelColor = AvocadoGreen.copy(alpha = 0.7f),
                    focusedTextColor = SaborBlack,
                    unfocusedTextColor = AvocadoGreen
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Confirm Password Field with Visibility Toggle (Required)
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password *") },
                singleLine = true,
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (confirmPasswordVisible) "Hide password" else "Show password"

                    IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                        Icon(imageVector = image, contentDescription = description, tint = AvocadoGreen.copy(alpha = 0.7f))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = NarangaOrange,
                    unfocusedBorderColor = SaborBlack.copy(alpha = 0.3f),
                    focusedLabelColor = NarangaOrange,
                    unfocusedLabelColor = AvocadoGreen.copy(alpha = 0.7f),
                    focusedTextColor = SaborBlack,
                    unfocusedTextColor = AvocadoGreen
                )
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Date of Birth Field (Required with Calendar Picker Dialog)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
            ) {
                OutlinedTextField(
                    value = dateOfBirth,
                    onValueChange = {},
                    readOnly = true,
                    enabled = false,
                    label = { Text("Date of Birth *") },
                    placeholder = { Text("MM/DD/YYYY") },
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(
                                imageVector = Icons.Filled.DateRange,
                                contentDescription = "Select Date of Birth",
                                tint = AvocadoGreen.copy(alpha = 0.7f)
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledBorderColor = SaborBlack.copy(alpha = 0.3f),
                        disabledLabelColor = AvocadoGreen.copy(alpha = 0.7f),
                        disabledTextColor = SaborBlack,
                        disabledTrailingIconColor = AvocadoGreen.copy(alpha = 0.7f)
                    )
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Gender Exposed Dropdown Menu (Optional)
            ExposedDropdownMenuBox(
                expanded = genderExpanded,
                onExpandedChange = { genderExpanded = !genderExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = gender,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Gender (optional)") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = genderExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NarangaOrange,
                        unfocusedBorderColor = SaborBlack.copy(alpha = 0.3f),
                        focusedLabelColor = NarangaOrange,
                        unfocusedLabelColor = AvocadoGreen.copy(alpha = 0.7f),
                        focusedTextColor = SaborBlack,
                        unfocusedTextColor = AvocadoGreen
                    )
                )

                ExposedDropdownMenu(
                    expanded = genderExpanded,
                    onDismissRequest = { genderExpanded = false }
                ) {
                    genderOptions.forEach { selectionOption ->
                        DropdownMenuItem(
                            text = { Text(selectionOption) },
                            onClick = {
                                gender = selectionOption
                                genderExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Terms and Conditions Checkbox Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = termsAccepted,
                    onCheckedChange = { termsAccepted = it },
                    colors = CheckboxDefaults.colors(checkedColor = AvocadoGreen)
                )
                Text(
                    text = "By creating an account, I have read and agree to the Terms and Privacy Policy *",
                    color = SaborBlack.copy(alpha = 0.7f),
                    fontSize = 11.sp
                )
            }

            // Display validation error if present
            if (displayError != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = displayError,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Create My Account Button with Firebase Integration & Loading State
            Button(
                onClick = {
                    when {
                        firstName.isBlank() -> localErrorMessage = "Please enter your first name."
                        lastName.isBlank() -> localErrorMessage = "Please enter your last name."
                        email.isBlank() -> localErrorMessage = "Please enter your email address."
                        !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> localErrorMessage = "Please enter a valid email address."
                        password.isBlank() -> localErrorMessage = "Please enter a password."
                        password != confirmPassword -> localErrorMessage = "Passwords do not match."
                        dateOfBirth.isBlank() -> localErrorMessage = "Please select your date of birth."
                        !termsAccepted -> localErrorMessage = "You must agree to the Terms and Privacy Policy to continue."
                        else -> {
                            localErrorMessage = null
                            authViewModel.register(email, password, onRegisterSuccess)
                        }
                    }
                },
                enabled = !authViewModel.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NarangaOrange)
            ) {
                if (authViewModel.isLoading) {
                    CircularProgressIndicator(color = SaborWhite, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "Create My Account",
                        color = SaborWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Google Sign-Up Button
            OutlinedButton(
                onClick = { onGoogleSignUpClick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(SaborBlack.copy(alpha = 0.2f))
                ),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = AvocadoGreen)
            ) {
                Text(
                    text = "Create account with Google",
                    color = SaborWhite,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}