package com.hugolm.aleatorizadorapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;

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
        ShowList();

    }


    private void ShowList() {

        String[] projectionParent = {"ParentCategory"};
        Cursor cursorParent = db.QuerySub(projectionParent, null, null, db.ColParentSubCategories);
        String[] projection = {"SubCategory"};
        Cursor cursor = db.QuerySub(projection, null, null, db.ColNameSubCategories);
        //String s = cursor.getString(cursor.getColumnIndex(db.ColNameSubCategories));
        //Toast.makeText(getApplicationContext(),"Aqui va: " + s, Toast.LENGTH_LONG).show();
        listData.clear();
        if (cursor.moveToFirst() && cursorParent.moveToFirst()) {
            //String tableData = "";
            do {
                //tableData += cursor.getString(cursor.getColumnIndex(db.ColNameCategories)) + "::";
                //String Compare = cursor.getString(cursor.getColumnIndex(db.ColNameSubCategories));
                String CompareParent = cursorParent.getString(cursorParent.getColumnIndex(db.ColParentSubCategories));
                Toast.makeText(getApplicationContext(), parentCategory + CompareParent,Toast.LENGTH_SHORT).show();
                if (parentCategory.equals(CompareParent))
                    listData.add(new AdapterItems(cursor.getString(cursor.getColumnIndex(db.ColNameSubCategories))));
            } while (cursor.moveToNext() && cursorParent.moveToNext());
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

            /*
            ImageButton ibDelete = (ImageButton) myView.findViewById(R.id.imageButtonDelete);
            ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                    alertDialog.setTitle("Deleting Category " + s.Category);
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
                            int result = db.delete("Category = ?", selectionArgs);
                            if (result > 0) {
                                Toast.makeText(getApplicationContext(), "Category: " + s.Category + " deleted", Toast.LENGTH_SHORT).show();
                                ShowList();
                            }
                        }
                    });
                    alertDialog.show();
                }
            });


            ImageButton BuUpdate = (ImageButton) myView.findViewById(R.id.imageButtonEdit);
            BuUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(getApplicationContext(), "Presionado", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), EditCategory.class);
                    intent.putExtra("Category", s.Category);
                    startActivity(intent);
                }
            });
            */
            return myView;
        }

    }


}
