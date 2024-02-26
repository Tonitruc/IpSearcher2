package org.example.ipsearcher.service;

import lombok.AllArgsConstructor;
import org.example.ipsearcher.dto.response.IpResponse;
import org.example.ipsearcher.model.IpEntity;
import org.example.ipsearcher.repository.IpRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class IpService {
    private final IpRepository ipRepository;
    private final RestTemplate restTemplate;

    public IpResponse getIpInfo(String ip) {
        if(!isValidIp(ip))
            throw new IllegalArgumentException("Invalid IP address");

        String baseUrl = "http://ip-api.com/json/";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(ip);
        IpResponse response = restTemplate.getForObject(builder.toUriString(), IpResponse.class);

        if (response.getCountry() != null && response.getRegionName() != null && response.getCity() != null) {
            ipRepository.save(new IpEntity(response.getQuery(), response.getCountry(),
                    response.getRegionName(), response.getCity()));
        }

        return response;
    }

    public boolean isValidIp(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }

        String[] parts = ip.split("\\.");
        if (parts.length != 4) {
            return false;
        }

        for (String part : parts) {
            try {
                int value = Integer.parseInt(part);
                if (value < 0 || value > 255) {
                    return false;
                }
            } catch (NumberFormatException e) {
                return false;
            }
        }

        return true;
    }
}
