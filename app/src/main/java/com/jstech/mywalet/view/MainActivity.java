package com.jstech.mywalet.view;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.jstech.mywalet.R;

public class MainActivity extends AppCompatActivity {


    Button btnPark, btnReturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPark = (Button)findViewById(R.id.buttonPark);
        btnReturn = (Button)findViewById(R.id.buttonReturn);
        btnPark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), NewParkActivity.class);
                startActivity(intent);
            }
        });
        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(view.getContext(), ReturnCarActivity.class);
                startActivity(intent1);
            }
        });
    }
}
