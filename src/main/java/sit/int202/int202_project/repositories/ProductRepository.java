package sit.int202.int202_project.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sit.int202.int202_project.entities.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    @Query("""
        select p from Product p where p.buyPrice between ?1 and ?2
        order by p.buyPrice desc 
""")
    List<Product> findByBuyPrice(BigDecimal lower, BigDecimal upper);
    @Query("""
        SELECT p FROM Product p
            JOIN Productline pl on p.productLine.productLine = pl.productLine
            WHERE pl.productLine = ?1
    """)
    Page<Product> findByProductLineWithDetails(String productLine, Pageable pageable);

}