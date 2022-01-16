package com.sample.domain.dao;

import java.time.LocalDateTime;

/**
 * 감사정보 홀더
 */
public class AuditInfoHolder {

    private static final ThreadLocal<String> AUDIT_USER = new ThreadLocal<>();

    private static final ThreadLocal<LocalDateTime> AUDIT_DATE_TIME = new ThreadLocal<>();

    /**
     * 감사정보를 보관한다.
     * 
     * @param username
     */
    public static void set(String username, LocalDateTime localDateTime) {
        AUDIT_USER.set(username);
        AUDIT_DATE_TIME.set(localDateTime);
    }

    /**
     * 감사 사용자를 반환한다.
     * 
     * @return
     */
    public static String getAuditUser() {
        return AUDIT_USER.get();
    }

    /**
     * 감사 시각을 반환한다.
     * 
     * @return
     */
    public static LocalDateTime getAuditDateTime() {
        return AUDIT_DATE_TIME.get();
    }

    /**
     * 감사 정보를 클리어한다.
     */
    public static void clear() {
        AUDIT_USER.remove();
        AUDIT_DATE_TIME.remove();
    }
}
