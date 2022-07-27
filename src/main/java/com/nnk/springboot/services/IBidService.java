package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;

public interface IBidService {
    /**
     * Save a bid list.
     *
     * @param bidList The bid list to be saved.
     * @return The BidList object that was saved.
     */
    public BidList saveBidList(BidList bidList);

    /**
     * Delete a bid list.
     *
     * @param bidList The bidList object to be deleted.
     * @return The BidList object that was deleted.
     */
    public void deleteBidList(BidList bidList);
}
