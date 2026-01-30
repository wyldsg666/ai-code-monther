package com.lele.aicodemonther;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lele.aicodemonther.mapper")
public class AiCodeMontherApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiCodeMontherApplication.class, args);
	}

}
