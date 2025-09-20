package com.raz.arnotebook;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class DbMaintainActivity extends AppCompatActivity {

    Intent txtmode;
    ;
    CheckBox dissecind, txtsecind;

    EditText txttittle, txtmsg;

    TextView txtView;
    Button butsave, butcancel;
    String edtittle, edmsg, edid, edtitle, eddesc, edsecind, edmode, edsave, recdetail;
    long recid;

    boolean success;

    NoteBkModel noteBkModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_maintain);

        //  Toast.makeText(DbMaintainActivity.this, "start db act", Toast.LENGTH_LONG).show();

        txttittle = (EditText) findViewById(R.id.ediTittle);
        txttittle.setTextColor(Color.WHITE);

        txtmsg = (EditText) findViewById(R.id.editMsg);
        dissecind = (CheckBox) findViewById(R.id.ch_Box);
        txtView = findViewById(R.id.textView3);
        butsave = (Button) findViewById(R.id.cmdsave);
        butcancel = findViewById(R.id.cmdcancel);

        txtmode = getIntent();

        edmode = txtmode.getStringExtra("wssmode");
        edtitle = txtmode.getStringExtra("tittle");
        eddesc = txtmode.getStringExtra("desc");
        edsecind = txtmode.getStringExtra("secind");
        edid = txtmode.getStringExtra("id");
        txtView.setText(edmode);


        //Toast.makeText(DbMaintainActivity.this, " edid = " + txtmode.getStringExtra("id"), Toast.LENGTH_LONG).show();


        if (!Objects.equals(edmode, "Add")) {
            txttittle.setText(edtitle);
            txttittle.setEnabled(false);
            txtmsg.setText(eddesc);
            txtmsg.setEnabled(false);
            if (edsecind.equals("1")) {
                //  Toast.makeText(DbMaintainActivity.this, "edsecind = " + edsecind, Toast.LENGTH_LONG).show();
                dissecind.setChecked(true);
            } else {
                dissecind.setChecked(false);
            }
            dissecind.setEnabled(false);
            butsave.setVisibility(View.GONE);


            //    Toast.makeText(DbMaintainActivity.this, "edmoded = " + edmode + "    edtitle = " + edtitle, Toast.LENGTH_LONG).show();

        }


        butcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(DbMaintainActivity.this, "Cancel by User....", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(DbMaintainActivity.this, ListActivity.class);
                startActivity(intent);
                finish();

            }
        });
        butsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String wssok = "y";

                dissecind = (CheckBox) findViewById(R.id.ch_Box);
                if (dissecind.isChecked()) {
                    edsecind = "1";
                } else {
                    edsecind = "0";
                }

                txttittle = (EditText) findViewById(R.id.ediTittle);
                if (txttittle.getText().toString().isEmpty()) {
                    Toast.makeText(DbMaintainActivity.this, "Title cannot be blank", Toast.LENGTH_LONG).show();
                    wssok = "N";
                }
                txtmsg = (EditText) findViewById(R.id.editMsg);
                if (txtmsg.getText().toString().isEmpty()) {
                    Toast.makeText(DbMaintainActivity.this, "Description cannot be blank", Toast.LENGTH_LONG).show();
                    wssok = "N";
                }

                try {
                    if (!wssok.equals("N")) {
                        //   Toast.makeText(DbMaintainActivity.this, "txttittle.getText().toString() " + txttittle.getText().toString(), Toast.LENGTH_LONG).show();
                        //  Toast.makeText(DbMaintainActivity.this, "txtmsg.getText().toString() " + txtmsg.getText().toString(), Toast.LENGTH_LONG).show();
                        //   Toast.makeText(DbMaintainActivity.this, "edsecind.toString() " + edsecind.toString(), Toast.LENGTH_LONG).show();
                        // Toast.makeText(DbMaintainActivity.this, "edmoode = " + edmode, Toast.LENGTH_LONG).show();


                        NoteBkModel noteBkModel = new NoteBkModel(-1, txttittle.getText().toString(), txtmsg.getText().toString(), edsecind.toString());

                        try (SQLITEDB sqlite = new SQLITEDB(DbMaintainActivity.this)) {


                            if (Objects.equals(edmode, "Add")) {
                                // Toast.makeText(DbMaintainActivity.this, "DOing Add   = " + edmode, Toast.LENGTH_LONG).show();

                                success = sqlite.AddRecord(noteBkModel);
                                if (success) {
                                    Toast.makeText(DbMaintainActivity.this, "You Have successfully Added this Record ", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(DbMaintainActivity.this, "Adding this Record is not successful....", Toast.LENGTH_LONG).show();
                                }
                            }
                            if (Objects.equals(edmode, "Edit")) {

                                Long updid = (long) noteBkModel.getId();

                                //  Toast.makeText(DbMaintainActivity.this, "Update = " + updid, Toast.LENGTH_LONG).show();

                                try {
                                    success = sqlite.updateRecord(Long.parseLong(edid), noteBkModel);
                                    if (success) {
                                        Toast.makeText(DbMaintainActivity.this, "You Have successfully updated " + noteBkModel.getTitle(), Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(DbMaintainActivity.this, "Adding this Record is not successful....", Toast.LENGTH_LONG).show();
                                    }
                                } catch (SQLiteConstraintException e) {
                                    // throw new RuntimeException(e);
                                    Toast.makeText(DbMaintainActivity.this, "This Title " + edtittle + " Already In System", Toast.LENGTH_LONG).show();
                                }
                            }

                        } catch (SQLiteConstraintException e) {
                            // throw new RuntimeException(e);
                            Toast.makeText(DbMaintainActivity.this, "This Title " + edtittle + " Already In System", Toast.LENGTH_LONG).show();
                        }

                        Intent intent = new Intent(DbMaintainActivity.this, ListActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Toast.makeText(DbMaintainActivity.this, "error adding ", Toast.LENGTH_LONG).show();
                    // noteBkModel = new NoteBkModel(-1, "error")
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.db_maintain, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // switch (id)
        {
            if (id == R.id.Edit) {
                txtView.setText("Edit");
                butsave.setVisibility(View.VISIBLE);
                txtmsg.setEnabled(true);
                txtmsg.setFocusable(true);
                dissecind.setEnabled(true);
                edmode = "Edit";
                // break;
            }
            if (id == R.id.Home) {
                Intent intent = new Intent(DbMaintainActivity.this, ListActivity.class);
                startActivity(intent);
                finish();
            }

            return super.onOptionsItemSelected(item);
        }
    }
}