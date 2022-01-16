package com.sample.web.base.security.rememberme;

import java.util.Date;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional(rollbackFor = Throwable.class, propagation = Propagation.REQUIRES_NEW)
public class MultiDeviceTokenRepository extends JdbcDaoSupport {

    public static final String tokensBySeriesSql = "SELECT username, ip_address, user_agent, series, token, last_used FROM persistent_logins WHERE series = ?";

    public static final String insertTokenSql = "INSERT INTO persistent_logins (username, ip_address, user_agent, series, token, last_used) VALUES (?, ?, ?, ?, ?, ?)";

    public static final String updateTokenSql = "UPDATE persistent_logins SET token = ?, last_used = ? WHERE series = ?";

    public static final String removeAllUserTokensSql = "DELETE FROM persistent_logins WHERE username = ?";

    public static final String removeUserTokensSql = "DELETE FROM persistent_logins WHERE username = ? AND user_agent = ?";

    /**
     * 생성자
     */
    public MultiDeviceTokenRepository() {

    }

    @Override
    protected void initDao() {
    }

    /**
     * 토큰을 작성한다
     * 
     * @param token
     */
    public void createNewToken(MultiDeviceRememberMeToken token) {
        val username = token.getUsername();
        val series = token.getSeries();
        val tokenValue = token.getTokenValue();
        val lastUsed = token.getLastUsed();
        val ipAddress = token.getRemoteAddress();
        val userAgent = token.getUserAgent();

        getJdbcTemplate().update(insertTokenSql, username, ipAddress, userAgent, series, tokenValue, lastUsed);
    }

    /**
     * 로그인 기록을 갱신한다.
     * 
     * @param series
     * @param tokenValue
     * @param lastUsed
     */
    public void updateToken(String series, String tokenValue, Date lastUsed) {
        getJdbcTemplate().update(updateTokenSql, tokenValue, lastUsed, series);
    }

    /**
     * 토큰을 취득한다.
     * 
     * @param series
     * @return
     */
    public MultiDeviceRememberMeToken getTokenForSeries(String series) {
        MultiDeviceRememberMeToken token = null;

        try {
            token = getJdbcTemplate().queryForObject(tokensBySeriesSql, (rs, rowNum) -> {
                val t = new MultiDeviceRememberMeToken();
                t.setUsername(rs.getString(1));
                t.setRemoteAddress(rs.getString(2));
                t.setUserAgent(rs.getString(3));
                t.setSeries(rs.getString(4));
                t.setTokenValue(rs.getString(5));
                t.setLastUsed(rs.getTimestamp(6).toLocalDateTime());
                return t;
            }, series);
        } catch (EmptyResultDataAccessException zeroResults) {
            if (log.isDebugEnabled()) {
                log.debug("Querying token for series '{}' returned no results.", series, zeroResults);
            }
        } catch (IncorrectResultSizeDataAccessException moreThanOne) {
            log.error("Querying token for series '{}' returned more than one value. Series should be unique", series);
        } catch (DataAccessException e) {
            log.error("Failed to load token for series {}", series, e);
        }

        return token;
    }

    /**
     * 로그인 기록을 모두 삭제한다.
     *
     * @param username
     */
    public void removeAllUserTokens(String username) {
        getJdbcTemplate().update(removeAllUserTokensSql, username);
    }

    /**
     * 로그인 기록을 삭제한다.
     *
     * @param username
     * @param userAgent
     */
    public void removeUserTokens(String username, String userAgent) {
        getJdbcTemplate().update(removeUserTokensSql, username, userAgent);
    }
}
