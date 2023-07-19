package com.dahuaboke;

import com.dahuaboke.netty.NettyStarter;
import com.dahuaboke.spring.SpringStarter;

/**
 * @author dahua
 * @time 2023/7/17 10:25
 */
public class Starter {

    public static void main(String[] args) {
        SpringStarter springStarter = new SpringStarter();
        springStarter.run();
        NettyStarter nettyStarter = new NettyStarter();
        nettyStarter.run();
    }
}
