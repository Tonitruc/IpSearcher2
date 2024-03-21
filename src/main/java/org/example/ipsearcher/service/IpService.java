package org.example.ipsearcher.service;

import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.example.ipsearcher.dto.response.IpResponse;
import org.example.ipsearcher.model.IpEntity;
import org.example.ipsearcher.model.IpHistoryEntity;
import org.example.ipsearcher.model.UserEntity;
import org.example.ipsearcher.dto.request.IpRequest;
import org.example.ipsearcher.repository.IpHistoryRepository;
import org.example.ipsearcher.repository.IpRepository;
import org.example.ipsearcher.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class IpService {
    private final IpRepository ipRepository;
    private final IpHistoryRepository ipHistoryRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;

    public IpResponse getIpLocation(String ip) {
        String baseUrl = "http://ip-api.com/json/";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path(ip);
        return restTemplate.getForObject(builder.toUriString(), IpResponse.class);
    }

    public IpResponse getIpInfo(IpRequest ipRequest) {
        String ip = ipRequest.getQuery();
        String username = ipRequest.getUsername();

        if (!isValidIp(ip)) {
            throw new IllegalArgumentException("Invalid IP address");
        }
        IpResponse response = getIpLocation(ip);

        IpEntity ipEntity = ipRepository.findByQuery(response.getQuery());
        if (ipEntity == null) {
            ipEntity = new IpEntity(response.getQuery(), response.getCountry(), response.getRegionName(), response.getCity());
            ipRepository.save(ipEntity);
        }

        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(username);
        UserEntity userEntity;
        if (userEntityOptional.isPresent()) {
            userEntity = userEntityOptional.get();
        } else {
            userEntity = new UserEntity(username);
            userRepository.save(userEntity);
        }

        IpHistoryEntity ipHistoryEntity = new IpHistoryEntity(LocalDate.now(), ipEntity);
        ipHistoryEntity.getUserEntities().add(userEntity); // добавляем пользователя в множество userEntities объекта IpHistoryEntity

        // Проверяем, существует ли уже запись с таким же значением "request_time" и "ip_entity_id"
        IpHistoryEntity existingIpHistoryEntity = ipHistoryRepository.findByRequestTimeAndIpEntity(ipHistoryEntity.getRequestTime(), ipHistoryEntity.getIpEntity());
        if (existingIpHistoryEntity == null) {
            // Если записи нет, сохраняем новую
            ipHistoryRepository.save(ipHistoryEntity);
        } else {
            // Если запись уже существует, добавляем пользователя в множество userEntities существующего объекта IpHistoryEntity
            existingIpHistoryEntity.getUserEntities().add(userEntity);
            ipHistoryRepository.save(existingIpHistoryEntity);
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

    public List<IpEntity> getAllIps() {
        return ipRepository.findAll();
    }

    public IpEntity getIpById(Long id) {
        return ipRepository.findById(id).orElseThrow(() -> new RuntimeException("IP not found"));
    }

    public IpEntity updateIp(IpEntity ipEntity) {
        Optional<IpEntity> isExist = ipRepository.findById(ipEntity.getId());
        if(isExist.isEmpty())
            return null;

        String ip = ipEntity.getQuery();
        if (!isValidIp(ip)) {
            throw new IllegalArgumentException("Invalid IP address");
        }

        IpResponse response = getIpLocation(ip);
        ipEntity.setCountry(response.getCountry());
        ipEntity.setRegionName(response.getRegionName());
        ipEntity.setCity(response.getCity());

        return ipRepository.save(ipEntity);
    }

    public Boolean deleteIp(Long id) {
        Optional<IpEntity> existIp = ipRepository.findById(id);
        if(existIp.isEmpty())
            return false;

        List<IpHistoryEntity> iph = ipHistoryRepository.findAllByIpEntity(existIp.get());
        ipHistoryRepository.deleteAll(iph);

        userRepository.deleteById(id);
        return true;
    }
}
