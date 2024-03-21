package org.example.ipsearcher.service;

import lombok.AllArgsConstructor;
import org.apache.catalina.User;
import org.example.ipsearcher.dto.request.UserRequest;
import org.example.ipsearcher.model.IpHistoryEntity;
import org.example.ipsearcher.model.UserEntity;
import org.example.ipsearcher.repository.IpHistoryRepository;
import org.example.ipsearcher.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final IpHistoryRepository ipHistoryRepository;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserEntity createUser(UserRequest userRequest) {
        return userRepository.save(new UserEntity(userRequest.getUsername()));
    }

    public UserEntity updateUser(UserEntity userEntity) {
        Optional<UserEntity> existUser = userRepository.findById(userEntity.getId());
        if(existUser.isEmpty() || userEntity.getUsername().isEmpty())
            return null;
        return userRepository.save(userEntity);
    }

    public Boolean deleteUser(Long id) {
        Optional<UserEntity> existUser = userRepository.findById(id);
        if(existUser.isEmpty())
            return false;

        List<IpHistoryEntity> iphs = ipHistoryRepository.findAll();
        for(IpHistoryEntity iph : iphs) {
            if(iph.getUserEntities().contains(existUser.get())) {
                iph.getUserEntities().remove(existUser.get());
            }
        }

        userRepository.deleteById(id);
        return true;
    }
}
