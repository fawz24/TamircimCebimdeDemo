package com.tamirdunyasi.tamircimcebimdedemo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.intellij.lang.annotations.RegExp;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.RegEx;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth dbAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;

    private FragmentsStatePagerAdapter mFragmentsPagerAdapter;
    private BottomNavigationView mBottomNavigationView;
    private Menu mBottomMenu;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
//        setTitle(R.string.main_activity_label);

        mFragmentsPagerAdapter = new FragmentsStatePagerAdapter(getSupportFragmentManager());
        mBottomNavigationView = findViewById(R.id.register_navigation_bottom);
        mBottomNavigationView.setOnNavigationItemSelectedListener(navigationListener);

        mBottomMenu = mBottomNavigationView.getMenu();

        mViewPager = findViewById(R.id.registerContainer);
        setUpViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(pageChangeListener);

        currentUser = null;
    }

    private void setUpViewPager(@NotNull ViewPager viewPager) {
        mFragmentsPagerAdapter.addFragment(new PersonalRegisterFragment());
        mFragmentsPagerAdapter.addFragment(new CompanyRegisterFragment());
        viewPager.setAdapter(mFragmentsPagerAdapter);
    }

    private  ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            setCheckBottomMenuItem(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    public void setCheckBottomMenuItem(int item){
        mBottomMenu.getItem(item).setChecked(true);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.nav_person:
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.nav_company:
                    mViewPager.setCurrentItem(1);
                    break;
            }
            return true;
        }
    };

    public void registerPersonListener(View v){
        EditText nameEdit = findViewById(R.id.registerName);
        EditText surnameEdit = findViewById(R.id.registerSurname);
        EditText emailEdit = findViewById(R.id.registerEmail);
        EditText passwordEdit = findViewById(R.id.registerPassword);
        EditText repeatPasswordEdit = findViewById(R.id.registerPasswordRepeat);
        EditText phoneEdit = findViewById(R.id.registerPhone);
        EditText addressEdit = findViewById(R.id.registerAddress);

        String name = nameEdit.getText().toString();
        String surname = surnameEdit.getText().toString();
        String email = emailEdit.getText().toString();
        String password = passwordEdit.getText().toString();
        String repeatPassword = repeatPasswordEdit.getText().toString();
        String phone = phoneEdit.getText().toString();
        String address = addressEdit.getText().toString();

        Matcher matcher = Pattern.compile("^[a-zA-Z0-1]+(\\.[a-zA-Z0-1]+)?@[a-zA-Z0-1]+\\.[a-zA-Z0-1]+$").matcher(email);

        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() ||
                repeatPassword.isEmpty()){

            Toast.makeText(RegisterActivity.this, "Lütfen Tüm Alanları Eksiksiz Doldurun!",
                    Toast.LENGTH_LONG).show();
            return;
        }
        else {
            password = Helpers.hashPassword(password);
            repeatPassword = Helpers.hashPassword(repeatPassword);

            if (!matcher.find()){
                Toast.makeText(RegisterActivity.this, "Lütfen Doğru Bir Email Adresi Giriniz!",
                        Toast.LENGTH_LONG).show();
                return;
            }
            else if (!password.equals(repeatPassword)){

                Toast.makeText(RegisterActivity.this, "Şifreler Uymuyor!",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }

        Client user = new Client(address, email, name, password, phone, surname, 0);
        createAccount(this, user, 0);
    }

    public void setCurrentUser(FirebaseUser currentUser) {
        this.currentUser = currentUser;
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    private void createAccount(final RegisterActivity context, final User user, final int type){
        dbAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");
                            final FirebaseUser loggedUser = dbAuth.getCurrentUser();
                            if (loggedUser != null){
                                context.setCurrentUser(loggedUser);
                            }

//                              Create a new user
                            Map<String, Object> userDoc = new HashMap<>();
                            userDoc.put("address", user.getAddress());
                            userDoc.put("email", user.getEmail());
                            userDoc.put("name", user.getName());
                            userDoc.put("password", user.getPassword());
                            userDoc.put("phone", user.getPhone());
                            userDoc.put("type", type);

                            db.collection("users").document(context.getCurrentUser().getUid())
                                    .set(userDoc).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

//                                      Create a new client
                                    Map<String, Object> clientDoc = new HashMap<>();
                                    if (type == 0){
                                        clientDoc.put("surname", ((Client)user).getSurname());

                                        db.collection("client").document(context.getCurrentUser().getUid())
                                                .set(clientDoc).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }
                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                        db.collection("users").document(loggedUser.getUid())
                                                                .delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                    }
                                                                });
                                                        loggedUser.delete()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                });
                                    }
                                    else if (type == 1){
//                                        Create a new company document
                                        Map<String, Object> companyDoc = new HashMap<>();
//                                        companyDoc.put("category", ((Company)user).getCategory());
                                        companyDoc.put("point", 0);
                                        companyDoc.put("registrationno", ((Company)user).getRegistrationNo());
                                        companyDoc.put("taxoffice", ((Company)user).getTaxOffice());
                                        companyDoc.put("taxidno", ((Company)user).getTaxIdNo());
                                        companyDoc.put("title", ((Company)user).getTitle());

                                        db.collection("company").document(context.getCurrentUser().getUid())
                                                .set(companyDoc).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                            }
                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {

                                                        db.collection("users").document(loggedUser.getUid())
                                                                .delete()
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                    }
                                                                })
                                                                .addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                    }
                                                                });
                                                        loggedUser.delete()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                        }
                                                                    }
                                                                });
                                                    }
                                                });
                                    }
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            loggedUser.delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                            }
                                                        }
                                                    });

                                        }
                                    });

                            Helpers.goToHome(RegisterActivity.this, RegisterActivity.this.getCurrentUser());
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Lütfen Tüm Alanları Eksiksiz Doldurun!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

