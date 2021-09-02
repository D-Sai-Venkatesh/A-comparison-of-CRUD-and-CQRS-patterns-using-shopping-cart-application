package shopping_cart.coreapi

import java.util.*

class ShoppingCartCreatedEvent(
        val shoppingCartId: UUID
)

data class ItemAddedEvent(
        val shoppingCartId: UUID,
        val productId: String,
        val quantity: Int
)

data class ItemQuantityAdjustedEvent(
        val shoppingCartId: UUID,
        val productId: String,
        val oldQuantity: Int,
        val newQuantity: Int
)

data class RemovedItemEvent(
        val shoppingCartId: UUID,
        val productId: String,
        val oldQuantity: Int
)

data class CheckedoutEvent(
        val shoppingCartId: UUID
)
