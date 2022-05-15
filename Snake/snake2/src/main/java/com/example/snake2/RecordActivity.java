package com.example.snake2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RecordActivity extends AppCompatActivity {

    private int score=0;
    private TextView textView;
    private Button button8;
    private File file;
    private static String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        button8 = findViewById(R.id.button8);
        textView = findViewById(R.id.textView2);
        score = GameView.getScore();
        path = String.valueOf(getDir("Score", MODE_PRIVATE));

        textView.setText(readFile());


        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeFile(0);
                textView.setText(readFile());
            }
        });
    }

    public static String readFile(){
        FileInputStream fis = null;
        String s = "";
        try {
            fis = new FileInputStream(path+"/Score.txt");
            int length = (int) new File(path+"/Score.txt").length();
            byte[] buffer = new byte[length];
            fis.read(buffer, 0, length);
            fis.close();


            for(int i = 0; i < buffer.length; i++)
            {
                s += (char)buffer[i];
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fis != null) {
                try {
                    fis.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
        return s;
    }

    public static void writeFile(int Score){
        String s= Integer.toString(Score);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path+"/Score.txt");
            byte[] buffer = s.getBytes();
            fos.write(buffer, 0, buffer.length);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(fos != null) {
                try {
                    fos.close();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }


}