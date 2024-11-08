package com.b108.hosteconnect;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ScholarshipInfoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ScholarshipAdapter adapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scholarship_info);

        recyclerView = findViewById(R.id.scholarshipRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ScholarshipAdapter();
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("scholarships");
        fetchScholarships();
    }

    private void fetchScholarships() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Scholarship> scholarshipList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Scholarship scholarship = snapshot.getValue(Scholarship.class);
                    scholarshipList.add(scholarship);
                }
                adapter.setScholarships(scholarshipList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors.
            }
        });
    }
}