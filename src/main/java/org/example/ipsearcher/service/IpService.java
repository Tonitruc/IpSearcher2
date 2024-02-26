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
        if(!isValidIp(ip))
            throw new IllegalArgumentException("Invalid IP address");

        String url = "http://ip-api.com/json/" + ip;
        IpResponse response = restTemplate.getForObject(url, IpResponse.class);

        if (response.getCountry() != null && response.getRegionName() != null && response.getCity() != null) {
            ipRepository.save(new IpEntity(response.getQuery(), response.getCountry(),
                    response.getRegionName(), response.getCity()));
        }

        return response;
    }

    public boolean isValidIp(String ip) {
        String ipPattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return Pattern.compile(ipPattern).matcher(ip).matches();
    }
}
