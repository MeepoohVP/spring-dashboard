package sit.int202.int202_project.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sit.int202.int202_project.entities.Product;
import sit.int202.int202_project.repositories.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repository;
    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }
    public Product getProduct(String productCode) {
        return repository.findById(productCode).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Product not found"));
    }
    public Page<Product> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }
    public Page<Product> findByProductLine(String productline, Pageable pageable) {
        return repository.findByProductLineWithDetails(productline, pageable);
    }
    public Product addProduct(Product product) {
        if (repository.existsById(product.getProductCode())){
            return null;
        }
        return repository.save(product);
    }
    public Product updateProduct(Product product) {
        if (product.getProductCode()==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Can't update, Product code '%s' does not exists",
                            product.getProductCode()));
        }
        return repository.save(product);
    }
    public Product deleteProduct(String productCode) {
        Product product = getProduct(productCode);
        if(product==null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Can't delete, Product code '%s' does not exists",
                            productCode));
        }
        repository.delete(product);
        return product;
    }
}