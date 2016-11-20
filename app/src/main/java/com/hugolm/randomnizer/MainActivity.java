package com.hugolm.randomnizer;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.os.Vibrator;
import android.preference.PreferenceFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ListView mylistView;
    ArrayList<AdapterItems> listData = new ArrayList<AdapterItems>();
    CustomAdapter myAdapter;
    DataBaseManager db;
    private boolean isLarge;
    private String showsub;
    ListView myListViewSub;
    /* Para las subcategorias */

    String parentCategory;
    ArrayList<AdapterItems> listDataSub = new ArrayList<AdapterItems>();
    CustomAdapterSub myAdapterSub;
    Bundle b;
    // Necesario para Acelerómetro
    Sensor sensor;
    SensorManager sensorManager;
    float xOld = 0;
    float yOld = 0;
    float zOld = 0;
    //speed debe ser mayor a 80
    float threaShold = 1000;
    long oldTime = 0;
    // Para imprimir el random de la lista
    AdapterItems adapterOption;
    // Para proximidad
    private SensorManager sensorManagerProximity;
    private Sensor sensorProximity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView myListView = (ListView) findViewById(R.id.lvlist);
        db = new DataBaseManager(this);
        chargeSubSensors();
        isLarge = getResources().getBoolean(R.bool.isLarge);
        myAdapter = new CustomAdapter(listData);
        myListView.setAdapter(myAdapter);
        ShowList();
        try {
            b = getIntent().getExtras();
            showsub = b.get("ShowSub").toString();
            if (showsub.equals("true")) {
                setContentView(R.layout.subcategories_main);
                parentCategory = b.get("Category").toString();
                ShowListSub();
            }


        } catch (Exception e) {
        }
        /*Subcategories*/
        if (isLarge) {
            try {
                b = getIntent().getExtras();
                parentCategory = b.get("Category").toString();
                ShowListSub();
            } catch (Exception e) {
            }
            chargeSubOps();
            chargeSubSensors();
        }
    }

    private void chargeSubOps() {
        myListViewSub = (ListView) findViewById(R.id.lvlistSub);
        myAdapterSub = new CustomAdapterSub(listDataSub);
        myListViewSub.setAdapter(myAdapterSub);
    }

    private void chargeSubSensors() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensor.TYPE_ACCELEROMETER);
        sensorManagerProximity = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorProximity = sensorManagerProximity.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }

    private void ShowList() {

        String[] projection = {"Category"};
        Cursor cursor = db.Query(projection, null, null, db.ColNameCategories);
        listData.clear();
        if (cursor.moveToFirst()) {
            do {
                listData.add(new AdapterItems(cursor.getString(cursor.getColumnIndex(db.ColNameCategories))));
            } while (cursor.moveToNext());
        }

        myAdapter = new CustomAdapter(listData);

        ListView lvData = (ListView) findViewById(R.id.lvlist);
        lvData.setAdapter(myAdapter);
        View header = getLayoutInflater().inflate(R.layout.header, null);
        lvData.addHeaderView(header);
    }


    public void ShowListSub() {
        int i = 0;
        String[] projectionParent = {"ParentCategory"};
        Cursor cursorParent = db.QuerySub(projectionParent, null, null, db.ColParentSubCategories);
        String[] projection = {"SubCategory", "ParentCategory"};
        Cursor cursor = db.QuerySub(projection, null, null, db.ColNameSubCategories);
        listDataSub.clear();
        if (cursor.moveToFirst()) {
            do {
                String CompareParent = cursor.getString(cursor.getColumnIndex(db.ColParentSubCategories));
                if (parentCategory.toLowerCase().equals(CompareParent.toLowerCase())) {
                    listDataSub.add(new AdapterItems(cursor.getString(cursor.getColumnIndex(db.ColNameSubCategories))));
                    i++;
                }
            } while (cursor.moveToNext());
            //Toast.makeText(getApplicationContext(), "List Reloaded", Toast.LENGTH_LONG).show();
        }

        myAdapterSub = new CustomAdapterSub(listDataSub);
        ListView lvlistSub = (ListView) findViewById(R.id.lvlistSub);
        lvlistSub.setAdapter(myAdapterSub);
        View header = getLayoutInflater().inflate(R.layout.headersub, null);
        lvlistSub.addHeaderView(header);
    }


    public void bu_OnAdd(View view) {
        //Toast.makeText(getApplicationContext(), "Presionado", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, AddCategory.class);
        startActivity(intent);
    }

    public void bu_OnAddSub(View view) {
        //b = getIntent().getExtras();
        if (parentCategory != null) {
            //parentCategory = b.get("Category").toString();
            Intent intent = new Intent(MainActivity.this, AddSubcategory.class);
            intent.putExtra("parentCategory", parentCategory);
            startActivity(intent);
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Error: ");
            alertDialog.setMessage("You should select a category previously");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }
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
        public View getView(int position, View convertView, final ViewGroup parent) {
            LayoutInflater mInflater = getLayoutInflater();
            View myView = mInflater.inflate(R.layout.layout_ticket, null);

            final AdapterItems s = listDataAdpater.get(position);

            TextView txtCategory = (TextView) myView.findViewById(R.id.tvlayoutcategory);
            txtCategory.setText(s.Category);


            txtCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isLarge) {
                        parentCategory = s.Category;
                        ShowListSub();
                    } else {
                        setContentView(R.layout.subcategories_main);
                        parentCategory = s.Category;
                        ShowListSub();
                    }

                }
            });


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

    }


    class CustomAdapterSub extends BaseAdapter {
        public ArrayList<AdapterItems> listDataAdapter;

        public CustomAdapterSub(ArrayList<AdapterItems> listDataAdapter) {
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

            TextView txtCategory = (TextView) myView.findViewById(R.id.tvlayoutSubcategory);
            txtCategory.setText(s.Category);


            ImageButton ibDelete = (ImageButton) myView.findViewById(R.id.imageButtonDeleteSub);
            ibDelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
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
                                Toast.makeText(MainActivity.this, "SubCategory: " + s.Category + " deleted", Toast.LENGTH_SHORT).show();
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
                    Intent intent = new Intent(MainActivity.this, EditSubcategory.class);
                    intent.putExtra("Category", s.Category);
                    intent.putExtra("parentCategory", parentCategory);
                    startActivity(intent);
                }
            });


            return myView;
        }

    }

    /*Para el funcionamiento de los sensores*/

    SensorEventListener sensorEventList = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                if (parentCategory != null)
                    shake(sensorEvent);
            }
            if (sensorEvent.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                if (parentCategory != null)
                    proximity(sensorEvent);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };


    @Override
    public void onResume() {
        // Si el teléfono dispone de sensor de proximidad, funciona con el mismo, sino con el acelerómetro
        super.onResume();
        //Si no está disponible en el dispositivo el sacelerómetro
        if (sensorProximity != null)
            sensorManagerProximity.registerListener(sensorEventList, sensorProximity, SensorManager.SENSOR_DELAY_NORMAL);
        if (sensor != null)
            sensorManager.registerListener(sensorEventList, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        //Si no está disponible en el dispositivo el sensor de próximidad
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(sensorEventList);
        sensorManagerProximity.unregisterListener(sensorEventList);
        //sManagerGiro.unregisterListener(this);
    }


    private void proximity(SensorEvent event) {
        //Sensor de proximidad
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -0.01 && event.values[0] <= 0.01) {
                //Cerca
                sensorManagerProximity.unregisterListener(sensorEventList);
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(500);
                showRandomOption();
            }
        }
    }

    private void shake(SensorEvent event) {
        //Acelerómetro
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        long currentTime = SystemClock.currentThreadTimeMillis();
        if ((currentTime - oldTime) > 150) {

            long timeDiff = currentTime - oldTime;
            oldTime = currentTime;
            // *10000 para volver a segundos
            float speed = Math.abs(x + y + z - xOld - yOld - zOld) / timeDiff * 10000;
            if (speed > threaShold) {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(500);
                sensorManager.unregisterListener(sensorEventList);
                showRandomOption();
            }
        }
    }

    private void showRandomOption() {
        Random selected = new Random();
        final int min = 0;
        final int max = listDataSub.size();
        System.out.println("Max: " + max);
        final int random = selected.nextInt((max - min) + 1) + min;
        System.out.println("random = " + random);
        try {
            adapterOption = listDataSub.get(random);
            Intent intentMain = new Intent(MainActivity.this, Show_randomly_selected.class);
            intentMain.putExtra("winner", adapterOption.Category.toString());
            intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intentMain);
        } catch (Exception e) {
            AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
            alertDialog.setTitle("Error: ");
            alertDialog.setMessage("You should insert at least one option");
            alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }

    }


}
