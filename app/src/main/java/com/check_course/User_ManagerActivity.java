package com.check_course;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User_ManagerActivity extends AppCompatActivity {


    private EditText inputName;
    private EditText inputPwd;
    private EditText inputAge;
    private Button select;
    private Button add;
    private Button delete;
    private Button update;
    private ListView lv;

    dbHelper dbHelper;
    String DB_Name="mydb";
    SQLiteDatabase db;
    Cursor cursor;
    ContentValues selCV;//插入数据

    private ArrayList<Map<String,Object>> data;//所有记录
    private Map<String,Object> item; //每一条记录
    private SimpleAdapter listAdapter; //适配器
    View view;

    String selId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__manager);

        inputName = (EditText) findViewById(R.id.editText3);
        inputAge = (EditText) findViewById(R.id.editText6);
        inputPwd = (EditText) findViewById(R.id.editText8);
        select = (Button) findViewById(R.id.button3);
        add = (Button) findViewById(R.id.button4);
        delete = (Button) findViewById(R.id.button5);
        update = (Button) findViewById(R.id.button13);
        lv = (ListView) findViewById(R.id.lv2);

        dbHelper = new dbHelper(this,DB_Name,null,1);
        db = dbHelper.getWritableDatabase();
        data = new ArrayList<Map<String, Object>>();//将data实例化

        dbFindAll(); //查询的方法
        //查询
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbFindAll();
            }
        });

        //添加
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbAdd();
                dbFindAll();
                inputName.setText("");
                inputPwd.setText("");
                inputAge.setText("");
            }
        });

        //删除
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbDelete();
                dbFindAll();
                inputName.setText("");
                inputPwd.setText("");
                inputAge.setText("");
                delete.setEnabled(false);
                update.setEnabled(false);
                inputName.setEnabled(true);
                Toast.makeText(User_ManagerActivity.this,"删除成功!",Toast.LENGTH_SHORT).show();
            }
        });

        //修改
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbUpdate();
                dbFindAll();
                inputName.setText("");
                inputPwd.setText("");
                inputAge.setText("");
                delete.setEnabled(false);
                update.setEnabled(false);
                inputName.setEnabled(true);
                Toast.makeText(User_ManagerActivity.this,"修改成功!",Toast.LENGTH_SHORT).show();
            }
        });

        //鼠标点击数据，可以获取相对应的数据，存入Map中
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                delete.setEnabled(true);
                update.setEnabled(true);
                inputName.setEnabled(false);
                Map<String,Object>listItem = (Map<String,Object>)lv.getItemAtPosition(i);
                inputName.setText((String)listItem.get("uname"));
                inputPwd.setText((String)listItem.get("pwd"));
                inputAge.setText((String)listItem.get("age"));
                String[] whereArgs = {String.valueOf(inputName.getText().toString())};
                cursor = db.query(dbHelper.TB_Name,null,"uname=?",whereArgs,null,null,null);
                cursor.moveToFirst();
                while(!cursor.isAfterLast()) {
                    selId = cursor.getString(0);
                    cursor.moveToNext();
                }
            }
        });
    }

    private void dbUpdate(){
        selCV = new ContentValues();//需要将数据存入到数据库中就要用到
        //selCV.put("uname",inputName.getText().toString());
        selCV.put("pwd",inputPwd.getText().toString());
        selCV.put("age",inputAge.getText().toString());
        //更新条件
        String whereClause = "uid=?";
        //更新条件的值
        String[] whereArgs={String.valueOf(selId)};
        db.update(dbHelper.TB_Name,selCV,whereClause,whereArgs);
    }

    private void dbDelete(){
        //删除条件
        String whereClause = "uid=?";
        //删除的值
        String[] whereArgs={String.valueOf(selId)};
        db.delete(dbHelper.TB_Name,whereClause,whereArgs);
    }

    private void dbAdd(){
        selCV = new ContentValues();//插入实例化
        selCV.put("uname",inputName.getText().toString());
        selCV.put("pwd",inputPwd.getText().toString());
        selCV.put("age",inputAge.getText().toString());

        int n=0;
        Cursor cursor1;
        cursor1 = db.query(dbHelper.TB_Name,null,"uname=?", new String[]{inputName.getText().toString()},null,null,null);
        cursor1.moveToFirst();
        if (!cursor1.isAfterLast()){
            n+=1;
            cursor1.moveToNext();
        }
        if (n>0){
            Toast.makeText(User_ManagerActivity.this,"用户名已存在，增加失败",Toast.LENGTH_SHORT).show();
        }else{
            long rowId = db.insert(dbHelper.TB_Name,null,selCV);
            if(rowId==-1){
                Toast.makeText(User_ManagerActivity.this,"发生未知错误，增加失败",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(User_ManagerActivity.this, "增加成功！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dbFindAll(){
        data.clear();//防止重复添加，每次清空
        cursor = db.query(dbHelper.TB_Name,null,null,null,null,null,null);//表名，列名，列的值，分组的列，分组的条件，排序的列，升序还是降序
        cursor.moveToFirst();//将游标移到第一个
        int num = 1;
        while(!cursor.isAfterLast()){
            String uid = cursor.getString(0);
            String name = cursor.getString(1);
            String pwd = cursor.getString(2);
            String age = cursor.getString(3);
            item = new HashMap<String, Object>();
            item.put("uid",num);
            item.put("uname",name);
            item.put("pwd",pwd);
            item.put("age",age);
            data.add(item);//将集合添加进list中
            num++;
            cursor.moveToNext();//游标下移
        }
        showList();
    }

    //显示所有的记录
    public void showList(){
        listAdapter = new SimpleAdapter(this,data,
                R.layout.sqllist,
                new String[]{"uid","uname","pwd","age"},
                new int[]{R.id.textView22,R.id.textView23,R.id.textView24,R.id.textView25});
        lv.setAdapter(listAdapter);
    }
}
