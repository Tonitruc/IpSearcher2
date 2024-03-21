package org.example.ipsearcher.controller;

import lombok.AllArgsConstructor;
import org.example.ipsearcher.dto.request.IpRequest;
import org.example.ipsearcher.dto.response.IpResponse;
import org.example.ipsearcher.model.IpEntity;
import org.example.ipsearcher.service.IpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/ip")
public class IpController {
    private final IpService ipService;

    @GetMapping("/all")
    public List<IpEntity> getAllIps() {
        return ipService.getAllIps();
    }

    @GetMapping("/id/{id}")
    public IpEntity getIpById(@PathVariable Long id) {
        return ipService.getIpById(id);
    }

    @PostMapping("/search")
    public ResponseEntity<IpResponse> handleIpRequest(@RequestBody IpRequest ipRequest) {
        IpResponse response = ipService.getIpInfo(ipRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    public IpEntity updateIp(@PathVariable Long id, @RequestBody IpRequest ipRequest) {
        IpEntity ipEntity = new IpEntity();
        ipEntity.setId(id);
        ipEntity.setQuery(ipRequest.getQuery());
        return ipService.updateIp(ipEntity);
    }

    @DeleteMapping("/delete/{id}")
    public HttpStatus deleteIp(@PathVariable Long id) {
        Boolean isExist =  ipService.deleteIp(id);
        if (Boolean.TRUE.equals(isExist)){
            return HttpStatus.OK;
        }else{
            return HttpStatus.BAD_REQUEST;
        }
    }
}

