package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;

import java.util.List;

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

    /**
     * It returns a list of all the bid lists.
     *
     * @return A list of all the bids in the database.
     */
    public List<BidList> getAllBidList();

    /**
     * > This function returns a BidList object with the given id
     *
     * @param id The id of the bid list you want to get.
     * @return A BidList object.
     */
    public BidList getBidListById(Integer id);
}
