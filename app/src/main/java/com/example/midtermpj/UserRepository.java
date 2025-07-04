package com.example.midtermpj;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserRepository {
    private static UserRepository instance;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private User currentUser;

    public interface OnUserLoadListener {
        void onUserLoaded();
        void onUserLoadFailed(Exception e);
    }

    private UserRepository() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public static synchronized UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public User getCurrentUser() {
        return this.currentUser;
    }


    public void loadInitialUser(final OnUserLoadListener listener) {
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser == null) {
            listener.onUserLoadFailed(new Exception("No user logged in."));
            return;
        }

        String uid = firebaseUser.getUid();
        db.collection("users").document(uid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            // Success! Store the user object in our instance variable.
                            this.currentUser = document.toObject(User.class);
                            listener.onUserLoaded(); // Notify the listener that we are done.
                        } else {
                            listener.onUserLoadFailed(new Exception("User data not found in database."));
                        }
                    } else {
                        listener.onUserLoadFailed(task.getException());
                    }
                });
    }

    public void clearCurrentUser() {
        this.currentUser = null;
    }
}