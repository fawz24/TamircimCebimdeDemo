package com.tamirdunyasi.tamircimcebimdedemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth dbAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
         currentUser = dbAuth.getCurrentUser();

//        Intent intent = new Intent(this, HomeActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("userEmail", currentUser.getEmail());
//        bundle.putString("userName", currentUser.getDisplayName());
//        intent.putExtras(bundle);
//        startActivity(intent);
        if (currentUser != null){
//            goToHome(currentUser);
            Helpers.goToHome(MainActivity.this, currentUser);
        }
    }

    public void loginListener(View v){
//        Intent intent = new Intent(this, LoginActivity.class);
//        startActivity(intent);
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);

        login(emailEditText.getText().toString(), Helpers.hashPassword(passwordEditText.getText().toString()));
    }

    public void login(String email, String password){
        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(MainActivity.this, "Lütfen Alanları Doldurun!",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        dbAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = dbAuth.getCurrentUser();
//                            goToHome(user);
                            Helpers.goToHome(MainActivity.this, user);
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Giriş Hatalıdır.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }

    public void goToRegister(View view){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

//    private void goToHome(FirebaseUser currentUser){
//        Intent intent = new Intent(this, HomeActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("userEmail", currentUser.getEmail());
//        bundle.putString("userName", currentUser.getDisplayName());
//        intent.putExtras(bundle);
//        startActivity(intent);
//    }

//    public void goToHome(View view){
//        Intent intent = new Intent(this, HomeActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("user", "Fawzy");
//        intent.putExtras(bundle);
//        startActivity(intent);
//    }


}
