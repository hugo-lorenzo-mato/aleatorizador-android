package com.hugolm.aleatorizadorapp;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class AddSubcategory extends AppCompatActivity {

    EditText et_addSub;
    ContentValues values;
    DataBaseManager db;
    String parentCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subcategory);
        et_addSub = (EditText) findViewById(R.id.et_addSub);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        values = new ContentValues();
        db = new DataBaseManager(this);
        Bundle b = getIntent().getExtras();
        parentCategory = b.get("parentCategory").toString();
        Toast.makeText(getApplicationContext(), "La categoría padre es" + parentCategory, Toast.LENGTH_LONG).show();
    }


    public void ConfirmAddSub(View view) {
        Toast.makeText(getApplicationContext(), "La categoría padre es" + parentCategory, Toast.LENGTH_LONG).show();
        values.put(DataBaseManager.ColParentSubCategories, parentCategory);
        values.put(DataBaseManager.ColNameSubCategories, et_addSub.getText().toString());
        long result = db.InsertSub(values);
        if (result > 0)
            Toast.makeText(getApplicationContext(), "SubCategory: " + et_addSub.getText().toString() +
                    ", added; parent: " + parentCategory, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(), "Not Added", Toast.LENGTH_LONG).show();
        Intent intentMain = new Intent(this, Subcategories.class);
        //Toast.makeText(getApplicationContext(), "La categoría que mandamos otra vez para hacer el show es: " + parentCategory, Toast.LENGTH_SHORT).show();
        intentMain.putExtra("Category", parentCategory);
        startActivity(intentMain);

    }
}
