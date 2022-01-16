package com.sample.common;

import java.io.File;

/**
 * 환경 정보
 */
public enum Environment {

    LOCAL, DEV, STG, PRODUCTION, UNKNOWN;

    private static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";

    private static final String LOCAL_PROFILE = "local";

    private static final String DEV_PROFILE = "development";

    private static final String STG_PROFILE = "staging";

    private static final String PRODUCTION_PROFILE = "production";

    /**
     * 환경을 판별하여 반환한다.
     * 
     * @return
     */
    public static Environment get() {
        // 시스템 프로퍼티에 설정한 spring 프로파일을 취득한다
        String environment = System.getProperty(SPRING_PROFILES_ACTIVE);

        if (LOCAL_PROFILE.equalsIgnoreCase(environment)) {
            return LOCAL;
        } else if (DEV_PROFILE.equalsIgnoreCase(environment)) {
            return DEV;
        } else if (STG_PROFILE.equalsIgnoreCase(environment)) {
            return STG;
        } else if (PRODUCTION_PROFILE.equalsIgnoreCase(environment)) {
            return PRODUCTION;
        }

        return UNKNOWN;
    }

    /**
     * OS가 Windows인 경우는 true를 반환한다。
     * 
     * @return
     */
    public static boolean isWindowsOs() {
        return File.separator.equals("\\");
    }
}
