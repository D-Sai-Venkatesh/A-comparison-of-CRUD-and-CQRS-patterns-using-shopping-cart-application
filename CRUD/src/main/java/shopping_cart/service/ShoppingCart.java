package shopping_cart.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import shopping_cart.api.*;
import shopping_cart.query.PopularityView;
import shopping_cart.query.PopularityViewRepository;
import shopping_cart.query.ShoppingCartView;
import shopping_cart.query.ShoppingCartViewRepository;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Component
public class ShoppingCart implements ShoppingCartInterface{

  private final ShoppingCartViewRepository shoppingCartViewRepository;

  private static final Logger logger = LoggerFactory.getLogger(ShoppingCart.class);

  public ShoppingCart(ShoppingCartViewRepository shoppingCartViewRepository, PopularityViewRepository popularityViewRepository) {
    logger.info("shopping cart created");
    this.shoppingCartViewRepository = shoppingCartViewRepository;
  }

  @Override
  public UUID CreateShoppingCart(CreateShoppingCartMsg msg) {
    ShoppingCartView shoppingCartView = new ShoppingCartView(msg.getShoppingCartId(), Collections.emptyMap());
    shoppingCartViewRepository.save(shoppingCartView);
    logger.info("create shopping cart {}", msg.getShoppingCartId());
    return msg.getShoppingCartId();
  }

  @Override
  public void AddItem(AddItemMsg msg) throws ItemAlreadyAddedException, CartNotFoundException{

    Optional<ShoppingCartView> optionalShoppingCartView = shoppingCartViewRepository.findById(msg.getShoppingCartId());
    ShoppingCartView shoppingCartView;

    if(optionalShoppingCartView.isPresent()) {
      shoppingCartView = optionalShoppingCartView.get();
      Map<String, Integer> products = shoppingCartView.getProducts();
      if(products.containsKey(msg.getProductId())) {
        // raise error
        throw new ItemAlreadyAddedException("Item " + msg.getProductId() + "has been already added");
      }
      else {
        products.put(msg.getProductId(), msg.getQuantity());
        shoppingCartViewRepository.save(new ShoppingCartView(shoppingCartView.getShoppingCartId(), products));
      }

    }
    else {
      throw new CartNotFoundException("Cart " + msg.getShoppingCartId().toString() + " not Found");
    }
  }

  @Override
  public void AdjustItemQuantity(AdjustItemQuantityMsg msg) throws ItemNotFoundException, CartNotFoundException{

    Optional<ShoppingCartView> optionalShoppingCartView = shoppingCartViewRepository.findById(msg.getShoppingCartId());
    ShoppingCartView shoppingCartView;
    int oldQuantity;

    if(optionalShoppingCartView.isPresent()) {
      shoppingCartView = optionalShoppingCartView.get();
      Map<String, Integer> products = shoppingCartView.getProducts();
      if(products.containsKey(msg.getProductId())) {
        oldQuantity = products.get(msg.getProductId());
        products.replace(msg.getProductId(), msg.getNewQuantity());
        shoppingCartViewRepository.save(new ShoppingCartView(shoppingCartView.getShoppingCartId(), products));

      }
      else {
        // raise error
        throw new ItemNotFoundException("Item " + msg.getProductId() + " not found");
      }

    }
    else {
      // raise exception
      throw new CartNotFoundException("Cart " + msg.getShoppingCartId().toString() + " not Found");
    }

    //=========================================================================



  }

  @Override
  public void RemoveItem(RemoveItemMsg msg) throws ItemNotFoundException, CartNotFoundException{
    Optional<ShoppingCartView> optionalShoppingCartView = shoppingCartViewRepository.findById(msg.getShoppingCartId());
    ShoppingCartView shoppingCartView;
    int oldQuantity;

    if(optionalShoppingCartView.isPresent()) {
      shoppingCartView = optionalShoppingCartView.get();
      Map<String, Integer> products = shoppingCartView.getProducts();
      if(products.containsKey(msg.getProductId())) {
        oldQuantity = products.get(msg.getProductId());
        products.remove(msg.getProductId());
        shoppingCartViewRepository.save(new ShoppingCartView(shoppingCartView.getShoppingCartId(), products));

      }
      else {
        // raise error
        throw new ItemNotFoundException("Item " + msg.getProductId() + " not found");
      }

    }
    else {
      // raise exception
      throw new CartNotFoundException("Cart " + msg.getShoppingCartId().toString() + " not Found");
    }

  }

  @Override
  public void Checkout(CheckoutMsg msg) {

  }

  @Override
  public ShoppingCartView GetCart(GetCartMsg msg) throws CartNotFoundException{
    Optional<ShoppingCartView> optionalShoppingCartView = shoppingCartViewRepository.findById(msg.getShoppingCartId());

    if(optionalShoppingCartView.isPresent()) {
      return optionalShoppingCartView.get();
    }
    else {
      throw new CartNotFoundException("Cart " + msg.getShoppingCartId().toString() + " not Found");
    }
  }

  @Override
  public PopularityDTO GetPopularity(GetPopularityMsg msg) throws ItemNotFoundException{

    return new PopularityDTO(msg.getProductId(), shoppingCartViewRepository.getPopularity(msg.getProductId()));
  }


}
