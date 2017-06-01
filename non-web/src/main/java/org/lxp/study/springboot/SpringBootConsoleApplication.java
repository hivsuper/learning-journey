package org.lxp.study.springboot;

import javax.annotation.Resource;

import org.lxp.study.springboot.service.CustomerService;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.lxp.study.springboot.dao")
public class SpringBootConsoleApplication implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(SpringBootConsoleApplication.class);
    @Resource
    private CustomerService customerService;

    public static void main(String[] args) throws Exception {
        // SpringApplication.run(SpringBootConsoleApplication.class, "display");
        SpringApplication.run(SpringBootConsoleApplication.class, "insert", "1", "1@2.com");
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length <= 0) {
            System.err.println("[Usage] java xxx.jar {insert name email | display}");
        } else {
            switch (args[0]) {
            case "insert":
                customerService.addCustomer(args[1], args[2]);
                break;

            case "display":
                customerService.findAll().forEach(x -> LOG.info("{}", x));
                break;
            }
            LOG.info("Done!");
        }
        System.exit(0);
    }
}