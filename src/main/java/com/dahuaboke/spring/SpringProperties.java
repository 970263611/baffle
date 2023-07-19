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
}
