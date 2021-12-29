package com.plf.action.boot;

import com.plf.action.boot.netty.NettyServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

/**
 * @author panlf
 * @date 2021/12/29
 */
@SpringBootApplication
public class BootNettyApplication implements CommandLineRunner {

    @Resource
    private NettyServer nettyServer;

    public static void main(String[] args) {
        SpringApplication.run(BootNettyApplication.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        nettyServer.run();
    }
}
