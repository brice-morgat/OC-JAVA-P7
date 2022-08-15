package com.nnk.springboot.unit.service;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.impl.TradeServiceImpl;
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
public class TradeServiceTest {
    @InjectMocks
    private TradeServiceImpl tradeService;

    @Mock
    private TradeRepository tradeRepository;

    @Test
    public void findAllTradeTest() {
        //Given
        List<Trade> trades = new ArrayList();
        Trade tradeOne = new Trade();
        Trade tradeTwo = new Trade();
        trades.add(tradeOne);
        trades.add(tradeTwo);
        //When
        when(tradeRepository.findAll()).thenReturn(trades);
        //Then
        assertEquals(tradeService.getAllTrade(), trades);
    }

    @Test
    public void saveTradeTest() {
        //Given
        Trade trade = new Trade();
        trade.setAccount("tradename");
        trade.setBenchmark("password");
        //When
        when(tradeRepository.save(any())).thenReturn(trade);
        //Then
        assertEquals(tradeService.saveTrade(trade), trade);
    }

    @Test
    public void deleteTradeTest() {
        //Then
        assertDoesNotThrow(() -> tradeService.deleteTrade(new Trade()));
    }

    @Test
    public void deleteTradeNullErrorTest() {
        //Then
        assertThrows(Exception.class,() -> tradeService.deleteTrade(null));
    }

    @Test
    public void getTradeByIdTest() {
        //Given
        Trade trade = new Trade();
        //When
        when(tradeRepository.getById(anyInt())).thenReturn(trade);
        //Then
        assertEquals(tradeService.getTradeById(1), trade);
    }
}
