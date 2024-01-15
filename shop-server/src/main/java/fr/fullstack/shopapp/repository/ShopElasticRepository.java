package fr.fullstack.shopapp.repository;

import fr.fullstack.shopapp.model.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ShopElasticRepository extends ElasticsearchRepository<Shop, Long> {
    List<Shop> findByName(String name);
    Page<Shop> findByName(String name, Pageable pageable);
}
