package edu.ewubd.project;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SellItem extends AppCompatActivity {
    TextView in, up;
    EditText quantity;
    Button donee, ap, atl;
    String key = "";
    ListView listed;
    ArrayList<ItemSell>itemsell;
    SellAdapter sadapt;
    String key1 = "";
    DatabaseReference databaseReference;
    DatabaseReference databaseReference1;
    Date date = Calendar.getInstance().getTime();
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss");
    String strDate = dateFormat.format(date);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_sell);
        in = findViewById(R.id.inname);
        up = findViewById(R.id.unitp);
        quantity = findViewById(R.id.quan);
        donee = findViewById(R.id.done);
        ap = findViewById(R.id.addp);
        databaseReference = FirebaseDatabase.getInstance().getReference("Stockinfo");
        databaseReference1 = FirebaseDatabase.getInstance().getReference("Sold Products");
        atl = findViewById(R.id.addL);
        listed = findViewById(R.id.Listed);
        itemsell = new ArrayList<>();
        donee.setOnClickListener(v->d());
        ap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(SellItem.this,StockItem.class);
                it.putExtra("clickable","1");
                startActivity(it);
            }
        });
        atl.setOnClickListener(v -> add());
        Intent i = getIntent();
        if(i.hasExtra("EventKey")){
            key = i.getStringExtra("EventKey");
            set(key);
        }
        loadData();
        listed.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String message = "Do you want to delete sell info for: "+itemsell.get(position).iname +" ?";
                showDialog(message, "Delete product", itemsell.get(position).key);
                return true;
            }
        });
    }
    private void d() {
        Intent i = new Intent(SellItem.this,StockItem.class);
        startActivity(i);
    }
    private void showDialog(String message, String title, String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        SiDB db = new SiDB(getApplicationContext());
                        db.deleteDataByKey(key);
                        databaseReference1.child(key).removeValue();
                        dialog.cancel();
                        loadData();
                        sadapt.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(),"History Removed",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    @SuppressLint("StaticFieldLeak")
    private void add() {
        try{
            String Iname = in.getText().toString();
            String Iquantity = quantity.getText().toString();
            String upr = up.getText().toString();
            int num1 = Integer.parseInt(Iquantity);
            int num2 = Integer.parseInt(upr);
            int num3=num1*num2;
            KeyValueDB db = new KeyValueDB(this);
            Cursor rows = db.execute("SELECT * FROM key_value_pairs");
            while (rows.moveToNext()) {
                String keys = rows.getString(0);
                if (keys.equals(key)) {
                    String k = db.getValueByKey(key);
                    String[] fieldValues = k.split("---");
                    String quantity = fieldValues[1];
                    int num5 = Integer.parseInt(quantity);
                    int num6 = num5-num1;
                    if(num6<0){
                        Toast.makeText(getApplicationContext(),"Out of Stock",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        String subT = Integer.toString(num3);
                        key1 = Iname+System.currentTimeMillis();
                        String value = Iname+"---"+Iquantity+"---"+upr+"---"+subT+"---"+strDate+"---";
                        SiDB kv = new SiDB(this);
                        HashMap<String,Object> hash = new HashMap<>();
                        hash.put("Name ",Iname);
                        hash.put("Quantity ",Iquantity);
                        hash.put("Unit Price ",upr);
                        hash.put("SubTotal",subT);
                        hash.put("Sell time",strDate);
                        kv.insertKeyValue(key1,value);
                        loadData();
                        String upt = Integer.toString(num6);
                        HashMap<String,Object> hash1 = new HashMap<>();
                        hash1.put("Name ",Iname);
                        hash1.put("Quantity ",upt);
                        hash1.put("Unit Price ",upr);
                        new AsyncTask<Void, Void, String>() {
                            protected String doInBackground(Void... param) {
                                try {
                                    databaseReference1.child(key1).setValue(hash);
                                    databaseReference.child(key).updateChildren(hash1);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                return null;
                            }
                            protected void onPostExecute(String data) {
                            }
                        }.execute();
                        String value1=Iname+"---"+upt+"---"+upr+"---";
                        db.updateValueByKey(key,value1);
                        Toast.makeText(getApplicationContext(),"Product sold",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        catch (NumberFormatException ex){
            ex.printStackTrace();
        }
    }

    private void set(String key) {
        KeyValueDB db = new KeyValueDB(this);
            String k = db.getValueByKey(key);
            String[] fieldValues = k.split("---");
            String name = fieldValues[0];
            String price = fieldValues[2];
            in.setText(name);
            up.setText(price);
    }
    private void loadData(){
        itemsell.clear();
        SiDB db = new SiDB(this);
        Cursor rows = db.execute("SELECT * FROM key_value_pairs1");
        while (rows.moveToNext()) {
            String key = rows.getString(0);
            String itemdata = rows.getString(1);
            String[] fieldValues = itemdata.split("---");
            String name = fieldValues[0];
            String quantity = fieldValues[1];
            String price = fieldValues[2];
            String subtotal = fieldValues[3];
            String time1 = fieldValues[4];
            ItemSell e = new ItemSell(key, name, quantity,price,subtotal,time1);
            itemsell.add(e);
        }
        db.close();
        sadapt = new SellAdapter(this, itemsell);
        listed.setAdapter(sadapt);
    }
}