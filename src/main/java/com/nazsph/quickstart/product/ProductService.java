package com.nazsph.quickstart.product;

import com.nazsph.quickstart.company.Company;
import com.nazsph.quickstart.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    @Qualifier("productsMongoTemplate")
    private MongoTemplate productsMongoTemplate; // productsMongoTemplate bean'i burada enjekte edilir.

    @Autowired
    CompanyService companyService;

    public List<Product> getAllProducts() {
        return productsMongoTemplate.findAll(Product.class);
    }

    public Optional<Product> findProductById(String id) {
        return Optional.ofNullable(productsMongoTemplate.findById(id, Product.class));
    }

    public Product addProduct(Product product) {
        return productsMongoTemplate.save(product);
    }

    public void deleteProduct(String id) {
        productsMongoTemplate.remove(productsMongoTemplate.findById(id, Product.class));
    }

    public Optional<Product> findProductByProductId(String productId) {
        return Optional.ofNullable(productsMongoTemplate.findOne(Query.query(Criteria.where("productId").is(productId)), Product.class));
    }

    public Company getCompanyByProductId(String productId) {
        Optional<Product> productOptional = findProductByProductId(productId);
        if (productOptional.isPresent()) {
            Product product = productOptional.get();
            String companyId = product.getCompanyId();

            // Company'i companyId ile bulma işlemi
            Optional<Company> companyOptional = companyService.findCompanyByCompanyId(companyId);
            if (companyOptional.isPresent()) {
                return companyOptional.get();
            } else {
                throw new IllegalArgumentException("Company not found for companyId: " + companyId);
            }
        } else {
            throw new IllegalArgumentException("Product not found for productId: " + productId);
        }
    }

    public List<Product> findProductsBySearchText(String searchText) {
        // Query objesi oluşturma
        Query query = new Query();

        // Tek bir kriterle tüm alanlarda filtreleme yapma
        Criteria criteria = new Criteria().orOperator(
                Criteria.where("productId").regex(searchText, "i"), // i harfi regex'in case insensitive olmasını sağlar.
                Criteria.where("productName").regex(searchText, "i"),
                Criteria.where("companyId").regex(searchText, "i"),
                Criteria.where("name").regex(searchText, "i")

                // İhtiyaç duyulan diğer alanlar burada eklenebilir.
        );
        query.addCriteria(criteria);

        // Sorguyu MongoDB'de çalıştırma ve sonuçları alın
        return productsMongoTemplate.find(query, Product.class);
    }

}
