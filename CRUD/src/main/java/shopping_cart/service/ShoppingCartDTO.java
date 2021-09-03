package shopping_cart.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDTO {
  private String shoppingCartId;
  private Map<String, Integer> selectedProducts;
  private boolean confirmed;
}
