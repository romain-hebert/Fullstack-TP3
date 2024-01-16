//package fr.fullstack.shopapp.config;
//
//import jakarta.persistence.EntityManager;
//import org.hibernate.search.mapper.orm.Search;
//import org.hibernate.search.mapper.orm.session.SearchSession;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import jakarta.persistence.PersistenceContext;
//
//@Configuration
//public class HibernateSearchConfig {
//
//    @PersistenceContext
//    private EntityManager em;
//
//    @Bean
//    public SearchSession fullTextEntityManager() {
//        return Search.session(em);
//    }
//}
