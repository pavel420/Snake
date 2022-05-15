package com.example.snake2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SkinActivity extends AppCompatActivity {

    Button button4;
    Button button5;
    Button button6;
    Button button7;

    public static Colors colors = new Colors(Color.WHITE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin);

        button4=findViewById(R.id.button4);
        button5=findViewById(R.id.button5);
        button6=findViewById(R.id.button6);
        button7=findViewById(R.id.button7);

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colors.setColor(Color.WHITE);
                Toast.makeText(SkinActivity.this, "A white shell is installed!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SkinActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colors.setColor(Color.GREEN);
                Toast.makeText(SkinActivity.this, "A green shell is installed!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SkinActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colors.setColor(Color.BLUE);
                Toast.makeText(SkinActivity.this, "A blue shell is installed!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SkinActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colors.setColor(Color.YELLOW);
                Toast.makeText(SkinActivity.this, "A yellow shell is installed!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SkinActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public static int getColors(){
        return colors.getColor();
    }
}