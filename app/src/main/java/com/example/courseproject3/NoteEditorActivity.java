package com.example.courseproject3;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity {
    int noteId;
    boolean changeFlag = false;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home && !changeFlag){
            startActivity(new Intent(NoteEditorActivity.this, MainActivity.class));
            return true;
        }
        else if(item.getItemId() == android.R.id.home && changeFlag){
            changeFlag = false;
            MainActivity.notes.remove(MainActivity.notes.size()-1);
            finish();
            return true;
        }
        if (item.getItemId() == R.id.confirm && changeFlag){
            changeFlag = false;
            startActivity(new Intent(NoteEditorActivity.this, MainActivity.class));
            return true;
        }
        else if(item.getItemId() == R.id.confirm && !changeFlag){
            finish();
            return true;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        setSupportActionBar(findViewById(R.id.NoteMenu));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        EditText editText = findViewById(R.id.editText);
        EditText editTitle = findViewById(R.id.editTitle);
        Intent intent = getIntent();

        noteId = intent.getIntExtra("noteId", -1);
        if (noteId != -1) {
            String[] kostyl= MainActivity.notes.get(noteId).split("\n");
            editTitle.setText(kostyl[0].substring(7, kostyl[0].length()));
            editText.setText(String.join("\n",Arrays.copyOfRange(kostyl,1,kostyl.length)));
        } else {
            MainActivity.notes.add("");
            noteId = MainActivity.notes.size() - 1;
            MainActivity.arrayAdapter.notifyDataSetChanged();
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                changeFlag = true;
                String title = String.valueOf(((EditText)findViewById(R.id.editTitle)).getText());
                MainActivity.notes.set(noteId, "Name : " + title+"\n"+String.valueOf(charSequence));
                MainActivity.arrayAdapter.notifyDataSetChanged();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                HashSet<String> set = new HashSet(MainActivity.notes);
                sharedPreferences.edit().putStringSet("notes", set).apply();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        editTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                changeFlag = true;
                String title = String.valueOf(((EditText)findViewById(R.id.editText)).getText());
                MainActivity.notes.set(noteId, "Name : " + String.valueOf(charSequence)+"\n"+title);
                MainActivity.arrayAdapter.notifyDataSetChanged();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notes", Context.MODE_PRIVATE);
                HashSet<String> set = new HashSet(MainActivity.notes);
                sharedPreferences.edit().putStringSet("notes", set).apply();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }
}