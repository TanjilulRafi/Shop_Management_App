package edu.ewubd.project;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;

public class SignUp extends AppCompatActivity {
    Button prev,savee,finish;
    EditText sname, semail, sphone, pass, repass;
    SharedPreferences sp;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        sp = this.getSharedPreferences("Shop", MODE_PRIVATE);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        sname = findViewById(R.id.name);
        semail = findViewById(R.id.email);
        sphone = findViewById(R.id.phone);
        pass = findViewById(R.id.password);
        repass = findViewById(R.id.rpass);
        prev = findViewById(R.id.back);
        savee = findViewById(R.id.save);
        finish = findViewById(R.id.exit);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignUp.this,MainActivity.class);
                startActivity(i);
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        savee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            SharedPreferences.Editor e = sp.edit();
            String ph = sphone.getText().toString();
            boolean value = semail.getText().toString().contains("@");
                String val = new Boolean(value).toString();
            if(sname.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "Fill up name field", Toast.LENGTH_SHORT).show();
            }
            else if(semail.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(),"Fill up e-mail field",Toast.LENGTH_SHORT).show();
            }
            else if(ph.isEmpty()||ph.length()<9||ph.length()>11){
                Toast.makeText(getApplicationContext(),"Fill up Phone field",Toast.LENGTH_SHORT).show();
            }
            else if(pass.getText().toString().isEmpty()){
                Toast.makeText(getApplicationContext(),"Check password field",Toast.LENGTH_SHORT).show();
            }
            else if(pass.getText().toString().length()<4){
                Toast.makeText(getApplicationContext(),"Password length is short",Toast.LENGTH_SHORT).show();
            }
            else if (val.equals("false")){
                Toast.makeText(getApplicationContext(),"Input valid e-mail",Toast.LENGTH_SHORT).show();
            }
            else{
                if(pass.getText().toString().equals(repass.getText().toString())) {
                    e.putString("name", sname.getText().toString());
                    e.putString("email", semail.getText().toString());
                    e.putString("phone", sphone.getText().toString());
                    e.putString("password", pass.getText().toString());
                    e.apply();
                    HashMap<String, Object> hash = new HashMap<>();
                    hash.put("Name ", sname.getText().toString());
                    hash.put("E-mail ", semail.getText().toString());
                    hash.put("Phone ", sphone.getText().toString());
                    hash.put("Password", pass.getText().toString());
                    databaseReference.child(sname.getText().toString()).setValue(hash);
                    Toast.makeText(getApplicationContext(), "Information saved", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(SignUp.this, Login.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Password does not match",Toast.LENGTH_SHORT).show();
                }
            }
            }
        });
    }
}