package com.example.android.miwok;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find the view that shows the numbers category
        TextView numbers = (TextView)findViewById(R.id.numbers);

        //Set a click listener on that view
        numbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create an intent to open the numbers activity
               Intent numbersIntent = new Intent(MainActivity.this, NumbersActivity.class);

               //Start the new activity
                startActivity(numbersIntent);
            }
        });

        //Find the view that shows the family category
        TextView family = (TextView)findViewById(R.id.family);

        //Set a click listener on that view
        family.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create an intent to open the family activity
                Intent familyIntent = new Intent(MainActivity.this, FamilyActivity.class);

                //Start the new activity
                startActivity(familyIntent);
            }
        });

        //Find the view that shows the colors category
        TextView colors = (TextView)findViewById(R.id.colors);

        //Set a click listener on that view
        colors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create an intent to open the colors activity
                Intent colorsIntent = new Intent(MainActivity.this, ColorsActivity.class);

                //Start the new activity
                startActivity(colorsIntent);
            }
        });

        //Find the view that shows the phrases category%
        TextView phrases = (TextView)findViewById(R.id.phrases);
        //Set a click listener on that view
        phrases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create an intent to open the phrases activity
                Intent phrasesIntent = new Intent(MainActivity.this, PhrasesActivity.class);

                //Start the new activity
                startActivity(phrasesIntent);
            }
        });
    }

}
