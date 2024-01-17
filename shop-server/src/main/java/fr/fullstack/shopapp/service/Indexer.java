package fr.fullstack.shopapp.service;

import fr.fullstack.shopapp.model.Shop;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.massindexing.MassIndexer;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Component;

import java.net.ConnectException;

@Transactional
@Component
public class Indexer {

    private final EntityManager em;

    public Indexer(EntityManager em) {
        this.em = em;
    }

    public void index(String className) {
        System.out.println("Indexing " + className + " entities...");
        try {
            Search.session(em).massIndexer(Class.forName(className))
                    .startAndWait();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Thread interrupted while waiting for indexing: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found while indexing: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error indexing entities: " + e.getMessage());
        }
    }

}
