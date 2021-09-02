package shopping_cart.query

import org.springframework.context.annotation.Profile
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id

@Entity
data class PopularityView(
        @Id val itemId: String,
        val popularity: Int
) {



    fun adjustPopularity(change: Int) = popularity + change;

}

@Profile("query")
interface PopularityViewRepository : JpaRepository<PopularityView, String>