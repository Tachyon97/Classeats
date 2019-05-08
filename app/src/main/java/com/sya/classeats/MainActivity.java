package com.sya.classeats;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.icu.util.Calendar;
import android.os.Build;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    final SimpleDateFormat format = new SimpleDateFormat("dd-MM");
    private FirebaseAuth mAuth;
    ListView list;
    FirebaseFirestore database;
    int month = 0, day = 0;
    private ArrayList<Group> groups = new ArrayList();
    private String anonymousID = "watt :O";


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
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
        anonymousID = mAuth.getCurrentUser().getUid();
        if (!anonymousID.equals("watt :O")) {
            Map<String, Object> default_Structure = new HashMap<>();
            default_Structure.put("Connected", 0);
            default_Structure.put("Wannabe", 0);
            database.collection("da").document(anonymousID).set(default_Structure);

        }
        loadInitialData();
        // retrieveDate();
    }

    private void updateUI(FirebaseUser u) {
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("@@@", "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("@@@", "signInAnonymously:failure", task.getException());
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }


    @TargetApi(Build.VERSION_CODES.N)
    private String date() {
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd");
        return df.format(d);
    }

    private void loadInitialData() {
        final Context context = this;
        database.collection("da").document(anonymousID).collection("dada").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        groups.add(new Group(document.getId()));
                    }
                    ListView listView = findViewById(R.id.database);
                    GroupAdapter adapter = new GroupAdapter(context, groups);
                    listView.setAdapter(adapter);
                } else {
                    Log.d("@@@", "get failed with ", task.getException());
                }
            }
        });
    }

    public void addGroup(View view) {
        final Map<String, Object> group = new HashMap();
        final Map<String, Object> counter = new HashMap<>();
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        final GroupAdapter<Group> adapter = new GroupAdapter<Group>(this, groups);
        list = findViewById(R.id.database);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                groups.get(position);
            }
        });
        b.setTitle("Join group");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        b.setView(input);
        b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                group.put("voted", false);
                counter.put("eat", 0);
                group.put("time", date());
                counter.put("voted", false);
                database.collection("da").document(anonymousID).collection("dada").document(input.getText().toString()).set(group);
                database.collection("counter").document(input.getText().toString()).set(counter);
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
        adapter.notifyDataSetChanged();
        b.show();
    }

    public String getAnonymousID() {
        return anonymousID;
    }
}
