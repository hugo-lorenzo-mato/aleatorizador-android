package com.hugolm.aleatorizadorapp;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditCategory extends AppCompatActivity {

    EditText etEdition;
    DataBaseManager db;
    ContentValues values;
    String previousName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        Bundle bundle = getIntent().getExtras();
        etEdition = (EditText) findViewById(R.id.editText);
        etEdition.setText(bundle.get("Category").toString());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        previousName = etEdition.getText().toString();
        db = new DataBaseManager(this);
        values = new ContentValues();
    }

    public void EditConfirmed(View view) {
        values.put(db.ColNameCategories, etEdition.getText().toString());
        String[] SelectionArgs = {previousName};
        int result = db.Update(values, "Category = ?", SelectionArgs);
        if (result > 0)
            Toast.makeText(getApplicationContext(), "Category: " + previousName + ", now is: " + etEdition.getText().toString(), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Not Updated", Toast.LENGTH_SHORT).show();
        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);
    }
}
