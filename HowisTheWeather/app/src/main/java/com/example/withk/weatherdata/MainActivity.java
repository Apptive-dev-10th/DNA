package com.example.withk.weatherdata;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Calendar;

import android.content.Context;
import android.location.Location;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    static TextView totalcnt,date,pm10grade;
    static int stationCnt=0;
    static Context mContext;	//static에서 context를 쓰기위해
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        init();
    }

    public void init() {
        mContext=getApplicationContext();	//static에서 context를 쓰기위해~
        //totalcnt=(TextView)findViewById(R.id.totalcnt);
        //date=(TextView)findViewById(R.id.date);
        //pm10grade=(TextView)findViewById(R.id.pm10grade);
    }



    public static void getFindDust(String name){	//대기정보를 가져오는 스레드

        GetFindDustThread.active=true;
        GetFindDustThread getweatherthread=new GetFindDustThread(false,name);		//스레드생성(UI 스레드사용시 system 뻗는다)
        getweatherthread.start();	//스레드 시작

    }

    public static void  FindDustThreadResponse(String getCnt,String[] sDate,String[] sPm10Grade){	//대기정보 가져온 결과값
        stationCnt=0;	//측정개수정보(여기선 1개만 가져온다
        stationCnt=Integer.parseInt(getCnt);

        Log.w("stationcnt", String.valueOf(stationCnt));

        if(stationCnt==0) {	//만약 측정정보가 없다면
            totalcnt.setText("측정소 정보가 없거나 측정정보가 없습니다.");
            date.setText("");	//
            pm10grade.setText("");
        }else{	//측정정보있으면
            totalcnt.setText(sDate[0] + "에 대기정보가 업데이트 되었습니다.");
            date.setText(sDate[0]);	//
            pm10grade.setText(transGrade(sPm10Grade[0]));

        }

        GetFindDustThread.active=false;
        GetFindDustThread.interrupted();
    }
    static public String transGrade(String intGrade){
        String trans=null;
        switch (intGrade){
            case "1":
                trans="좋음";
                break;
            case "2":
                trans="보통";
                break;
            case "3":
                trans="나쁨";
                break;
            case "4":
                trans="매우나쁨";
                break;
            default:
                break;

        }
        return trans;
    }

    public static void main(String[] args) {
        //현재 날씨를 보기 위하여 현재의 날짜와 시간 긁어와 계산하는 부분
        Date date = new Date(System.currentTimeMillis());
        Calendar calnow = new GregorianCalendar();
        calnow.setTime(date);
        if (calnow.get(Calendar.MINUTE) < 40) {
            if (calnow.get(Calendar.HOUR_OF_DAY) == 0) {
                calnow.add(Calendar.DAY_OF_MONTH, -1);
                calnow.set(Calendar.HOUR_OF_DAY, 23);
            } else {
                calnow.add(Calendar.HOUR_OF_DAY, -1);
            }
        }
        SimpleDateFormat sdf_basedate = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf_basetime = new SimpleDateFormat("HH");
        String baseDate_now = sdf_basedate.format(calnow.getTime());
        String baseTime_now = sdf_basetime.format(calnow.getTime()) + "00";

        Calendar caldaily = new GregorianCalendar();
        caldaily.setTime(date);
        caldaily.set(Calendar.HOUR_OF_DAY, 2);
        if (caldaily.get(Calendar.HOUR_OF_DAY) < 2) {
            caldaily.add(Calendar.DAY_OF_MONTH, -1);

        }
        String baseDate_daily = sdf_basedate.format(caldaily.getTime());
        String baseTime_daily = sdf_basetime.format(caldaily.getTime()) + "00";
        //String baseDate_weekly = "20180606";
        //String baseTime_weekly = "0200";
        //이하 db에서 가져올것

        int x = 98; //db에서 가져오기
        int y = 77; //db에서 가져오기


        //now_listJSON nlJson = new now_listJSON();
        //now_list nl = nlJson.getnow_list(baseDate_now, baseTime_now, x, y);


        daily_listJSON dlJson = new daily_listJSON();
        daily_list dl = dlJson.getdaily_list(baseDate_daily, baseTime_daily, x, y);

        for (int i = 0; i < 50; i++){
        System.out.println(dl.baseDate);
        System.out.println(dl.baseTime);

        System.out.println(dl.fcstDate0);
        System.out.println(dl.fcstTime_06);

        System.out.println(dl.pop_06);
        System.out.println(dl.pty_06);
        System.out.println(dl.r06_06);
        System.out.println(dl.sky_06);
        System.out.println(dl.t3h_06);
    }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
