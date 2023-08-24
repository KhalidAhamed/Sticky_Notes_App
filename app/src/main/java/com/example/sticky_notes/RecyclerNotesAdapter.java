package com.example.sticky_notes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerNotesAdapter extends RecyclerView.Adapter<RecyclerNotesAdapter.ViewHolder> {

    Context context;
    ArrayList<NotesModel> arrNotes;

    public RecyclerNotesAdapter(Context context, ArrayList<NotesModel> arrNotes) {
        this.context = context;
        this.arrNotes = arrNotes;
    }

    @NonNull
    @Override
    public RecyclerNotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerNotesAdapter.ViewHolder holder, int position) {
        NotesModel note = arrNotes.get(position);
        holder.txtTitle.setText(note.title);
        holder.txtDescription.setText(note.description);

        holder.updateClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.add_layout);

                EditText edtTitle = dialog.findViewById(R.id.edtTitle);
                EditText edtDes = dialog.findViewById(R.id.edtDes);
                Button updateBtn = dialog.findViewById(R.id.addBtn);

                NotesModel currentNote = arrNotes.get(position);

                edtTitle.setText(currentNote.title);
                edtDes.setText(currentNote.description);
                updateBtn.setText("Update");

                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String title = edtTitle.getText().toString();
                        String description = edtDes.getText().toString();

                        if (!title.isEmpty() && !description.isEmpty()) {
                            currentNote.title = title;
                            currentNote.description = description;
                            MyDBHelper dbHelper = new MyDBHelper(context);
                            dbHelper.updateNote(currentNote);
                            notifyItemChanged(position);
                            dialog.dismiss();
                            Toast.makeText(context, "Note updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Please Enter to update", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                dialog.show();
            }
        });

        holder.updateClick.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are you sure want to delete?")
                        .setIcon(R.drawable.baseline_folder_delete_24)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyDBHelper dbHelper = new MyDBHelper(context);
                                dbHelper.deleteContact(note.id);
                                arrNotes.remove(position);
                                notifyItemRemoved(position);
                                notifyDataSetChanged(); // Update positions
                                Toast.makeText(context, "Note deleted successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Do nothing
                            }
                        });
                builder.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDescription;
        LinearLayout updateClick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtDescription = itemView.findViewById(R.id.txtDes);
            updateClick = itemView.findViewById(R.id.updateClkll);
        }
    }
}
