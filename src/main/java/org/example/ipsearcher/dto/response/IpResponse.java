package org.example.ipsearcher.dto.response;

import lombok.Data;

@Data
public class IpResponse {
    private String query;
    private String country;
    private String regionName;
    private String city;
}
