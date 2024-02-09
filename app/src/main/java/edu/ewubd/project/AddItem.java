package edu.ewubd.project;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class AddItem extends AppCompatActivity {
    Button sv,bk;
    EditText name,quantity,unitP;
    String key = "";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_stock_items);
        bk = findViewById(R.id.back);
        sv = findViewById(R.id.save);
        name = findViewById(R.id.entryIname);
        quantity = findViewById(R.id.entryIquantity);
        unitP = findViewById(R.id.entryprice);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Stockinfo");
        bk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddItem.this,StockItem.class);
                startActivity(i);
            }
        });
        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
        Intent i = getIntent();
        if(i.hasExtra("click")){
            String value = i.getStringExtra("click");
            if (value.equals("1")) {
                String kk = i.getStringExtra("EventKey");
                sv.setOnClickListener(v->save1(kk));
            }
        }
    }

    public void save(){
        String Iname = name.getText().toString();
        String Iquantity = quantity.getText().toString();
        String up = unitP.getText().toString();
        HashMap<String,Object> hash = new HashMap<>();
        hash.put("Name ",Iname);
        hash.put("Quantity ",Iquantity);
        hash.put("Unit Price ",up);
        key = Iname;
        String value = Iname+"---"+Iquantity+"---"+up+"---";
        KeyValueDB kv = new KeyValueDB(this);
        kv.insertKeyValue(key,value);
        databaseReference.child(key).setValue(hash);
        Toast.makeText(getApplicationContext(),"Item added successfully",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(AddItem.this,StockItem.class);
        startActivity(i);

    }
    public void save1(String k){
        String Iname = name.getText().toString();
        String Iquantity = quantity.getText().toString();
        String up = unitP.getText().toString();
        HashMap<String,Object> hash = new HashMap<>();
        hash.put("Name ",Iname);
        hash.put("Quantity ",Iquantity);
        hash.put("Unit Price ",up);
        String value = Iname+"---"+Iquantity+"---"+up+"---";
        KeyValueDB kv = new KeyValueDB(this);
        kv.updateValueByKey(k,value);
        databaseReference.child(k).updateChildren(hash);
        Toast.makeText(getApplicationContext(),"Item Edited",Toast.LENGTH_SHORT).show();
        Intent i = new Intent(AddItem.this,StockItem.class);
        startActivity(i);
    }
}