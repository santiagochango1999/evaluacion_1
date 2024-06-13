package com.distribuida.config;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@ApplicationScoped
public class JpaConfig {
    EntityManagerFactory ema;

    @PostConstruct
    public void init(){
        System.out.println("*** JPA CONFIG::init");
        ema= Persistence.createEntityManagerFactory("pu-distribuida");

    }

    @Produces
    public EntityManager em(){
        System.out.println("*** JPA CONFIG::em");
        return ema.createEntityManager();
    }
}
