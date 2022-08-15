package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.ITradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TradeServiceImpl implements ITradeService {
    @Autowired
    private TradeRepository tradeRepository;

    @Override
    public Trade saveTrade(Trade trade) {
        Objects.requireNonNull(trade);
        if (trade.getAccount() == null) {
            throw new InvalidInputException("Rule name must not be null");
        }
        return tradeRepository.save(trade);
    }

    @Override
    public void deleteTrade(Trade trade) {
        Objects.requireNonNull(trade);
        tradeRepository.delete(trade);
    }

    @Override
    public List<Trade> getAllTrade() {
        return tradeRepository.findAll();
    }

    @Override
    public Trade getTradeById(Integer id) {
            return tradeRepository.getById(id);
    }
}
