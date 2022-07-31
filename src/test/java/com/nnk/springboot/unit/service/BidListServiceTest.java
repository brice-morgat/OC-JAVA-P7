package com.nnk.springboot.unit.service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.InvalidInputException;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.services.impl.BidServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
public class BidListServiceTest {
    @InjectMocks
    private BidServiceImpl bidService;

    @Mock
    private BidListRepository bidListRepository;

    @Test
    public void findAllBidListTest() {
        //Given
        List<BidList> bidLists = new ArrayList();
        BidList bidListOne = new BidList("Account1", "Type1", 1d);
        BidList bidListTwo = new BidList("Account2", "Type2", 2d);
        bidLists.add(bidListOne);
        bidLists.add(bidListTwo);
        //When
        when(bidListRepository.findAll()).thenReturn(bidLists);
        //Then
        assertEquals(bidService.getAllBidList(), bidLists);
    }

    @Test
    public void saveBidListTest() {
        //Given
        BidList bidList = new BidList("Account1", "Type1", 1d);
        //When
        when(bidListRepository.save(any())).thenReturn(bidList);
        //Then
        assertEquals(bidService.saveBidList(bidList), bidList);
    }

    @Test
    public void saveBidListWithoutAllArgErrorTest() {
        //Given
        BidList bidList = new BidList();
        //Then
        assertThrows(InvalidInputException.class, () -> bidService.saveBidList(bidList));
    }

    @Test
    public void saveBidListWithoutAccountErrorTest() {
        //Given
        BidList bidList = new BidList(null, "Type1", 1d);
        //Then
        assertThrows(InvalidInputException.class, () -> bidService.saveBidList(bidList));
    }

    @Test
    public void saveBidListWithoutTypeErrorTest() {
        //Given
        BidList bidList = new BidList("Account", null, 1d);
        //Then
        assertThrows(InvalidInputException.class, () -> bidService.saveBidList(bidList));
    }

    @Test
    public void deleteBidListTest() {
        //Then
        assertDoesNotThrow(() -> bidService.deleteBidList(new BidList()));
    }

    @Test
    public void deleteBidListNullErrorTest() {
        //Then
        assertThrows(Exception.class,() -> bidService.deleteBidList(null));
    }

    @Test
    public void getBidListByIdTest() {
        //Given
        BidList bidList = new BidList("account", "type", 1d);
        //When
        when(bidListRepository.getById(anyInt())).thenReturn(bidList);
        //Then
        assertEquals(bidService.getBidListById(1), bidList);
    }
}
