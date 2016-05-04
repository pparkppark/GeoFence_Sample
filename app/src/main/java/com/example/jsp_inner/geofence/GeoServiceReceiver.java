package com.example.jsp_inner.geofence;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Environment;
import android.util.Log;

import com.skt.geofence.GeoConstData;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;

//import com.example.ui_tester_sh_system.MainActivity;
//import com.example.ui_tester_sh_system.manager_64_Activity;

//	Check In / Out / Stay 이벤트를 받기 위해서는 BroadcastReceiver를 등록해야 합니다.
//BroadcastReceiver를 상속받아 클래스를 만들어 줍니다//하기와 같이!!!

public class GeoServiceReceiver extends BroadcastReceiver {
	
	//public static Context mContext;
	public Context mContext = null;
	private final String TAG = "GeoFenceReceiver";
	private static int Ticker_count = 0;
	private int fenceId =0;
	private String eventName = null;
	private int fenceNumCnt =0;
	private static String all_message= "";
	 public static final String STRSAVEPATH = Environment.getExternalStorageDirectory()+"/GeoFenceSample/";
	 //public static final String STRSAVEPATH2 = Environment.getExternalStorageDirectory()+"/testfolder2/";
	 public static final String SAVEFILEPATH = "GeoFenceEventLog.txt";
	 //File dir = makeDirectory(STRSAVEPATH);
     //파일 생성
     //File file = makeFile(dir, (STRSAVEPATH+SAVEFILEPATH));   
	@Override
	public void onReceive(Context context, Intent intent) {
		
		this.mContext = context;
		
		String action = intent.getAction();
		//mContext = this;
		//Log.d(TAG, "onReceive:" + action);
		//Log.d(TAG, "intent:package = " + intent.getPackage());


		 //폴더 생성
        //File dir = makeDirectory(STRSAVEPATH);
        //파일 생성
        //File file = makeFile(dir, (STRSAVEPATH+SAVEFILEPATH));
        //절대 경로
        //Log.i(TAG, ""+getAbsolutePath(dir));
        //Log.i(TAG, ""+getAbsolutePath(file));
        
		if (intent.getAction().equals("com.skt.geofencesample.GEOEVENT")) {

			fenceId = intent.getIntExtra(GeoConstData.FENCE_ID, 0);
			eventName = intent.getStringExtra(GeoConstData.EVENT_TYPE);
			if ( eventName != null)
			{
				Log.d("SampleAPP","fence id ="+fenceId+",eventName="+eventName);
			
				Date date = new Date(System.currentTimeMillis());
			    SimpleDateFormat test = new SimpleDateFormat("MM-dd HH:mm:ss");
			    String testNow = test.format(date);

				String message = new String(testNow+" > Id ="+fenceId+",Event = "+eventName+"\n");
				
				all_message = all_message + message;
				
				Log.d("SampleAPP","all_message----"+all_message);
				//String content = new String(all_message);
				File file = new File(Environment.getExternalStorageDirectory() + "/GeoFenceSample/GeoFenceEventLog.txt");
				boolean deleted = file.delete();
				write(all_message);
				//writeFile(MainActivity.file , content.getBytes());
				
		        
//				Intent i = new Intent( context, MainActivity.class );
//				i.putExtra("fenceId", fenceId);
//				i.putExtra("eventName", eventName);
////				
//				PendingIntent pi = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_ONE_SHOT);
//				try {
//						  Log.d("send","send");
//				          pi.send();
//				} catch (CanceledException e) {
//				          e.printStackTrace();
//				}

			}
			//String eventName = intent.getStringExtra(GeoConstData.META_INFO);
			Intent content = new Intent(context, MainActivity.class);
			content.putExtra(GeoConstData.FENCE_ID, fenceId);
			content.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);

			PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0,  content, 0);

			NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			 
			android.support.v4.app.NotificationCompat.Builder mBuilder = new android.support.v4.app.NotificationCompat.Builder(context.getApplicationContext());
			mBuilder.setSmallIcon(R.drawable.logo);
			mBuilder.setContentTitle(eventName);
			mBuilder.setContentText("fence id"+ String.valueOf(fenceId) + "(Appid: " + context.getPackageName() + ")");
			mBuilder.setTicker("App Ticker ="+context.getPackageName()+",fenceid=" + String.valueOf(fenceId));
			mBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
			mBuilder.setAutoCancel(false);
			mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
            mBuilder.setContentIntent(resultPendingIntent);//required
			
			mNotificationManager.notify(Ticker_count, mBuilder.build());
//			//Calendar c = Calendar.getInstance();
//			//SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
//		   // String formattedDate = df.format(c.getTime());
//			
//			//eventInfoList.get(fenceNumCnt).fenceId = fenceId;
//			//eventInfoList.get(fenceNumCnt).time = df.format(c.getTime());;
//			//eventInfoList.get(fenceNumCnt).EventType = eventName;
			fenceNumCnt++;
//			
//			//Message msg = MainActivity.mHandler.obtainMessage(MainActivity.MSG_CHCEK , 0, 0, 0);
//			//MainActivity.mHandler.sendMessage(msg);
//			
			Ticker_count = Ticker_count+1;//
		        
		}
	}
	public void write( String message )
    {	
		
		 //File file = new File(Environment.getExternalStorageDirectory() + "/GeoFenceSample/GeoFenceEventLog.txt");
		 File direct = new File(Environment.getExternalStorageDirectory() + "/GeoFenceSample/");

		// 일치하는 폴더가 없으면 생성
		if( !direct.exists() ) {
			direct.mkdirs();
			
		}

		// txt 파일 생성
		//String testStr = "ABCDEFGHIJK...";
		//File savefile = new File(dirPath+"/test.txt");\\
		
		Log.d("message","message == "+all_message);
		File savefile = new File(Environment.getExternalStorageDirectory() + "/GeoFenceSample/GeoFenceEventLog.txt");
		try{
			FileOutputStream fos = new FileOutputStream(savefile);
			fos.write(all_message.getBytes());
			fos.close();
			
		} catch(IOException e){}

		
    }
	
	
	/**
     * 디렉토리 생성 
     * @return dir
     */
    private File makeDirectory(String dir_path){
        File dir = new File(dir_path);
        if (!dir.exists())
        {
            dir.mkdirs();
            Log.i( TAG , "!dir.exists" );
        }else{
            Log.i( TAG , "dir.exists" );
        }
 
        return dir;
    }
 
    /**
     * 파일 생성
     * @param dir
     * @return file 
     */
    private File makeFile(File dir , String file_path){
        File file = null;
        boolean isSuccess = false;
        if(dir.isDirectory()){
            file = new File(file_path);
            if(file!=null&&!file.exists()){
                Log.i( TAG , "!file.exists" );
                try {
                    isSuccess = file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally{
                    Log.i(TAG, "파일생성 여부 = " + isSuccess);
                }
            }else{
                Log.i( TAG , "file.exists" );
            }
        }
        return file;
    }
 
    /**
     * (dir/file) 절대 경로 얻어오기
     * @param file
     * @return String
     */
    private String getAbsolutePath(File file){
        return ""+file.getAbsolutePath();
    }
 
    /**
     * (dir/file) 삭제 하기
     * @param file
     */
    private boolean deleteFile(File file){
        boolean result;
        if(file!=null&&file.exists()){
            file.delete();
            result = true;
        }else{
            result = false;
        }
        return result;
    }
 
    /**
     * 파일여부 체크 하기
     * @param file
     * @return
     */
    private boolean isFile(File file){
        boolean result;
        if(file!=null&&file.exists()&&file.isFile()){
            result=true;
        }else{
            result=false;
        }
        return result;
    }
 
    /**
     * 디렉토리 여부 체크 하기
     * @param dir
     * @return
     */
    private boolean isDirectory(File dir){
        boolean result;
        if(dir!=null&&dir.isDirectory()){
            result=true;
        }else{
            result=false;
        }
        return result;
    }
 
    /**
     * 파일 존재 여부 확인 하기
     * @param file
     * @return
     */
    private boolean isFileExist(File file){
        boolean result;
        if(file!=null&&file.exists()){
            result=true;
        }else{
            result=false;
        }
        return result;
    }
     
    /**
     * 파일 이름 바꾸기
     * @param file
     */
    private boolean reNameFile(File file , File new_name){
        boolean result;
        if(file!=null&&file.exists()&&file.renameTo(new_name)){
            result=true;
        }else{
            result=false;
        }
        return result;
    }
     
    /**
     * 디렉토리에 안에 내용을 보여 준다.
     * @param file
     * @return
     */
    private String[] getList(File dir){
        if(dir!=null&&dir.exists())
            return dir.list();
        return null;
    }
 
    /**
     * 파일에 내용 쓰기
     * @param file
     * @param file_content
     * @return
     */
    private boolean writeFile(File file , byte[] file_content){
        boolean result;
        FileOutputStream fos;
        if(file!=null&&file.exists()&&file_content!=null){
            try {
                fos = new FileOutputStream(file);
                try {
                    fos.write(file_content);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            result = true;
        }else{
            result = false;
        }
        return result;
    }
 
    /**
     * 파일 읽어 오기 
     * @param file
     */
    private void readFile(File file){
        int readcount=0;
        if(file!=null&&file.exists()){
            try {
                FileInputStream fis = new FileInputStream(file);
                readcount = (int)file.length();
                byte[] buffer = new byte[readcount];
                fis.read(buffer);
                for(int i=0 ; i<file.length();i++){
                    Log.d(TAG, ""+buffer[i]);
                }
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
     
    /**
     * 파일 복사
     * @param file
     * @param save_file
     * @return
     */
    private boolean copyFile(File file , String save_file){
        boolean result;
        if(file!=null&&file.exists()){
            try {
                FileInputStream fis = new FileInputStream(file);
                FileOutputStream newfos = new FileOutputStream(save_file);
                int readcount=0;
                byte[] buffer = new byte[1024];
                while((readcount = fis.read(buffer,0,1024))!= -1){
                    newfos.write(buffer,0,readcount);
                }
                newfos.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            result = true;
        }else{
            result = false;
        }
        return result;
    }
}
