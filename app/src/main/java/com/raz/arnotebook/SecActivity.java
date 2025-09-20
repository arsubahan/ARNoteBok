package com.raz.arnotebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class SecActivity extends AppCompatActivity {
    private final static String STORETEXT = "NBSECFL.txt";

    EditText txtpin;
    EditText txtreconf;

    TextView txtview1;
    TextView txtview2;
    TextView txtview3;

    public static String wsspin;

    Intent txtmode;
    String edtittle, edmsg, edid, edtitle, eddesc, edsecind, edmode, edsave, recdetail;
    String wssind;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sec);

        File file = getBaseContext().getFileStreamPath(STORETEXT);

        final Button button = findViewById(R.id.cmdcancel);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SecActivity.this, ListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        if (file.exists()) {
            readFileInEditor();

            txtreconf = (EditText) findViewById(R.id.editreconfpin);
            txtreconf.setVisibility(View.GONE);

            txtview2 = (TextView) findViewById(R.id.textView2);
            txtview2.setVisibility(View.GONE);

            txtview3 = (TextView) findViewById(R.id.textView3);
            txtview3.setVisibility(View.GONE);

            txtpin = (EditText) findViewById(R.id.editpin);
            txtpin.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    // TODO Auto-generated method stub
                    //if (event.getAction() == KeyEvent.ACTION_DOWN)
                    //{
                    Bundle bundle = getIntent().getExtras();
                    if (txtpin.getText().toString().equals(wsspin.toString())) {
                        txtmode = getIntent();
                        edtitle = txtmode.getStringExtra("tittle");
                        eddesc = txtmode.getStringExtra("desc");
                        edsecind = txtmode.getStringExtra("secind");
                        edid = txtmode.getStringExtra("id");

                        Intent wssintent = new Intent(SecActivity.this, DbMaintainActivity.class);
                        wssintent.putExtra("wssmode", "View");
                        wssintent.putExtra("tittle",  edtitle);
                        wssintent.putExtra("desc",  eddesc);
                        wssintent.putExtra("secind", edsecind);
                        wssintent.putExtra("id",  edid);
                        startActivity(wssintent);
                        finish();

                      //  Toast.makeText(SecActivity.this, "from security : " + edtitle, Toast.LENGTH_LONG).show();

                        startActivity(wssintent);
                        finish();



                    } else {
                        Toast.makeText(SecActivity.this, " Invalid PIN Entered....", Toast.LENGTH_LONG).show();

                    }


                    return false;
                }

            });

        } else {

            txtview1 = (TextView) findViewById(R.id.textView1);
            txtview1.setText("Please Create Your Pin ");

            txtpin = (EditText) findViewById(R.id.editpin);
            txtpin.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    // TODO Auto-generated method stub
                    //if (event.getAction() == KeyEvent.ACTION_DOWN)
                    //{
                    //	Toast.makeText(SecActivity.this, " exist no  pin =" + txtpin.getText().toString() + "   wsspin = " + wsspin, Toast.LENGTH_LONG).show();


                    txtreconf = (EditText) findViewById(R.id.editreconfpin);
                    txtreconf.setOnEditorActionListener(new TextView.OnEditorActionListener() {

                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            // TODO Auto-generated method stub
                            ////if (event.getAction() == KeyEvent.ACTION_DOWN)
                            ////{
                            //	Toast.makeText(getBaseContext(), txtreconf.getText() + " exist no  pin =" + txtpin.getText(), Toast.LENGTH_LONG).show();

                            // Perform action on key press
                            if (txtpin.getText().toString().equals(txtreconf.getText().toString())) {
                                try {
                                    if (txtpin.getText().toString().trim().length() == 0 || txtreconf.getText().toString().trim().length() == 0) {
                                        Toast.makeText(getBaseContext(), "PIN or Reconfirm PIN cannot be blank....", Toast.LENGTH_LONG).show();
                                    } else {
                                        OutputStreamWriter out = new OutputStreamWriter(openFileOutput(STORETEXT, MODE_PRIVATE));
                                        out.write(txtpin.getText().toString() + "\n");
                                        out.close();

                                        //Toast.makeText(getBaseContext(), txtreconf.getText() + "   pin =" + txtpin.getText(), Toast.LENGTH_LONG).show();
                                        final Intent wssintent = new Intent(SecActivity.this, MainActivity.class);
                                        startActivity(wssintent);
                                        finish();

                                    }

                                } catch (FileNotFoundException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }

                            } else {
                                Toast.makeText(getBaseContext(), "PIN and Confirm PIN not the same....." + txtreconf.getText() + "   pin =" + txtpin.getText(), Toast.LENGTH_LONG).show();

                            }
                            //}
                            return false;
                        }
                    });
                    return false;
                }
            });
        }
    }

    private void readFileInEditor() {
        try {
            InputStream in = openFileInput(STORETEXT);
            InputStreamReader tmp = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(tmp);
            wsspin = reader.readLine();
        } //end try
        catch (java.io.FileNotFoundException e) {
            // that's OK, we probably haven't created it yet
            Toast.makeText(SecActivity.this, "Exception: " + e.toString(), Toast.LENGTH_LONG).show();
        } // end catch
        catch (Throwable t) {
            Toast.makeText(SecActivity.this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }  // end catch
        //		return wssind;

    } // end read file


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.sec, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}