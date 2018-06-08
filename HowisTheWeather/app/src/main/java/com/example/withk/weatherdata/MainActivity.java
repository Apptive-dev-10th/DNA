package com.example.withk.weatherdata;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static void main(String[] args) {

        //현재 날씨를 보기 위하여 현재의 날짜와 시간 긁어와 계산하는 부분
        Date date = new Date(System.currentTimeMillis());
        Calendar calnow = new GregorianCalendar();
        calnow.setTime( date );
        if (calnow.get(Calendar.MINUTE) < 40) {
            if (calnow.get(Calendar.HOUR_OF_DAY) == 0) {
                calnow.add(Calendar.DAY_OF_MONTH, -1);
                calnow.set(Calendar.HOUR_OF_DAY, 23);
            }
            else {
                calnow.add(Calendar.HOUR_OF_DAY, -1);
            }
        }
        SimpleDateFormat sdf_basedate = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sdf_basetime = new SimpleDateFormat("HH");
        String baseDate_now = sdf_basedate.format(calnow.getTime());
        String baseTime_now = sdf_basetime.format(calnow.getTime())+ "00";

        Calendar caldaily = new GregorianCalendar();
        caldaily.setTime( date );
        caldaily.set(Calendar.HOUR_OF_DAY, 2);
        if (caldaily.get(Calendar.HOUR_OF_DAY) < 2) {
            caldaily.add(Calendar.DAY_OF_MONTH, -1);

        }
        String baseDate_daily = sdf_basedate.format(caldaily.getTime());
        String baseTime_daily = sdf_basetime.format(caldaily.getTime())+ "00";
        //String baseDate_weekly = "20180606";
        //String baseTime_weekly = "0200";
        //이하 db에서 가져올것

        int x = 98; //db에서 가져오기
        int y = 77; //db에서 가져오기


        //now_listJSON nlJson = new now_listJSON();
        //now_list nl = nlJson.getnow_list(baseDate_now, baseTime_now, x, y);


        daily_listJSON dlJson = new daily_listJSON();
        daily_list dl = dlJson.getdaily_list(baseDate_daily, baseTime_daily, x, y);

        System.out.println(dl.fcstDate0 + " " + dl.fcstTime_06 + " " + dl.sky_06);
        System.out.println(dl.fcstTime_18+ " " + dl.sky_18 + " " + dl.baseTime + " " + dl.baseDate);
        System.out.println(dl.fcstTime_24 + " " + dl.sky_24 + " " + dl.baseDate + " " + dl.baseTime + " " + dl.fcstDate1);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
