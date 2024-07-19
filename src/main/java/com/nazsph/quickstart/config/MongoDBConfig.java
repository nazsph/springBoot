package com.nazsph.quickstart.config;

import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoDBConfig {

    @Value("${spring.data.mongodb.products.uri}")
    private String productsUri;

    @Value("${spring.data.mongodb.companies.uri}")
    private String companiesUri;

    @Bean(name = "productsMongoTemplate")
    public MongoTemplate productsMongoTemplate() {
        return new MongoTemplate(MongoClients.create(productsUri), "products");
    }

    @Bean(name = "companiesMongoTemplate")
    public MongoTemplate companiesMongoTemplate() {
        return new MongoTemplate(MongoClients.create(companiesUri), "companies");
    }

     @Bean(name = "mongoTemplate") // Bu şekilde global bir mongoTemplate bean'i de tanımlayabilirsiniz.
     public MongoTemplate mongoTemplate() {
         return new MongoTemplate(MongoClients.create(), "yourDatabaseName");
     }
}
