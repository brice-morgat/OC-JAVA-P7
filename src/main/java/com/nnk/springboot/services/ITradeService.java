package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;

import java.util.List;

public interface ITradeService {

    /**
     * Save a trade.
     *
     * @param trade The trade object to be saved.
     * @return Trade
     */
    public Trade saveTrade(Trade trade);

    /**
     * Delete the given trade from the database.
     *
     * @param trade The trade to be deleted.
     */
    public void deleteTrade(Trade trade);

    /**
     * Get all the trades.
     *
     * @return A list of all the trades in the database.
     */
    public List<Trade> getAllTrade();

    /**
     * Get a trade by its id.
     *
     * @param id The id of the trade to be retrieved.
     * @return A Trade object.
     */
    public Trade getTradeById(Integer id);
}
