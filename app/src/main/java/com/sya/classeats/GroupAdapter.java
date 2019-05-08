package com.sya.classeats;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class GroupAdapter<G> extends ArrayAdapter<Group> {
    private Context context;
    private ArrayList<Group> groups;
    private LayoutInflater inflater;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Group_info obj = new Group_info();
    MainActivity main = new MainActivity();
    int dbData = 0;


    int localA = 0;
    boolean voted = false;


    public GroupAdapter(Context context, ArrayList<Group> groups) {
        super(context, -1, groups);
        this.context = context;
        this.groups = groups;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private String date() {
        Date d = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd");
        return df.format(d);
    }


    @NonNull
    @Override
    public View getView(final int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
        final View items = inflater.inflate(R.layout.group_adapter, parent, false);
        final TextView name = items.findViewById(R.id.groupName);
        final TextView connected = items.findViewById(R.id.connected);
        final Button button = items.findViewById(R.id.viewButton);
        final Intent intent = new Intent(context, Group_info.class);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DocumentReference ref = db.collection("counter").document(groups.get(position).group_Name);
                final DocumentReference groupRef = db.collection("da").document(main.getAnonymousID()).collection("dada").document(groups.get(position).group_Name);
                groupRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            dbData = Integer.parseInt(document.get("time").toString());
                            if (dbData > Integer.parseInt(date())) {
                                groupRef.update("voted", false).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                                ref.update("eat", 0).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                            } else if (dbData < Integer.parseInt(document.get("time").toString())) {
                                groupRef.update("time", date()).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                            Log.d("@@@@@@", "Document " + document.get("voted"));
                            Log.d("@@@@@@", "Document " + document.get("time"));
                            Log.d("@@@@@@", "auth: " + main.getAnonymousID());
                            intent.putExtra("voted", document.getBoolean("voted"));


                        } else {
                            Log.d("@@@@@@", "Document doesn't exist");
                        }
                    }
                });
                ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                localA = Integer.parseInt(document.get("eat").toString());
                                obj.setLocalA(Integer.parseInt(document.get("eat").toString()));
                                intent.putExtra("groupname", groups.get(position).getGroup_Name());
                                intent.putExtra("id", main.getAnonymousID());
                                intent.putExtra("eat", localA);
                                context.startActivity(intent);
                            } else {
                                Log.d("@@@", "Document does not exist");
                            }
                        }

                    }
                });

            }
        });
        name.setText(groups.get(position).getGroup_Name());
        connected.setText("connected: " + groups.get(position).getConencted());

        return items;
    }

    private void readData() {

    }

}
