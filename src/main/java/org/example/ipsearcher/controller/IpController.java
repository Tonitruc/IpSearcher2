package org.example.ipsearcher.controller;

import lombok.AllArgsConstructor;
import org.example.ipsearcher.dto.request.IpRequest;
import org.example.ipsearcher.dto.response.IpResponse;
import org.example.ipsearcher.service.IpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class IpController {
    private IpService ipService;

    @PostMapping("ip")
    public ResponseEntity<IpResponse> handleIpRequest(@RequestBody IpRequest ipRequest) {
        IpResponse response = ipService.getIpInfo(ipRequest.getQuery());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/param")
    public ResponseEntity<IpResponse> getIpInfo(@RequestParam String ip) {
        IpResponse response = ipService.getIpInfo(ip);
        return ResponseEntity.ok(response);
    }
}

