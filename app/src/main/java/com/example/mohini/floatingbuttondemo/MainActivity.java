package com.example.mohini.floatingbuttondemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HandlePermissions();
//        initialiseView();
    }

    private void HandlePermissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package" + getPackageName( )));

                startActivityForResult(intent, 12);
            }
            catch (Exception e){

            }
        } else {

            initialiseView( );
        }
    }

    private void initialiseView() {

        findViewById(R.id.notif_me).setOnClickListener(new View.OnClickListener( ) {

            @Override
            public void onClick(View v) {

                startService(new Intent(MainActivity.this, FloatingViewService.class));
                finish( );
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 12) {

            if (resultCode == RESULT_OK)
                initialiseView( );

            else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show( );
                finish( );
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
