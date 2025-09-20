package com.raz.arnotebook;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private final static String STORETEXT = "NBSECFL.txt";

    DBAdapter myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openDB();
        //  myDb.exportDB();
        myDb.getAllRows();
        //   Toast.makeText(MainActivity.this, " db...." + myDb.toString() , Toast.LENGTH_LONG).show();
        File file = getBaseContext().getFileStreamPath(STORETEXT);
        if (file.exists()) {
            readFileInEditor();
            //     Toast.makeText(MainActivity.this, " SecActivity.wsspin...." + SecActivity.wsspin , Toast.LENGTH_LONG).show();
            if (SecActivity.wsspin.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, SecActivity.class);
                startActivity(intent);
                finish();
            } else {
                final Button button = findViewById(R.id.cont);
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, ListActivity.class);
                        startActivity(intent);
                        finish();
                        // Toast.makeText(MainActivity.this, " got to list....", Toast.LENGTH_LONG).show();
                    }
                });

            }
        } else {
            Intent intent = new Intent(MainActivity.this, SecActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void openDB() {
        myDb = new DBAdapter(this);
        myDb.open();
    }

    private void closeDB() {
        myDb.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //switch (id)
        //  {

        if (id == R.id.Backup) {
            // Toast.makeText(MainActivity.this, " Back up...." , Toast.LENGTH_LONG).show();
            Intent bkintent = new Intent(this,BackupRestoreActivity.class);
            bkintent.putExtra("wssmode", "Backup");
            startActivity(bkintent);              //  break;

        }
        if (id == R.id.Restore) {

            //Toast.makeText(MainActivity.this, " restore....", Toast.LENGTH_LONG).show();
            //  Toast.makeText(MainActivity.this, " restore...." , Toast.LENGTH_LONG).show();
            Intent rsintent = new Intent(this,BackupRestoreActivity.class);
            rsintent.putExtra("wssmode", "Restore");
            startActivity(rsintent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void readFileInEditor() {
        try {
            InputStream in = openFileInput(STORETEXT);
            InputStreamReader tmp = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(tmp);
            SecActivity.wsspin = reader.readLine();
        } //end try
        catch (java.io.FileNotFoundException e) {
            // that's OK, we probably haven't created it yet
            Toast.makeText(MainActivity.this, "Exception: " + e, Toast.LENGTH_LONG).show();
        } // end catch
        catch (Throwable t) {
            Toast.makeText(MainActivity.this, "Exception: " + t, Toast.LENGTH_LONG).show();
        }  // end catch
        //		return wssind;

    } // end read file

}