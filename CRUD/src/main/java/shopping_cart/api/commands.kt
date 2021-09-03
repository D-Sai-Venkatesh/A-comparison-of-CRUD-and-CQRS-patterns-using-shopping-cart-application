package shopping_cart.api


import java.util.*

class CreateShoppingCartMsg(
        val shoppingCartId: UUID
)

data class AddItemMsg(
        val shoppingCartId: UUID,
        val productId: String,
        val quantity: Int
)

data class AdjustItemQuantityMsg(
        val shoppingCartId: UUID,
        val productId: String,
        val newQuantity: Int
)


data class RemoveItemMsg(
        val shoppingCartId: UUID,
        val productId: String
)

data class CheckoutMsg(
        val shoppingCartId: UUID
)

data class GetCartMsg(
        val shoppingCartId: UUID
)

data class GetPopularityMsg(
        val productId: String
)
