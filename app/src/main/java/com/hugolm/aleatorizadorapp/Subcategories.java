package com.hugolm.aleatorizadorapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Random;

public class Subcategories extends AppCompatActivity {

    String parentCategory;
    ArrayList<AdapterItems> listData = new ArrayList<AdapterItems>();
    CustomAdapter myAdapter;
    DataBaseManager db;
    Bundle b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_subcategories);
        b = getIntent().getExtras();
        parentCategory = b.get("Category").toString();
        ListView myListView = (ListView) findViewById(R.id.lvlistSub);
        db = new DataBaseManager(this);
        myAdapter = new CustomAdapter(listData);
        myListView.setAdapter(myAdapter);
        ShowListSub();
    }


    public void bu_goHome(View view) {
        Intent intentMain = new Intent(this, MainActivity.class);
        intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentMain);
    }


    private void ShowListSub() {
        int i = 0;
        String[] projectionParent = {"ParentCategory"};
        Cursor cursorParent = db.QuerySub(projectionParent, null, null, db.ColParentSubCategories);
        String[] projection = {"SubCategory", "ParentCategory"};
        Cursor cursor = db.QuerySub(projection, null, null, db.ColNameSubCategories);
        listData.clear();
        if (cursor.moveToFirst()) {
            do {
                //String Compare = cursor.getString(cursor.getColumnIndex(db.ColNameSubCategories));
                String CompareParent = cursor.getString(cursor.getColumnIndex(db.ColParentSubCategories));
                //Toast.makeText(getApplicationContext(), parentCategory + CompareParent,Toast.LENGTH_SHORT).show();
                if (parentCategory.toLowerCase().equals(CompareParent.toLowerCase())) {
                    //Toast.makeText(getApplicationContext(), "IGUALES" + i, Toast.LENGTH_SHORT).show();
                    listData.add(new AdapterItems(cursor.getString(cursor.getColumnIndex(db.ColNameSubCategories))));
                    i++;
                }
            } while (cursor.moveToNext());
            //Toast.makeText(getApplicationContext(), "List Reloaded", Toast.LENGTH_LONG).show();
        }

        myAdapter = new CustomAdapter(listData);
        ListView lvlistSub = (ListView) findViewById(R.id.lvlistSub);
        lvlistSub.setAdapter(myAdapter);
    }


    public void bu_OnAddSub(View view) {
        b = getIntent().getExtras();
        parentCategory = b.get("Category").toString();
        Intent intent = new Intent(this, AddSubcategory.class);
        intent.putExtra("parentCategory", parentCategory);
        startActivity(intent);
    }


    class CustomAdapter extends BaseAdapter {
        public ArrayList<AdapterItems> listDataAdapter;

        public CustomAdapter(ArrayList<AdapterItems> listDataAdapter) {
            this.listDataAdapter = listDataAdapter;
        }


        @Override
        public int getCount() {
            return listDataAdapter.size();
        }

        @Override
        public String getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater mInflater = getLayoutInflater();
            View myView = mInflater.inflate(R.layout.layout_ticket_subcategories, null);

            final AdapterItems s = listDataAdapter.get(position);

            TextView txtCategory = (TextView) myView.findViewById(R.id.tvlayoutcategorySub);
            txtCategory.setText(s.Category);


            ImageButton ibDelete = (ImageButton) myView.findViewById(R.id.imageButtonDeleteSub);
            ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(Subcategories.this, R.style.myDialog)).create();
                    alertDialog.setTitle("Deleting Subcategory " + s.Category);
                    alertDialog.setMessage("Are you sure you want to delete it?");
                    alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Dismiss", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            String[] selectionArgs = {s.Category};
                            int result = db.deleteSub("SubCategory = ?", selectionArgs);
                            if (result > 0) {
                                Toast.makeText(getApplicationContext(), "SubCategory: " + s.Category + " deleted", Toast.LENGTH_SHORT).show();
                                ShowListSub();
                            }
                        }
                    });
                    alertDialog.show();
                }
            });


            ImageButton BuUpdate = (ImageButton) myView.findViewById(R.id.imageButtonEditSub);
            BuUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getApplicationContext(), "Presionado", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), EditSubcategory.class);
                    intent.putExtra("Category", s.Category);
                    intent.putExtra("parentCategory", parentCategory);
                    startActivity(intent);
                }
            });


            return myView;
        }

    }


}
