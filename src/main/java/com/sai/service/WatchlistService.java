package com.sai.service;

import com.sai.modal.Coin;
import com.sai.modal.User;
import com.sai.modal.Watchlist;
import com.sai.repository.WatchlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

public interface WatchlistService {

   public Watchlist findUserWatchlist(Long userId) throws Exception;

    public Watchlist createWatchList(User user);

  public   Watchlist findById(Long id) throws Exception;

   public Coin addItemToWatchlist(Coin coin, User user) throws Exception;
}