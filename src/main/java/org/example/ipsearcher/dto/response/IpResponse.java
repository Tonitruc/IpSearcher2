package org.example.ipsearcher.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IpResponse {
    private String query;
    private String country;
    private String regionName;
    private String city;
}
