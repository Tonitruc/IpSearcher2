package org.example.ipsearcher.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ip_information")
public class IpEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String query;
    private String country;
    private String regionName;
    private String city;

    public IpEntity(String query, String country, String regionName, String city) {
        this.query = query;
        this.country = country;
        this.regionName = regionName;
        this.city = city;
    }
}

