package com.fruits.wechatWebsocket.utils;

import com.fruits.wechatWebsocket.ResponEntity.AccToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class WechatUtils {
    private static final Logger logger = LoggerFactory.getLogger(WechatUtils.class);

    // 配置项（通过setter注入）
    private static String CORPID;
    private static String CORPSECRET;
    private static String PROXY_URL;

    // 令牌缓存和锁
    private static String accessToken;
    private static long tokenExpireTime; // 令牌过期时间戳（毫秒）
    private static final ReentrantLock lock = new ReentrantLock(); // 用于同步刷新令牌

    // RestTemplate单例
    private static final RestTemplate restTemplate = new RestTemplate();

    // Setter方法注入配置值
    @Value("${wx.corpid}")
    public void setCorpid(String corpid) {
        CORPID = corpid;
    }

    @Value("${wx.corpsecret}")
    public void setCorpsecret(String corpsecret) {
        CORPSECRET = corpsecret;
    }

    @Value("${wx.proxyUrl}")
    public void setProxyUrl(String proxyUrl) {
        PROXY_URL = proxyUrl;
    }

    /**
     * 获取微信访问令牌（带缓存机制）
     * @return 有效的访问令牌，获取失败返回null
     */
    public static String getAccessToken() {
        // 检查缓存是否有效
        if (isTokenValid()) {
            logger.debug("Using cached access token");
            return accessToken;
        }

        // 缓存无效，尝试获取锁刷新令牌
        try {
            // 尝试获取锁（最多等待1秒）
            if (lock.tryLock(1, TimeUnit.SECONDS)) {
                try {
                    // 双重检查，避免其他线程已经刷新
                    if (!isTokenValid()) {
                        logger.info("Access token expired, refreshing...");
                        refreshAccessToken();
                    }
                } finally {
                    lock.unlock();
                }
            } else {
                logger.warn("Failed to acquire lock for token refresh");
            }
        } catch (InterruptedException e) {
            logger.error("Token refresh interrupted", e);
            Thread.currentThread().interrupt();
        }

        return accessToken;
    }

    /**
     * 刷新访问令牌（直接调用微信API）
     */
    private static void refreshAccessToken() {
        try {
            String url = String.format("%scgi-bin/gettoken?corpid=%s&corpsecret=%s",
                    PROXY_URL, CORPID, CORPSECRET);
            logger.debug("刷新令牌: {}", url);

            ResponseEntity<AccToken> response = restTemplate.getForEntity(url, AccToken.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                AccToken token = response.getBody();

                // 检查微信API返回的错误码
                if (token.getErrcode() != null && token.getErrcode() == 0) {
                    // 更新缓存
                    accessToken = token.getAccess_token();

                    // 计算过期时间：微信返回的expires_in是秒，转换成毫秒时间戳
                    // 提前5分钟过期，避免在临界点使用时过期
                    long expiresInMillis = token.getExpires_in() != null ? token.getExpires_in() * 1000L : 0;
                    tokenExpireTime = System.currentTimeMillis() + expiresInMillis - (5 * 60 * 1000);

                    logger.info("Successfully refreshed token. Expires at: {}", tokenExpireTime);
                } else {
                    String errorMsg = token.getErrmsg() != null ? token.getErrmsg() : "Unknown error";
                    logger.error("Failed to refresh token. Wx error: [{}] {}",
                            token.getErrcode(), errorMsg);
                    // 清除无效缓存
                    clearTokenCache();
                }
            } else {
                logger.error("Failed to refresh token. HTTP status: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Exception while refreshing token: {}", e.getMessage(), e);
            clearTokenCache();
        }
    }

    /**
     * 清除令牌缓存
     */
    private static void clearTokenCache() {
        accessToken = null;
        tokenExpireTime = 0;
    }

    /**
     * 检查令牌是否有效
     * @return true 有效，false 无效或过期
     */
    private static boolean isTokenValid() {
        return accessToken != null && !accessToken.isEmpty() &&
                System.currentTimeMillis() < tokenExpireTime;
    }

    /**
     * 强制刷新访问令牌（用于外部显式刷新）
     */
    public static void forceRefreshToken() {
        logger.info("Force refreshing access token");
        lock.lock();
        try {
            refreshAccessToken();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 检查令牌是否即将过期（在1分钟内过期）
     * @return true 即将过期，false 无需刷新
     */
    public static boolean isTokenAboutToExpire() {
        return accessToken == null ||
                System.currentTimeMillis() > (tokenExpireTime - (60 * 1000));
    }

    /**
     * 获取令牌缓存状态（用于监控）
     * @return 令牌状态字符串
     */
    public static String getTokenStatus() {
        if (accessToken == null) {
            return "Token not initialized";
        }

        long now = System.currentTimeMillis();
        if (now > tokenExpireTime) {
            return "Token expired (expired at: " + tokenExpireTime + ")";
        }

        long secondsLeft = (tokenExpireTime - now) / 1000;
        return "Token valid, expires in: " + secondsLeft + " seconds";
    }
}