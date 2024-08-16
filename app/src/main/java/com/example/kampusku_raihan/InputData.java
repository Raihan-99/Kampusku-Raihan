package com.example.kampusku_raihan;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.content.Intent;
import android.database.Cursor;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class InputData extends AppCompatActivity {

    Button btnInput;
    EditText nomor, nama1, ttl, jenis, alamat;
    DatabaseHelper dbmaster;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_input_data);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnInput = findViewById(R.id.btnInput);
        nomor = findViewById(R.id.nomor);
        nama1 = findViewById(R.id.nama1);
        ttl = findViewById(R.id.ttl);
        jenis = findViewById(R.id.jenis);
        alamat = findViewById(R.id.alamat);
        dbmaster = new DatabaseHelper(this);

        ttl.setOnClickListener(v -> {
            // Mendapatkan tanggal saat ini
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            // Membuat DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    InputData.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Format tanggal dan set ke EditText
                        ttl.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

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
                        btnInput.setText("Update Data");
                        break;
                    }
                } while (data.moveToNext());
            }

            btnInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isUpdated = dbmaster.updateData(String.valueOf(Integer.parseInt(nim)), nama1.getText().toString(), ttl.getText().toString(),jenis.getText().toString(),alamat.getText().toString());
                    if (isUpdated)
                        Toast.makeText(InputData.this, "Data Berhasil Diupdate!", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(InputData.this, "Data Gagal Diupdate!", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            btnInput.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isInserted = dbmaster.insertData(Integer.parseInt(nomor.getText().toString()), nama1.getText().toString(), ttl.getText().toString(),jenis.getText().toString(),alamat.getText().toString());
                    if (isInserted)
                        Toast.makeText(InputData.this, "Data Berhasil Ditambahkan!", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(InputData.this, "Data Gagal Ditambahkan!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}