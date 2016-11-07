package com.hugolm.aleatorizadorapp;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

public class AddCategory extends AppCompatActivity {

    EditText et_add;
    ContentValues values;
    DataBaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);
        et_add = (EditText) findViewById(R.id.et_add);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        values = new ContentValues();
        db = new DataBaseManager(this);

    }

    public void ConfirmAdd(View view) {
        values.put(DataBaseManager.ColNameCategories, et_add.getText().toString());
        if (values.size() > 0) {
            long result = db.Insert(values);
            if (result > 0)
                Toast.makeText(getApplicationContext(),"Category: " + et_add.getText().toString() +
                        ", added", Toast.LENGTH_LONG).show();
            else
               Toast.makeText(getApplicationContext(),"Not Added", Toast.LENGTH_LONG).show();

        }else {
            Toast.makeText(getApplicationContext(),"Not Added: Insert some value",Toast.LENGTH_LONG).show();
        }
        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);

    }
}
