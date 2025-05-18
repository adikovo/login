package com.example.ex2login;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.Navigation;

import com.example.ex2login.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");
    }

    public void login() {
        String username = ((EditText)findViewById(R.id.editTextTextEmailAddress)).getText().toString();
        String password = ((EditText)findViewById(R.id.editTextTextPassword)).getText().toString();

        // Convert username to email format for Firebase Auth
        String email = username + "@yourdomain.com";

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Login successful!", Toast.LENGTH_LONG).show();
                            // Only navigate if login is successful
                            Navigation.findNavController(findViewById(R.id.fragmentContainerView2))
                                    .navigate(R.id.action_lobbyFragment_to_cartFragment);
                        } else {
                            String errorMessage = task.getException() != null ? 
                                task.getException().getMessage() : "Login failed";
                            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void register() {
        String username = ((EditText)findViewById(R.id.editTextEmailAddressReg)).getText().toString();
        String password = ((EditText)findViewById(R.id.editTextPasswordReg)).getText().toString();
        String passwordConf = ((EditText)findViewById(R.id.editTextPasswordConfirmationReg)).getText().toString();
        String phone = ((EditText)findViewById(R.id.editTextPhoneReg)).getText().toString();

        // Check if passwords match
        if (!password.equals(passwordConf)) {
            Toast.makeText(MainActivity.this, "Passwords don't match!", Toast.LENGTH_LONG).show();
            return;
        }

        // Convert username to email format for Firebase Auth
        String email = username + "@yourdomain.com";

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Call addData and handle navigation based on its success/failure
                            addData()
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(MainActivity.this, "Registration successful!", Toast.LENGTH_LONG).show();
                                    Navigation.findNavController(findViewById(R.id.fragmentContainerView2))
                                            .navigate(R.id.action_registerFragment_to_lobbyFragment);
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(MainActivity.this, "Failed to save user data: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    // If database save fails, delete the auth user
                                    if (mAuth.getCurrentUser() != null) {
                                        mAuth.getCurrentUser().delete();
                                    }
                                });
                        } else {
                            String errorMessage = task.getException() != null ? 
                                task.getException().getMessage() : "Registration failed";
                            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public Task<Void> addData() {
        String username = ((EditText) findViewById(R.id.editTextEmailAddressReg)).getText().toString();
        String phone = ((EditText) findViewById(R.id.editTextPhoneReg)).getText().toString();

        User user = new User(username, "", phone); // Not storing password in Realtime DB for security
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users").child(username);
        return myRef.setValue(user);
    }
}