package org.example.ipsearcher.controller;

import lombok.AllArgsConstructor;
import org.example.ipsearcher.dto.request.UserRequest;
import org.example.ipsearcher.model.IpHistoryEntity;
import org.example.ipsearcher.model.UserEntity;
import org.example.ipsearcher.service.IpHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/ipHistories")
public class IpHistoryController {
    private final IpHistoryService ipHistoryService;

    @GetMapping("/all")
    public List<IpHistoryEntity> getAllIpHistories() {
        return ipHistoryService.getAllIpHistories();
    }

    @GetMapping("/id/{id}")
    public IpHistoryEntity getIpHistoryById(@PathVariable Long id) {
        return ipHistoryService.getIpHistoryById(id);
    }

    @PutMapping("/add_user/{id}")
    public IpHistoryEntity addUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        UserEntity userEntity = new UserEntity(userRequest.getUsername());
        return ipHistoryService.addUser(id, userEntity);
    }

    @PutMapping("/delete_user/{id}")
    public IpHistoryEntity deleteUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
        UserEntity userEntity = new UserEntity(userRequest.getUsername());
        return ipHistoryService.deleteUser(id, userEntity);
    }

    @DeleteMapping("/delete/{id}")
    public HttpStatus deleteIpHistory(@PathVariable Long id) {
        Boolean isExist =  ipHistoryService.deleteIpHistory(id);
        if (Boolean.TRUE.equals(isExist)){
            return HttpStatus.OK;
        }else{
            return HttpStatus.BAD_REQUEST;
        }
    }
}
