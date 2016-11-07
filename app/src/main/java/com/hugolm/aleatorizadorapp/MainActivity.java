package com.hugolm.aleatorizadorapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<AdapterItems> listData = new ArrayList<AdapterItems>();
    CustomAdapter myAdapter;
    DataBaseManager db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView myListView = (ListView) findViewById(R.id.lvlist);
        db = new DataBaseManager(this);
        myAdapter = new CustomAdapter(listData);
        myListView.setAdapter(myAdapter);
        ShowList();
        // Al hacer clic mostramos la categoría pulsada
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final AdapterItems s = listData.get(i);
                TextView tvCat = (TextView) view.findViewById(R.id.tvlayoutcategory);
                Toast.makeText(getApplicationContext(), s.Category, Toast.LENGTH_LONG).show();
            }
        });

    }

    public void bu_OnAdd(View view) {
        //Toast.makeText(getApplicationContext(), "Presionado", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, AddCategory.class);
        startActivity(intent);
    }

    public void ReloadList(View view) {
        ShowList();
    }

    private void ShowList() {

        String[] projection = {"Category"};
        Cursor cursor = db.Query(projection, null, null, db.ColNameCategories);
        listData.clear();
        if (cursor.moveToFirst()) {
            //String tableData = "";
            do {
                //tableData += cursor.getString(cursor.getColumnIndex(db.ColNameCategories)) + "::";
                listData.add(new AdapterItems(cursor.getString(cursor.getColumnIndex(db.ColNameCategories))));
            } while (cursor.moveToNext());
            //Toast.makeText(getApplicationContext(), "List Reloaded", Toast.LENGTH_LONG).show();
        }

        myAdapter = new CustomAdapter(listData);

        ListView lvData = (ListView) findViewById(R.id.lvlist);
        lvData.setAdapter(myAdapter);
    }

    class CustomAdapter extends BaseAdapter {
        public ArrayList<AdapterItems> listDataAdpater;

        public CustomAdapter(ArrayList<AdapterItems> listDataAdapter) {
            this.listDataAdpater = listDataAdapter;
        }


        @Override
        public int getCount() {
            return listDataAdpater.size();
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
            View myView = mInflater.inflate(R.layout.layout_ticket, null);

            final AdapterItems s = listDataAdpater.get(position);

            TextView txtCategory = (TextView) myView.findViewById(R.id.tvlayoutcategory);
            txtCategory.setText(s.Category);


            ImageButton ibDelete = (ImageButton) myView.findViewById(R.id.imageButtonDelete);
            ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
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

            return myView;
        }
        //Just a coment to test git and AS2.2

    }

}
