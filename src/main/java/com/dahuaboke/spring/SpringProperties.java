package com.dahuaboke.spring;

import com.dahuaboke.model.BaffleMode;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

/**
 * @author dahua
 * @time 2023/7/18 9:49
 */
public class SpringProperties {

    @Value("${baffle.data.filename}")
    private String[] dataFilename;

    @Value("${baffle.forward.address}")
    private String[] forwardAddress;

    @Value("${baffle.global.mode}")
    private String baffleMode;

    @Value("${baffle.global.timeout}")
    private int globalTimeout;

    @Value("${baffle.forward.connect.timeout}")
    private String forwardConnectTimeout;

    @Value("${baffle.forward.read.timeout}")
    private String forwardReadTimeout;

    @Value("${baffle.enable.inbound-links}")
    private String enableInboundLinks;

    @Value("${baffle.data.check-method}")
    private String dataCheckMethod;

    @Value("${baffle.global.http.wait}")
    private int globalHttpWait;

    @Value("#{${baffle.http.wait.uri-map}}")
    private Map<String, Integer> httpUriWaiters;

    public String[] getDataFilename() {
        return dataFilename;
    }

    public void setDataFilename(String[] dataFilename) {
        this.dataFilename = dataFilename;
    }

    public String[] getForwardAddress() {
        return forwardAddress;
    }

    public void setForwardAddress(String[] forwardAddress) {
        this.forwardAddress = forwardAddress;
    }

    public BaffleMode getBaffleMode() {
        return BaffleMode.getBaffleMode(baffleMode);
    }

    public void setBaffleMode(String baffleMode) {
        this.baffleMode = baffleMode;
    }

    public int getForwardConnectTimeout() {
        return forwardConnectTimeout == null ? 3000 : Integer.parseInt(forwardConnectTimeout);
    }

    public void setForwardConnectTimeout(String forwardConnectTimeout) {
        this.forwardConnectTimeout = forwardConnectTimeout;
    }

    public int getForwardReadTimeout() {
        return forwardReadTimeout == null ? 3000 : Integer.parseInt(forwardReadTimeout);
    }

    public void setForwardReadTimeout(String forwardReadTimeout) {
        this.forwardReadTimeout = forwardReadTimeout;
    }

    public boolean getEnableInboundLinks() {
        return "true".equalsIgnoreCase(enableInboundLinks);
    }

    public void setEnableInboundLinks(String enableInboundLinks) {
        this.enableInboundLinks = enableInboundLinks;
    }

    public boolean getDataCheckMethod() {
        return dataCheckMethod == null ? false : Boolean.parseBoolean(dataCheckMethod);
    }

    public void setDataCheckMethod(String dataCheckMethod) {
        this.dataCheckMethod = dataCheckMethod;
    }

    public long getGlobalTimeout() {
        return globalTimeout == 0 ? 5000 : new Long(globalTimeout);
    }

    public void setGlobalTimeout(int globalTimeout) {
        this.globalTimeout = globalTimeout;
    }

    public int getGlobalHttpWait() {
        return globalHttpWait;
    }

    public void setGlobalHttpWait(int globalHttpWait) {
        this.globalHttpWait = globalHttpWait;
    }

    public Map<String, Integer> getHttpUriWaiters() {
        return httpUriWaiters;
    }

    public void setHttpUriWaiters(Map<String, Integer> httpUriWaiters) {
        this.httpUriWaiters = httpUriWaiters;
    }
}
