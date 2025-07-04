package com.example.midtermpj;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.function.Consumer;

public class ProfileActivity extends AppCompatActivity {
    private User currentUser;
    private FirebaseFirestore db;
    private String currentUserId;
    private TextView nameValue, phoneValue, emailValue, addressValue;

    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        
        currentUser = UserRepository.getInstance().getCurrentUser();

        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            UserRepository.getInstance().clearCurrentUser();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);

            finish();
        });

        setupProfileSections();

        View backButton = findViewById(R.id.backProfileButton);
        backButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void setupProfileSections() {
        View nameSection = findViewById(R.id.profile_name_section);
        ImageView nameIcon = nameSection.findViewById(R.id.profile_item_icon);
        TextView nameLabel = nameSection.findViewById(R.id.profile_item_label);

        nameIcon.setImageResource(R.drawable.name_icon);
        nameLabel.setText("Full name");
        nameValue = nameSection.findViewById(R.id.profile_item_value);
        nameValue.setText(currentUser.getUserName());
        nameSection.findViewById(R.id.profile_item_edit_button).setOnClickListener(v -> {
            showEditDialog("Edit Full Name", currentUser.getUserName(), InputType.TYPE_CLASS_TEXT, newName -> {
                updateFieldInFirestore("username", newName, () -> {
                    currentUser.setUserName(newName);
                    nameValue.setText(newName);
                });
            });        });

        View phoneSection = findViewById(R.id.profile_phone_section);
        ImageView phoneIcon = phoneSection.findViewById(R.id.profile_item_icon);
        TextView phoneLabel = phoneSection.findViewById(R.id.profile_item_label);

        phoneIcon.setImageResource(R.drawable.phone_icon);
        phoneLabel.setText("Phone number");
        phoneValue = phoneSection.findViewById(R.id.profile_item_value);
        phoneValue.setText(currentUser.getPhone());
        phoneSection.findViewById(R.id.profile_item_edit_button).setOnClickListener(v -> {
            showEditDialog("Edit Phone Number", currentUser.getPhone(), InputType.TYPE_CLASS_PHONE, newPhone -> {
                updateFieldInFirestore("phone", newPhone, () -> {
                    currentUser.setPhone(newPhone);
                    phoneValue.setText(newPhone);
                });
            });
        });

        View emailSection = findViewById(R.id.profile_email_section);
        ImageView emailIcon = emailSection.findViewById(R.id.profile_item_icon);
        TextView emailLabel = emailSection.findViewById(R.id.profile_item_label);

        emailIcon.setImageResource(R.drawable.mail_icon);
        emailLabel.setText("Email");
        emailValue = emailSection.findViewById(R.id.profile_item_value);
        emailValue.setText(currentUser.getEmail());
        emailSection.findViewById(R.id.profile_item_edit_button).setOnClickListener(v -> {
            Toast.makeText(ProfileActivity.this, "Sorry, you should not change email!", Toast.LENGTH_SHORT).show();
        });

        View addressSection = findViewById(R.id.profile_address_section);
        ImageView addressIcon = addressSection.findViewById(R.id.profile_item_icon);
        TextView addressLabel = addressSection.findViewById(R.id.profile_item_label);

        addressIcon.setImageResource(R.drawable.address_icon);
        addressLabel.setText("Address");
        addressValue = addressSection.findViewById(R.id.profile_item_value);
        addressValue.setText(currentUser.getAddress());
        addressSection.findViewById(R.id.profile_item_edit_button).setOnClickListener(v -> {
            showEditDialog("Edit Address", currentUser.getAddress(), InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE, newAddress -> {
                updateFieldInFirestore("address", newAddress, () -> {
                    currentUser.setAddress(newAddress);
                    addressValue.setText(newAddress);
                });
            });
        });
    }

    private void updateFieldInFirestore(String field, String value, Runnable onSuccessAction) {
        db.collection("users").document(currentUserId)
                .update(field, value)
                .addOnSuccessListener(aVoid -> {
                    onSuccessAction.run();
                    Toast.makeText(ProfileActivity.this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ProfileActivity.this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }


    private void showEditDialog(String title, String currentValue, int inputType, Consumer<String> onSave) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);

        final EditText input = new EditText(this);
        input.setInputType(inputType);
        input.setText(currentValue);
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newValue = input.getText().toString();
            onSave.accept(newValue);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}