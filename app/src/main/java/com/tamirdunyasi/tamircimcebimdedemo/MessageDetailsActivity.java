package com.tamirdunyasi.tamircimcebimdedemo;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageDetailsActivity extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private FirebaseAuth dbAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    private String mMessageId;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mMessageId = bundle.getString("messageId");

        if (currentUser != null){
            db.collection("users").document(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String id = document.getId();
                            String address = document.get("address").toString();
                            String email = document.get("email").toString();
                            String name = document.get("name").toString();
                            String password = document.get("password").toString();
                            String phone = document.get("phone").toString();
                            String type = document.get("type").toString();

                            mUser = new User(id, address, email, name, password, phone, type);

                            fillData();
                        } else {
                        }
                    } else {
                    }
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void sendMessageListener(View v){
        saveMessage(mMessageId);
    }

    public void saveMessage(String messageId){
        final EditText messageEdit = findViewById(R.id.newMessageContent);
        final String content = messageEdit.getText().toString();
        final LinearLayout messageContainer = findViewById(R.id.messageContainer);

        if (content.isEmpty()){
            return;
        }

        final FloatingActionButton sendButton = findViewById(R.id.sendButton);
        sendButton.setClickable(false);

        // Add a new document with a generated id.
        Map<String, Object> data = new HashMap<>();
        data.put("body", content);
        data.put("date", new Date());
        data.put("senderid", mUser.id);

        db.collection("message").document(messageId).collection("contents")
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(getBaseContext(), "Mesaj kaydedildi!", Toast.LENGTH_LONG).show();

//                          Create text view for content
                        TextView ctnt = new TextView(getBaseContext());
                        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0,  Helpers.dpToPx(getBaseContext(),5), 0, Helpers.dpToPx(getBaseContext(), 5));
                        ctnt.setLayoutParams(params);
                        ctnt.setTextSize(Helpers.dpToPx(getBaseContext(), 7));
                        ctnt.setPadding(0, 0, Helpers.dpToPx(getBaseContext(), 60), 0);
                        ctnt.setText(content);
//                          Create a divider
                        View div = new View(getBaseContext());
                        div.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Helpers.dpToPx(getBaseContext(), 1)));
                        div.setBackgroundColor(getResources().getColor(R.color.colorLightGray));

//                          Attach the new content and divider to the contents linear layout
                        messageContainer.addView(div, 0);
                        messageContainer.addView(ctnt, 0);

                        messageEdit.setText("");
                        sendButton.setClickable(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getBaseContext(), "Mesaj kaydedilemedi!", Toast.LENGTH_LONG).show();

                        sendButton.setClickable(true);
                    }
                });
    }

    public void fillData(){
        String user = "companyname";
        if (mUser.type.equals("co")){
            user = "clientname";
        }
        final String peerText = user;

        final TextView peer = findViewById(R.id.peer);
        final TextView title = findViewById(R.id.title);

        db.collection("message").document(mMessageId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();

                    peer.setText(doc.get(peerText).toString());
                    title.setText(doc.get("requesttitle").toString());
                }
            }
        });

        db.collection("message").document(mMessageId).collection("contents")
                .orderBy("date", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot ctnt : task.getResult()) {
//                          Add message content
                        addMessageContent(ctnt);
                    }
                }
            }
        });
    }

    private void addMessageContent(/*Context context, LinearLayout contents, */QueryDocumentSnapshot ctnt){
        LinearLayout messageContainer = findViewById(R.id.messageContainer);
//          Create text view for content
        TextView content = new TextView(this);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,  Helpers.dpToPx(this,5), 0, Helpers.dpToPx(this, 5));
        content.setLayoutParams(params);
        content.setTextSize(Helpers.dpToPx(this, 7));
        if (ctnt.get("senderid").toString().equals(mUser.id)){
            content.setPadding(0, 0, Helpers.dpToPx(this, 60), 0);
        }
        else {
            content.setPadding(Helpers.dpToPx(this, 60), 0, 0, 0);
        }
        content.setText(ctnt.get("body").toString());
//          Create a divider
        View div = new View(this);
        div.setLayoutParams(new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Helpers.dpToPx(this, 1)));
        div.setBackgroundColor(getResources().getColor(R.color.colorLightGray));

//          Attach the new content and divider to the contents linear layout
        messageContainer.addView(content);
        messageContainer.addView(div);
    }
}
