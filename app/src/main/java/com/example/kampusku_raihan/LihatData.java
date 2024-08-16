package com.example.kampusku_raihan;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class LihatData extends AppCompatActivity {

    DatabaseHelper myDb;
    ListView listView;
    ArrayList<String> listData;
    ArrayAdapter<String> adapter;
    EditText editTextSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_data);

        listView = findViewById(R.id.list_view);
        editTextSearch = findViewById(R.id.editTextText);
        myDb = new DatabaseHelper(this);
        listData = new ArrayList<>();

        loadData();

        // Setup adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        listView.setAdapter(adapter);

        // Pencarian data
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Klik item untuk update data
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);
                showOptionsDialog(selectedItem);
            }
        });
    }

    private void showOptionsDialog(final String selectedItem) {
        // Split item to get ID
        String[] itemParts = selectedItem.split("\n");
        final String itemId = itemParts[0].replace("NIM: ", "");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Aksi")
                .setItems(new CharSequence[]{"Lihat", "Update", "Hapus"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // View
                                viewItem(itemId);
                                break;
                            case 1: // Update
                                updateItem(itemId);
                                break;
                            case 2: // Delete
                                deleteItem(itemId);
                                break;
                        }
                    }
                });
        builder.create().show();
    }

    private void viewItem(String itemId) {
        Intent intent = new Intent(LihatData.this, DetailData.class);
        intent.putExtra("NIM", itemId);
        startActivity(intent);
    }

    private void updateItem(String itemId) {
        Intent intent = new Intent(LihatData.this, InputData.class);
        intent.putExtra("NIM", itemId);
        startActivity(intent);
    }

    private void deleteItem(final String itemId) {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Penghapusan")
                .setMessage("Apakah kamu yakin untuk menghapus?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Integer deletedRows = myDb.deleteData(itemId);
                        if (deletedRows > 0) {
                            Toast.makeText(LihatData.this, "Data Berhasil Dihapus!", Toast.LENGTH_LONG).show();
                            loadData();
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(LihatData.this, "Data Gagal Dihapus!", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("Tidak", null)
                .create().show();
    }


    private void loadData() {
        listData.clear();
        Cursor data = myDb.getAllData();
        if (data.getCount() == 0) {
            Toast.makeText(this, "Data tidak Ditemukan!", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                listData.add("NIM: " + data.getString(0) + "\nNama: " + data.getString(1) + "\nTTL: " + data.getString(2) + "\nJenis: " + data.getString(3) + "\nAlamat: " + data.getString(4 ));
            }
        }
    }
}