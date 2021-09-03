package shopping_cart.service;

import shopping_cart.api.*;
import shopping_cart.query.PopularityView;
import shopping_cart.query.ShoppingCartView;

import java.util.UUID;

public interface ShoppingCartInterface {

  public abstract UUID CreateShoppingCart(CreateShoppingCartMsg msg);
  public abstract void AddItem(AddItemMsg msg) throws ItemAlreadyAddedException, CartNotFoundException;
  public abstract void AdjustItemQuantity(AdjustItemQuantityMsg msg) throws ItemNotFoundException, CartNotFoundException;
  public abstract void RemoveItem(RemoveItemMsg msg) throws ItemNotFoundException, CartNotFoundException;
  public abstract void Checkout(CheckoutMsg msg);
  public abstract ShoppingCartView GetCart(GetCartMsg msg) throws CartNotFoundException;
  public abstract PopularityDTO GetPopularity(GetPopularityMsg msg) throws ItemNotFoundException;

}
