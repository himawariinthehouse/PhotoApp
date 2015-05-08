package com.example.photoapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.app.Activity;
import android.app.Instrumentation.ActivityResult;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final int TAKE_PHOTO = 1; 
	public static final int CROP_PHOTO = 2; 
	private Button takePhotoBn; 
	private ImageView showImage; 
	private Uri imageUri; //图片路径 
	private String filename; //图片名称 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        takePhotoBn=(Button) findViewById(R.id.button1);
        showImage = (ImageView) findViewById(R.id.imageView1); 
        takePhotoBn.setOnClickListener(new OnClickListener() {         
        	@Override        public void onClick(View v) {             //图片名称 时间命名             
        		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");             
        		Date date = new Date(System.currentTimeMillis());             
        		filename = format.format(date);             
        		//创建File对象用于存储拍照的图片 SD卡根目录                       
        		//File outputImage = new File(Environment.getExternalStorageDirectory(),test.jpg);             
        		//存储至DCIM文件夹             
        		File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);              
        		File outputImage = new File(path,filename+".jpg");            
        		try {                 if(outputImage.exists()) {                     
        			outputImage.delete();                 }                 
        		outputImage.createNewFile();             }
        		catch(IOException e) {                 e.printStackTrace();             }             //将File对象转换为Uri并启动照相程序             
        		imageUri = Uri.fromFile(outputImage);             
        		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE"); //照相          
        		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); //指定图片输出地址             
        		startActivityForResult(intent,TAKE_PHOTO); //启动照相             //拍完照startActivityForResult() 结果返回onActivityResult()函数    
        		}     }); 
//        if (savedInstanceState == null) {      
//        	getFragmentManager().beginTransaction()            
//        	.add(R.id.container, new PlaceholderFragment())            
//        	.commit();     } 
        	}

    
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	// TODO Auto-generated method stub
	super.onActivityResult(requestCode, resultCode, data);
	if (resultCode != RESULT_OK) {       
		Toast.makeText(MainActivity.this,  resultCode , Toast.LENGTH_SHORT).show();     
		return;      }   
	switch(requestCode) {   
	case TAKE_PHOTO:      
		Intent intent = new Intent("com.android.camera.action.CROP"); //剪裁  
		intent.setDataAndType(imageUri,"image/*");      
		   intent.putExtra("scale", true);         //设置宽高比例       
		   intent.putExtra("aspectX", 1);        
		   intent.putExtra("aspectY", 1);         //设置裁剪图片宽高    
		   intent.putExtra("outputX", 340);         
		   intent.putExtra("outputY", 340);        
		   intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);     
		   Toast.makeText(MainActivity.this, "剪裁图片", Toast.LENGTH_SHORT).show();         //广播刷新相册   
		   Intent intentBc = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);    
		   intentBc.setData(imageUri);         
		   this.sendBroadcast(intentBc);         
		   startActivityForResult(intent, CROP_PHOTO); //设置裁剪参数显示图片至ImageView        
		   break;   
		   case CROP_PHOTO:    
			   try {                 //图片解析成Bitmap对象      
				   Bitmap bitmap = BitmapFactory.decodeStream(            
						   getContentResolver().openInputStream(imageUri));    
				   Toast.makeText(MainActivity.this, imageUri.toString(), Toast.LENGTH_SHORT).show();        
				   showImage.setImageBitmap(bitmap); //将剪裁后照片显示出来    
				   } catch(FileNotFoundException e) {         
					   e.printStackTrace();         }       
			   break;   
			   default:      
				   break;     } 
			   }
	



}



    

