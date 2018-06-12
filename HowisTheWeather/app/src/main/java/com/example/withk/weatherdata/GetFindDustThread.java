package com.example.withk.weatherdata;

import android.os.Handler;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

class GetFindDustThread extends Thread {	//기상청 연결을 위한 스레드
    static public boolean active=false;
    //파서용 변수
    int data=0;			//이건 파싱해서 array로 넣을때 번지
    public boolean isreceiver;
    String sTotalCount;	//결과수
    String[] sDate,sPm10Grade;
    boolean bTotalCount,bDate,bPm10Grade;	//여긴 저장을 위한 플래그들
    boolean tResponse;	//이건 text로 뿌리기위한 플래그
    String dongName = "부곡동";
    Handler handler;	//날씨저장 핸들러
    String Servicekey="ServiceKey=KmuN74waxVR22ZA96tDJj8VOl0r5SD3YD8MntKdvnmJ3CA1oWXciQgyFgC4yL0QWNkj2WnUiRJydu2Iwv0DzQw%3D%3D";
    String getInfo="http://openapi.airkorea.or.kr/openapi/services/rest/ArpltnInforInqireSvc/";
    String getStationFindDust="getMsrstnAcctoRltmMesureDnsty?";
    String searchDate="dataTerm=daily";
    String station="stationName=";
    String infoCnt="numOfRows=50";

    public GetFindDustThread(boolean receiver,String dong){

        Log.w("스레드가 받은 측정소", dong);
        handler=new Handler();
        isreceiver=receiver;
        //dongName=dong;
        try{
            dongName = URLEncoder.encode(dong, "utf-8");
        }catch(Exception e){

        }


        bTotalCount=bPm10Grade=false;	//부울상수는 false로 초기화해주자
    }
    public void run(){

        if(active){
            try{
                sDate=new String[100];	//측정일
                sPm10Grade=new String[100];	//미세먼지 지수
                data=0;
                XmlPullParserFactory factory= XmlPullParserFactory.newInstance();	//이곳이 풀파서를 사용하게 하는곳
                factory.setNamespaceAware(true);									//이름에 공백도 인식
                XmlPullParser xpp=factory.newPullParser();							//풀파서 xpp라는 객체 생성
                String dustUrl=getInfo+getStationFindDust+station+dongName+"&"+infoCnt+"&"+searchDate+"&"+Servicekey;
                Log.w("스레드가 받은 ", dustUrl);
                URL url=new URL(dustUrl);		//URL객체생성
                InputStream is=url.openStream();	//연결할 url을 inputstream에 넣어 연결을 하게된다.
                xpp.setInput(is,"UTF-8");			//이렇게 하면 연결이 된다. 포맷형식은 utf-8로

                int eventType=xpp.getEventType();	//풀파서에서 태그정보를 가져온다.

                while(eventType!= XmlPullParser.END_DOCUMENT){	//문서의 끝이 아닐때

                    switch(eventType){
                        case XmlPullParser.START_TAG:	//'<'시작태그를 만났을때

                            if(xpp.getName().equals("dataTime")){	//측정일
                                bDate=true;

                            } if(xpp.getName().equals("pm10Grade")){	//미세먼지 지수
                            bPm10Grade=true;

                        }

                            break;

                        case XmlPullParser.TEXT:	//텍스트를 만났을때
                            //앞서 시작태그에서 얻을정보를 만나면 플래그를 true로 했는데 여기서 플래그를 보고
                            //변수에 정보를 넣어준 후엔 플래그를 false로~
                            if(bDate){				//동네이름
                                sDate[data]=xpp.getText();
                                bDate=false;
                            } if(bPm10Grade){
                            sPm10Grade[data]=xpp.getText();
                            bPm10Grade=false;
                        }if(bTotalCount){
                            sTotalCount=xpp.getText();
                            bTotalCount=false;
                        }
                            break;

                        case XmlPullParser.END_TAG:		//'</' 엔드태그를 만나면 (이부분이 중요)

                            if(xpp.getName().equals("response")){	//태그가 끝나느 시점의 태그이름이 item이면(이건 거의 문서의 끝
                                tResponse=true;						//따라서 이때 모든 정보를 화면에 뿌려주면 된다.
                                view_text();					//뿌려주는 곳~
                            }if(xpp.getName().equals("item")){	//item 예보시각기준 예보정보가 하나씩이다.
                            data++;							//즉 item == 예보 개수 그러므로 이때 array를 증가해주자
                        }
                            break;
                    }
                    eventType=xpp.next();	//이건 다음 이벤트로~
                }

            }catch(Exception e){
                e.printStackTrace();
            }
        }



    }

    /**
     * 이 부분이 뿌려주는곳
     * 뿌리는건 핸들러가~
     * @author Ans
     */
    private void view_text(){

        handler.post(new Runnable() {	//기본 핸들러니깐 handler.post하면됨

            @Override
            public void run() {

                active=false;
                if(tResponse){		//문서를 다 읽었다
                    tResponse=false;
                    data=0;
                    MainActivity.FindDustThreadResponse(sTotalCount,sDate,sPm10Grade);

                }


            }
        });
    }
}
