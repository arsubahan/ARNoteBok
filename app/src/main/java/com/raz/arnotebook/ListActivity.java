package com.raz.arnotebook;


import static android.widget.AdapterView.*;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ListActivity extends AppCompatActivity {


    //  String lstdissecind;
    public String wssdel;

    String tsttittle;
    String tstmsg;
    String tstsecind;

    ListView noteList;
    ListView simpleList;

    boolean success;

    // ArrayAdapter notebkArr;
    SQLITEDB sqlitedb;

    DesignBaseAdapter notdesadapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        noteList = findViewById(R.id.listViewFromDB);

        sqlitedb = new SQLITEDB(ListActivity.this);

        List<NoteBkModel> displayAll = sqlitedb.getAll();

        notdesadapter = new DesignBaseAdapter(displayAll, this);
        noteList.setAdapter(notdesadapter);


        //Toast.makeText(ListActivity.this, " notdesadapter.." + notdesadapter , Toast.LENGTH_LONG).show();

        if (notdesadapter.getCount() != 0) {
            noteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    NoteBkModel clicknote = (NoteBkModel) parent.getItemAtPosition(position);
                    if (Objects.equals(wssdel, "Y")) {
                    //    Toast.makeText(ListActivity.this, "delete ok  = " + wssdel + "       click note = " + clicknote.toString(), Toast.LENGTH_LONG).show();

                        AlertDialog ntdialog = new AlertDialog.Builder(ListActivity.this)
                                .setTitle("Are You sure You want to Delete " + clicknote.getTitle())
                                .setMessage("Please Select Option")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        success = sqlitedb.deleteOne(clicknote);

                                        Toast.makeText(ListActivity.this, "You Have successfully Deleted " + clicknote.getTitle(), Toast.LENGTH_LONG).show();
                                        NoteOnListView();

                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(ListActivity.this, clicknote.getTitle() + " No Deleted ", Toast.LENGTH_LONG).show();
                                    }
                                }).show();
                        wssdel = "n";

                        //  NoteBkModel noteBkModel = new NoteBkModel(-1, clicknote .getTitle(), clicknote.getDesc(), clicknote.getSecind());

                        //  try (SQLITEDB sqlite = new SQLITEDB(ListActivity.this)) {


                        //  Intent intent = new Intent(ListActivity.this, ListActivity.class);
                        //  startActivity(intent);
                        //  finish();

                        //  }


                    } else {
                        if (clicknote.getSecind().equals("1")) {
                            Intent intent = new Intent(ListActivity.this, SecActivity.class);
                            intent.putExtra("wssmode", "View");
                            intent.putExtra("tittle", clicknote.getTitle());
                            intent.putExtra("desc", clicknote.getDesc());
                            intent.putExtra("secind", clicknote.getSecind());
                            intent.putExtra("id", Long.toString(clicknote.getId()));
                            startActivity(intent);
                            finish();

                        } else {


                            Intent wssintent = new Intent(ListActivity.this, DbMaintainActivity.class);
                            wssintent.putExtra("wssmode", "View");
                            wssintent.putExtra("tittle", clicknote.getTitle());
                            wssintent.putExtra("desc", clicknote.getDesc());
                            wssintent.putExtra("secind", clicknote.getSecind());
                            wssintent.putExtra("id", Long.toString(clicknote.getId()));

                            //	Toast.makeText(ListActivity.this, "from List : " + Long.toString(clicknote.getId()), Toast.LENGTH_LONG).show();

                            startActivity(wssintent);
                            finish();
                        }
                    }
                    NoteOnListView();
                }

                private void NoteOnListView() {
                    List<NoteBkModel> displayAll = sqlitedb.getAll();
                    notdesadapter = new DesignBaseAdapter(displayAll, ListActivity.this);
                    noteList.setAdapter(notdesadapter);
                }
            });

        } else {

            Toast.makeText(ListActivity.this, "Database Is EMPTY !!!!!!", Toast.LENGTH_LONG).show();

        } //en dof if list null
    }// end of on create


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.list, menu);
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
            if (id == R.id.Add) {
                Intent wssintent = new Intent(this, DbMaintainActivity.class);
                wssintent.putExtra("wssmode", "Add");
                startActivity(wssintent);
                // break;
            }

            if (id == R.id.Delete) {
                Toast.makeText(ListActivity.this, "Select Item", Toast.LENGTH_LONG).show();
                wssdel = "Y";
            }

            if (id == R.id.Exit) {
                Toast.makeText(ListActivity.this, "Leaving AR Note Book", Toast.LENGTH_LONG).show();
                finish();
            }
            return super.onOptionsItemSelected(item);
        }
    }
}