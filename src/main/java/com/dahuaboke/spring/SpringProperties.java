package com.dahuaboke.spring;

import com.dahuaboke.model.BaffleMode;
import org.springframework.beans.factory.annotation.Value;

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

    @Value("${baffle.forward.connect.timeout}")
    private String forwardConnectTimeout;

    @Value("${baffle.forward.read.timeout}")
    private String forwardReadTimeout;

    @Value("${baffle.enable.inbound-links}")
    private String enableInboundLinks;

    @Value("${baffle.data.check-method}")
    private String dataCheckMethod;

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
}
