package com.george.artdex;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //1
        //java c Dex1.java -> Dex1.class
        //java c Dex2.java -> Dex2.class

        //2
        //jar cvf AllDex.jar Dex1.class Dex2.class

        //3.
        //dx --dex --output AllDex.dex AllDex.jars

    }
}
