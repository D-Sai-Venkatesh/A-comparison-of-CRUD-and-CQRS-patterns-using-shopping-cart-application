package shopping_cart.query;

import lombok.val;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ShoppingCartViewRepository extends JpaRepository<ShoppingCartView, UUID> {

  @Query(value = "select SUM(products) from shopping_cart_view_products where products_key= ?1", nativeQuery = true)
  public Long getPopularity(String product_id);

}
