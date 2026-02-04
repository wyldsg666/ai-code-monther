package com.lele.aicodemonther.core;

import com.lele.aicodemonther.model.enums.CodeGenTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.List;

@SpringBootTest
class AiCodeGeneratorFacadeTest {

    @Resource
    private AiCodeGeneratorFacade aiCodeGeneratorFacade;

    @Test
    void generatorAndSaveCode() {
        File filePath = aiCodeGeneratorFacade.generatorAndSaveCode("生成一个登录页面,总共不超过50行代码", CodeGenTypeEnum.MULTI_FILE, 1L);
        Assertions.assertNotNull(filePath);
    }

    @Test
    void generatorAndSaveCodeStream() {
        Flux<String> codeStream = aiCodeGeneratorFacade.generatorAndSaveCodeStream("生成一个界面美观的登录页面,总共不超过100行代码", CodeGenTypeEnum.HTML, 1L);
        // 阻塞等待所有数据收集完成
        List<String> resultList = codeStream.collectList().block();
        // 验证结果
        Assertions.assertNotNull(resultList);
        // 将结果拼接成完整的代码
        String completeCode = String.join("", resultList);
        Assertions.assertNotNull(completeCode);
    }
}