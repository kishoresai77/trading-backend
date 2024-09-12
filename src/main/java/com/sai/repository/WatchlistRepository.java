package com.sai.repository;

import com.sai.modal.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchlistRepository  extends JpaRepository<Watchlist,Long> {
    Watchlist findByUserId(Long userId);
}
