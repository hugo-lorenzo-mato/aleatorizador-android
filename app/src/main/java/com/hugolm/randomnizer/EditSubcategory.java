package com.hugolm.randomnizer;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class EditSubcategory extends AppCompatActivity {

    EditText etEdition;
    DataBaseManager db;
    ContentValues values;
    String previousName;
    String parentCategory;
    boolean isLarge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_subcategory);
        Bundle bundle = getIntent().getExtras();
        etEdition = (EditText) findViewById(R.id.editTextSub);
        etEdition.setText(bundle.get("Category").toString());
        parentCategory = bundle.get("parentCategory").toString();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        isLarge = getResources().getBoolean(R.bool.isLarge);
        previousName = etEdition.getText().toString();
        db = new DataBaseManager(this);
        values = new ContentValues();
    }

    public void EditConfirmedSub(View view) {
        values.put(db.ColNameSubCategories, etEdition.getText().toString());
        String[] SelectionArgs = {previousName};
        int result = db.UpdateSub(values, "SubCategory = ?", SelectionArgs);
        if (result > 0)
            Toast.makeText(getApplicationContext(), "Option: " + previousName + ", now is: " + etEdition.getText().toString(), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "Not Updated", Toast.LENGTH_SHORT).show();
        Intent intentMain = new Intent(this, MainActivity.class);
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