package com.example.mosebach.gamedroplogin;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

public class ElementSelectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element_selector);
        GridView gridview = (GridView) findViewById(R.id.element_selector_grid);
        gridview.setAdapter(new ElementAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("position", position);
                returnIntent.putExtra("drawable_id", id);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

    }
}
