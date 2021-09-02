
package shopping.cart;

import akka.projection.eventsourced.EventEnvelope;
import akka.projection.jdbc.javadsl.JdbcHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import shopping.cart.repository.HibernateJdbcSession;
import shopping.cart.repository.ItemPopularityRepository;

public final class ItemPopularityProjectionHandler
    extends JdbcHandler<EventEnvelope<ShoppingCart.Event>, HibernateJdbcSession> { 
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final String tag;
  private final ItemPopularityRepository repo;
  static int flag = 0;

  public ItemPopularityProjectionHandler(String tag, ItemPopularityRepository repo) {
    this.tag = tag;
    this.repo = repo;
    logger.info("projection Started  =============================");
  }

  private ItemPopularity findOrNew(String itemId) {
    return repo.findById(itemId).orElseGet(() -> new ItemPopularity(itemId, 0, 0));
  }

  @Override
  public void process(
      HibernateJdbcSession session, EventEnvelope<ShoppingCart.Event> envelope) { 
    ShoppingCart.Event event = envelope.event();

    if (event instanceof ShoppingCart.ItemAdded) { 
      ShoppingCart.ItemAdded added = (ShoppingCart.ItemAdded) event;
      String itemId = added.itemId;

      ItemPopularity existingItemPop = findOrNew(itemId);
      ItemPopularity updatedItemPop = existingItemPop.changeCount(added.quantity);
      repo.save(updatedItemPop);

      logger.info(
          "ItemPopularityProjectionHandler({}) item popularity for '{}': [{}]",
          this.tag,
          itemId,
          updatedItemPop.getCount());
      
    } else if (event instanceof ShoppingCart.ItemQuantityAdjusted) {
      logger.info("Item quantity adjusted projection =============================");
      ShoppingCart.ItemQuantityAdjusted adjusted = (ShoppingCart.ItemQuantityAdjusted) event;
      String itemId = adjusted.itemId;

      ItemPopularity existingItemPop = findOrNew(itemId);
      ItemPopularity updatedItemPop =
          existingItemPop.changeCount(adjusted.newQuantity - adjusted.oldQuantity);
      repo.save(updatedItemPop);

    } else if (event instanceof ShoppingCart.ItemRemoved) {
      logger.info("projection item removed ===================================");
      ShoppingCart.ItemRemoved removed = (ShoppingCart.ItemRemoved) event;
      String itemId = removed.itemId;

      ItemPopularity existingItemPop = findOrNew(itemId);
      ItemPopularity updatedItemPop = existingItemPop.changeCount(-removed.oldQuantity);
      if(this.flag == 0) {
        logger.info("projection failed ======================================");
        int i = 100/0;
        ItemPopularityProjectionHandler.flag = 1;
      }

      repo.save(updatedItemPop);
      
    } else {
      // skip all other events, such as `CheckedOut`
    }
  }
}

