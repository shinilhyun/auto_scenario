package com.enjoybt.scenario.service.impl;

import com.enjoybt.scenario.service.ScenarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 0. Project  : 화산재해대응시스템
 * 1. Package : com.enjoybt.scenario.service.impl
 * 2. Comment : 
 * 3. Auth  : aiden_shin
 * 4. Date  : 2018-05-29 오전 9:50
 * 5. History : 
 * 이름     : 일자          : 근거자료   : 변경내용
 * ------------------------------------------------------
 * aiden_shin : 2018-05-29 :            : 신규 개발.
 *
 */
@Service
public class ScenarioServiceImpl implements ScenarioService {

    private static Logger logger = LoggerFactory.getLogger(ScenarioServiceImpl.class);

    @Value("${api.url}")
    String apiUrl;

    @Value("${staging.root}")
    String stagingRoot;

    @Value("${api.endpoint}")
    String endPoint;

    // 매일 22시에 실행
    @Scheduled(cron = "0 0 22 * * *")
    public void requestScenario(){

        String[] gvpList = {"305060", "283110", "283030","282110","306030"};
        String[] veiList = {"4","5","6","7"};
        String mdlCode = "LDS";
        try {

//            for (String gvp : gvpList) {
//                for (String vei : veiList) {
//                    Map<String, Object> param = makeParams(gvp, vei, mdlCode);
//                    executeRequest(param);
//                }
//            }

            String gvp = "305060";
            String vei = "4";
            Map<String, Object> param = makeParams(gvp, vei, mdlCode);
            executeRequest(param);
        } catch (Exception e) {

            logger.info("ERROR",e);
        }

    }

    public Map<String, Object> makeParams(String gvpCode, String vei, String mdlCode){
        //3초 지연
        delaySecond(3);

        logger.info("requestCal");

        Map<String, Object> message = new LinkedHashMap<>();
        Map<String, Object> eruption = new LinkedHashMap<>();
        Map<String, Object> volcano = new LinkedHashMap<>();
        Map<String, Object> meteorology = new LinkedHashMap<>();
        Map<String, Object> model = new LinkedHashMap<>();
        Map<String, Object> staging = new LinkedHashMap<>();
        Map<String, Object> notify = new LinkedHashMap<>();

        String volcanoName = null;
        String enCode = null;

        int obsr = 60;
        int duration = 24;
        String lat = null;
        String lon = null;
        int elevation = 0;
        String mdlType = null;

        int colum = 0;

        String durationParam;
        String obsrParam;

        if (duration < 10) {
            durationParam = "0" + String.valueOf(duration);
        } else {
            durationParam = String.valueOf(duration);
        }

        if (obsr < 10) {
            obsrParam = "0" + String.valueOf(obsr);
        } else {
            obsrParam = String.valueOf(obsr);
        }

        String modelName = "S" + gvpCode + "_"
                + getStartTime().replaceAll("\\D","")
                + "_0" + String.valueOf(vei).substring(0,1) + "0_0"+ durationParam
                + "_0" + obsrParam + "_" + mdlCode;


        if (gvpCode.equals("283110")) {
            volcanoName = "야사마야마(타루마이)";
            lon = "138.523";
            lat = "36.406";
            elevation = 2568;
            enCode = "MT_ASAMAYAMA";
        } else if (gvpCode.equals("283030")) {
            volcanoName = "후지산";
            lon = "138.728";
            lat = "35.361";
            elevation = 3776;
            enCode = "MT_FUJI";
        } else if (gvpCode.equals("282110")) {
            volcanoName = "아소산";
            lon = "131.104";
            lat = "32.884";
            enCode = "MT_ASO";
            elevation = 1592;
        } else if (gvpCode.equals("305060")) {
            volcanoName = "백두산";
            lon = "128.08";
            lat = "41.98";
            elevation = 2744;
            enCode = "MT_BAEKDU";
        } else if (gvpCode.equals("306030")) {
            volcanoName = "울릉";
            lon = "130.87";
            lat = "37.5";
            elevation = 984;
            enCode = "ULREUNG";
        }

        if (mdlCode.equals("F3D")) {
            mdlType = "fall3d";
        } else if (mdlCode.equals("LDS")) {
            mdlType = "ladasva";
        } else {
            mdlType = "puff";
        }

        if(Integer.parseInt(vei) <= 4) {
            colum = 10000;
        } else {
            colum = 25000;
        }

        //eruption
        eruption.put("time", getStartTime());
        eruption.put("vei", Float.parseFloat(vei));
        eruption.put("column", colum);
        eruption.put("duration", duration);

        //volcano
        volcano.put("name", volcanoName);
        volcano.put("encode", enCode);
        volcano.put("gcode", gvpCode);
        volcano.put("latitude", Float.parseFloat(lat));
        volcano.put("longitude", Float.parseFloat(lon));
        volcano.put("elevation", elevation);

        //meteorology
        meteorology.put("region", "RDAPS");
        meteorology.put("start", getStartTime());
        meteorology.put("end", getLaterTime(obsr));
        meteorology.put("base", 0);
        meteorology.put("source", "UM");

        //model
        model.put("name", modelName);
        model.put("code", mdlType);
        model.put("version", "latest");
        model.put("registry", "http://localhost");
        model.put("methoad", "FULL");

        // staging
        staging.put("url", stagingRoot + modelName);
        staging.put("protocol", "LOCAL");

        // notify
        notify.put("endpoint", endPoint);
        notify.put("version", "1.0");
        notify.put("topic", modelName);

        message.put("eruption", eruption);
        message.put("volcano", volcano);
        message.put("meteorology", meteorology);
        message.put("model", model);
        message.put("staging", staging);
        message.put("notify", notify);

        return message;
    }

    public void delaySecond(int time) {
        for (int i = 0; i < time; i++) {
            logger.info(i + "초 지연중..");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.info("딜레이 실패");
            }

        }
    }

    public String getStartTime(){

        //2018-01-29T17:00:00Z
        Date now = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00'Z'");
        String result = df.format(now);

        return result;
    }

    public String getLaterTime(int obsr) {
        Date now = new Date();
        now.setHours(now.getHours() + obsr);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'00:00:00'Z'");
        String result = df.format(now);

        return result;
    }

    public void executeRequest(Map<String,Object> param) throws Exception {
        CloseableHttpClient client = HttpClientBuilder.create().build();

        ObjectMapper om = new ObjectMapper();
        String jsonStr = om.writeValueAsString(param);
        logger.info(jsonStr);

        //api 요청
        HttpPost request = new HttpPost(new URI(apiUrl + "/execute/"));
        StringEntity strEntity = new StringEntity(jsonStr);
        request.setEntity(strEntity);
        request.addHeader("Content-Type", "application/json");
        CloseableHttpResponse response = client.execute(request);


        if((response.getStatusLine().getStatusCode()/100) != 2){
            logger.info("계산서버 error응답:"
                    + response.getStatusLine().getStatusCode(),logger);
            throw new Exception();
        }

        // 요청결과 화면에 받아오기
        HttpEntity responseEntity = response.getEntity();
        JSONObject resJsonObject;

        if (responseEntity != null) {
            String resEntityString = EntityUtils.toString(responseEntity);
            resJsonObject = new JSONObject(resEntityString);
            System.out.println(resJsonObject);
        }

    }
}


