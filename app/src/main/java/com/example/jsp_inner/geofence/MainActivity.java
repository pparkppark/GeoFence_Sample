package com.example.jsp_inner.geofence;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;

import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.skt.geofence.AppData;
import com.skt.geofence.GeoConstData;
import com.skt.geofence.GeoFenceSyncState;
import com.skt.geofence.agent.service.AgentManager;
import com.skt.geofence.agent.service.GeoFenceAgentListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.ArrayList;

public class MainActivity extends Activity {
    // sample
    private final static String tdcProjectKey = "asdfasdf-976f-ffff-asdf-4df52basdfsdf";
    static AgentManager mAgentManager = null;
    private static Button mBtnTitle_storegroupmap, mBtnAddstoregroupmap, mBtnUpdatestoregroupmap, mBtnDeletestoregroupmap;
    private static Button mBtnTitle_storegroup, mBtnGetstoregouplist, mBtnAddstoregroup, mBtnUpdatestoregroup, mBtnGetstoregroup, mBtnDeletestoregroup;
    private static Button mBtnTitle_store, mBtnGetstorelist, mBtnAddstore, mBtnUpdatestore, mBtnGetstore, mBtnDeletestore;
    private static Button mBtnTitle_event, mBtnGeteventlist, mBtnAddevent, mBtnUpdateevent, mBtnGetevent, mBtnDeleteevent;
    private static Button mBtnTitle_eventstore, mBtnAddeventstore, mBtnUpdateeventstore, mBtnDeleteeventstore;
    private static Button mBtnSyncDB, mBtnAppSync, mBrnCheckEnviroment, mBtnsettingAccuracy;
    private static boolean onServiceConnected_flag = false;
    private static EditText mLat, mLon;
    private static TextView mWarining_message;
    private static TextView mEvent_message;
    private static String all_message = null;
    private static AlertDialog mDialog = null;
    private static RadioGroup mRdg;
    private static Context mContext;
    private static String TAG = "GeoFenceSample";
    private static boolean add_store_group_map_flag = false;
    private static boolean add_store_group_flag = false;
    private static boolean add_store_flag = false;
    //private TextView get_id_text,get_text;
    private static boolean add_event_flag = false;
    private static boolean add_event_store_flag = false;
    private static String Store_Group_name = null;
    private static String Store_name = null;
    private static String Event_name = null;
    private static String Store_Group_ID = null;
    private static String Store_ID = null;
    private static String Event_Group_ID = null;
    private static String Event_ID = null;
    private static String latitude = null, Longitude = null;
    JSONStringer jParser = new JSONStringer();
    private String mPakacgeName;
    private int mIndex = 1;
    private int mLevel = 2;
    private GeoFenceAgentListener mAgentListener = new GeoFenceAgentListener() {

        @Override
        public void onError(int errCode, String errMsg) {
            Log.d(TAG, "onError errCode = " + errCode);
            Log.d(TAG, "onError errMsg = " + errMsg);
        }

        @Override
        public void onResponseReceived(int reusltCode, String resultData) {


            Log.e(TAG, "------------onResponseReceived--------------");
            Log.e(TAG, resultData);

            if (resultData == null) {
                Log.d(TAG, "resultData is null...please check your option");
            } else {

                final String return_code = String.valueOf(reusltCode);
                final String return_data = resultData;
                if (reusltCode == 200) {
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            Looper.prepare();
//                            Toast.makeText(getApplicationContext(), "성공입니다 : " + String.valueOf(return_code) + ":" + return_data, Toast.LENGTH_LONG).show();
//                            Looper.loop();
//                        }
//                    }.start();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(), "성공입니다 : " + String.valueOf(return_code) + ":" + return_data, Toast.LENGTH_LONG).show();
                        }
                    });

                } else {
//                    new Thread() {
//                        @Override
//                        public void run() {
//                            Looper.prepare();
//                            mWarining_message.setText("실패입니다 : " + String.valueOf(return_code) + ":" + return_data);
//                            Looper.loop();
//                        }
//                    }.start();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mWarining_message.setText("실패입니다 : " + String.valueOf(return_code) + ":" + return_data);
                        }
                    });

                    //Toast.makeText(getApplicationContext(), "실패입니다 : return code = "+reusltCode , Toast.LENGTH_LONG).show();
                    return;
                }

                Log.d(TAG, "onResponseReceived reusltCode = " + reusltCode);
                Log.d(TAG, "onResponseReceived resultData = " + resultData);

                try {
                    JSONObject json = new JSONObject(resultData);
                    String result_id = json.getString("data");

                    //Log.d(TAG, "onResponseReceived result_id = " + result_id);

                    if (add_store_group_flag == true) {
                        add_store_group_flag = false;
                        Store_Group_ID = result_id;
                    } else if (add_store_flag == true) {
                        add_store_flag = false;
                        Store_ID = result_id;
                    } else if (add_store_flag == true) {
                        add_store_flag = false;
                        Event_Group_ID = result_id;
                    } else if (add_event_flag == true) {
                        add_event_flag = false;
                        Event_ID = result_id;
                    }

                } catch (JSONException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }

        @Override
        public void onHttpStatusChanged(int httpState) {
            Log.e(TAG, "***** onHttpStatusChanged = " + httpState);


            if (httpState == 1) {
                new Thread() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "펜스정보 요청.", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }.start();

            } else if (httpState == 2) {
                new Thread() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        Toast.makeText(getApplicationContext(), "응답 수신.", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                }.start();

            }

        }

        @Override
        public void onSynchronizeStatusChanged(GeoFenceSyncState arg0, boolean arg1) {

        }

        @Override
        public void onServiceConnected() {
            Log.d(TAG, "onServiceConnected");

            onServiceConnected_flag = true;


            AppData appdata = new AppData();
            appdata.appID = ""; // not use
            //appdata.appName = "geofencesample";
            //appdata.packageName = mPakacgeName;
            appdata.appName = "geofencesample";
            appdata.packageName = mPakacgeName;
            appdata.tdcProjectKey = "de9cb1f7-7ef9-40ff-bdd4-6fa8e027e0b7";

            Log.d("[HC] mPakacgeName", mPakacgeName);

            appdata.tdcProjectKey = tdcProjectKey;

            if (mAgentManager != null) {
                mAgentManager.setAppData(appdata);
            }
        }
    };

    public static void set_event_message(String message) {

        if (message != null)
            all_message = all_message + message;

        if (all_message != null)
            mEvent_message.setText(all_message);
    }

    public static void onReceivingDbRemoveBr() {

        //final AlertDialog mDialog = null;

        AlertDialog.Builder ab = new AlertDialog.Builder(mContext);
        ab.setTitle("Title");
        ab.setMessage("내용");
        ab.setCancelable(false);
        ab.setIcon(mContext.getResources().getDrawable(R.drawable.logo));

        ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            //@Override
            public void onClick(DialogInterface arg0, int arg1) {
                mDialog.dismiss();
            }
        });

        mWarining_message.setText("경고! GeoService 의 DB 파일이 강제 삭제되었습니다.!" +
                "\r\n " +
                "\r\n 다시 WebPoc 동기화를 시작하시기 바랍니다. ");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        mPakacgeName = this.getClass().getPackage().getName();
        all_message = "";
        //try {
        //	versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        //} catch (NameNotFoundException e1) {
        // TODO Auto-generated catch block
        //	e1.printStackTrace();
        //}

        //TextView version_text = (TextView)findViewById(R.id.title_with_version);
        mWarining_message = (TextView) findViewById(R.id.warning_message);
        mEvent_message = (TextView) findViewById(R.id.event_message);
        //version_text.setText(" GeoFenceSample : ver "+versionName + "\r\n");


        //mBtnSyncDB = (Button) findViewById(R.id.sync_db);
        //mBtnSyncDB.setOnClickListener(mClickListener);

        //mBrnTutorial = (Button) findViewById(R.id.tutorial);
        //mBtnsettingAccuracy = (Button) findViewById(R.id.set_accuracy);
        //mBrnTutorial.setOnClickListener(mClickListener);

        //mBrnCheckEnviroment = (Button) findViewById(R.id.check_sample);
        //mBrnCheckEnviroment.setOnClickListener(mClickListener);

        mLat = (EditText) findViewById(R.id.latitude);
        mLon = (EditText) findViewById(R.id.longitude);

        mLat.setVisibility(View.GONE);
        mLon.setVisibility(View.GONE);

        mBtnTitle_storegroupmap = (Button) findViewById(R.id.title_storegroupmap);
        mBtnAddstoregroupmap = (Button) findViewById(R.id.addstoregroupmap);
        mBtnUpdatestoregroupmap = (Button) findViewById(R.id.updatestoregroupmap);
        mBtnDeletestoregroupmap = (Button) findViewById(R.id.deletestoregroupmap);

        mBtnTitle_storegroup = (Button) findViewById(R.id.title_storegroup);
        mBtnGetstoregouplist = (Button) findViewById(R.id.getstoregrouplist);
        mBtnAddstoregroup = (Button) findViewById(R.id.addstoregroup);
        mBtnUpdatestoregroup = (Button) findViewById(R.id.updatestoregroup);
        mBtnGetstoregroup = (Button) findViewById(R.id.getstoregroup);
        mBtnDeletestoregroup = (Button) findViewById(R.id.deletestoregroup);

        mBtnTitle_store = (Button) findViewById(R.id.title_store);
        mBtnGetstorelist = (Button) findViewById(R.id.getstorelist);
        mBtnAddstore = (Button) findViewById(R.id.addstore);
        mBtnUpdatestore = (Button) findViewById(R.id.updatestore);
        mBtnGetstore = (Button) findViewById(R.id.getstore);
        mBtnDeletestore = (Button) findViewById(R.id.deletestore);

        mBtnTitle_event = (Button) findViewById(R.id.title_event);
        mBtnGeteventlist = (Button) findViewById(R.id.geteventlist);
        mBtnAddevent = (Button) findViewById(R.id.addevent);
        mBtnUpdateevent = (Button) findViewById(R.id.updateevent);
        mBtnGetevent = (Button) findViewById(R.id.getevent);
        mBtnDeleteevent = (Button) findViewById(R.id.deleteevent);

        mBtnTitle_eventstore = (Button) findViewById(R.id.title_eventstore);
        mBtnAddeventstore = (Button) findViewById(R.id.addeventstore);
        mBtnUpdateeventstore = (Button) findViewById(R.id.updateeventstore);
        mBtnDeleteeventstore = (Button) findViewById(R.id.deleteeventstore);

        mBtnSyncDB = (Button) findViewById(R.id.sync_db);
        mBtnAppSync = (Button) findViewById(R.id.app_sync);
        mBrnCheckEnviroment = (Button) findViewById(R.id.check_sample);
        mBtnsettingAccuracy = (Button) findViewById(R.id.set_accuracy);


        all_button_disabled();

        mBtnSyncDB.setEnabled(false);
        mBtnAppSync.setEnabled(false);
        mBtnsettingAccuracy.setEnabled(false);
        mBrnCheckEnviroment.setEnabled(true);

        mLat.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                latitude = mLat.getText().toString();
            }
        });
        mLon.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void afterTextChanged(Editable s) {
                Longitude = mLon.getText().toString();
            }
        });


        Toast.makeText(getApplicationContext(), "잠시만 기다리세요.초기화중입니다 ", Toast.LENGTH_LONG).show();

        //beforeOnconnect();

        if (mAgentManager == null) {
            mAgentManager = AgentManager.getInstance();
        }


        if (mAgentManager != null) {
            try {
                mAgentManager.initialize(this, mAgentListener);
            } catch (AgentManager.GeoFenceAgentException e) {
                e.printStackTrace();
            }
        }


        mWarining_message.setText(" GeoFenceSample apk 의 준비가 완료되었습니다." +
                "\r\n " +
                "\r\n설정확인을 누르시면 필요한 설정이 되어있는지 확인합니다.");
    }

    private void all_button_disabled() {
        mBtnTitle_storegroupmap.setEnabled(false);
        mBtnAddstoregroupmap.setEnabled(false);
        mBtnUpdatestoregroupmap.setEnabled(false);
        mBtnDeletestoregroupmap.setEnabled(false);

        mBtnTitle_storegroup.setEnabled(false);
        mBtnGetstoregouplist.setEnabled(false);
        mBtnAddstoregroup.setEnabled(false);
        mBtnUpdatestoregroup.setEnabled(false);
        mBtnGetstoregroup.setEnabled(false);
        mBtnDeletestoregroup.setEnabled(false);

        mBtnTitle_store.setEnabled(false);
        mBtnGetstorelist.setEnabled(false);
        mBtnAddstore.setEnabled(false);
        mBtnUpdatestore.setEnabled(false);
        mBtnGetstore.setEnabled(false);
        mBtnDeletestore.setEnabled(false);

        mBtnTitle_event.setEnabled(false);
        mBtnGeteventlist.setEnabled(false);
        mBtnAddevent.setEnabled(false);
        mBtnUpdateevent.setEnabled(false);
        mBtnGetevent.setEnabled(false);
        mBtnDeleteevent.setEnabled(false);

        mBtnTitle_eventstore.setEnabled(false);
        mBtnAddeventstore.setEnabled(false);
        mBtnUpdateeventstore.setEnabled(false);
        mBtnDeleteeventstore.setEnabled(false);

    }

    @Override
    protected void onResume() {

        super.onResume();
    }

    public boolean validate_sample_sate() {


        //data available 확인//태블릿에서 에러가 발생한다....나중에 좀 수정해야 할듯.
        if (!isOnline()) {
            Toast.makeText(getApplicationContext(), "네크웍이 연결되지 않았습니다. 확인후 다시 시도하세요 ", Toast.LENGTH_LONG).show();
            return false;
        }

        // 와이파이 ON/OFF확인
        //if(! isWifiTurnON()){
        //	 Toast.makeText(getApplicationContext(), "WIFI 가 ON 되지 않았습니다. On 후 다시 시도하세요 ", Toast.LENGTH_SHORT).show();
        //	 return false;
        //}

        //GeofenceService apk 가 install 되어있는지 확인
        Log.d(TAG, "GeofenceService apk install check");
        if (!(isServiceRunning("com.skt.geofence.GeoFenceAgent")
                && isServiceRunning("com.skt.geofence.GeoFenceService"))
                ) {
            mWarining_message.setText("GeoFenceService 가 인스톨되지 않았습니다." +
                    "\r\n 먼저 GeoFenceService apk 를 인스톨해 주십시요.");
            return false;
        }

        //타사폰 or simcard 없는 SKT 폰 or WIFI-only 폰의 경우 Google service player 가 install 되어있는지 확인
        Log.d(TAG, "GooglePlayService apk install check");
        if (!checkSktImsi()) { //타사폰 or simcard 없는 SKT 폰 or WIFI-only 폰
            Log.d(TAG, "this device is not a SKT device or SKT device without sim card");
            final int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

            LocationManager locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);

            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
//			 //       //All location services are disabled
                mWarining_message.setText("Google location service 가 Enable 되지 않았습니다."
                        + "\r\n귀하의 폰은 SKT 폰이 아니거나 Simcard 가 없는 SKT 폰입니다."
                        + "\r\n 귀하의 폰은 GPS 를 on 하거나, 설정-위치서비스-내 위치 정보 사용 를 check 하셔야 합니다.");
                return false;
            }

            if (result != ConnectionResult.SUCCESS) {

                mWarining_message.setText("Google Play service 가 인스톨되지 않았습니다."
                        + "\r\n귀하의 폰은 SKT 폰이 아니거나 Simcard 가 없는 SKT 폰입니다."
                        + "\r\n 귀하의 폰은 Google service player 와 GeofenceService apk 가 미리 인스폴되어있어야 합니다.");
                return false;
            }
        } else
            Log.d(TAG, "this device is a SKT device with sim card");

        if (onServiceConnected_flag == false) {
            mWarining_message.setText("GeoFense Service 연결이 아직 되지 않았습니다. 조금후에 다시 시도해보세요."
                    + "만약 계속 연결이 되지 않는다면 리부팅을 하세요");
            return false;
        }
        return true;
    }

    //SKT 폰인지 chcek하는 함수
    protected boolean checkSktImsi() {
        TelephonyManager mTelManager;
        mTelManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);

        String imsi;

        if (mTelManager == null) {
            imsi = "0";
        } else {
            imsi = mTelManager.getSubscriberId();
            if (imsi == null) {
                imsi = "0";
            }
        }

        boolean isSktImsi = imsi.startsWith("45005");

        if (isSktImsi == true) {
            return true;
        } else {
            return false;
        }
    }

    //service available check
    public Boolean isServiceRunning(String serviceName) {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {

            //Log.d(TAG,"serviceName = "+serviceName);
            //Log.d(TAG,runningServiceInfo.service.getClassName());
            if (serviceName.contains(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

	/*
    public void hundred_fence(View v) {

		//data.put("latitude", 37.566463);//SKT타워 주소//테스트 코드
		//data.put("longitude", 126.985056);/

		lati = 37.566463;
		lon = 126.985056;



		beforeTutorial();

		for ( int i=0;i<5;i++){
			Log.d(TAG,"do i = "+i);
			Log.d(TAG,"do lati = "+lati);
			Log.d(TAG,"do lon = "+lon);
			beforeTutorial();
			mBrnTutorial.performClick();
			for ( int j=0;j<4;j++){
				mBtnAdd.performClick();
				Log.d(TAG,"do j = "+j);
				SystemClock.sleep(1000);
			}
			lati = (double)lati + (double)0.22;
			lon = 	(double)lon + (double)0.22;
		}
	}
	*/

    public void check_state_sample(View v) {

        Log.d(TAG, "check_environment");
        //Toast.makeText(getApplicationContext(), "서비스 check 를 시작합니다. ", Toast.LENGTH_SHORT).show();

        //if ( validate_sample_sate() )
        {
            mWarining_message.setText("서비스가 연결되었습니다! " +
                    "\r\n 측위 정확도를 선택하세요." +
                    "\r\n    1. Low 	 	- 기지국                  이용측위 " +
                    "\r\n    2. Middle  	- 기지국, WIFI      이용측위 " +
                    "\r\n    3. High 		- GPS, 기지국, WIFI 이용측위 " +
                    "\r\n\n  - 선택후에 튜토리얼을 시작하여 Fence 를 생성한후 WebPOC 동기화 버튼을 누르거나. " +
                    "\r\n     - WebPOC 서버에서 Fence 를 생성한후 WebPOC 동기화 버튼을 누르세요.");
            //settingAccuracy();
            beforestart();
        }
    }

    void beforestart() {
        mBtnTitle_storegroup.setEnabled(true);
        mBtnTitle_store.setEnabled(true);
        mBtnTitle_event.setEnabled(true);
        mBtnTitle_storegroupmap.setEnabled(true);
        mBtnTitle_eventstore.setEnabled(true);

        mBtnSyncDB.setEnabled(true);
        mBtnAppSync.setEnabled(true);
        mBtnsettingAccuracy.setEnabled(true);
        mBrnCheckEnviroment.setEnabled(false);
    }
	/*
	private boolean isWifiTurnON() { //  WIFI turn on check
	 		ConnectivityManager cManager;
			NetworkInfo wifi;

			Log.d("isWifiTurnON","isWifiTurnON");
			cManager=(ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
			wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			if( wifi.isAvailable())
			{
				Log.d("return true","");
				return true;
			}
			else
				Log.d("return false","");

			return true;
	}*/

    public void set_accuracy(View v) {

        Log.d(TAG, "set_accuracy mPakacgeName = " + mPakacgeName);
        //Toast.makeText(getApplicationContext(), "서비스 check 를 시작합니다. ", Toast.LENGTH_SHORT).show();

        boolean result = false;
        if (mLevel == 0 || mLevel >= 3) {
            result = mAgentManager.setUserAccuracy(mPakacgeName, GeoConstData.ACCURACY_LEVEL_HIGH);
        } else if (mLevel == 1) {
            result = mAgentManager.setUserAccuracy(mPakacgeName, GeoConstData.ACCURACY_LEVEL_LOW);
        } else if (mLevel == 2) {
            result = mAgentManager.setUserAccuracy(mPakacgeName, GeoConstData.ACCURACY_LEVEL_MIDDLE);
        }

        Log.d(TAG, "set_accuracy2");

        if (result) {
            if (mLevel == 0 || mLevel == 3)
                Toast.makeText(getApplicationContext(), "측위정확도 = HIGH", Toast.LENGTH_LONG).show();
            else if (mLevel == 2)
                Toast.makeText(getApplicationContext(), "측취정확도 = MIDDLE", Toast.LENGTH_LONG).show();
            else if (mLevel == 1)
                Toast.makeText(getApplicationContext(), "측위정확도 = LOW", Toast.LENGTH_LONG).show();
        } else
            Toast.makeText(getApplicationContext(), "Set Accuracy failed", Toast.LENGTH_LONG).show();

        mLevel++;

        if (mLevel >= 3)
            mLevel = 0;

        //mWarining_message.setText("서비스가 연결되었습니다. " +
        //		"\r\n 1. 튜토리얼을 시작하여 Fence 를 생성한후 WebPOC 동기화 버튼을 누르거나. " +
        //		"\r\n 2. WebPOC 서버에서 Fence 를 생성한후 WebPOC 동기화 버튼을 누르세요.");
        //beforeTutorial();

    }

    private boolean isOnline() { // network 연결 상태 확인 check
        ConnectivityManager cManager;
        NetworkInfo mobile;
        NetworkInfo wifi;

        //Log.d("isOnline","");
        cManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        mobile = cManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        wifi = cManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mobile.isConnected() || wifi.isConnected()) {
            //Log.d("return true","");
            return true;
        }
        //Log.d("return false","");
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    //////////////////////Store Group map /////////////////////////

    @Override
    protected void onDestroy() {
        try {
            if (mAgentManager != null) {
                mAgentManager.release();
            }
        } catch (AgentManager.GeoFenceAgentException e) {
            e.printStackTrace();
        }

        super.onDestroy();
    }

    public void enable_storegroupmap(View v) {

        mBtnAddstoregroupmap.setEnabled(true);
        mBtnUpdatestoregroupmap.setEnabled(true);
        mBtnDeletestoregroupmap.setEnabled(true);
    }

    public void disable_storegroupmap() {

        mBtnAddstoregroupmap.setEnabled(false);
        mBtnUpdatestoregroupmap.setEnabled(false);
        mBtnDeletestoregroupmap.setEnabled(false);
    }

    //1.1
    public void set_storegroupmap(View v) throws JSONException {
        Log.i(TAG, "set_storegroupmap");
        ArrayList<String> StoreIdList = new ArrayList<String>();
        JSONObject data = new JSONObject();

        //StoreIdList.add("1");
        //StoreIdList.add("2");
        StoreIdList.add(Store_ID);

        data.put("storeGroupId", Store_Group_ID);
        data.put("storeIdList", new JSONArray(StoreIdList));

        if (mAgentManager != null) {
            mAgentManager.setWStoreGroupMap(data.toString());
        }
    }

    //1.2
    public void update_storegroupmap(View v) throws JSONException {
        Log.i(TAG, "update_storegroupmap");

        ArrayList<String> StoreIdList = new ArrayList<String>();
        JSONObject data = new JSONObject();

        StoreIdList.add("2403");


        data.put("storeGroupId", Store_Group_ID);
        data.put("storeIdList", new JSONArray(StoreIdList));

        if (mAgentManager != null) {
            mAgentManager.updateWStoreGroupMap(data.toString());
        }
    }
    //////////////////////Store Group ///////////////////////////

    //1.3
    public void delete_storegroupmap(View v) throws JSONException {
        Log.i(TAG, "delete_storegroupmap");

        ArrayList<String> StoreIdList = new ArrayList<String>();
        JSONObject data = new JSONObject();
        data.put("storeGroupId", Store_Group_ID);
        //StoreIdList.add(Store_ID);
        //data.put("storeIdList",new JSONArray(StoreIdList));
        data.put("storeIdList", Store_ID);
        data.put("storeIdList", Store_ID + "," + Store_ID);
        if (mAgentManager != null) {
            mAgentManager.deleteWStoreGroupMap(data.toString());
        }

        disable_storegroupmap();
    }

    public void enable_storegroup(View v) {

        mBtnGetstoregouplist.setEnabled(true);
        mBtnAddstoregroup.setEnabled(true);
        mBtnUpdatestoregroup.setEnabled(true);
        mBtnGetstoregroup.setEnabled(true);
        mBtnDeletestoregroup.setEnabled(true);
    }

    public void disable_storegroup() {

        mBtnGetstoregouplist.setEnabled(false);
        mBtnAddstoregroup.setEnabled(false);
        mBtnUpdatestoregroup.setEnabled(false);
        mBtnGetstoregroup.setEnabled(false);
        mBtnDeletestoregroup.setEnabled(false);
    }

    //2.1
    public void get_storegrouplist(View v) throws JSONException {

        Log.i(TAG, "get_storegrouplist");

        JSONObject data = new JSONObject();
        data.put("searchType", "");//검색 종류 ( 1 - Store Group 명 default, 2 - Store 명 )//optional
        data.put("searchWord", "");//검색어//optional
        data.put("page", "");//페이지 번호, default 1//optional
        data.put("rowPerPage", "");//페이지 당 목록 개수, default 10//optional
        data.put("sortType", "");//정렬 항목 ( 1 - 등록일 default, 2 - Store Group 명, 3 - Store 수 )//optional
        data.put("sortOrder", "");//정렬 순서 ( 1 - 오름차순 default, 2 - 내림차순)//optional

        if (mAgentManager != null) {
            mAgentManager.getWStoreGroupAll(data.toString());
        }
    }

    //2.2
    public void add_storegroup(View v) throws JSONException {
        Log.i(TAG, "add_storegroup");

        AlertDialog.Builder alertdia = new AlertDialog.Builder(this);

        final EditText edittext = new EditText(this);
        alertdia.setMessage("StoreGroup 입력창");
        alertdia.setTitle("Store Group name");

        edittext.setText("SO 그룹");

        alertdia.setView(edittext);

        alertdia.setPositiveButton("Yes Option", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                Store_Group_name = edittext.getText().toString();

                JSONObject data = new JSONObject();

                try {
                    data.put("groupName", Store_Group_name);
                    data.put("groupDesc", "종합투자회사");

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (mAgentManager != null) {
                    add_store_group_flag = true;
                    mAgentManager.setWStoreGroup(data.toString());
                }
            }

        });

        alertdia.show();
    }

    //2.3
    public void get_storegroup(View v) throws JSONException {
        Log.i(TAG, "get_storegroup");

        if (mAgentManager != null) {
            mAgentManager.getWStoreGroup(Store_Group_ID);
        }
    }

    //2.4
    public void update_storegroup(View v) throws JSONException {

        Log.i(TAG, "update_storegroup");

        JSONObject data = new JSONObject();

        data.put("storeGroupId", Store_Group_ID);
        //data.put("groupName", Store_Group_name);
        data.put("groupName", "확인요");
        data.put("groupDesc", "오늘 테스트2");
        if (mAgentManager != null) {
            mAgentManager.updateWStoreGroup(data.toString());
        }
    }
    //////////////////////Store///////////////////////////

    //2.5
    public void delete_storegroup(View v) throws JSONException {

        Log.i(TAG, "delete_storegroup");


        //JSONObject data = new JSONObject();

        if (mAgentManager != null) {
            mAgentManager.deleteWStoreGroup(Store_Group_ID);
        }

        disable_storegroup();
    }

    public void enable_store(View v) {

        mBtnGetstorelist.setEnabled(true);
        mBtnAddstore.setEnabled(true);
        mBtnUpdatestore.setEnabled(true);
        mBtnGetstore.setEnabled(true);
        mBtnDeletestore.setEnabled(true);

        mLat.setVisibility(View.VISIBLE);
        mLon.setVisibility(View.VISIBLE);
    }

    public void disable_store() {

        mBtnGetstorelist.setEnabled(false);
        mBtnAddstore.setEnabled(false);
        mBtnUpdatestore.setEnabled(false);
        mBtnGetstore.setEnabled(false);
        mBtnDeletestore.setEnabled(false);
    }

    //3.1
    public void get_all_store(View v) throws JSONException {
        Log.i(TAG, "get_all_store");

        JSONObject data = new JSONObject();
        data.put("searchType", "");//검색 종류 (1 - Store 명, 2 - 주소, 3 - 태그명)//optional
        data.put("searchWord", "");//검색어//optional
        data.put("page", "");//페이지 번호, default 1//optional
        data.put("rowPerPage", "");//페이지 당 목록 개수, default 10//optional
        data.put("sortType", "");//정렬 항목 ( 1 - 등록일 default, 2 - Store Group 명, 3 - Store 수 )//optional
        data.put("sortOrder", "");//정렬 순서 ( 1 - 오름차순 default, 2 - 내림차순)//optional

        if (mAgentManager != null) {
            mAgentManager.getWStoreAll(data.toString());
        }
    }

    //3.2
    public void add_store(View v) throws JSONException {
        Log.i(TAG, "add_store");


        AlertDialog.Builder alertdia = new AlertDialog.Builder(this);

        final EditText edittext = new EditText(this);

        alertdia.setTitle("Add Store ");

        alertdia.setMessage("Store Name 입력창");
        edittext.setText("셀리지온0");
        alertdia.setView(edittext);


        alertdia.setPositiveButton("Yes Option", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                Store_name = edittext.getText().toString();

                JSONObject data = new JSONObject();

                try {
                    data.put("name", Store_name);
                    if (latitude == null && Longitude == null) {
                        data.put("address", "경기 성남시 분당구 구미동 153");

                        data.put("latitude", 37.566463);//SKT타워 주소//테스트 코드
                        data.put("longitude", 126.985056);

                        //data.put("latitude", 37.533390);//강동구 주소//테스트 코드
                        //data.put("longitude", 127.140693);

                        //data.put("latitude","37.342150");//셀리지온 주소//테스트 코드
                        //data.put("longitude","127.108246");

                        data.put("floor", 12);
                        data.put("telNo", "1");
                        data.put("homepage", "122");
                        ArrayList<String> taglist = new ArrayList<String>();

                        taglist.add("111");
                        taglist.add("222");
                        data.put("tagList", new JSONArray(taglist));


                    } else {
                        data.put("address", "Test Place");
                        data.put("latitude", latitude);
                        data.put("longitude", Longitude);

                        Log.d("location", "lat = " + latitude + ",lon = " + Longitude);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if (mAgentManager != null) {
                    add_store_flag = true;
                    mAgentManager.setWStore(data.toString());
                }
                latitude = null;
                Longitude = null;
            }

        });

        alertdia.show();
    }

    //3.3
    public void get_store(View v) throws JSONException {
        Log.i(TAG, "get_store");

        //Alert_Input();
        if (mAgentManager != null) {
            mAgentManager.getWStore(Store_ID);
            //mAgentManager.getWStore(null);
        }
    }

    //3.4
    public void update_store(View v) throws JSONException {

        Log.i(TAG, "update_storegroup");

        JSONObject data = new JSONObject();

        data.put("name", Store_name);
        data.put("storeId", Store_ID);
        data.put("address", "경기 성남시 분당구 구미동 1589999");
        //data.put("latitude", 37.533390);//강동구 주소//테스트 코드
        //data.put("longitude", 127.140693);
        data.put("latitude", 53.560364);//미국 주소//테스트 코드
        data.put("longitude", -113.496695);
        data.put("floor", 99);

        ArrayList<String> taglist = new ArrayList<String>();

        taglist.add("333");
        taglist.add("444");
        data.put("tagList", new JSONArray(taglist));


        if (mAgentManager != null) {
            mAgentManager.updateWStore(data.toString());
        }
    }

//////////////////////Event///////////////////////////

    //3.5
    public void delete_store(View v) throws JSONException {

        Log.i(TAG, "delete_storegroup");


        JSONObject data = new JSONObject();

        //data.put("storeGroupId", "2091");

        if (mAgentManager != null) {
            mAgentManager.deleteWStore(Store_ID);
        }

        disable_store();
    }

    public void enable_event(View v) {

        mBtnGeteventlist.setEnabled(true);
        mBtnAddevent.setEnabled(true);
        mBtnUpdateevent.setEnabled(true);
        mBtnGetevent.setEnabled(true);
        mBtnDeleteevent.setEnabled(true);
    }

    public void disable_event() {

        mBtnGeteventlist.setEnabled(false);
        mBtnAddevent.setEnabled(false);
        mBtnUpdateevent.setEnabled(false);
        mBtnGetevent.setEnabled(false);
        mBtnDeleteevent.setEnabled(false);
    }

    //4.1
    public void get_all_event(View v) throws JSONException {
        Log.i(TAG, "get_all_event");

        JSONObject data = new JSONObject();
        data.put("searchUse", "");//사용 여부 ( 1 - 사용, 2 - 사용 안함, 3 - 전체 )//mandatory
        data.put("searchStatus", "");//진행 상태 ( 1 - 대기, 2 - 진행중, 3 - 종료, 4 - 전체 )//mandatory
        data.put("searchWord", "");//Campaign 명//optional
        data.put("page", "");//페이지 번호 default 1//optional
        data.put("rowPerPage", "");//페이지당 목록 개수 default 15//optional
        data.put("sortType", "");//정렬 항목 ( 1 - 등록일 default, 2 - Campaign 명, 3 - Store 수 )//optional
        data.put("sortOrder", "");//정렬 순서 ( 1 - 오름차순 default, 2 - 내림차순 )//optional

        if (mAgentManager != null) {
            mAgentManager.getWEventAll(data.toString());
        }
    }

    //4.2
    public void add_event(View v) throws JSONException {
        Log.i(TAG, "add_event");

        AlertDialog.Builder alertdia = new AlertDialog.Builder(this);

        final EditText edittext = new EditText(this);
        alertdia.setMessage("Add Event 입력창");
        alertdia.setTitle("Event id");

        edittext.setText("셀리지온 이벤트 0");

        alertdia.setView(edittext);

        alertdia.setPositiveButton("Yes Option", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int whichButton) {
                Event_name = edittext.getText().toString();

                JSONObject data = new JSONObject();

                try {
                    String start_date = "2016-02-10";//사용자 start 날짜 설정
                    String end_date = "2016-12-30";//사용자 end 날짜 설정

                    data.put("eventName", Event_name);
                    data.put("availableWeekDate", "MON, TUE, WED, THU, FRI, SAT, SUN");
                    data.put("startDate", start_date);
                    data.put("endDate", end_date);
                    data.put("isActivate", "Y");
                    data.put("eventCheckType", "CheckIn");//CheckIn, CheckOut, Stay
                    data.put("eventStayMinute", 1);
                    data.put("isAllTime", "N");

                    data.put("metaInfo", "Y");

                    JSONArray TimeList = new JSONArray();
                    JSONObject Time = new JSONObject();
                    Time.put("startTime", "00:30");
                    Time.put("endTime", "23:20");
                    TimeList.put(Time);


                    data.put("eventTimeList", TimeList);

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                if (mAgentManager != null) {
                    add_event_flag = true;
                    Log.d("data = ", "data" + data.toString());
                    mAgentManager.setWEvent(data.toString());
                }
            }

        });

        alertdia.show();

    }

    //4.3
    public void get_event(View v) throws JSONException {

        Log.i(TAG, "get_event");

        if (mAgentManager != null) {
            mAgentManager.getWEvent(Event_ID);
        }

    }

    //4.4
    public void update_event(View v) throws JSONException {

        Log.i(TAG, "update_event");

        JSONObject data = new JSONObject();


        String start_date = "2016-02-10";//사용자 start 날짜 설정
        String end_date = "2016-12-30";//사용자 end 날짜 설정
        data.put("eventId", Event_ID);
        data.put("eventName", Event_name);
        data.put("availableWeekDate", "MON");
        data.put("startDate", start_date);
        data.put("endDate", end_date);
        data.put("isActivate", "Y");
        data.put("eventCheckType", "CheckOut");//CheckIn, CheckOut, Stay
        data.put("eventStayMinute", 1);
        data.put("isAllTime", "N");

        data.put("metaInfo", "kkkkkkkkk");

        JSONArray TimeList = new JSONArray();
        JSONObject Time = new JSONObject();
        Time.put("startTime", "12:30");
        Time.put("endTime", "18:20");
        TimeList.put(Time);
        data.put("eventTimeList", TimeList);

        mAgentManager.updateWEvent(data.toString());

    }

    //4,5
    public void delete_event(View v) throws JSONException {

        Log.i(TAG, "delete_event");

        JSONObject data = new JSONObject();

        mAgentManager.deleteWEvent(Event_ID);
        disable_event();

    }

    //////////////////////Event Store ///////////////////////////
    public void enable_eventstore(View v) {

        mBtnAddeventstore.setEnabled(true);
        mBtnUpdateeventstore.setEnabled(true);
        mBtnDeleteeventstore.setEnabled(true);
    }

    public void disable_eventstore() {

        mBtnAddeventstore.setEnabled(false);
        mBtnUpdateeventstore.setEnabled(false);
        mBtnDeleteeventstore.setEnabled(false);
    }

    //5.1
    public void add_eventstore(View v) throws JSONException {
        Log.i(TAG, "add_eventstore");

        ArrayList<String> StoreIdList = new ArrayList<String>();
        JSONObject data = new JSONObject();


        //StoreIdList.add(100);
        StoreIdList.add(Store_ID);

        data.put("eventId", Event_ID);
        data.put("storeIdList", new JSONArray(StoreIdList));

        if (mAgentManager != null) {
            mAgentManager.setWEventStore(data.toString());
        }
    }

    //5.2
    public void update_eventstore(View v) throws JSONException {

        Log.i(TAG, "update_eventstore");
        //setWStoreGroupMap(String storeGroupId, List<StoreGroupMapData> StoreIdList)
        ArrayList<String> StoreIdList = new ArrayList<String>();

        //StoreIdList.add("100");
        StoreIdList.add("2412");
        StoreIdList.add("2413");
        StoreIdList.add("2414");
        JSONObject data = new JSONObject();

        data.put("eventId", Event_ID);
        data.put("storeIdList", new JSONArray(StoreIdList));

        if (mAgentManager != null) {
            mAgentManager.updateWEventStore(data.toString());
        }
    }

    //5.3
    public void delete_eventstore(View v) throws JSONException {
        Log.i(TAG, "delete_eventstore");
        //setWStoreGroupMap(String storeGroupId, List<StoreGroupMapData> StoreIdList)


        JSONObject data = new JSONObject();
        ArrayList<String> StoreIdList = new ArrayList<String>();

        //data.put("eventstore","12,13");
        //StoreIdList.add("100");
        //StoreIdList.add(Store_ID);

        data.put("eventId", Event_ID);
        //data.put("storeIdList",new JSONArray(StoreIdList));

        data.put("storeIdList", Store_ID);
        data.put("storeIdList", Store_ID + "," + Store_ID);

        if (mAgentManager != null) {
            mAgentManager.deleteWEventStore(data.toString());
        }
        disable_eventstore();
    }

    public void syncdb(View v) throws JSONException {
        Log.i(TAG, "syncdb");

        AppData appdata = new AppData();
        appdata.appID = "";
        appdata.appName = "geofenceApp";
        appdata.packageName = mPakacgeName;

        appdata.tdcProjectKey = tdcProjectKey;

        appdata.regId = "";
        if (mAgentManager != null) {
            mAgentManager.syncWDB(appdata);
        }
    }

    public void appsync(View v) throws JSONException {
        Log.i(TAG, "appsync");

        AppData appdata = new AppData();
        appdata.appID = "";
        appdata.appName = "geofenceApp";
        appdata.packageName = mPakacgeName;

        appdata.tdcProjectKey = tdcProjectKey;

        appdata.regId = "";
        if (mAgentManager != null) {
            mAgentManager.appWSync(appdata);
        }
    }

    @Override
    public void onBackPressed() {

        finish();

        super.onBackPressed();
    }


}
