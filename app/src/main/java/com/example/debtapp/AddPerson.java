package com.example.debtapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddPerson extends AppCompatActivity {

    public static final String EXTRA_NAME = AddPerson.class.getPackage().getName()+"Extra_name";

    private EditText mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        mName = findViewById(R.id.edit_text_new_person_name);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("add person");
    }

    private void savePerson() {
        String name = mName.getText().toString();

        if (name.trim().isEmpty()){
            Toast.makeText(this, "noName", Toast.LENGTH_LONG).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, name);
        setResult(RESULT_OK,data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_new_person_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_person:
                savePerson();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
