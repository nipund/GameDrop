package com.example.mosebach.gamedroplogin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class EditorActivity extends AppCompatActivity {

    private TileView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        tv = (TileView) findViewById(R.id.tileView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_element:
                Intent i = new Intent(this, ElementSelectorActivity.class);
                startActivityForResult(i, 1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                int position = data.getIntExtra("position", -1);
                int id = (int) data.getLongExtra("drawable_id", -1);
                Toast.makeText(this, "DEBUG: Selected element " + position + ", ID: " + id,
                        Toast.LENGTH_SHORT).show();
                Drawable d = ContextCompat.getDrawable(getApplicationContext(), id);
                tv.elements.add(new GameElement(d, 0, 0, 100, 100, "Test"));
                tv.invalidate();
            }
        }
    }
}
