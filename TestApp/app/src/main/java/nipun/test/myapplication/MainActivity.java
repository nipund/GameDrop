package nipun.test.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView t = (TextView) findViewById(R.id.t1);
        t.setText("Hello!!!!!");
        Button b = (Button) findViewById(R.id.b1);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView newT = new TextView(MainActivity.this);
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy - HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());
                newT.setText("Created at " + currentDateandTime);
                newT.setPadding(10, 10, 10, 10);
                LinearLayout layout = (LinearLayout) findViewById(R.id.listlayout);
                layout.addView(newT);
            }
        });
        Button pb = (Button) findViewById(R.id.playButton);
        pb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
            }
        });
    }
}
