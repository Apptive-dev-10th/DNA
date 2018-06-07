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

public class daily_listJSON {
    final static String serviceKey = "KmuN74waxVR22ZA96tDJj8VOl0r5SD3YD8MntKdvnmJ3CA1oWXciQgyFgC4yL0QWNkj2WnUiRJydu2Iwv0DzQw%3D%3D";
    public daily_list getdaily_list(String baseDate_daily, String baseTime_daily, int x, int y) {
        // JSON데이터를 요청하는 URLstr을 만듭니다.
        String urlStr = "http://newsky2.kma.go.kr/service/SecndSrtpdFrcstInfoService2/ForecastSpaceData?"
                + "serviceKey=" + serviceKey + "&base_date=" + baseDate_daily + "&base_time=" + baseTime_daily
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

        daily_list dl = new daily_list(); // 결과 데이터를 저장할 동내기상객체를 만듭니다.
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

            dl.baseDate = baseDate_daily;
            dl.baseTime = baseTime_daily;

            for(int i = 0; i < parse_item.size(); i++) {
                obj = (JSONObject) parse_item.get(i); // 해당 item을 가져옵니다. i는 0부터 item의 사이즈까지
                category = (String)obj.get("category"); //item에서 카테고리를 검색해옵니다.
                dl.fcstDate = (obj.get("fcstDate")).toString();
                dl.fcstTime = (obj.get("fcstTime")).toString();
                switch(category) {
                        case "POP":
                            dl.pop = (obj.get("fcstValue")).toString();
                            break;
                        case "PTY":
                            dl.pty = (obj.get("fcstValue")).toString();
                            break;
                        case "R06":
                            dl.r06 = (obj.get("fcstValue")).toString();
                            break;
                        case "SKY":
                            dl.sky = (obj.get("fcstValue")).toString();
                            break;
                        case "T3H":
                            dl.t3h = (obj.get("fcstValue")).toString();
                            break;
                        }
                    dl.fcstDate = (obj.get("fcstDate")).toString();
                    dl.fcstTime = (obj.get("fcstTime")).toString();
                }

        } catch (MalformedURLException e) {
            System.out.println("MalformedURLException : " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException : " + e.getMessage());
        } catch (ParseException e) {
            System.out.println("ParseException : " + e.getMessage());
        }


            return dl;
    }
}
