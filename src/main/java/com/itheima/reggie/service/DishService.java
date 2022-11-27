package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;

/**
 * @author 金宗文
 * @version 1.0
 */
public interface DishService extends IService<Dish> {
    /**
     * 新增菜品,同时插入菜品对应的口味数据,需要操作两张表:dish,dish_flavor
     * @param dishDto
     */
    public void saveWithFlavor(DishDto dishDto);

    /**
     * 查询菜品详情,同时查询菜品对应的口味数据
     * @param id
     * @return
     */
    public DishDto getByIdWithFlavor(Long id);

    /**
     * 更新菜品信息和口味信息
     * @param dishDto
     */
    public void updateWithFlavor(DishDto dishDto);

}
