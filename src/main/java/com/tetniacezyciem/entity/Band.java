package com.tetniacezyciem.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

/**
 * Created by mateusz on 10.04.2017.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Band {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    private String id;
    @Column
    private String ownerName;
    @Column
    private String phoneNumber;
    @Column
    private String bandShortId;
    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Disease.class)
    @JoinTable
    private Set<Disease> diseases;
    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Contact.class)
    @JoinTable
    private Set<Contact> contacts;

}
