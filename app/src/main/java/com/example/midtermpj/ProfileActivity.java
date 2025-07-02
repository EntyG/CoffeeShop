package com.example.midtermpj;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.function.Consumer;

public class ProfileActivity extends AppCompatActivity {
    private User currentUser;
    private TextView nameValue, phoneValue, emailValue, addressValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        currentUser = UserRepository.getInstance().getCurrentUser();

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
        nameValue.setText(currentUser.getName());
        nameSection.findViewById(R.id.profile_item_edit_button).setOnClickListener(v -> {
            showEditDialog("Edit Full Name", currentUser.getName(), InputType.TYPE_CLASS_TEXT, newName -> {
                currentUser.setName(newName);
                nameValue.setText(newName);
            });
        });

        View phoneSection = findViewById(R.id.profile_phone_section);
        ImageView phoneIcon = phoneSection.findViewById(R.id.profile_item_icon);
        TextView phoneLabel = phoneSection.findViewById(R.id.profile_item_label);

        phoneIcon.setImageResource(R.drawable.phone_icon);
        phoneLabel.setText("Phone number");
        phoneValue = phoneSection.findViewById(R.id.profile_item_value);
        phoneValue.setText(currentUser.getPhone());
        phoneSection.findViewById(R.id.profile_item_edit_button).setOnClickListener(v -> {
            showEditDialog("Edit Phone Number", currentUser.getPhone(), InputType.TYPE_CLASS_PHONE, newPhone -> {
                currentUser.setPhone(newPhone);
                phoneValue.setText(newPhone);
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
            showEditDialog("Edit Email", currentUser.getEmail(), InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS, newEmail -> {
                currentUser.setEmail(newEmail);
                emailValue.setText(newEmail);
            });
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
                currentUser.setAddress(newAddress);
                addressValue.setText(newAddress);
            });
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