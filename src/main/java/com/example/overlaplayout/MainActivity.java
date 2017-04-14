package com.example.overlaplayout;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import view.OverlapLayout;

public class MainActivity extends AppCompatActivity {

    private OverlapLayout overlapLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        overlapLayout = (OverlapLayout) findViewById(R.id.overlab);
        TextView textView = new TextView(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(200,200);
        textView.setLayoutParams(layoutParams);
        textView.setText("添加的");
        textView.setBackgroundColor(Color.YELLOW);
        overlapLayout.addView(textView,1);


        overlapLayout.setiClick(new OverlapLayout.IClick() {
            @Override
            public void onIClick(View view) {

                Log.e("onIClick  ","-----------------");
            }
        });
    }
}
