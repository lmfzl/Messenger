package com.example.messenger;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationViewModel extends ViewModel {
    private FirebaseAuth auth;
    MutableLiveData<FirebaseUser> user = new MutableLiveData<>();
    MutableLiveData<String> error = new MutableLiveData<>();
    FirebaseDatabase database;
    DatabaseReference usersReference;

    public RegistrationViewModel() {
        auth= FirebaseAuth.getInstance();
        auth.addAuthStateListener(firebaseAuth ->{
            if (firebaseAuth.getCurrentUser()!=null){
                user.setValue(firebaseAuth.getCurrentUser());
            }
        });
        database = FirebaseDatabase.getInstance();
        usersReference = database.getReference("Users");
    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    public LiveData<String> getError() {
        return error;
    }

    public void registerNewUser(
            String email,
            String password,
            String name,
            String lastName,
            int age){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnFailureListener( e -> {
                    error.setValue(e.getMessage());
                }).addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = authResult.getUser();
                    if (firebaseUser == null){
                        return;
                    }
                    User user=new User(firebaseUser.getUid(), name,lastName,age, false);
                    usersReference.child(user.getId()).setValue(user);
                        });

    }
}
