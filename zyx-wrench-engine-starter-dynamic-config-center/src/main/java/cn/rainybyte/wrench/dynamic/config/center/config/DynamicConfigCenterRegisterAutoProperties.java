package cn.rainybyte.wrench.dynamic.config.center.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 动态配置中心配置文件
 */
@ConfigurationProperties(prefix = "zyx.wrench.config.register", ignoreInvalidFields = true)
public class DynamicConfigCenterRegisterAutoProperties {

    /** redis-host **/
    private String host;
    /** redis-port **/
    private int port;
    /** 账号密码 **/
    private String password;
    /** 设置连接池大小，默认为64 **/
    private int poolSize = 64;
    /** 设置连接池的最小默认连接数，默认为10 **/
    private int midIdleSize = 10;
    /** 设置连接的最大空闲时间（单位：毫秒）， 超过该时间的空闲连接将被关闭，默认为1000 **/
    private int idleTimeout = 1000;
    /** 设置连接超时时间（单位：毫秒），默认为1000 **/
    private int connectTimeout = 1000;
    /** 设置连接重试次数，默认为3 **/
    private int retryAttempts = 3;
    /** 设置连接重试的时间间隔（单位：毫秒）， 默认为1000 **/
    private int retryInterval = 1000;
    /** 设置定期检查连接是否可用的时间间隔 （单位：毫秒），默认为0，表示不进行定期检查**/
    private int pingInterval = 0;
    /** 设置是否保持长连接 **/
    private boolean keepAlive = true;

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public void setMidIdleSize(int midIdleSize) {
        this.midIdleSize = midIdleSize;
    }

    public void setIdleTimeout(int idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public void setConnectTimeout(int connectTimeout){
        this.connectTimeout = connectTimeout;
    }

    public void setRetryAttempts(int retryAttempts) {
        this.retryAttempts = retryAttempts;
    }

    public void setRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
    }

    public void setPingInterval(int pingInterval) {
        this.pingInterval = pingInterval;
    }

    public void setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public int getMidIdleSize() {
        return midIdleSize;
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }

    public int getConnectTimeout(){
        return connectTimeout;
    }

    public int getRetryAttempts() {
        return retryAttempts;
    }

    public int getRetryInterval() {
        return retryInterval;
    }

    public int getPingInterval() {
        return pingInterval;
    }

    public boolean isKeepAlive() {
        return keepAlive;
    }
}
