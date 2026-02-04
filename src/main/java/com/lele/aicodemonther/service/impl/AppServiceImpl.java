package com.lele.aicodemonther.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.lele.aicodemonther.model.entity.App;
import com.lele.aicodemonther.mapper.AppMapper;
import com.lele.aicodemonther.service.AppService;
import org.springframework.stereotype.Service;

/**
 * 应用 服务层实现。
 *
 * @author <a href="https://github.com/wyldsg666">ZoneSt</a>
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App>  implements AppService{

}
