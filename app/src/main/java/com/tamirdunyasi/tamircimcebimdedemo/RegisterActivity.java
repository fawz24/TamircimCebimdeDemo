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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.RegEx;

public class RegisterActivity extends AppCompatActivity implements MultiSelectionSpinner.OnMultipleItemsSelectedListener {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth dbAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;

    private FragmentsStatePagerAdapter mFragmentsPagerAdapter;
    private BottomNavigationView mBottomNavigationView;
    private Menu mBottomMenu;
    private ViewPager mViewPager;
    private MultiSelectionSpinner mMultiSelectionSpinner;

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

    public MultiSelectionSpinner getmMultiSelectionSpinner() {
        return mMultiSelectionSpinner;
    }

    public void setmMultiSelectionSpinner(MultiSelectionSpinner mMultiSelectionSpinner) {
        this.mMultiSelectionSpinner = mMultiSelectionSpinner;
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }

    @Override
    public void selectedStrings(List<String> strings) {
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

    public void registerListener(View v){
        String address = "";
        String email = "";
        String name = "";
        String password = "";
        String repeatPassword = "";
        String phone = "";

        EditText addressEdit = findViewById(R.id.registerAddress);
        EditText addressCompanyEdit = findViewById(R.id.registerAddressCompany);
        EditText emailEdit = findViewById(R.id.registerEmail);
        EditText emailCompanyEdit = findViewById(R.id.registerEmailCompany);
        EditText nameEdit = findViewById(R.id.registerName);
        EditText nameCompanyEdit = findViewById(R.id.registerNameCompany);
        EditText passwordEdit = findViewById(R.id.registerPassword);
        EditText passwordCompanyEdit = findViewById(R.id.registerPasswordCompany);
        EditText repeatPasswordEdit = findViewById(R.id.registerPasswordRepeat);
        EditText repeatPasswordCompanyEdit = findViewById(R.id.registerPasswordRepeatCompany);
        EditText phoneEdit = findViewById(R.id.registerPhone);
        EditText phoneCompanyEdit = findViewById(R.id.registerPhoneCompany);

        switch (v.getId()){
            case R.id.registerButtonPersonal:
                address = addressEdit.getText().toString();
                email = emailEdit.getText().toString();
                name = nameEdit.getText().toString();
                password = passwordEdit.getText().toString();
                repeatPassword = repeatPasswordEdit.getText().toString();
                phone = phoneEdit.getText().toString();
                break;
            case R.id.registerButtonCompany:
                address = addressCompanyEdit.getText().toString();
                email = emailCompanyEdit.getText().toString();
                name = nameCompanyEdit.getText().toString();
                password = passwordCompanyEdit.getText().toString();
                repeatPassword = repeatPasswordCompanyEdit.getText().toString();
                phone = phoneCompanyEdit.getText().toString();
                break;
        }

        Matcher matcher = Pattern.compile("^[a-zA-Z0-1]+(\\.[a-zA-Z0-1]+)?@[a-zA-Z0-1]+\\.[a-zA-Z0-1]+$").matcher(email);

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() ||
                repeatPassword.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Lütfen Tüm Zorunlu Alanları Eksiksiz Doldurun! 111",
                    Toast.LENGTH_LONG).show();
            return;
        }
        else {
            password = Helpers.hashPassword(password);
            repeatPassword = Helpers.hashPassword(repeatPassword);

            if (!matcher.find()){
                Toast.makeText(RegisterActivity.this, "Lütfen Doğru Bir Email Adresi Giriniz! 222",
                        Toast.LENGTH_LONG).show();
                return;
            }
            else if (!password.equals(repeatPassword)){

                Toast.makeText(RegisterActivity.this, "Şifreler Uymuyor!",
                        Toast.LENGTH_LONG).show();
                return;
            }
        }

        User user = null;

        switch (v.getId()){
            case R.id.registerButtonPersonal:
                EditText surnameEdit = findViewById(R.id.registerSurname);
                String surname = surnameEdit.getText().toString();
                if (surname.isEmpty()){
                    Toast.makeText(RegisterActivity.this, "Lütfen Tüm Zorunlu Alanları Eksiksiz Doldurun! 333",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                user = new Client(address, email, name, password, phone, surname, "cl");
                break;
            case R.id.registerButtonCompany:
                EditText titleEdit = findViewById(R.id.registerTitleCompany);
                EditText registrationNoEdit = findViewById(R.id.registerSicilNoCompany);
                EditText vergiDairesiEdit = findViewById(R.id.registerVergiDairesiCompany);
                EditText vergiDairesiNoEdit = findViewById(R.id.registerVergiDairesiNoCompany);

                String title = titleEdit.getText().toString();
                String registrationNo = registrationNoEdit.getText().toString();
                String taxOffice = vergiDairesiEdit.getText().toString();
                String taxIdNo = vergiDairesiNoEdit.getText().toString();

                if (title.isEmpty() || registrationNo.isEmpty() || taxIdNo.isEmpty() || taxOffice.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Lütfen Tüm Zorunlu Alanları Eksiksiz Doldurun! 444",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                List<String> categories = mMultiSelectionSpinner.getSelectedStrings();
                if (categories == null || categories.size() <= 0){
                    Toast.makeText(this, "Lütfen en az bir kategori seçınız!", Toast.LENGTH_LONG).show();
                    return;
                }
                user = new Company(address, email, name, password, phone, "co", categories, registrationNo, taxIdNo, taxOffice, title);
                break;
        }
        createAccount(user, user.getType());
    }

    public void setCurrentUser(FirebaseUser currentUser) {
        this.currentUser = currentUser;
    }

    public FirebaseUser getCurrentUser() {
        return currentUser;
    }

    private void createAccount(final User user, final String type){
        final RegisterActivity context = this;
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

                                    if (type.equals("cl")){
//                                      Create a new client
                                        Map<String, Object> clientDoc = new HashMap<>();
                                        clientDoc.put("surname", ((Client)user).getSurname());
                                        clientDoc.put("name", user.getName());

                                        db.collection("client").document(context.getCurrentUser().getUid())
                                                .set(clientDoc).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Helpers.goToHome(RegisterActivity.this, RegisterActivity.this.getCurrentUser());
                                            }
                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(context, "Kayıt esnasında bir hata oluştu!", Toast.LENGTH_LONG).show();
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
                                    else if (type.equals("co")){
//                                        Create a new company document
                                        Map<String, Object> companyDoc = new HashMap<>();
                                        companyDoc.put("category", ((Company)user).getCategory());
                                        companyDoc.put("point", 0);
                                        companyDoc.put("registrationno", ((Company)user).getRegistrationNo());
                                        companyDoc.put("taxoffice", ((Company)user).getTaxOffice());
                                        companyDoc.put("taxidno", ((Company)user).getTaxIdNo());
                                        companyDoc.put("title", ((Company)user).getTitle());
                                        companyDoc.put("name", user.getName());

                                        db.collection("company").document(context.getCurrentUser().getUid())
                                                .set(companyDoc).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Helpers.goToHome(RegisterActivity.this, RegisterActivity.this.getCurrentUser());
                                            }
                                        })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(context, "Kayıt esnasında bir hata oluştu!", Toast.LENGTH_LONG).show();
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
                                            Toast.makeText(context, "Kayıt esnasında bir hata oluştu!", Toast.LENGTH_LONG).show();
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
                        } else {
                            Toast.makeText(RegisterActivity.this, "Lütfen Tüm Alanları Eksiksiz Doldurun!",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

