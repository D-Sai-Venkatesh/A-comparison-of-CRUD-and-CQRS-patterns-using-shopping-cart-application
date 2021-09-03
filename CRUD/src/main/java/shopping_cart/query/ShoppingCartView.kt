package shopping_cart.query

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ElementCollection
import javax.persistence.FetchType

@Entity
data class ShoppingCartView (
  @Id val shoppingCartId: UUID,
  @ElementCollection(fetch = FetchType.EAGER) val products: MutableMap<String, Int>
  ) {

  fun addProducts(productId: String, amount: Int) =
    products.compute(productId) {_, quantity -> (quantity ?: 0) + amount};

  fun adjustProducts(productId: String, oldQuantity: Int, newQuantity: Int) =
    products.compute(productId) {_, quantity -> (quantity ?: 0) + newQuantity - oldQuantity};

  fun removeProducts(productId: String) {
    products.remove(productId)
  }


}


