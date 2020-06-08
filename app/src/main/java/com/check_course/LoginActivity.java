package com.check_course;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class LoginActivity extends AppCompatActivity {

    EditText username,pwd,inputCode;
    Button login,exit;
    TextView register,us,showCode,forget;
    String yzm;

    CheckBox saveName;
    String sname;
    String spwd;

    dbHelper dbHelper;
    String DB_Name="mydb";
    SQLiteDatabase db;
    Cursor cursor;
    Cursor cursor2;
    private String forgetmima;

    boolean flag = false;
    boolean flag2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText)findViewById(R.id.editText);
        pwd = (EditText)findViewById(R.id.editText2);
        inputCode = (EditText)findViewById(R.id.code);
        login = (Button)findViewById(R.id.button);
        exit = (Button)findViewById(R.id.button2);
        register = (TextView)findViewById(R.id.textView4);
        us = (TextView)findViewById(R.id.textView6);
        saveName = (CheckBox)findViewById(R.id.saveName);
        showCode=(TextView)findViewById(R.id.textView5);
        forget=(TextView)findViewById(R.id.textView36);

        //先判断是否存过
        final SharedPreferences sp = getSharedPreferences("sname",MODE_PRIVATE);
        final SharedPreferences sp1 = getSharedPreferences("spwd",MODE_PRIVATE);


        sname = sp.getString("sname","");
        spwd = sp1.getString("spwd","");
        if(!sname.equals("")&&!spwd.equals("")){
            username.setText(sp.getString("sname",""));
            pwd.setText(sp1.getString("spwd",""));
            saveName.setChecked(true);
        }

        //yzm = yzm();
        showCode.setText(yzm());
        //刷新验证码
        showCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //yzm = yzm();
                showCode.setText(yzm());
            }
        });

        //创建连接，并打开数据库
        dbHelper = new dbHelper(this,DB_Name,null,1);
        db = dbHelper.getWritableDatabase();

        saveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("")||pwd.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this,"请输入用户名", Toast.LENGTH_SHORT).show();
                }else{
                    cursor2 = db.query(dbHelper.TB_Name, null, null, null, null, null, "uid ASC");
                    cursor2.moveToFirst();
                    while (!cursor2.isAfterLast()) {
                        if (username.getText().toString().trim().equals(cursor2.getString(1))) {
                            forgetmima=cursor2.getString(2);
                            flag2 = true;
                        }
                        cursor2.moveToNext();
                    }
                    if (flag2==true){
                        AlertDialog.Builder ab = new AlertDialog.Builder(LoginActivity.this);
                        ab.setMessage("用户"+username.getText().toString().trim()+",您的密码是:"+forgetmima);
                        ab.create().show();
                        forgetmima="";
                        flag2 = false;
                    }else{
                        Toast.makeText(LoginActivity.this,"未注册过，请注册", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().equals("admin")&&pwd.getText().toString().equals("123")) {
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this,Course_ManagerActivity.class);
                    startActivity(intent);
                }else{
                    cursor = db.query(dbHelper.TB_Name, null, null, null, null, null, "uid ASC");
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        if (username.getText().toString().trim().equals(cursor.getString(1))
                                && pwd.getText().toString().trim().equals(cursor.getString(2))) {
                            flag = true;
                        }
                        cursor.moveToNext();
                    }
                    if (flag == true && inputCode.getText().toString().equals(showCode.getText())) {
                        Toast.makeText(LoginActivity.this, "欢迎回来，" + username.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                        flag = false;
                        Intent intent = new Intent();
                        intent.putExtra("name", username.getText().toString());
                        intent.setClass(LoginActivity.this, StudentActivity.class);
                        startActivity(intent);
                        //登陆成功时，存saveName
                        if(saveName.isChecked()==true){
                            //SharedPreferences sp2 = getSharedPreferences("sname",MODE_PRIVATE);
                            sname = username.getText().toString();
                            spwd = pwd.getText().toString();
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("sname",sname);
                            SharedPreferences.Editor editor1 = sp1.edit();
                            editor1.putString("spwd",spwd);
                            editor.commit();
                            editor1.commit();
                        }else{
                            SharedPreferences.Editor editor = sp.edit();
                            editor.clear();
                            editor.commit();
                        }
                    } else if (flag == true && !inputCode.getText().toString().equals(showCode.getText())) {
                        Toast.makeText(LoginActivity.this,"验证码错误", Toast.LENGTH_SHORT).show();
                    } else if (flag == false) {
                        Toast.makeText(LoginActivity.this, "用户名或密码不存在，登录失败！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ab = new AlertDialog.Builder(LoginActivity.this);
                ab.setMessage("您是否要退出？");
                ab.setPositiveButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(LoginActivity.this,"明智的选择",Toast.LENGTH_SHORT).show();
                    }
                });
                ab.setNegativeButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        LoginActivity.this.finish();//返回上一页面
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });
                ab.create().show();
            }
        });
    }

    //产生验证码
    public String yzm(){
        String str ="0,1,2,3,4,5,6,7,8,9,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z";
        String str2[] = str.split(",");
        Random rand = new Random();
        int index = 0;
        String randStr = "";
        for(int i=0;i<4;i++){
            index = rand.nextInt(str2.length-1);
            randStr += str2[index];
        }
        return randStr;
    }
    long firstTime = 0;

    //keyCode按键的编码，KeyEvent事件
    public boolean onKeyDown(int keyCode,KeyEvent event){
        long secondTime = System.currentTimeMillis();//时间间隔
        //选择返回键
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(secondTime-firstTime<2000){
                System.exit(0);
            }else{
                Toast.makeText(LoginActivity.this,"再按一次，程序退出!",Toast.LENGTH_SHORT).show();
                firstTime = System.currentTimeMillis();
            }
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}
