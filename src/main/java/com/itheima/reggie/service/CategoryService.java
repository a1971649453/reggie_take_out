package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Category;

/**
 * @author 金宗文
 * @version 1.0
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
