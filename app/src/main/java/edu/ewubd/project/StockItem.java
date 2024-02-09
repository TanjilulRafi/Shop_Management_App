package edu.ewubd.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class StockItem extends AppCompatActivity {
    ListView list;
    ArrayList<Item> item;
    ItemAdapter adapt;
    Button se,edt;
    SharedPreferences sp;
    TextView tv;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stock_items);
        sp = this.getSharedPreferences("Shop", MODE_PRIVATE);
        se = findViewById(R.id.sell);
        edt = findViewById(R.id.edit);
        list = findViewById(R.id.listed);
        item = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Stockinfo");
        tv = findViewById(R.id.textv);
        tv.setText(sp.getString("name",""));
        loadData();
        edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StockItem.this,AddItem.class);
                startActivity(i);
            }
        });
        se.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(StockItem.this,SellItem.class);
                startActivity(i);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Intent i = new Intent(StockItem.this, SellItem.class);
                i.putExtra("EventKey", item.get(position).key);
                startActivity(i);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                Intent i = new Intent(StockItem.this, AddItem.class);
                i.putExtra("EventKey", item.get(position).key);
                i.putExtra("click","1");
                startActivity(i);
            }
        });
        Intent it = getIntent();
        if(it.hasExtra("clickable")) {
            String value = it.getStringExtra("clickable");
            if (value.equals("1")) {
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                        Intent i = new Intent(StockItem.this, SellItem.class);
                        i.putExtra("EventKey", item.get(position).key);
                        startActivity(i);
                    }
                });
            }
        }
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String message = "Do you want to delete product: "+item.get(position).iname +" ?";
                showDialog(message, "Delete product: ", item.get(position).key);
                return true;
            }
        });
    }
    private void showDialog(String message, String title, String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        KeyValueDB db = new KeyValueDB(getApplicationContext());
                        db.deleteDataByKey(key);
                        databaseReference.child(key).removeValue();
                        dialog.cancel();
                        loadData();
                        adapt.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(),"Removed from List",Toast.LENGTH_SHORT).show();
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
    private void loadData(){
        item.clear();
        KeyValueDB db = new KeyValueDB(this);
        Cursor rows = db.execute("SELECT * FROM key_value_pairs");
        while (rows.moveToNext()) {
            String key = rows.getString(0);
            String itemdata = rows.getString(1);
            String[] fieldValues = itemdata.split("---");
            String name = fieldValues[0];
            String quantity = fieldValues[1];
            String price = fieldValues[2];
            Item e = new Item(key, name, quantity,price);
            item.add(e);
        }
        db.close();
        adapt = new ItemAdapter(this, item);
        list.setAdapter(adapt);
    }
}