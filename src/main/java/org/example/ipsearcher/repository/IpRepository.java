package org.example.ipsearcher.repository;

import org.example.ipsearcher.model.IpEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IpRepository extends JpaRepository<IpEntity, Long> {
    IpEntity findByQuery(String query);

}
