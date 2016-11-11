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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class Subcategories extends AppCompatActivity implements SensorEventListener {

    String parentCategory;
    ArrayList<AdapterItems> listData = new ArrayList<AdapterItems>();
    CustomAdapter myAdapter;
    DataBaseManager db;
    Bundle b;
    // Necesario para Acelerómetro
    Sensor sensor;
    SensorManager sensorManager;
    float xOld = 0;
    float yOld = 0;
    float zOld = 0;
    //speed debe ser mayor a 80
    float threaShold = 600;
    long oldTime = 0;
    // Para imprimir el random de la lista
    AdapterItems adapterOption;
    // Para proximidad
    private SensorManager sensorManagerProximity;
    private Sensor sensorProximity;
    // Para Giroscopio
    private SensorManager sManagerGiro;
    private Sensor sensorGiro;



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
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensor.TYPE_ACCELEROMETER);
        sensorManagerProximity = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorProximity = sensorManagerProximity.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        //giroscopio
        //sManagerGiro = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //sensorGiro = sManagerGiro.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

    }

    @Override
    public void onResume() {
        super.onResume();
        //Si no está disponible en el dispositivo el sacelerómetro
        if (sensor != null)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        //Si no está disponible en el dispositivo el sensor de próximidad
        if (sensorProximity != null)
            sensorManagerProximity.registerListener(this, sensorProximity, SensorManager.SENSOR_DELAY_NORMAL);
        //if (sensorGiro != null)
        //    sManagerGiro.registerListener(this,sensorGiro,sManagerGiro.SENSOR_DELAY_GAME);


    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        sensorManagerProximity.unregisterListener(this);
        //sManagerGiro.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            shake(event);
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY)
            proximity(event);
        //if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE)
        //    Toast.makeText(this,"giro",Toast.LENGTH_LONG).show();
    }

    private void giro(SensorEvent event) {
        DecimalFormat temp = new DecimalFormat("#.##");
        String x = temp.format(event.values[0]);
        String y = temp.format(event.values[1]);
        String z = temp.format(event.values[2]);
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
        showRandomOption();



    }

    private void proximity(SensorEvent event) {
        //Sensor de proximidad
        if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
            if (event.values[0] >= -0.01 && event.values[0] <= 0.01) {
                //Cerca
                sensorManagerProximity.unregisterListener(this);
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
        if ((currentTime - oldTime) > 100) {

            long timeDiff = currentTime - oldTime;
            oldTime = currentTime;
            // *10000 para volver a segundos
            float speed = Math.abs(x + y + z - xOld - yOld - zOld) / timeDiff * 10000;
            if (speed > threaShold) {
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(500);
                //Toast.makeText(getApplicationContext(), "It Works Mateo!!!! hahaha", Toast.LENGTH_SHORT).show();
                sensorManager.unregisterListener(this);
                showRandomOption();
            }
        }
    }

    private void showRandomOption() {
        Random selected = new Random();
        final int min = 0;
        final int max = listData.size();
        System.out.println("Max: " + max);
        final int random = selected.nextInt((max - min) - 1) + min;
        System.out.println("random = " + random);
        adapterOption = listData.get(random);
        Intent intentMain = new Intent(getApplicationContext(), Show_randomly_selected.class);
        intentMain.putExtra("winner", adapterOption.Category.toString());
        intentMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentMain);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

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
