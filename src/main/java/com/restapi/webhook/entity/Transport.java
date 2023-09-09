package com.restapi.webhook.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "transport")
public class Transport {
    @Id
    @JsonIgnore
    @SequenceGenerator(name = "transportIdSeq", sequenceName = "transport_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transportIdSeq")
    @Column(name = "id")
    private int id;
    @Column(name = "type")
    private String type;
    @Column(name = "endpoint")
    private String endpoint;

    @OneToMany(mappedBy = "transport", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Subscription> subscription = new ArrayList<>();

}
