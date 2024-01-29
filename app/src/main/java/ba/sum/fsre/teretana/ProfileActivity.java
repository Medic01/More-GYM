package ba.sum.fsre.teretana;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {


    ImageButton imageButtonHome;
    private View logoutButton;
    private TextInputEditText editTextEmail;
    private View saveNow;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        imageButtonHome = findViewById(R.id.imageButtonHome);
        logoutButton = findViewById(R.id.btn_logout);
        editTextEmail = findViewById(R.id.editTextEmail);
        saveNow = findViewById(R.id.saveNow);

        // Display the current email when the profile activity is created
        displayCurrentEmail();


        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        imageButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the MainActivity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        saveNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save changes to the email
                saveChanges();
            }
        });
    }

    private void displayCurrentEmail() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String currentEmail = currentUser.getEmail();
            editTextEmail.setText(currentEmail);
        }
    }

    private void saveChanges() {
        String editedEmail = editTextEmail.getText().toString();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.updateEmail(editedEmail)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Successfully updated email
                                Toast.makeText(ProfileActivity.this, "Email updated successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                // Failed to update email
                                Toast.makeText(ProfileActivity.this, "Failed to update email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}