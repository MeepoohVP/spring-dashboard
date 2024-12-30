package sit.int202.int202_project.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int202.int202_project.entities.Productline;
import sit.int202.int202_project.repositories.ProductlineRepository;

import java.util.List;

@Service
public class ProductlineService {
    private final ProductlineRepository productlineRepository;

    public ProductlineService(ProductlineRepository productlineRepository) {
        this.productlineRepository = productlineRepository;
    }
    public List<Productline> getAllProductline() {
        return productlineRepository.findAll();
    }
    public Productline getProductline(String productLine) {
        return productlineRepository.findById(productLine).orElseThrow(
                () ->new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "ProductLine not found")
        );
    }
}
