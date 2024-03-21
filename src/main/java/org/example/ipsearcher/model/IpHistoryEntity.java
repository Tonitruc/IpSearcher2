package org.example.ipsearcher.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate ;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ip_history_entity", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"request_time", "ip_entity_id"})
})
public class IpHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate requestTime;

    @ManyToOne
    @JoinColumn(name = "ip_entity_id")
    @JsonManagedReference
    private IpEntity ipEntity;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "user_ip_history",
            joinColumns = @JoinColumn(name = "ip_history_entity_id"),
            inverseJoinColumns = @JoinColumn(name = "user_entity_id"))
    @JsonManagedReference
    private Set<UserEntity> userEntities = new HashSet<>();

    public IpHistoryEntity(LocalDate  requestTime, IpEntity ipEntity) {
        this.requestTime = requestTime;
        this.ipEntity = ipEntity;
    }
}
