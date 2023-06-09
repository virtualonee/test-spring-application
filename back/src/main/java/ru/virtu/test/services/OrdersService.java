package ru.virtu.test.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.virtu.test.dao.OrdersRepository;
import ru.virtu.test.models.OrderGoods;

import java.util.List;


@Service
@Transactional(readOnly = true)
public class OrdersService {

    private final OrdersRepository ordersRepository;

    @Autowired
    public OrdersService(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    public List<OrderGoods> findAll() {
        List<OrderGoods> orders = ordersRepository.findAll();

        return orders;
    }
    

    public OrderGoods findOne(Long id) {
        return ordersRepository.findById(id).orElse(null);
    }

    @Transactional
    public void save(OrderGoods orderGoods) {
        ordersRepository.save(orderGoods);
    }

    @Transactional
    public void update(Long id, OrderGoods updatedOrderGoods) {
        updatedOrderGoods.setId(id);
        ordersRepository.save(updatedOrderGoods);
    }

    @Transactional
    public void updateClientData(Long id, OrderGoods updatedOrderGoods) {
        updatedOrderGoods.setId(id);
        updatedOrderGoods.setOrderLines(ordersRepository.findById(id).get().getOrderLines());
        ordersRepository.save(updatedOrderGoods);
    }

    @Transactional
    public void delete(Long id) {
        ordersRepository.deleteById(id);
    }
}
