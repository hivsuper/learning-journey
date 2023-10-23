package org.lxp.springboot;

import javax.annotation.Resource;

import org.lxp.springboot.service.CustomerService;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@MapperScan("org.lxp.springboot.dao")
public class Bootstrap implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(Bootstrap.class);
    @Resource
    private CustomerService customerService;

    public static void main(String[] args) throws Exception {
        // SpringApplication.run(Bootstrap.class, "display");
        SpringApplication.run(Bootstrap.class, "insert", "1", "1@2.com");
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