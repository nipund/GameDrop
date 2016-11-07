package com.example.mosebach.gamedroplogin;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

public class EditorActivity extends AppCompatActivity {

    private TileView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        tv = (TileView) findViewById(R.id.tileView);
        tv.setOnTouchListener(new EditorTouchListener(tv, this));
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
            case R.id.next_element:
                tv.nextElement();
                tv.invalidate();
                return true;
            case R.id.saveLevel:
                Gson gson = new Gson();
                //Type aryType = new TypeToken<GameElement>(){}.getType();
                String json = gson.toJson(tv.elements);
                System.out.println(json);
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
                Drawable d = ContextCompat.getDrawable(getApplicationContext(), id);
                tv.elements.add(new GameElement(d, id, 100, 100, 100, 100, "Test"));
                tv.selectLastElement();
                tv.invalidate();
            }
        }
    }
}
