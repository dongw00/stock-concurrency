package com.example.stock.facade;

import com.example.stock.repository.RedisRepository;
import com.example.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LettuceLockStockFacade {

    private final RedisRepository redisRepository;

    private final StockService stockService;

    public void decrease(Long key, Long quantity) throws InterruptedException {
        while (!redisRepository.lock(key)) {
            Thread.sleep(100);
        }

        try {
            stockService.decrease(key, quantity);
        } finally {
            redisRepository.unlock(key);
        }
    }
}
