package com.tamirdunyasi.tamircimcebimdedemo;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostDetailsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private Spinner mCategory;
    private ArrayAdapter<CharSequence> categoriesAdapter;
    private Post mPost;
    private int mSelectedVote;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String postId;

        mPost = new Post();
        mPost.setState("Yeni");

        mSelectedVote = -1;

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        db.collection("users").document(currentUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot document) {
                if (document.exists()){
                    mUser = new User(document.getId(), document.get("address").toString(), document.get("email").toString(),
                            document.get("name").toString(), document.get("password").toString(), document.get("phone").toString(),
                            document.get("type").toString());
                }
            }
        });

        try {
            postId = bundle.getString("postId");
            if (!postId.isEmpty()){
                final PostDetailsActivity context = this;
                db.collection("request").document(postId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()){
                                long point;
                                try {
                                    point = document.getLong("point");
                                } catch (Exception e){
                                    point = 0;
                                }
                                mPost = new Post(document.getId(), document.getString("category"), document.getString("clientid"),
                                        document.getString("clientname"), document.getString("companyid"), document.getString("companyname"),
                                        document.getString("content"), document.getDate("date"), point,
                                        document.getString("state"), document.getString("title"));

                                TextView state = context.findViewById(R.id.status);
                                EditText title = context.findViewById(R.id.postTitle);
                                EditText content = context.findViewById(R.id.postContent);
                                Spinner category = context.findViewById(R.id.category);
                                EditText companyName = context.findViewById(R.id.companyName);
                                RadioGroup voteGroup = context.findViewById(R.id.voteGroup);
                                TextView pointText = context.findViewById(R.id.pointText);
                                LinearLayout pointStars = context.findViewById(R.id.pointStars);
                                ImageView pointStar1 = context.findViewById(R.id.pointStar1);
                                ImageView pointStar2 = context.findViewById(R.id.pointStar2);
                                RadioButton radioButtonDislike = context.findViewById(R.id.dislike);
                                RadioButton radioButtonNotSure = context.findViewById(R.id.notSure);
                                RadioButton radioButtonLike = context.findViewById(R.id.like);

                                title.setText(mPost.getTitle());
                                content.setText(mPost.getContent());

                                switch (mPost.getState()){
                                    case "Açık":
                                        state.setText("Açık");
                                        category.setSelection(Helpers.getSelectedCategory(context, mPost.getCategory()));
                                        break;
                                    case "Beklemede":
                                        state.setText("Beklemede");
                                        companyName.setText(mPost.getCompanyName());
//                                        companyName.setEnabled(false);
                                        companyName.setKeyListener(null);
//                                        title.setEnabled(false);
                                        title.setKeyListener(null);
//                                        content.setEnabled(false);
                                        content.setKeyListener(null);
                                        category.setEnabled(false);
                                        category.setSelection(Helpers.getSelectedCategory(context, mPost.getCategory()));
                                        voteGroup.setVisibility(View.VISIBLE);
                                        break;
                                    case "Kapalı":
                                        state.setText("Kapalı");
//                                        companyName.setEnabled(false);
                                        companyName.setText(mPost.getCompanyName());
                                        companyName.setKeyListener(null);
//                                        title.setEnabled(false);
                                        title.setKeyListener(null);
//                                        content.setEnabled(false);
                                        content.setKeyListener(null);
                                        category.setEnabled(false);
                                        category.setSelection(Helpers.getSelectedCategory(context, mPost.getCategory()));
                                        voteGroup.setVisibility(View.VISIBLE);
                                        pointText.setText("Onayladığınız puan");
                                        pointStars.setVisibility(View.VISIBLE);
                                        radioButtonDislike.setEnabled(false);
                                        radioButtonNotSure.setEnabled(false);
                                        radioButtonLike.setEnabled(false);

                                        Button saveButton = findViewById(R.id.saveButton);
                                        saveButton.setVisibility(View.GONE);

                                        if (mPost.getPoint() == 0){
                                            radioButtonDislike.setChecked(true);
                                        } else {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                                                pointStar1.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_second_24dp,
                                                        context.getApplicationContext().getTheme()));
                                            } else {
                                                pointStar1.setImageResource(R.drawable.ic_star_second_24dp);
                                            }
//                                            pointStar1.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_second_24dp));
                                            if (mPost.getPoint() == 1){
                                                radioButtonNotSure.setChecked(true);
                                            } if (mPost.getPoint() == 2){
                                                radioButtonLike.setChecked(true);
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                                                    pointStar2.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_second_24dp,
                                                            context.getApplicationContext().getTheme()));
                                                }
                                                else {
                                                    pointStar2.setImageResource(R.drawable.ic_star_second_24dp);
                                                }
//                                                pointStar2.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_star_second_24dp));
//                                                pointStar2.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_star_second_24dp));
                                            }
                                        }
                                        break;
                                }
                            }
                        }
                    }
                });
            }
        } catch (Exception e){
        }

        mCategory = findViewById(R.id.category);
        categoriesAdapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
//          Specify the layout to use when the list of choices appears
        categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//          Apply the adapter to the spinner
        mCategory.setAdapter(categoriesAdapter);
        mCategory.setOnItemSelectedListener(this);
    }

    public void saveListener(View v){
        String status = ((TextView)findViewById(R.id.status)).getText().toString();
        String title = ((EditText)findViewById(R.id.postTitle)).getText().toString();
        String content = ((EditText)findViewById(R.id.postContent)).getText().toString();
        Spinner category = findViewById(R.id.category);
        Button saveButton = findViewById(R.id.saveButton);
        final String company = ((EditText)findViewById(R.id.companyName)).getText().toString();
//        RadioGroup vote = findViewById(R.id.voteGroup);
        RadioButton dislike = findViewById(R.id.dislike);
        RadioButton notSure = findViewById(R.id.notSure);
        RadioButton like = findViewById(R.id.like);

        saveButton.setEnabled(false);

        final Map<String, Object> value = new HashMap<>();
        value.put("clientid", mUser.getId());
        value.put("clientname", mUser.getName());

        String prevStatus = "Kapalı";

        if (status.equals("Kapalı")){
            saveButton.setEnabled(true);
            return;
        }
        if (status.equals("Beklemede")){
            if (dislike.isChecked()){
                mSelectedVote = 0;
            } else if (notSure.isChecked()){
                mSelectedVote = 1;
            } else if (like.isChecked()){
                mSelectedVote = 2;
            } else if (mSelectedVote < 0 || mSelectedVote > 2){
                mSelectedVote = -1;
                saveButton.setEnabled(true);
                return;
            }

            value.put("point", mSelectedVote);
            value.put("state", "Kapalı");
            mPost.setPoint(mSelectedVote);
            prevStatus = "Beklemede";
        } else if (status.equals("Açık") || status.equals("Yeni")){
            if (status.equals("Yeni")){
                value.put("date", new Date());
                status = "Açık";
                prevStatus = "Yeni";
            }else {
                prevStatus = "Açık";
            }

            if (title.isEmpty()){
                Toast.makeText(this, "Lütfen başlık yazınız!", Toast.LENGTH_LONG).show();
                saveButton.setEnabled(true);
                return;
            }
            if (content.isEmpty()){
                Toast.makeText(this, "Lütfen arızayı tanıtınız!", Toast.LENGTH_LONG).show();
                saveButton.setEnabled(true);
                return;
            }
            if (category.getSelectedItemPosition() == 0){
                Toast.makeText(this, "Lütfen kategori seçiniz!", Toast.LENGTH_LONG).show();
                saveButton.setEnabled(true);
                return;
            }
            value.put("category", categoriesAdapter.getItem(category.getSelectedItemPosition()).toString());
            value.put("title", title);
            value.put("content", content);
//            db.collection("request").document(mPost.id).set(value).
            if (!company.isEmpty()){
//                status = "Beklemede";
                final String previousStatus = prevStatus;
                db.collection("users").whereEqualTo("name", company).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots != null && (!queryDocumentSnapshots.isEmpty() || queryDocumentSnapshots.size() > 0)){
                            for (final DocumentSnapshot document: queryDocumentSnapshots){
                                db.collection("company").document(document.getId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        value.put("state", "Beklemede");
                                        value.put("companyid", document.getId());
                                        value.put("companyname", company);
                                        savePost(value, previousStatus);
                                    }
                                });
                            }
                        }else {
                            Toast.makeText(getBaseContext(), "Şirket bulunamadı!", Toast.LENGTH_LONG).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getBaseContext(), "Şirket bulunamadı!", Toast.LENGTH_LONG).show();
                    }
                });
                saveButton.setEnabled(true);
                return;
            } else {
                value.put("state", status);
            }
        }
        final String previousStatus = prevStatus;
        savePost(value, previousStatus);
    }

    public void savePost(final Map<String, Object> data, final String previousStatus){
        final Context context = this;
        findViewById(R.id.saveButton).setEnabled(true);
        if (mPost.getId().isEmpty()){
            db.collection("request").add(data).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(final DocumentReference document) {
//                    Intent intent = new Intent(context, PostDetailsActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("postId", documentReference.getId());
//                    intent.putExtras(bundle);
                    document.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(final DocumentSnapshot post) {
                            if (previousStatus.equals("Yeni") && data.containsKey("category")){
                                db.collection("company").whereArrayContains("category", data.get("category"))
                                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (DocumentSnapshot company: queryDocumentSnapshots){
                                            final Map<String, Object> message = new HashMap<>();
                                            message.put("clientid", mUser.getId());
                                            message.put("clientname", mUser.getName());
                                            message.put("companyid", company.getId());
                                            message.put("companyname", company.get("name"));
                                            message.put("requestid", post.getId());
                                            message.put("requesttitle", post.get("title"));
                                            db.collection("message").add(message).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference messageReference) {
                                                    messageReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot messageSnapshot) {
                                                            if (messageSnapshot.exists()){
                                                                Map<String, Object> content = new HashMap<>();
                                                                content.put("body", "Yeni bir talepte bulunmaktayım.");
                                                                content.put("date", new Date());
                                                                content.put("senderid", mUser.getId());
                                                                db.collection("message").document(messageSnapshot.getId())
                                                                        .collection("contents").add(content).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference) {

                                                                    }
                                                                });
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                        closePost();
                                    }
                                });
                            }
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getBaseContext(), "Kaydedilemedi!", Toast.LENGTH_LONG).show();
                }
            });
        }
        else {
            db.collection("request").document(mPost.id).update(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    closePost();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getBaseContext(), "Kaydedilemedi!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void closePost(){
        Toast.makeText(this, "Başarı ile kaydedildi!", Toast.LENGTH_LONG).show();
//        if (intent != null){
//            finish();
//            startActivity(intent);
//            return;
//        }
//        recreate();
        getParent().recreate();
        finish();
    }


    public void voteListener(View v){
        switch (v.getId()){
            case R.id.dislike:
                mSelectedVote = 0;
                break;
            case R.id.notSure:
                mSelectedVote = 1;
                break;
            case R.id.like:
                mSelectedVote = 2;
                break;
            default:
                mSelectedVote = -1;
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(this, "Selected category: " + parent.getItemAtPosition(position), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
