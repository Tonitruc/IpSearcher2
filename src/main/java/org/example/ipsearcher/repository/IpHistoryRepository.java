package org.example.ipsearcher.repository;

import org.example.ipsearcher.model.IpEntity;
import org.example.ipsearcher.model.IpHistoryEntity;
import org.example.ipsearcher.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface IpHistoryRepository extends JpaRepository<IpHistoryEntity, Long> {
    @Query("SELECT iph FROM IpHistoryEntity iph WHERE iph.requestTime = :requestTime AND iph.ipEntity = :ipEntity")
    IpHistoryEntity findByRequestTimeAndIpEntity(@Param("requestTime") LocalDate requestTime, @Param("ipEntity") IpEntity ipEntity);

    @Query("SELECT iph FROM IpHistoryEntity iph WHERE iph.ipEntity = :ipEntity")
    List<IpHistoryEntity> findAllByIpEntity(@Param("ipEntity") IpEntity ipEntity);
}
