package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public void add(ShoppingCartDTO dto) {
        ShoppingCart cart = new ShoppingCart();
        BeanUtils.copyProperties(dto, cart);
        cart.setUserId(BaseContext.getCurrentId());
        List<ShoppingCart> list = shoppingCartMapper.getByUserId(BaseContext.getCurrentId());
        for (ShoppingCart sc : list) {
            if (dto.getDishId() != null && dto.getDishId().equals(sc.getDishId()) && cart.getDishFlavor() != null && cart.getDishFlavor().equals(sc.getDishFlavor())) {
                sc.setNumber(sc.getNumber() + 1);
                Dish dish = dishMapper.getById(dto.getDishId());
                sc.setAmount(dish.getPrice().multiply(new java.math.BigDecimal(sc.getNumber())));
                shoppingCartMapper.update(sc);
                return;
            }
            if (dto.getSetmealId() != null && dto.getSetmealId().equals(sc.getSetmealId())) {
                sc.setNumber(sc.getNumber() + 1);
                Setmeal sm = setmealMapper.getById(dto.getSetmealId());
                sc.setAmount(sm.getPrice().multiply(new java.math.BigDecimal(sc.getNumber())));
                shoppingCartMapper.update(sc);
                return;
            }
        }
        if (dto.getDishId() != null) {
            Dish dish = dishMapper.getById(dto.getDishId());
            cart.setName(dish.getName());
            cart.setImage(dish.getImage());
            cart.setAmount(dish.getPrice());
            cart.setNumber(1);
        } else if (dto.getSetmealId() != null) {
            Setmeal sm = setmealMapper.getById(dto.getSetmealId());
            cart.setName(sm.getName());
            cart.setImage(sm.getImage());
            cart.setAmount(sm.getPrice());
            cart.setNumber(1);
        }
        cart.setCreateTime(LocalDateTime.now());
        shoppingCartMapper.insert(cart);
    }

    @Override
    public List<ShoppingCart> list() {
        return shoppingCartMapper.getByUserId(BaseContext.getCurrentId());
    }

    @Override
    public void sub(ShoppingCartDTO dto) {
        List<ShoppingCart> list = shoppingCartMapper.getByUserId(BaseContext.getCurrentId());
        for (ShoppingCart sc : list) {
            if (dto.getDishId() != null && dto.getDishId().equals(sc.getDishId())) {
                if (sc.getNumber() == 1) {
                    shoppingCartMapper.deleteById(sc.getId());
                } else {
                    sc.setNumber(sc.getNumber() - 1);
                    Dish dish = dishMapper.getById(dto.getDishId());
                    sc.setAmount(dish.getPrice().multiply(new java.math.BigDecimal(sc.getNumber())));
                    shoppingCartMapper.update(sc);
                }
                return;
            }
            if (dto.getSetmealId() != null && dto.getSetmealId().equals(sc.getSetmealId())) {
                if (sc.getNumber() == 1) {
                    shoppingCartMapper.deleteById(sc.getId());
                } else {
                    sc.setNumber(sc.getNumber() - 1);
                    Setmeal sm = setmealMapper.getById(dto.getSetmealId());
                    sc.setAmount(sm.getPrice().multiply(new java.math.BigDecimal(sc.getNumber())));
                    shoppingCartMapper.update(sc);
                }
                return;
            }
        }
    }

    @Override
    public void clean() {
        shoppingCartMapper.deleteByUserId(BaseContext.getCurrentId());
    }
}
