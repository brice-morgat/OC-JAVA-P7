package com.nnk.springboot.services.impl;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.IBidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BidServiceImpl implements IBidService {

    @Autowired
    BidListRepository bidListRepository;

    @Override
    public BidList saveBidList(BidList bidList) {
        return bidListRepository.save(bidList);
    }

    @Override
    public void deleteBidList(BidList bidList) {
        bidListRepository.delete(bidList);
    }
}
