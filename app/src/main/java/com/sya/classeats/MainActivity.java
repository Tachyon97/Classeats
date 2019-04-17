package com.sya.classeats;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Group> groups = new ArrayList<>();
    private FirebaseAuth mAuth;
    ListView list;
    FirebaseFirestore database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.database);
        database = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser u) {
        if(u == null) {
            mAuth.signInAnonymously().addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                        Log.d("@@@", "Login successful" + user.getUid());
                    } else {
                        Log.d("@@@","Login failed " + task.getException());
                        updateUI(null);
                    }
                }
            });
        }
    }

    /*   private void loadData() {
        database.collection("group").document(user).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.exists()) {
                        ArrayList<String> list = (ArrayList<String>) doc.get("groups");
                        for(String g : list) {
                            Log.d("@@@@", g);
                        }
                    }
                }
            }
        });
    } */

    public void addGroup(View view) {

      //database.collection("group").document(user).get();
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        final GroupAdapter adapter = new GroupAdapter(this, groups);
        list = findViewById(R.id.database);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                groups.get(position);
            }
        });
        b.setTitle("Add group");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        b.setView(input);
        b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                groups.add(new Group(input.getText().toString()));

            }
        });
        b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        list.setAdapter(adapter);
        b.show();
    }
    public void removeGroup(View view) {
    }
    public void openGroup(View view) {

    }
}
