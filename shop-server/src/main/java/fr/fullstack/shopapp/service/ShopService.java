package fr.fullstack.shopapp.service;

import fr.fullstack.shopapp.model.Product;
import fr.fullstack.shopapp.model.Shop;
import fr.fullstack.shopapp.repository.ShopElasticRepository;
import fr.fullstack.shopapp.repository.ShopRepository;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ShopService {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private ShopElasticRepository shopElasticRepository;

    @Transactional
    public Shop createShop(Shop shop) throws Exception {
        try {
            Shop newShop = shopRepository.save(shop);
            // Refresh the entity after the save. Otherwise, @Formula does not work.
            em.flush();
            em.refresh(newShop);
            return newShop;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    @Transactional
    public void deleteShopById(long id) throws Exception {
        try {
            Shop shop = getShop(id);
            // delete nested relations with products
            deleteNestedRelations(shop);
            shopRepository.deleteById(id);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Shop getShopById(long id) throws Exception {
        try {
            return getShop(id);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Page<Shop> getShopList(
            Optional<String> sortBy,
            Optional<Boolean> inVacations,
            Optional<String> createdBefore,
            Optional<String> createdAfter,
            Pageable pageable
    ) {
        // SORT
        if (sortBy.isPresent()) {
            switch (sortBy.get()) {
                case "name":
                    return shopRepository.findByOrderByNameAsc(pageable);
                case "createdAt":
                    return shopRepository.findByOrderByCreatedAtAsc(pageable);
                default:
                    return shopRepository.findByOrderByNbProductsAsc(pageable);
            }
        }

        // FILTERS
        Page<Shop> shopList = getShopListWithFilter(inVacations, createdBefore, createdAfter, pageable);
        if (shopList != null) {
            return shopList;
        }

        // NONE
        return shopRepository.findByOrderByIdAsc(pageable);
    }

    @Transactional
    public Shop updateShop(Shop shop) throws Exception {
        try {
            getShop(shop.getId());
            return this.createShop(shop);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void deleteNestedRelations(Shop shop) {
        List<Product> products = shop.getProducts();
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            product.setShop(null);
            em.merge(product);
            em.flush();
        }
    }

    private Shop getShop(Long id) throws Exception {
        Optional<Shop> shop = shopRepository.findById(id);
        if (!shop.isPresent()) {
            throw new Exception("Shop with id " + id + " not found");
        }
        return shop.get();
    }

    private Page<Shop> getShopListWithFilter(
            Optional<Boolean> inVacations,
            Optional<String> createdAfter,
            Optional<String> createdBefore,
            Pageable pageable
    ) {
        if (inVacations.isPresent() && createdBefore.isPresent() && createdAfter.isPresent()) {
            return shopRepository.findByInVacationsAndCreatedAtGreaterThanAndCreatedAtLessThan(
                    inVacations.get(),
                    LocalDate.parse(createdAfter.get()),
                    LocalDate.parse(createdBefore.get()),
                    pageable
            );
        }

        if (inVacations.isPresent() && createdBefore.isPresent()) {
            return shopRepository.findByInVacationsAndCreatedAtLessThan(
                    inVacations.get(), LocalDate.parse(createdBefore.get()), pageable
            );
        }

        if (inVacations.isPresent() && createdAfter.isPresent()) {
            return shopRepository.findByInVacationsAndCreatedAtGreaterThan(
                    inVacations.get(), LocalDate.parse(createdAfter.get()), pageable
            );
        }

        if (inVacations.isPresent()) {
            return shopRepository.findByInVacations(inVacations.get(), pageable);
        }

        if (createdBefore.isPresent() && createdAfter.isPresent()) {
            return shopRepository.findByCreatedAtBetween(
                    LocalDate.parse(createdAfter.get()), LocalDate.parse(createdBefore.get()), pageable
            );
        }

        if (createdBefore.isPresent()) {
            return shopRepository.findByCreatedAtLessThan(
                    LocalDate.parse(createdBefore.get()), pageable
            );
        }

        if (createdAfter.isPresent()) {
            return shopRepository.findByCreatedAtGreaterThan(
                    LocalDate.parse(createdAfter.get()), pageable
            );
        }

        return null;
    }

    // Search with Elasticsearch

    public Page<Shop> searchShopByName(String name, Pageable pageable) {
        return shopElasticRepository.findByName(name, pageable);
    }
//    public Page<Shop> searchShopByName(String name, Pageable pageable) {
//        var searchResults = Search.session(em)
//            .search(Shop.class)
//            .where(f -> f.match()
//                .fields("name")
//                .matching(name)
//            ).fetchAll();
//        return searchResultsToPage(searchResults, pageable);
//    }
//
//    private Page<Shop> searchResultsToPage(SearchResult<Shop> searchResult, Pageable pageable) {
//        List<Shop> shops = searchResult.hits();
//        long totalHits = searchResult.total().hitCount();
//        return PageableExecutionUtils.getPage(shops, pageable, () -> totalHits);
//    }
//
//    @Transactional
//    public void initElasticsearch() throws InterruptedException {
//        System.out.println("Init Elasticsearch");
//        var searchSession = Search.session(em);
//        var indexer = searchSession.massIndexer(Shop.class);
//        indexer.startAndWait();
//        System.out.println("End init Elasticsearch");
//    }
}
