package com.example.debtapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditPersonActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = AddEditPersonActivity.class.getPackage().getName()+"Extra_name";
    public static final String EXTRA_ID = AddEditPersonActivity.class.getPackage().getName()+"Extra_id";
    public static final String IS_CURRENT_CREDITOR_EDIT = "this is current creditor";
    private EditText mNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        mNameEditText = findViewById(R.id.edit_text_new_person_name);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)){
            setTitle("edit Person");
            mNameEditText.setText(intent.getStringExtra(EXTRA_NAME));
        }else {
            setTitle("add person");
            mNameEditText.setText("");
        }
    }

    private void savePerson() {
        String name = mNameEditText.getText().toString();

        if (name.trim().isEmpty()){
            Toast.makeText(this, "noName", Toast.LENGTH_LONG).show();
            return;
        }

        Intent data = new Intent();
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        boolean isCurrentCreditor = getIntent().getBooleanExtra(PeopleListActivity.IS_CURRENT_CREDITOR, false);
        if (id!=-1){
            data.putExtra(EXTRA_ID, id);
        }
        if (isCurrentCreditor){
            data.putExtra(IS_CURRENT_CREDITOR_EDIT, true);
        }
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
