package com.sai.repository;

import com.sai.modal.Asset;
import org.hibernate.type.descriptor.sql.internal.BinaryFloatDdlType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface  AssetsRepository extends JpaRepository<Asset, Long> {
    Asset findByIdAndUserId(Long assetId, Long userId);

    List<Asset> findByUserId(Long userId);

    Asset findByUserIdAndCoinId(Long userId, String coinId);
}
