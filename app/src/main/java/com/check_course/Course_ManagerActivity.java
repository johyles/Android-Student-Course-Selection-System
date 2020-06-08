package com.check_course;

import android.content.ContentValues;
import android.content.Intent;
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

public class Course_ManagerActivity extends AppCompatActivity {

    private EditText inputBianhao;
    private EditText inputName;
    private EditText inputScore;
    private Button select;
    private Button add;
    private Button delete;
    private Button update;
    private Button User;
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
        setContentView(R.layout.activity_course_manager);

        inputBianhao= (EditText) findViewById(R.id.inputName);
        inputName = (EditText) findViewById(R.id.inputPwd);
        inputScore = (EditText) findViewById(R.id.editText7);
        select = (Button) findViewById(R.id.button9);
        add = (Button) findViewById(R.id.button10);
        delete = (Button) findViewById(R.id.button11);
        update = (Button) findViewById(R.id.button12);
        User= (Button) findViewById(R.id.button6);
        lv = (ListView) findViewById(R.id.lv);

        dbHelper = new dbHelper(this,DB_Name,null,1);
        db = dbHelper.getWritableDatabase();
        data = new ArrayList<Map<String, Object>>();//将data实例化

        dbFindAll(); //查询的方法//查询
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbFindAll();
            }
        });

        User.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Course_ManagerActivity.this,User_ManagerActivity.class);
                startActivity(intent);
            }
        });
        //添加
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbAdd();
                dbFindAll();
                inputBianhao.setText("");
                inputName.setText("");
                inputScore.setText("");
            }
        });

        //删除
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbDelete();
                dbFindAll();
                inputBianhao.setText("");
                inputName.setText("");
                inputScore.setText("");
                delete.setEnabled(false);
                update.setEnabled(false);
                inputName.setEnabled(true);
                Toast.makeText(Course_ManagerActivity.this,"删除成功!",Toast.LENGTH_SHORT).show();
            }
        });

        //修改
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbUpdate();
                dbFindAll();
                inputBianhao.setText("");
                inputName.setText("");
                inputScore.setText("");
                delete.setEnabled(false);
                update.setEnabled(false);
                inputName.setEnabled(true);
                Toast.makeText(Course_ManagerActivity.this,"修改成功!",Toast.LENGTH_SHORT).show();
            }
        });
        /*1.修改代码，实现界面显示连贯序号
        2.不能添加已存在的用户名
        3.控件内容为空的时候不能实现操作且有对应提示
        4.登录时勾选，可以记住用户名和密码*/

        //鼠标点击数据，可以获取相对应的数据，存入Map中
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                delete.setEnabled(true);
                update.setEnabled(true);
                inputBianhao.setEnabled(true);
                inputName.setEnabled(true);
                Map<String,Object>listItem = (Map<String,Object>)lv.getItemAtPosition(i);
                inputBianhao.setText((String)listItem.get("cid"));
                inputName.setText((String)listItem.get("cname"));
                inputScore.setText((String)listItem.get("cscore"));
                String[] whereArgs = {String.valueOf(inputName.getText().toString())};
                cursor = db.query(dbHelper.TB2_Name,null,"cname=?",whereArgs,null,null,null);
                cursor.moveToFirst();
                while(!cursor.isAfterLast()) {
                    selId = cursor.getString(1);
                    cursor.moveToNext();
                }
            }
        });
    }

    private void dbUpdate(){
        selCV = new ContentValues();//需要将数据存入到数据库中就要用到
        selCV.put("cid",inputBianhao.getText().toString());
        selCV.put("cname",inputName.getText().toString());
        selCV.put("cscore",inputScore.getText().toString());
        //更新条件
        String whereClause = "cid=?";
        //更新条件的值
        String[] whereArgs={String.valueOf(selId)};
        db.update(dbHelper.TB2_Name,selCV,whereClause,whereArgs);
    }

    private void dbDelete(){
        //删除条件
        String whereClause = "cid=?";
        //删除的值
        String[] whereArgs={String.valueOf(selId)};
        db.delete(dbHelper.TB2_Name,whereClause,whereArgs);
        db.delete(dbHelper.TB3_Name,whereClause,whereArgs);
    }

    private void dbAdd(){
        selCV = new ContentValues();//插入实例化
        selCV.put("cid",inputBianhao.getText().toString());
        selCV.put("cname",inputName.getText().toString());
        selCV.put("cscore",inputScore.getText().toString());

        int n=0;
        Cursor cursor1;
        cursor1 = db.query(dbHelper.TB2_Name,null,"cname=?", new String[]{inputName.getText().toString()},null,null,null);
        cursor1.moveToFirst();
        if (!cursor1.isAfterLast()){
            n+=1;
            cursor1.moveToNext();
        }
        if (n>0){
            Toast.makeText(Course_ManagerActivity.this,"课程已存在，增加失败",Toast.LENGTH_SHORT).show();
        }else{
            long rowId = db.insert(dbHelper.TB2_Name,null,selCV);
            if(rowId==-1){
                Toast.makeText(Course_ManagerActivity.this,"发生未知错误，增加失败",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(Course_ManagerActivity.this, "增加成功！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dbFindAll(){
        data.clear();//防止重复添加，每次清空
        cursor = db.query(dbHelper.TB2_Name,null,null,null,null,null,null);//表名，列名，列的值，分组的列，分组的条件，排序的列，升序还是降序
        cursor.moveToFirst();//将游标移到第一个
        int num = 1;
        while(!cursor.isAfterLast()){
            String uid = cursor.getString(0);
            String cid = cursor.getString(1);
            String cname = cursor.getString(2);
            String cscore = cursor.getString(3);
            item = new HashMap<String, Object>();
            item.put("id",num);
            item.put("cid",cid);
            item.put("cname",cname);
            item.put("cscore",cscore);
            data.add(item);//将集合添加进list中
            num++;
            cursor.moveToNext();//游标下移
        }
        showList();
    }

    //显示所有的记录
    public void showList(){
        listAdapter = new SimpleAdapter(this,data,
                R.layout.sqlist,
                new String[]{"id","cid","cname","cscore"},
                new int[]{R.id.textView14,R.id.textView19,R.id.textView20,R.id.textView21});
        lv.setAdapter(listAdapter);
    }
}