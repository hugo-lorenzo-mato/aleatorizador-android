package com.hugolm.randomnizer;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class AddSubcategory extends AppCompatActivity {

    EditText et_addSub;
    ContentValues values;
    DataBaseManager db;
    String parentCategory;
    boolean isLarge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_subcategory);
        et_addSub = (EditText) findViewById(R.id.et_addSub);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        isLarge = getResources().getBoolean(R.bool.isLarge);
        values = new ContentValues();
        db = new DataBaseManager(this);
        Bundle b = getIntent().getExtras();
        parentCategory = b.get("parentCategory").toString();
        //Toast.makeText(getApplicationContext(), "La categoría padre es" + parentCategory, Toast.LENGTH_LONG).show();
    }


    public void ConfirmAddSub(View view) {
        //Toast.makeText(getApplicationContext(), "La categoría padre es" + parentCategory, Toast.LENGTH_LONG).show();
        values.put(DataBaseManager.ColParentSubCategories, parentCategory);
        values.put(DataBaseManager.ColNameSubCategories, et_addSub.getText().toString());
        long result = db.InsertSub(values);
        if (result > 0)
            Toast.makeText(getApplicationContext(), this.getString(R.string.toast_subcat_add1) + et_addSub.getText().toString() +
                    this.getString(R.string.toast_subcat_add2) + parentCategory, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(), this.getString(R.string.toast_subcat_notadded), Toast.LENGTH_LONG).show();
        Intent intentMain = new Intent(this, MainActivity.class);
        //Toast.makeText(getApplicationContext(), "La categoría que mandamos otra vez para hacer el show es: " + parentCategory, Toast.LENGTH_SHORT).show();
        intentMain.putExtra("Category", parentCategory);
        intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (isLarge) {
            startActivity(intentMain);
            finish();
        } else{
            intentMain.putExtra("ShowSub", "true");
            startActivity(intentMain);
            finish();
        }


    }
}
