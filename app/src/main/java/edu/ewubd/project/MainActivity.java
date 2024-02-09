package edu.ewubd.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button sign,login;
    SharedPreferences sp;
    String rem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch);
        sp = this.getSharedPreferences("Shop",MODE_PRIVATE);
        sign = findViewById(R.id.sup);
        rem = sp.getString("remUser","");
        if(rem.equals("YES")){
            Intent i = new Intent(MainActivity.this,StockItem.class);
            startActivity(i);
        }
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,SignUp.class);
                startActivity(i);
            }
        });
        login = findViewById(R.id.lin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,Login.class);
                startActivity(i);
            }
        });

    }
}