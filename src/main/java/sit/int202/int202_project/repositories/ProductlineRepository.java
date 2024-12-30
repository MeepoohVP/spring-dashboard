package sit.int202.int202_project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.int202.int202_project.entities.Productline;

public interface ProductlineRepository extends JpaRepository<Productline, String> {
}