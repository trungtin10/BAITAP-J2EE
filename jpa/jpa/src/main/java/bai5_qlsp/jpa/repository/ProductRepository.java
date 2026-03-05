package bai5_qlsp.jpa.repository;

import bai5_qlsp.jpa.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
