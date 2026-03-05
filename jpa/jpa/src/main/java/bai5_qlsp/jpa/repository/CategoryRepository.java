package bai5_qlsp.jpa.repository;

import bai5_qlsp.jpa.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
