package fr.fullstack.shopapp.config;

import jakarta.persistence.EntityManager;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.PersistenceContext;

@Configuration
public class HibernateSearchConfig {

    @PersistenceContext
    private EntityManager em;

    @Bean
    public FullTextEntityManager fullTextEntityManager() {
        return Search.getFullTextEntityManager((javax.persistence.EntityManager) em);
    }
}
