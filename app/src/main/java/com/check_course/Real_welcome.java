package com.check_course;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Real_welcome extends AppCompatActivity {

    boolean isFirst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_welcome);

        //读取数据
        SharedPreferences sp = getSharedPreferences("isFirst",MODE_PRIVATE);
        isFirst = sp.getBoolean("isFirst",true);
        //如果用户是第一次进入，进入到滑动Image页面，进入到倒计时页面
        if(isFirst){
            startActivity(new Intent(Real_welcome.this,Welcome1.class));
        }else{
            startActivity(new Intent(Real_welcome.this,Welcome2.class));
        }
        finish();
        //实例化编辑器
        SharedPreferences.Editor editor = sp.edit();
        //存入数据
        editor.putBoolean("isFirst",false);
        //提交修改
        editor.commit();

    }
}
