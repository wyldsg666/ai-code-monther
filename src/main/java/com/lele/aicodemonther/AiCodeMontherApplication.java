package com.lele.aicodemonther;

import dev.langchain4j.community.store.embedding.redis.spring.RedisEmbeddingStoreAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {RedisEmbeddingStoreAutoConfiguration.class})
@MapperScan("com.lele.aicodemonther.mapper")
public class AiCodeMontherApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiCodeMontherApplication.class, args);
    }

}
