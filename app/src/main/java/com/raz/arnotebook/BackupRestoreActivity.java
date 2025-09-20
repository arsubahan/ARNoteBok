package com.raz.arnotebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
//.MANAGE_EXTERNAL_STORAGE;

import static android.os.Build.VERSION.SDK_INT;
import static java.util.Arrays.stream;


public class BackupRestoreActivity extends AppCompatActivity {

    ListView noteList;

    TextView txtmsg;

    SQLITEDB sqlitedb;

    NoteBkModel noteBkModel;

    DesignBaseAdapter notdesadapter;

    private static final int PERMISSION_REQUEST_CODE = 1234;
    private int STORAGE_PERMISSION_CODE = 1;

    public Calendar calendar;
    public SimpleDateFormat dateFormat;
    public String date;

    String[] content;
    String wsstype, wssflsel;
    boolean success;
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat wssdate = new SimpleDateFormat("ddMMMyyyy");

    //public String dirpath = Environment.getExternalStorageDirectory() + "/ARNoteBook_back", wsstype;

    String path = Environment.getExternalStorageDirectory().getPath();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_restore);
        txtmsg = findViewById(R.id.storage_view);
        Intent txtmode = getIntent();
        wsstype = txtmode.getStringExtra("wssmode");
        wssflsel = txtmode.getStringExtra("path");

        if (Objects.equals(wsstype, "selected")) {
            txtmsg.setText("Please wait  while Restoring Data  in Progress...");
        } else {
            txtmsg.setText("Please wait  while " + wsstype + "  in Progress...");
        }

        if (SDK_INT >= Build.VERSION_CODES.R) {

            if (Environment.isExternalStorageManager()) {
                // Toast.makeText(com.raz.arnotebook.BackupRestoreActivity.this, " wssflsele = " + wssflsel, Toast.LENGTH_LONG).show();
                // Toast.makeText(com.raz.arnotebook.BackupRestoreActivity.this, "    wsstype = " + wsstype, Toast.LENGTH_LONG).show();

                if (Objects.equals(wsstype, "selected")) {
                    //  Toast.makeText(com.raz.arnotebook.BackupRestoreActivity.this, "file selected raz = " + wssflsel, Toast.LENGTH_LONG).show();
                    process_restore(wssflsel);
                }
                if (Objects.equals(wsstype, "Restore")) {
                    Intent intent = new Intent(com.raz.arnotebook.BackupRestoreActivity.this, FileListActivity.class);
                    String path = Environment.getExternalStorageDirectory().getPath();
                    intent.putExtra("path", path);
                    startActivity(intent);
                } else if (Objects.equals(wsstype, "Backup")) {
                    try {
                        process_backup();
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            } else { //request for the permission
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
                Toast.makeText(com.raz.arnotebook.BackupRestoreActivity.this, "Storage permission ALLOWED", Toast.LENGTH_LONG).show();
            }
        } else {
            //below android 11=======
            startActivity(new Intent(this, MainActivity.class));
            ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    // backup files
    public void process_backup() throws FileNotFoundException {
        sqlitedb = new SQLITEDB(com.raz.arnotebook.BackupRestoreActivity.this);
        List<NoteBkModel> displayAll = sqlitedb.getAll();

        File dir = new File(Environment.getExternalStorageDirectory() + "/ARNoteBook_back/");
        if (!dir.exists()) {
            dir.mkdirs();
            //    Toast.makeText(getApplicationContext(), "diredctory  not exist : " + dir.getName() + "    path = " + path, Toast.LENGTH_LONG).show();
        }

        //   Toast.makeText(getApplicationContext(), "diredctory  not exist : " + dir.getName() + "    path = " + path, Toast.LENGTH_LONG).show();


        //  String content;
        String[] contentarr = new String[Math.toIntExact(displayAll.stream().count())];

        for (int i = 0; i < displayAll.stream().count(); i++) {
            contentarr[i] = displayAll.get(i).getTitle().trim() + "|" + displayAll.get(i).getDesc().trim() + "|" + displayAll.get(i).getSecind().trim() + "\n$*$*\n";
        }
        //  Toast.makeText(BackupRestoreActivity.this, "test = " + Arrays.toString(contentarr), Toast.LENGTH_LONG).show();

        Save_rec(contentarr);
    }

    private void Save_rec(String[] bknotedata) {
        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHMMSS", Locale.getDefault());
        String wssdate = sdf.format(currentDate);

        Toast.makeText(com.raz.arnotebook.BackupRestoreActivity.this, "wssdate = " + wssdate, Toast.LENGTH_LONG).show();

        File bkfile = new File(Environment.getExternalStorageDirectory() + "/ARNoteBook_back/" + wssdate + ".txt");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(bkfile);
            for (int i = 0; i < bknotedata.length; i++) {
                Toast.makeText(com.raz.arnotebook.BackupRestoreActivity.this, "data[" + i + "]= " + bknotedata[i], Toast.LENGTH_LONG).show();
                try {
                    fos.write(bknotedata[i].getBytes());
                    //if (i < bknotedata.length ) {
                    //   fos.write("\n".getBytes());
                    // }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(com.raz.arnotebook.BackupRestoreActivity.this, "error writing" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            Toast.makeText(com.raz.arnotebook.BackupRestoreActivity.this, "Backup successfully to  : ", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(com.raz.arnotebook.BackupRestoreActivity.this, ListActivity.class);
            startActivity(intent);
            finish();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // restore files
    public void process_restore(String filenm) {


        Toast.makeText(BackupRestoreActivity.this, "Please wait...Restoring Data", Toast.LENGTH_LONG).show();
        StringBuilder sb = new StringBuilder();

        try {
            File txtfile = new File(filenm);
            FileInputStream fis = new FileInputStream(txtfile);

            if (fis != null) {
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader buff = new BufferedReader(isr);

                String line = null;
                while ((line = buff.readLine()) != null) {
                    //  if(line.equals("$*$*")){
                    // Toast.makeText(this, "Reading sb = " + sb + "     -     Reading line = " + line, Toast.LENGTH_LONG).show();
                    //   sb.append( "\n");
                    // } else {
                    sb.append(line + "\n");
                    //  sb.append( "\n");
                    //  Toast.makeText(this, "Reading from External Storage = " + sb, Toast.LENGTH_LONG).show();
                    //  }
                }

                String[] restarr = sb.toString().split("\\$\\*\\$\\*");
                SQLITEDB sqlite = new SQLITEDB(BackupRestoreActivity.this);

                sqlite.deleteAll();

                //restore from backup file
                for (int i = 0; i < (restarr.length - 1); i++) {
                    //       Toast.makeText(BackupRestoreActivity.this, "Saved = " + restarr[i].toString(), Toast.LENGTH_LONG).show();
                    String[] wssdb = restarr[i].split("\\|");
                    //  for (int j = 0; j < wssdb.length; j++){
                    //  Toast.makeText(BackupRestoreActivity.this, "wssdb[1] = " + wssdb.length, Toast.LENGTH_LONG).show();
                    //   Toast.makeText(BackupRestoreActivity.this, "wssdb[" +  j + "] = " + wssdb[j].toString() , Toast.LENGTH_LONG).show();
                    //         "     wssdb[1] = " + wssdb[1].toString() + "     wssdb[2] = " + wssdb[2].toString(), Toast.LENGTH_LONG).show();
                    // if (wssdb[0] != "" ) {
                    //   Toast.makeText(BackupRestoreActivity.this, "wssdb[0] = " + wssdb[0].toString() + "      wssdb[1] = " + wssdb[1].toString() + "     wssdb[2] = " + wssdb[2].toString(), Toast.LENGTH_LONG).show();
                    NoteBkModel noteBkModel = new NoteBkModel(-1, wssdb[0].toString().trim(), wssdb[1].toString().trim(), wssdb[2].toString().trim());
                    success = sqlite.AddRecord(noteBkModel);
                    //myDb.insertRow(wssdb[0].toString().trim(), wssdb[1].toString().trim(), wssdb[2].toString().trim());
                    //  }
                }

                fis.close();
                Toast.makeText(this, "Successful Restoration of you Data", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(BackupRestoreActivity.this, ListActivity.class);
                startActivity(intent);
                finish();

            }


            Toast.makeText(this, "Successful Restoration of you Data", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error from  catch : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}// end of BackupRestoredb