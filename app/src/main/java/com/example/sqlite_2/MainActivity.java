package com.example.sqlite_2;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase pictureDatabase;
    private ImageView imageView1;
    private Button button1;
    private Button button2;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DBHelper(getApplicationContext(), "picture.db", null, 1);
        pictureDatabase = dbHelper.getWritableDatabase();

        imageView1 = (ImageView) findViewById(R.id.imageView_1);

        Cursor c = pictureDatabase.rawQuery("SELECT * FROM picture;", null);
        if (c == null) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.line1);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] bytes = baos.toByteArray();

            // 1. 第一種將照片存入SQLite的方法
            pictureDatabase.execSQL("INSERT INTO picture (picture) VALUES (?)", new byte[][]{bytes});

            // 2.第二種將照片存入SQLite的方法
            //        ContentValues cv = new ContentValues();
            //        cv.put( "picture", bytes );
            //        long cnt = dataBase.insert( "picture_detail", null, cv );

            Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.line2);
            ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, baos2);
            byte[] bytes2 = baos2.toByteArray();
            ContentValues cv = new ContentValues();
            cv.put("picture", bytes2);
            long cnt = pictureDatabase.insert("picture", null, cv);
        }

        button1 = (Button)findViewById(R.id.button_1);
        button2 = (Button)findViewById(R.id.button_2);

        button1.setOnClickListener(new View.OnClickListener() {
            private byte[] blob;
            @Override
            public void onClick(View v) {
                Cursor c = pictureDatabase.rawQuery("SELECT * FROM picture WHERE picture_id=1;", null);
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    blob = c.getBlob(1);
                }
                Bitmap bm = BitmapFactory.decodeByteArray(blob, 0, blob.length);
                imageView1.setImageBitmap(bm);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            private byte[] blob;
            @Override
            public void onClick(View v) {
                Cursor c = pictureDatabase.rawQuery("SELECT * FROM picture WHERE picture_id=2;",null);
                if(c.getCount()>0){
                    c.moveToFirst();
                    blob = c.getBlob(1);
                }
                Bitmap bm = BitmapFactory.decodeByteArray(blob,0,blob.length);
                imageView1.setImageBitmap(bm);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pictureDatabase.close();
        dbHelper.close();
    }
}