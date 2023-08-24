package com.example.sticky_notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<NotesModel> arrNotes = new ArrayList<>();
    RecyclerView rcView;
    FloatingActionButton flBtn;
    RecyclerNotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcView = findViewById(R.id.recView);
        flBtn = findViewById(R.id.flbtn);

        rcView.setLayoutManager(new LinearLayoutManager(this));
        MyDBHelper dbHelper = new MyDBHelper(MainActivity.this);

        arrNotes = dbHelper.fetchNotes();
        for (int i = 0; i < arrNotes.size(); i++) {
            arrNotes.get(i).position = i;
        }





        flBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.add_layout);

                EditText edtTitle = dialog.findViewById(R.id.edtTitle);
                EditText edtDes = dialog.findViewById(R.id.edtDes);
                Button addBtn = dialog.findViewById(R.id.addBtn);

                addBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            String title = edtTitle.getText().toString();
                            String description = edtDes.getText().toString();

                            if (!title.isEmpty() && !description.isEmpty()) {
                                long result = dbHelper.addNotes(title, description);
                                if (result != -1) {
                                    NotesModel newNote = new NotesModel(title, description);
                                    arrNotes.add(newNote);
                                    adapter.notifyItemInserted(arrNotes.size() - 1);
                                    rcView.scrollToPosition(arrNotes.size() - 1);
                                    dialog.dismiss();
                                    Toast.makeText(MainActivity.this, "Note added successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainActivity.this, "Failed to add note", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(MainActivity.this, "Please enter both title and description", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "An error occurred while adding the note", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
            }
        });






        arrNotes = dbHelper.fetchNotes();
        adapter = new RecyclerNotesAdapter(MainActivity.this, arrNotes);
        rcView.setAdapter(adapter);



    }
}