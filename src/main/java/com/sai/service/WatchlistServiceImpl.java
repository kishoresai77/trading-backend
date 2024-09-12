package com.sai.service;

import com.sai.modal.Coin;
import com.sai.modal.User;
import com.sai.modal.Watchlist;
import com.sai.repository.WatchlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WatchlistServiceImpl implements WatchlistService {

    private final WatchlistRepository watchlistRepository;


    @Override
    public Watchlist findUserWatchlist(Long userId) throws Exception {
        Watchlist watchlist = watchlistRepository.findByUserId(userId);
        if (watchlist == null) {
            throw new Exception("watch not found");
        }
        return watchlist;
    }

    @Override
    public Watchlist createWatchList(User user) {
        Watchlist watchlist = new Watchlist();
        watchlist.setUser(user);
        return watchlistRepository.save(watchlist);
    }

    @Override
    public Watchlist findById(Long id) throws Exception {
        Optional<Watchlist> optionalWatchlist = watchlistRepository.findById(id);
        if (optionalWatchlist.isEmpty()) {
            throw new Exception("watch list not found");
        }
        return optionalWatchlist.get();
    }

    @Override
    public Coin addItemToWatchlist(Coin coin, User user) throws Exception {
        Watchlist watchlist = findUserWatchlist(user.getId());

        if (watchlist.getCoins().contains(coin)) {
            watchlist.getCoins().remove(coin);
        } else watchlist.getCoins().add(coin);
        watchlistRepository.save(watchlist);
        return coin;
    }
}