package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.IBidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BidServiceImpl implements IBidService {
    @Autowired
    private BidListRepository bidListRepository;


    @Override
    public BidList saveBidList(BidList bidList) {
        Objects.requireNonNull(bidList);
        if (bidList.getAccount() == null || bidList.getType() == null) {
            throw new InvalidInputException("At least one of required field is missing");
        }
        return bidListRepository.save(bidList);
    }

    @Override
    public void deleteBidList(BidList bidList) {
        Objects.requireNonNull(bidList);
        bidListRepository.delete(bidList);
    }

    @Override
    public List<BidList> getAllBidList() {
        return bidListRepository.findAll();
    }

    @Override
    public BidList getBidListById(Integer id) {
        BidList bidList = bidListRepository.getById(id);
        if (bidList != null) {
            return bidList;
        }
        throw new InvalidInputException("BidList not Found");
    }


}
