package com.nazsph.quickstart.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {

    @Autowired
    @Qualifier("companiesMongoTemplate")
    private MongoTemplate companiesMongoTemplate; // İlgili MongoTemplate bean'i burada enjekte edilir.

    public List<Company> getAllCompanies(){
        return companiesMongoTemplate.findAll(Company.class);
    }

    public Optional<Company> findCompanyById(String id){
        return Optional.ofNullable(companiesMongoTemplate.findById(id, Company.class));
    }

    public Company addCompany(Company company){
        return companiesMongoTemplate.save(company);
    }

    public void deleteCompany(String id){
        companiesMongoTemplate.remove(companiesMongoTemplate.findById(id, Company.class));
    }

    public Optional<Company> findCompanyByCompanyId(String companyId) {
        // companyId ile Company nesnesini bulma işlemi
        return Optional.ofNullable(companiesMongoTemplate.findOne(Query.query(Criteria.where("companyId").is(companyId)), Company.class));
    }
}
