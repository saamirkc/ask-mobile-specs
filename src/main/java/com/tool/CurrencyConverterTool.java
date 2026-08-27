package com.tool;

import com.dto.CurrencyConverterResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RequiredArgsConstructor
@Service
@Slf4j
public class CurrencyConverterTool {

    @Value("${EXCHANGE_RATE_KEY}")
    private String key;

    private final RestClient restClient;

    @Tool(description = "Convert a monetary amount from one currency to another using live exchange rates. " +
            "Call this whenever the user asks for a price in a different currency than the one given in context.")
    public String convert( @ToolParam(description = "Numeric amount to convert, e.g. 330") double amount,
                                              @ToolParam(description = "3-letter ISO code of the currency you have, e.g. EUR") String fromCurrency,
                                              @ToolParam(description = "3-letter ISO code you want to convert to, e.g. USD") String toCurrency){
        log.info("Converting {} {} -> {}", amount, fromCurrency, toCurrency);

        CurrencyConverterResponse response = restClient.
                get().
                uri("https://v6.exchangerate-api.com/v6/{key}/latest/{base}",key,fromCurrency.toUpperCase()).
                retrieve().
                body(CurrencyConverterResponse.class);

        BigDecimal rate= response.getConversionRates().get(toCurrency.toUpperCase());
        BigDecimal converted= rate.multiply(BigDecimal.valueOf(amount))
                .setScale(2, RoundingMode.HALF_UP);

           return converted + " " + toCurrency.toUpperCase();

        
    }

}
