package shopping_cart.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShoppingCartDTO {
  private UUID shoppingCartId;
  private Map<String, Integer> selectedProducts;
  private boolean confirmed;
}
