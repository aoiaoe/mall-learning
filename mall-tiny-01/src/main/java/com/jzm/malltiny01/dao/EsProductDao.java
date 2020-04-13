package com.jzm.malltiny01.dao;

import com.jzm.malltiny01.es.entity.EsProduct;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 搜索系统中的商品管理自定义Dao
 * Created by macro on 2018/6/19.
 */
@Component
public interface EsProductDao {
    List<EsProduct> getAllEsProductList(@Param("id") Long id);
}
