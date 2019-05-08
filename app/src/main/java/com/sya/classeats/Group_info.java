package com.sya.classeats;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Group_info extends AppCompatActivity {

    public static int localA = 0;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onStart() {
        super.onStart();
        retrieveData();
    }

    public void retrieveData() {
        DocumentReference ref = db.collection("counter").document(getIntent().getStringExtra("groupname"));
        DocumentReference votedRef = db.collection("counter").document(getIntent().getStringExtra("groupname"));
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        localA = Integer.parseInt(document.get("eat").toString());
                    } else {
                        Log.d("@@@", "Document does not exist");
                    }
                }

            }
        });
        votedRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                if (task.isSuccessful()) {
//                    voted = document.getBoolean("voted");
                    //   Log.d("@@@@@@@", "voted: " + voted);
                } else {
                    Log.d("@@@@@", "Document not found, Exception: " + task.getException());
                }
            }
        });
    }


    public void updateData() {
        DocumentReference ref = db.collection("counter").document(getIntent().getStringExtra("groupname"));
        DocumentReference groupRef = db.collection("da").document(getIntent().getStringExtra("id")).collection("dada").document(getIntent().getStringExtra("groupname"));


        groupRef.update("voted", true).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("@@@", "DocumentSnapshot successfully updated!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("@@@", "Error updating document", e);
                    }
                });

        ref.update("eat", localA + 1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("@@@", "DocumentSnapshot successfully updated!");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("@@@", "Error updating document", e);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);
//        Log.d("@@@@@", "wannabe: " + Integer.parseInt(getIntent().getStringExtra("eat")));
        TextView gn = findViewById(R.id.groupName);
        final TextView join = findViewById(R.id.doJoin);
        final TextView amount = findViewById(R.id.amountEating);
        amount.setText(localA + " Wants to eat");
        final Button yeB = findViewById(R.id.yesB);
        final Button yeN = findViewById(R.id.noB);
        if (!getIntent().getBooleanExtra("voted", true)) {
            yeN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    yeB.setEnabled(false);
                    yeN.setEnabled(false);
                    join.setText("Come back Tomorrow!");
                }
            });
            yeB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    yeB.setEnabled(false);
                    yeN.setEnabled(false);
                    join.setText("Updated");
                    updateData();
                    amount.setText(++localA + " Wants to eat");
                }
            });
        } else {
            yeB.setEnabled(false);
            yeN.setEnabled(false);
            join.setText("Already voted");
        }

        gn.setText("#" + getIntent().getStringExtra("groupname"));
    }

    public int setLocalA(int a) {
        return localA = a;
    }
}
