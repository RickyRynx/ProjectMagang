package com.if4b.aplikasiabsensikeretaapi.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.if4b.aplikasiabsensikeretaapi.AdapterHistory;
import com.if4b.aplikasiabsensikeretaapi.R;
import com.if4b.aplikasiabsensikeretaapi.model.ModelAbsensiMasuk;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private ListView listViewHistory;
    private AdapterHistory absenAdapter;
    private List<ModelAbsensiMasuk> absenList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listViewHistory = findViewById(R.id.list_view_history);
        absenList = new ArrayList<>();
        absenAdapter = new AdapterHistory(this, absenList);
        listViewHistory.setAdapter(absenAdapter);

        // Mendapatkan data absen dari Firebase
        FirebaseDatabase.getInstance().getReference("modelAbsensiMasuk").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                absenList.clear();
                    ModelAbsensiMasuk absen = snapshot.getValue(ModelAbsensiMasuk.class);
                    absenList.add(absen);
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}