package com.example.uploadpic;

import java.io.File;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener{
    private static final String TAG = "uploadImage";
    //这里写自己服务器后台的地址，后台源码会附上
    private static String requestURL = "http://119.29.136.149:8080/upload.jsp";
    private Button selectImage,uploadImage;
    private ImageView imageView;
    
    private String picPath = null;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        
        selectImage = (Button) this.findViewById(R.id.selectImage);

        uploadImage = (Button) this.findViewById(R.id.uploadImage);
        selectImage.setOnClickListener(this);
        uploadImage.setOnClickListener(this);
        
        imageView = (ImageView) this.findViewById(R.id.imageView);
        
        
    }
    
    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
        case R.id.selectImage:
            /***
             * 打开选择图片的工具
             * 想上传其他类型文件的用第三方文件浏览器
             */
            final Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, 1);
            break;
        case R.id.uploadImage:
            final File file = new File(picPath);
            if(file!=null)
            {
                
				//final String request = UploadUtil.uploadFile( file, requestURL);
				new AsyncTask<Void, Void, String>(){

					@Override
					protected String doInBackground(Void... arg0) {
						// TODO Auto-generated method stub
						final String request = UploadUtil.uploadFile( file, requestURL);
						return request;
					}
					
					protected void onPostExecute(String result) {

						uploadImage.setText(result);
					};
				}.execute();
				
            }
            break;
        default:
            break;
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if(resultCode==Activity.RESULT_OK)
        {
            final Uri uri = data.getData();
            Log.e(TAG, "uri = "+ uri);

                final String[] pojo = {MediaStore.Images.Media.DATA};

                final Cursor cursor = managedQuery(uri, pojo, null, null,null);
                if(cursor!=null)
                {
                    final ContentResolver cr = this.getContentResolver();
                    final int colunm_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    final String path = cursor.getString(colunm_index);
                    picPath = path;
                }
            }

        super.onActivityResult(requestCode, resultCode, data);
    }
    
}
