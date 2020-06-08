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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StudentActivity extends AppCompatActivity {

    private Button check,shuaxin;
    private ListView lv4;

    dbHelper dbHelper;
    String DB_Name="mydb";
    SQLiteDatabase db;
    Cursor cursor;

    private ArrayList<Map<String,Object>> data;//所有记录
    private Map<String,Object> item; //每一条记录
    private SimpleAdapter listAdapter; //适配器

    String selId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        check = (Button) findViewById(R.id.button14);
        shuaxin = (Button) findViewById(R.id.button18);
        lv4 = (ListView) findViewById(R.id.lv4);

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(StudentActivity.this,Course_StudentActivity.class);
                startActivity(intent);
            }
        });

        shuaxin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbFindAll();

            }
        });

        dbHelper = new dbHelper(this,DB_Name,null,1);
        db = dbHelper.getWritableDatabase();
        data = new ArrayList<Map<String, Object>>();//将data实例化

        dbFindAll(); //查询的方法//查询

        lv4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map<String,Object>listItem = (Map<String,Object>)lv4.getItemAtPosition(i);
                cursor = db.query(dbHelper.TB3_Name,null,null,null,null,null,null);
                cursor.moveToFirst();
                while(!cursor.isAfterLast()) {
                    selId = cursor.getString(0);
                    cursor.moveToNext();
                }
            }
        });
    }
    private void dbFindAll(){
        data.clear();//防止重复添加，每次清空
        cursor = db.query(dbHelper.TB3_Name,null,null,null,null,null,null);//表名，列名，列的值，分组的列，分组的条件，排序的列，升序还是降序
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
        lv4.setAdapter(listAdapter);
    }
}
