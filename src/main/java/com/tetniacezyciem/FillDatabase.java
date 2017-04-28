package com.tetniacezyciem;

import com.tetniacezyciem.entity.Disease;
import com.tetniacezyciem.repository.DiseaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by mateusz on 10.04.2017.
 */
@Component
public class FillDatabase {

    private final DiseaseRepository diseaseRepository;

    @Autowired
    public FillDatabase(DiseaseRepository diseaseRepository) {
        this.diseaseRepository = diseaseRepository;
    }

    @PostConstruct
    public void fill() {
        diseaseRepository.save(disease1());
        diseaseRepository.save(disease2());
        diseaseRepository.save(disease3());
    }

    private Disease disease3() {
        return Disease.builder()
                .name("rak")
                .description("ojojoj")
                .howToHelp("")
                .build();
    }

    private Disease disease1() {
        return Disease.builder()
                .name("padaczka")
                .description("Trzesace rece")
                .howToHelp("Wsadz patyk do ust")
                .build();
    }

    private Disease disease2() {
        return Disease.builder()
                .name("cukrzyca")
                .description("blada twarz")
                .howToHelp("podaj jablko")
                .build();
    }

}
