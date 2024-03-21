package org.example.ipsearcher.service;

import lombok.AllArgsConstructor;
import org.example.ipsearcher.dto.request.UserRequest;
import org.example.ipsearcher.model.IpHistoryEntity;
import org.example.ipsearcher.model.UserEntity;
import org.example.ipsearcher.repository.IpHistoryRepository;
import org.example.ipsearcher.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.StyledEditorKit;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class IpHistoryService {

    private final IpHistoryRepository ipHistoryRepository;
    private final UserRepository userRepository;

    public List<IpHistoryEntity> getAllIpHistories() {
        return ipHistoryRepository.findAll();
    }

    public IpHistoryEntity getIpHistoryById(Long id) {
        return ipHistoryRepository.findById(id).orElseThrow(() -> new RuntimeException("IP history not found"));
    }

    public Boolean deleteIpHistory(Long id) {
        Optional<IpHistoryEntity> existIpHistory = ipHistoryRepository.findById(id);
        if(existIpHistory.isEmpty())
            return false;

        ipHistoryRepository.deleteById(id);
        return true;
    }

    public IpHistoryEntity addUser(Long id, UserEntity userEntity) {
        Optional<IpHistoryEntity> existIpHistory = ipHistoryRepository.findById(id);
        Optional<UserEntity> existUser = userRepository.findByUsername(userEntity.getUsername());
        if(existIpHistory.isEmpty())
            return null;
        if(existUser.isEmpty())
            userEntity = userRepository.save(userEntity);

        existIpHistory.get().getUserEntities().add(userEntity);
        ipHistoryRepository.save(existIpHistory.get());

        return existIpHistory.get();
    }

    public IpHistoryEntity deleteUser(Long id, UserEntity userEntity) {
        Optional<IpHistoryEntity> existIpHistory = ipHistoryRepository.findById(id);
        Optional<UserEntity> existUser = userRepository.findByUsername(userEntity.getUsername());
        if(existIpHistory.isEmpty()) {
            return null;
        }
        if(existUser.isEmpty()) {
            return null;
        } else {
            existIpHistory.get().getUserEntities().remove(existUser.get());
            ipHistoryRepository.save(existIpHistory.get());
        }

        return existIpHistory.get();
    }
}
