package org.example.ipsearcher.service;

import lombok.AllArgsConstructor;
import org.example.ipsearcher.dto.response.IpResponse;
import org.example.ipsearcher.model.IpEntity;
import org.example.ipsearcher.repository.IpRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class IpService {
    private final IpRepository ipRepository;
    private final RestTemplate restTemplate;

    public IpResponse getIpInfo(String ip) {
        if (!isValidIp(ip)) {
            throw new IllegalArgumentException("Invalid IP address");
        }

        String url = "http://ip-api.com/json/" + ip;
        IpResponse response = restTemplate.getForObject(url, IpResponse.class);

        if (response.getCountry() != null && response.getRegionName() != null && response.getCity() != null) {
            ipRepository.save(new IpEntity(response.getQuery(), response.getCountry(), response.getRegionName(), response.getCity()));
        }

        return response;
    }

    private boolean isValidIp(String ip) {
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
