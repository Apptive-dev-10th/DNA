package com.example.withk.weatherdata;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class now_listJSON {
    final static String serviceKey = "KmuN74waxVR22ZA96tDJj8VOl0r5SD3YD8MntKdvnmJ3CA1oWXciQgyFgC4yL0QWNkj2WnUiRJydu2Iwv0DzQw%3D%3D";
    public now_list getnow_list(String baseDate_now, String baseTime_now, int x, int y) {
        // JSON데이터를 요청하는 URLstr을 만듭니다.
        String urlStr = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastGrib?"
                + "serviceKey=" + serviceKey + "&base_date=" + baseDate_now + "&base_time=" + baseTime_now
                + "&nx="+ x + "&ny=" + y + "&_type=json";
        OkHttpClient client = new OkHttpClient();


            Request request = new Request.Builder()
                    .url(urlStr)
                    .build();

            Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

        now_list nl = new now_list(); // 결과 데이터를 저장할 동내기상객체를 만듭니다.
        try {
            URL url = new URL(urlStr); // 완성된 urlStr을 사용해서 URL 만들어 해당 데이터를 가져옵니다.
            BufferedReader bf = new BufferedReader(new InputStreamReader(url.openStream()));
            String line = "";
            String result="";
            //버퍼에 있는 정보를 문자열로 변환.
            while((line=bf.readLine())!=null){ //bf 에 있는값을 읽어와서 하나의 문자열로 만듭니다.
                result=result.concat(line);
            }
            //System.out.println(result);

            //문자열을 JSON으로 파싱합니다. 마지막 배열형태로 저장된 데이터까지 파싱해냅니다.
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObj = (JSONObject) jsonParser.parse(result);
            JSONObject parse_response = (JSONObject) jsonObj.get("response");
            JSONObject parse_body = (JSONObject) parse_response.get("body");// response 로 부터 body 찾아오기
            JSONObject parse_items = (JSONObject) parse_body.get("items");// body 로 부터 items 받아오기
            JSONArray parse_item = (JSONArray) parse_items.get("item");// items로 부터 itemlist 를 받아오기 itemlist : 뒤에 [ 로 시작하므로 jsonarray이다.

            JSONObject obj;
            String category;

            nl.baseDate = baseDate_now;
            nl.baseTime = baseTime_now;

            for(int i = 0; i < parse_item.size(); i++) {
                obj = (JSONObject) parse_item.get(i); // 해당 item을 가져옵니다.
                category = (String)obj.get("category"); //item에서 카테고리를 검색해옵니다.

                // 검색한 카테고리와 일치하는 변수에 문자형으로 데이터를 저장합니다.
                //데이터들이 형태가 달라 문자열로 통일해야 편합니다. 꺼내서 사용할때 다시변환하는게 좋습니다.
                switch(category) {
                    case "T1H":
                        nl.t1h = (obj.get("obsrValue")).toString();
                        break;
                    case "RN1":
                        nl.rn1 = (obj.get("obsrValue")).toString();
                        break;
                    case "SKY":
                        nl.sky = (obj.get("obsrValue")).toString();
                        break;
                    case "PTY":
                        nl.pty = (obj.get("obsrValue")).toString();
                        break;
                }
            }

        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException : " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException : " + e.getMessage());
        } catch (ParseException e) {
            System.out.println("ParseException : " + e.getMessage());
        }


        return nl;// 모든값이 저장된 VillageWeather객체를 반환합니다.
    }
}
