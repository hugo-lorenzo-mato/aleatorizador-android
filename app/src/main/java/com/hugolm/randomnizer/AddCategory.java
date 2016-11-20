package com.hugolm.randomnizer;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class AddCategory extends AppCompatActivity {

    EditText et_add;
    ContentValues values;
    DataBaseManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_category);
        et_add = (EditText) findViewById(R.id.et_addSub);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        values = new ContentValues();
        db = new DataBaseManager(this);

    }

    public void ConfirmAdd(View view) {
        values.put(DataBaseManager.ColNameCategories, et_add.getText().toString());
        if (values.size() > 0) {
            long result = db.Insert(values);
            if (result > 0)
                Toast.makeText(getApplicationContext(),this.getString(R.string.toast_cat_add1) + et_add.getText().toString() +
                        this.getString(R.string.toast_cat_add2) , Toast.LENGTH_LONG).show();
            else
               Toast.makeText(getApplicationContext(),this.getString(R.string.toast_cat_notadded), Toast.LENGTH_LONG).show();

        }else {
            Toast.makeText(getApplicationContext(),this.getString(R.string.toast_cat_notadded2),Toast.LENGTH_LONG).show();
        }
        Intent intentMain = new Intent(this, MainActivity.class);
        intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentMain);
        finish();
    }
}
