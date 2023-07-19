package com.dahuaboke.spring;

import com.dahuaboke.model.BaffleMode;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author dahua
 * @time 2023/7/18 9:49
 */
public class SpringProperties {

    @Value("${baffle.forward.address}")
    private String[] forwardAddress;

    @Value("${baffle.global.mode}")
    private String baffleMode;

    @Value("${baffle.forward.connect.timeout}")
    private String forwardConnectTimeout;

    @Value("${baffle.forward.read.timeout}")
    private String forwardReadTimeout;

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
        return forwardConnectTimeout == null ? 5000 : Integer.parseInt(forwardConnectTimeout);
    }

    public void setForwardConnectTimeout(String forwardConnectTimeout) {
        this.forwardConnectTimeout = forwardConnectTimeout;
    }

    public int getForwardReadTimeout() {
        return forwardReadTimeout == null ? 5000 : Integer.parseInt(forwardReadTimeout);
    }

    public void setForwardReadTimeout(String forwardReadTimeout) {
        this.forwardReadTimeout = forwardReadTimeout;
    }
}
