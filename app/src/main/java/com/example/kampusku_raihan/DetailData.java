package com.example.kampusku_raihan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetailData extends AppCompatActivity {
    Button btnInput;
    EditText nomor, nama1, ttl, jenis, alamat;
    DatabaseHelper dbmaster;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        nomor = findViewById(R.id.nomor);
        nama1 = findViewById(R.id.nama1);
        ttl = findViewById(R.id.ttl);
        jenis = findViewById(R.id.jenis);
        alamat = findViewById(R.id.alamat);
        dbmaster = new DatabaseHelper(this);

        Intent intent = getIntent();
        String nim = intent.getStringExtra("NIM");

        if (nim != null) {
            Cursor data = dbmaster.getAllData();
            if (data.moveToFirst()) {
                do {
                    if (data.getString(0).equals(nim)) {
                        nomor.setText(data.getString(0));
                        nama1.setText(data.getString(1));
                        ttl.setText(data.getString(2));
                        jenis.setText(data.getString(3));
                        alamat.setText(data.getString(4));
                        break;
                    }
                } while (data.moveToNext());
            }
        }
    }
}