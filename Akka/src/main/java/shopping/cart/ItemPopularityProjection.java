
package shopping.cart;

import akka.actor.typed.ActorSystem;
import akka.cluster.sharding.typed.ShardedDaemonProcessSettings;
import akka.cluster.sharding.typed.javadsl.ShardedDaemonProcess;
import akka.persistence.jdbc.query.javadsl.JdbcReadJournal;
import akka.persistence.query.Offset;
import akka.projection.ProjectionBehavior;
import akka.projection.ProjectionId;
import akka.projection.Projection;
import akka.projection.eventsourced.EventEnvelope;
import akka.projection.eventsourced.javadsl.EventSourcedProvider;
import akka.projection.javadsl.ExactlyOnceProjection;
import akka.projection.javadsl.SourceProvider;
import akka.projection.jdbc.javadsl.JdbcProjection;

import java.time.Duration;
import java.util.Optional;
import org.springframework.orm.jpa.JpaTransactionManager;
import shopping.cart.repository.HibernateJdbcSession;
import shopping.cart.repository.ItemPopularityRepository;

//int saveOffsetAfterEnvelopes = 100;
//Duration saveOffsetAfterDuration = Duration.ofMillis(500);

public final class ItemPopularityProjection {

  private ItemPopularityProjection() {}

  
  public static void init(
      ActorSystem<?> system,
      JpaTransactionManager transactionManager,
      ItemPopularityRepository repository) {

    ShardedDaemonProcess.get(system)
        .init( 
            ProjectionBehavior.Command.class,
            "ItemPopularityProjection",
            ShoppingCart.TAGS.size(),
            index ->
                ProjectionBehavior.create(
                    createProjectionFor(system, transactionManager, repository, index)),
            ShardedDaemonProcessSettings.create(system),
            Optional.of(ProjectionBehavior.stopMessage()));
  }
  

  private static ExactlyOnceProjection<Offset, EventEnvelope<ShoppingCart.Event>>
      createProjectionFor(
          ActorSystem<?> system,
          JpaTransactionManager transactionManager,
          ItemPopularityRepository repository,
          int index) {

    String tag = ShoppingCart.TAGS.get(index); 

    SourceProvider<Offset, EventEnvelope<ShoppingCart.Event>> sourceProvider = 
        EventSourcedProvider.eventsByTag(
            system,
            JdbcReadJournal.Identifier(),
            tag);

    return JdbcProjection.exactlyOnce( 
        ProjectionId.of("ItemPopularityProjection", tag),
        sourceProvider,
        () -> new HibernateJdbcSession(transactionManager), 
        () -> new ItemPopularityProjectionHandler(tag, repository), 
        system);
  }
}

