package io.depression;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 抑郁症复发预警大数据平台启动类
 */
@SpringBootApplication
@MapperScan("io.depression.mapper")
public class DepressionSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(DepressionSystemApplication.class, args);
    }
}





