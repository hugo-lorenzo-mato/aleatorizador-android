package com.hugolm.randomnizer;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class EditCategory extends AppCompatActivity {

    EditText etEdition;
    DataBaseManager db;
    ContentValues values;
    String previousName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_category);
        Bundle bundle = getIntent().getExtras();
        etEdition = (EditText) findViewById(R.id.editText);
        etEdition.setText(bundle.get("Category").toString());
        // Para mostrar siempre el teclado
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        previousName = etEdition.getText().toString();
        db = new DataBaseManager(EditCategory.this);
        values = new ContentValues();
    }

    public void EditConfirmed(View view) {
        values.put("Category", etEdition.getText().toString());
        String[] SelectionArgs = {previousName};
        int result = db.Update(values, "Category = ?", SelectionArgs);
        if (result > 0) {
            Toast.makeText(getApplicationContext(), this.getString(R.string.toast_editcat1) + previousName
                    + this.getString(R.string.toast_editcat2)
                    + etEdition.getText().toString(), Toast.LENGTH_SHORT).show();
            values.clear();
            values.put("ParentCategory", etEdition.getText().toString());
            result = db.UpdateSub(values,"ParentCategory = ?", SelectionArgs);
        }else
            Toast.makeText(getApplicationContext(), this.getString(R.string.toast_noteditcat), Toast.LENGTH_SHORT).show();
        Intent intentMain = new Intent(this, MainActivity.class);
        startActivity(intentMain);
    }
}
