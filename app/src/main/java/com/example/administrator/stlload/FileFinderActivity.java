package com.example.administrator.stlload;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class FileFinderActivity extends AppCompatActivity {

    private ListView mFileList;
    private ArrayList<ListItem> lTemp = null;
    private String mRoot ="";
    private TextView mPath;
    private ListViewAdapter mAdapter = null;
    private String mDirPath;
    private Intent stlLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRoot = rootFind();  // devices find root
        mPath = (TextView)findViewById(R.id.tvPath);
        mFileList = (ListView)findViewById(R.id.filelist);
        mAdapter = new ListViewAdapter(this);
        getDir(mRoot);
        mFileList.setAdapter(mAdapter);
        stlLoad = new Intent(getApplicationContext(),STLViewActivity.class);

        mFileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                FileInfo fileInfo = (FileInfo)mAdapter.getItem(position);
                File file = new File(fileInfo.getFilePath());
                if(file.isDirectory()){
                    if(file.canRead()){
                        getDir(fileInfo.getFilePath());
                    }else{
                        Toast.makeText(FileFinderActivity.this, "can not read", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    stlLoad.putExtra("stl_path",fileInfo.getFilePath());
                    startActivity(stlLoad);
                }
            }
        });
    }


    private void getDir(String dirPath){
        mAdapter = new ListViewAdapter(this);
        mPath.setText("Location: " + dirPath);
        mDirPath = dirPath;

        lTemp = new ArrayList<ListItem>();

        File f = new File(dirPath);
        File[] files = f.listFiles();

        ListItem listItem = null;
        for(int i=0; i<files.length; i++){
            listItem = new ListItem();
            File file = files[i];
            if (file.isDirectory()){
                listItem.setName("./" + file.getName());
            }else{
                if(!FileUtil.isStlFile(file.getName())){
                    continue;
                }
                listItem.setName(file.getName());
            }
            listItem.setPath(file.getAbsolutePath());
            lTemp.add(listItem);
        }

        Collections.sort(lTemp, new Comparator<ListItem>() {

            @Override
            public int compare(ListItem lhs, ListItem rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });

        FileInfo fileInfo = null;
        if(!dirPath.equals(mRoot)){
            fileInfo = new FileInfo();
            fileInfo.setFileName("../");
            fileInfo.setFilePath(f.getParent());
            mAdapter.addItem(fileInfo);
        }

        for(int i=0; i<lTemp.size(); i++){
            File file = new File(lTemp.get(i).getPath());
            if(file != null){
                fileInfo = new FileInfo();
                if(file.isDirectory()){
                    fileInfo.setFileName(file.getName() + "/");
                    fileInfo.setFileSize("");
                    fileInfo.setFileIcon(R.drawable.folder_icon);
                }else{
                    fileInfo.setFileName(file.getName());
                    fileInfo.setFileSize(file.length()+"");
                    fileInfo.setFileIcon(R.drawable.stl_icon);
                }
                fileInfo.setFilePath(file.getAbsolutePath());

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.KOREA);
                Date date = new Date(file.lastModified());
                String fileDate = formatter.format(date);
                fileInfo.setFileDate(fileDate);
                mAdapter.addItem(fileInfo);
            }

        }
        mFileList.setAdapter(mAdapter);
    }

    private String rootFind(){
        String root = Environment.getExternalStorageDirectory().getAbsolutePath();
        int end = 0;
        if(root != null) {
            root.trim();

            end = root.indexOf('/', 1);
            root = root.substring(0, end);
        }else{
            root = "/storage";
        }
        return root;
    }

    @Override
    public void onBackPressed() {
        if(mRoot.equals(mDirPath)){            //if rootDirectory   app finish!!!!
            //Toast.makeText(FileFinderActivity.this, "bye~", Toast.LENGTH_SHORT).show();
            finish();
        }else{
            FileInfo fileInfo = (FileInfo)mAdapter.getItem(0);
            File file = new File(fileInfo.getFilePath());
            if(file.isDirectory()){
                if(file.canRead()){
                    getDir(fileInfo.getFilePath());
                }
            }
        }
    }

}
