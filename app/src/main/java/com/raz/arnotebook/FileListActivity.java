package com.raz.arnotebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

public class FileListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_list);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        TextView noFilesText = findViewById(R.id.nofiles_textview);

        String wsstype = getIntent().getStringExtra("wssmode");
        String path = getIntent().getStringExtra("path");
        assert path != null;
        File root = new File(path);
        // Toast.makeText(FileListActivity.this, "root = " + root, Toast.LENGTH_LONG).show();

        File[] filesAndFolders = root.listFiles();

     //   Toast.makeText(FileListActivity.this.getApplicationContext(), "before wssmode = " + wsstype, Toast.LENGTH_LONG).show();

        if (Objects.equals(wsstype, "selected")) {
        //    Toast.makeText(FileListActivity.this.getApplicationContext(), "at selected = " + filesAndFolders, Toast.LENGTH_LONG).show();
            Intent wssintent = new Intent(FileListActivity.this, BackupRestoreActivity.class);
            wssintent.putExtra("flsel", filesAndFolders[0].getAbsoluteFile());
            wssintent.putExtra("wssmode", "Selected");
            startActivity(wssintent);
            finish();
        } else {

            if (filesAndFolders == null || filesAndFolders.length == 0) {
                //          Toast.makeText(FileListActivity.this, "File[]= " + Arrays.toString(filesAndFolders), Toast.LENGTH_LONG).show();
                noFilesText.setVisibility(View.VISIBLE);
                return;
            }


            noFilesText.setVisibility(View.INVISIBLE);

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(new FileAdapter(getApplicationContext(), filesAndFolders));

        }
    }
}
