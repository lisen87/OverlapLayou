package com.example.overlaplayout;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import view.OverlapLayout;

public class MainActivity extends AppCompatActivity {

    private OverlapLayout overlapLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
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

                if (isDeviceRooted()) {
//                    setSystemTime("20170414.013900");//2017/04/14 09:39:00
                } else {
                    getRootPermission(getPackageCodePath());
                }

            }
        });
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }
    /**
     * 判断设备是否root
     *
     * @return the boolean{@code true}: 是<br>{@code false}: 否
     */
    public boolean isDeviceRooted(){
        String su = "su";
        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/", "/system/bin/failsafe/",
                "/data/local/xbin/", "/data/local/bin/", "/data/local/"};
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }
    public void setSystemTime(String datetimes) {
        try {
            Process process = Runtime.getRuntime().exec("su");
//          String datetime = "20131023.112800"; // 测试的设置的时间【时间格式
            String datetime = ""; // 测试的设置的时间【时间格式
            datetime = datetimes.toString(); // yyyyMMdd.HHmmss】
            DataOutputStream os = new DataOutputStream(
                    process.getOutputStream());
            os.writeBytes("setprop persist.sys.timezone GMT\n");
            os.writeBytes("/system/bin/date -s " + datetime + "\n");
            os.writeBytes("clock -w\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            Toast.makeText(this, "请获取Root权限", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean getRootPermission(String pkgCodePath) {
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd = "chmod 777 " + pkgCodePath;
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            Toast.makeText(this, "root error!" + pkgCodePath, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();


            } catch (Exception e) {
            }
        }
        Toast.makeText(this, "root success!" + pkgCodePath, Toast.LENGTH_SHORT).show();
        return true;
    }
}
