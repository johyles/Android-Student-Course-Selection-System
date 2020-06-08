package com.check_course;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Welcome2 extends AppCompatActivity {

    private RelativeLayout welcomeLayout;
    private TextView timeover;
    Timer timer = new Timer(); //生成一个Timer对象，计时器
    int num = 6;  //起始设置为6，打开显示5秒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome2);

        //多线程
        timeover = (TextView) findViewById(R.id.timeover);
        welcomeLayout = (RelativeLayout) findViewById(R.id.welcomeLayout);

        welcomeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.com"));
                startActivity(intent);
            }
        });
        //与时间相关的线程，函数体有run的大部分是线程
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //执行线程
                runOnUiThread(new Runnable() {
                    @Override
                    //执行的是什么
                    public void run() {
                        num--;
                        timeover.setText("- "+num+"秒 -\n跳过");
                        if(num<=1){
                            timer.cancel();
                            Intent intent = new Intent();
                            intent.setClass(Welcome2.this,LoginActivity.class);
                            startActivity(intent);
                            Welcome2.this.finish();
                        }
                    }
                });
            }
        };
        //点击跳过，可以跳过广告进入主界面
        timeover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                Intent intent = new Intent();
                intent.setClass(Welcome2.this,LoginActivity.class);
                startActivity(intent);
                Welcome2.this.finish();
            }
        });

        timer.schedule(task,1000,1000); //计划执行线程，每隔1000ms，执行1000ms

        //点广告其他位置转到其他页面

    }
}
