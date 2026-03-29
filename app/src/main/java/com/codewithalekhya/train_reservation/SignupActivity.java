package com.codewithalekhya.train_reservation;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class SignupActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etName = findViewById(R.id.et_signup_name);
        etEmail = findViewById(R.id.et_signup_email);
        etPassword = findViewById(R.id.et_signup_password);
        Button btnSignup = findViewById(R.id.btn_signup_confirm);
        TextView tvBackToLogin = findViewById(R.id.tv_back_to_login);

        btnSignup.setOnClickListener(v -> {
            if (etName.getText() == null || etEmail.getText() == null || etPassword.getText() == null) return;
            
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                
                // Store password and name using email as a unique key to support multiple accounts
                editor.putString("password_" + email, password);
                editor.putString("name_" + email, name);
                editor.apply();

                Toast.makeText(this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        tvBackToLogin.setOnClickListener(v -> finish());
    }
}