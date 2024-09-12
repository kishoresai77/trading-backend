package com.sai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sai.modal.Coin;
import com.sai.repository.CoinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CoinServiceImpl implements CoinService{
    private final CoinRepository coinRepository;
    private final ObjectMapper objectMapper;
    @Value("${coingecko.api.key}")
    private String API_KEY;

    @Override
    public List<Coin> getCoinList(int page) throws Exception {
        String url="https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&per_page=10&page="+page;
        RestTemplate restTemplate =  new RestTemplate();
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-cg-demo-api-key", API_KEY);


            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            log.info(response.getBody());
            return objectMapper.readValue(response.getBody(), new TypeReference<>() {
            });


        }catch(HttpClientErrorException | HttpServerErrorException | JsonProcessingException e) {
            log.error("Error: " + e);
            // Handle error accordingly
            throw new Exception("please wait for time because you are using free plan");
        }
    }

    @Override
    public String getMarketChart(String coinId, int days) throws Exception {
        String url = "https://api.coingecko.com/api/v3/coins/"+coinId+"/market_chart?vs_currency=usd&days="+days;

        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-cg-demo-api-key", API_KEY);

            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            log.error("Error: " + e);
            // Handle error accordingly
//            return null;
            throw new Exception("you are using free plan");
        }

    }

    @Override
    public String getCoinDetails(String coinId) throws JsonProcessingException {
        String baseUrl ="https://api.coingecko.com/api/v3/coins/"+coinId;

        log.error("------------------ get coin details base url "+baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-cg-demo-api-key", API_KEY);

        HttpEntity<String> entity = new HttpEntity<>(headers);


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.GET, entity, String.class);

//        Coin coins = objectMapper.readValue(response.getBody(), new TypeReference<>() {
//        });
//        coinRepository.save(coins);
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
        jsonNode.get("image").get("large");
        log.info(String.valueOf(jsonNode.get("image").get("large")));

        Coin coin = new Coin();

        coin.setId(jsonNode.get("id").asText());
        coin.setSymbol(jsonNode.get("symbol").asText());
        coin.setName(jsonNode.get("name").asText());
        coin.setImage(jsonNode.get("image").get("large").asText());

        JsonNode marketData = jsonNode.get("market_data");

        coin.setCurrentPrice(marketData.get("current_price").get("usd").asDouble());
        coin.setMarketCap(marketData.get("market_cap").get("usd").asLong());
        coin.setMarketCapRank(jsonNode.get("market_cap_rank").asInt());
        coin.setTotalVolume(marketData.get("total_volume").get("usd").asLong());
        coin.setHigh24h(marketData.get("high_24h").get("usd").asDouble());
        coin.setLow24h(marketData.get("low_24h").get("usd").asDouble());
        coin.setPriceChange24h(marketData.get("price_change_24h").asDouble());
        coin.setPriceChangePercentage24h(marketData.get("price_change_percentage_24h").asDouble());
        coin.setMarketCapChange24h(marketData.get("market_cap_change_24h").asLong());
        coin.setMarketCapChangePercentage24h(marketData.get("market_cap_change_percentage_24h").asDouble());
        coin.setCirculatingSupply(marketData.get("circulating_supply").asLong());
        coin.setTotalSupply(marketData.get("total_supply").asLong());

        coinRepository.save(coin);
        return response.getBody();
    }

    @Override
    public Coin findById(String coinId) throws Exception {
        Optional<Coin> optionalCoin;
        try {
            // Convert the string to a Long
            optionalCoin = coinRepository.findById(coinId);
        } catch (NumberFormatException e) {
            // Handle the case where the input is not a valid number
            log.info("Invalid input: " + coinId + " is not a valid number.");
            return null;
        }
        if(optionalCoin.isEmpty()) throw new Exception("invalid coin id");
        return  optionalCoin.get();

    }
    private double convertToDouble(Object value) {
        if (value instanceof Integer) {
            return ((Integer) value).doubleValue();
        } else if (value instanceof Long) {
            return ((Long) value).doubleValue();
        } else if (value instanceof Double) {
            return (Double) value;
        } else {
            throw new IllegalArgumentException("Unsupported data type: " + value.getClass().getName());
        }
    }

    @Override
    public String searchCoin(String keyword) {
        String baseUrl ="https://api.coingecko.com/api/v3/search?query="+keyword;

        HttpHeaders headers = new HttpHeaders();
        headers.set("x-cg-demo-api-key", API_KEY);

        HttpEntity<String> entity = new HttpEntity<>(headers);


        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.GET, entity, String.class);

        System.out.println(response.getBody());

        return response.getBody();

    }

    @Override
    public String getTop50CoinsByMarketCapRank() {
        String url = "https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&page=1&per_page=50";

        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-cg-demo-api-key", API_KEY);

            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Error: " + e);
            // Handle error accordingly
            return null;
        }
    }

    @Override
    public String getTradingCoins() {
        String url = "https://api.coingecko.com/api/v3/search/trending";

        RestTemplate restTemplate = new RestTemplate();
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("x-cg-demo-api-key", API_KEY);

            HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            return response.getBody();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Error: " + e);
            // Handle error accordingly
            return null;
        }
    }
    }

