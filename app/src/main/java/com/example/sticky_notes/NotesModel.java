package com.example.sticky_notes;

public class NotesModel {
    public int position;
    int id;
    String title;
    String description;

    public NotesModel(){
        this.id = id;
        this.title = title;
        this.description = description;
    }

    public NotesModel(String title,String description){
        this.title = title;
        this.description = description;
    }
}
