package edu.ewubd.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    Button login;
    EditText uname,upass;
    RadioButton remember;
    SharedPreferences sp;
    String rem,user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        sp = this.getSharedPreferences("Shop", MODE_PRIVATE);
        uname = findViewById(R.id.entrymail);
        upass = findViewById(R.id.entrypass);
        remember = findViewById(R.id.rem);
        login = findViewById(R.id.loginb);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor e = sp.edit();
                rem = sp.getString("password","");
                user = sp.getString("email","");
                if(upass.getText().toString().equals(rem) && uname.getText().toString().equals(user)){
                    Intent i = new Intent(Login.this,StockItem.class);
                    startActivity(i);
                    Toast.makeText(getApplicationContext(),"Welcome",Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(),"Check your information",Toast.LENGTH_SHORT).show();
                }
                if(remember.isChecked()){
                    e.putString("remUser","YES");
                    e.apply();
                }
                else {
                    e.putString("remUser","NO");
                    e.apply();
                }
            }
        });
    }
}
