package com.enjoybt.scenario.service;

import java.util.Map;

/**
 * 0. Project  : 화산재해대응시스템
 * 1. Package : com.enjoybt.scenario.service
 * 2. Comment :
 * 3. Auth  : aiden_shin
 * 4. Date  : 2018-05-29 오전 9:50
 * 5. History :
 * 이름     : 일자          : 근거자료   : 변경내용
 * ------------------------------------------------------
 * aiden_shin : 2018-05-29 :            : 신규 개발.
 *
 */
public interface RequestService {

    /**
     * 유사시나리오 생성 실행
     */
    public void requestScenario();

    /**
     * 유사시나리오 요청 파라미터 생성
     *
     * @param gvpCode the gvp code
     * @param vei the vei
     * @param mdlCode the mdl code
     * @return the map
     */
    public Map<String, Object> makeParams(String gvpCode, String vei, String mdlCode)
            throws Exception;

    /**
     * Delay second.
     *
     * @param time the time
     */
    public void delaySecond(int time);

    /**
     * Gets start time.
     *
     * @return the start time
     */
    public String getStartTime();

    /**
     * Gets later time.
     *
     * @param obsr the obsr
     * @return the later time
     */
    public String getLaterTime(int obsr);

    /**
     * 계산서버에 연산 요청
     *
     * @param param the param
     * @throws Exception the exception
     */
    public void executeRequest(Map<String,Object> param) throws Exception;
}
