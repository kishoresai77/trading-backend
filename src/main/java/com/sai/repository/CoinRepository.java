package com.sai.repository;

import com.sai.modal.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CoinRepository  extends JpaRepository<Coin,String> {
    Optional<Coin> findById(String id);
//    @Query("select c from coin c where c.id= :id")
//    Optional<Coin> findCoinByCustomerId(@Param("id") String id);
}
