package com.xuanyuan.mypicture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Test2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        findViewById(R.id.tvTest2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(Test2Activity.this, TestService.class);
//                stopService(intent);

                Intent intent1=new Intent(Test2Activity.this,MainActivity.class);
                startActivity(intent1);
            }
        });
    }
}
