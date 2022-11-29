package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;

import java.util.List;

/**
 * @author 金宗文
 * @version 1.0
 */
public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐,同时保存套餐和菜品的关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐,同时删除套餐和菜品的关系
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

    /**
     * 根据id查询套餐,同时查询套餐和菜品的关系
     * @param id
     * @return
     */
    public SetmealDto getByIdWithDish(Long id);

    /**
     * 更新套餐,同时更新套餐和菜品的关系
     * @param setmealDto
     */
    public void updateWithDish(SetmealDto setmealDto);
}
