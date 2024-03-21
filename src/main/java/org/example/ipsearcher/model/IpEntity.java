package org.example.ipsearcher.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ip_entity", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"query"})
})
public class IpEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String query;
    private String country;
    private String regionName;
    private String city;

    @OneToMany(mappedBy = "ipEntity")
    @JsonBackReference
    Set<IpHistoryEntity> ipHistoryEntities = new HashSet<>();

    public IpEntity(String query, String country, String regionName, String city) {
        this.query = query;
        this.country = country;
        this.regionName = regionName;
        this.city = city;
    }

}

