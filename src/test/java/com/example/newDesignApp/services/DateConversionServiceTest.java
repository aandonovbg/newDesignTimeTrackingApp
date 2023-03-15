package com.example.newDesignApp.services;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ContextConfiguration(classes = {DateConversionService.class})
@ExtendWith(SpringExtension.class)
class DateConversionServiceTest {
    @MockBean
    private DateConversionService dateConversionService;


    @Test
    void testGetExpirationDateFormatted() {
        assertEquals("01.03.2020", dateConversionService.getExpirationDateFormatted("2020-03-01"));
    }

    @Test
    public void testGetCurrentDateFormatted() {
        Date date = new Date();
        String result = dateConversionService.getCurrentDateFormatted();

        String expectedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        Assert.assertEquals(expectedDate, result);
    }
}

