//package shopping_cart.query;
//
//
//import org.axonframework.config.ProcessingGroup;
//import org.axonframework.eventhandling.EventHandler;
//import org.axonframework.queryhandling.QueryHandler;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Profile;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import shopping_cart.coreapi.ItemAddedEvent;
//import shopping_cart.coreapi.ItemPopularityQuery;
//import shopping_cart.coreapi.ItemQuantityAdjustedEvent;
//import shopping_cart.coreapi.RemovedItemEvent;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Optional;
//import shopping_cart.query.PopularityView;
//import shopping_cart.query.PopularityViewRepository;
//
//import java.util.Collections;
//
//@Profile("query")
//@ProcessingGroup("shoppingCartProcessor")
//@Component
//class PopularityProjector {
//
//    boolean isConsistent = true;
//
//    private static final Logger logger = LoggerFactory.getLogger(PopularityProjector.class);
//
//    private final PopularityViewRepository popularityViewRepository;
//    private static int eventsNumber = 0;
//    private static Map<String, Integer> net_changes = new HashMap<>();
//
//    public PopularityProjector(PopularityViewRepository popularityViewRepository) {
//        this.popularityViewRepository = popularityViewRepository;
//        logger.info("[PopularityProjection] has been started");
//    }
//
//
//
//    private static void netChange(String productId, int change, PopularityViewRepository popularityViewRepository) {
//        if(net_changes.containsKey(productId)) {
//            int temp = net_changes.get(productId);
//            net_changes.put(productId, temp + change);
//        }
//        else {
//            net_changes.put(productId, change);
//        }
//        eventsNumber ++;
//        logger.info("[Projection] net change for item {} is {}", productId, change);
//        if(eventsNumber >= 100) {
//            SaveProjection(popularityViewRepository);
//            eventsNumber = 0;
//        }
//
//    }
//
//    private static void SaveProjection (PopularityViewRepository popularityViewRepository) {
//            for (Map.Entry<String, Integer> entry: net_changes.entrySet()) {
//                Optional<PopularityView>  optionalPopularityView = popularityViewRepository.findById(entry.getKey());
//                PopularityView popularityView;
//
//                if(optionalPopularityView.isPresent()) {
//                    popularityView = optionalPopularityView.get();
//                    int temp = popularityView.getPopularity();
//                    popularityView = new PopularityView(popularityView.getItemId(), popularityView.getPopularity() + entry.getValue());
//                    logger.info("[PopularityProjection-AddItem] Item {} Popularity updated from {} to {}", entry.getKey(), temp, popularityView.getPopularity());
//                }
//                else {
//                    popularityView = new PopularityView(entry.getKey(), entry.getValue());
//                    logger.info("[PopularityProjection-AddItem] Item {} Popularity updated from {} to {}", entry.getKey(), 0, popularityView.getPopularity());
//                }
//                popularityViewRepository.save(popularityView);
//            }
//
//
//    }
//
//    @ExceptionHandler
//    public void on(Exception ex) {
//        this.isConsistent = false;
//    }
//
//    @EventHandler
//    public void on(ItemAddedEvent event) {
////        Optional<PopularityView>  optionalPopularityView = popularityViewRepository.findById(event.getProductId());
////        PopularityView popularityView;
////
////        if(optionalPopularityView.isPresent()) {
////            popularityView = optionalPopularityView.get();
////            int temp = popularityView.getPopularity();
////            popularityView = new PopularityView(popularityView.getItemId(), popularityView.getPopularity() + event.getQuantity());
////            logger.info("[PopularityProjection-AddItem] Item {} Popularity updated from {} to {}, isconsistent {}", event.getProductId(), temp, popularityView.getPopularity(), isConsistent);
////        }
////        else {
////            popularityView = new PopularityView(event.getProductId(), event.getQuantity());
////            logger.info("[PopularityProjection-AddItem] Item {} Popularity updated from {} to {}, isconsistent {}", event.getProductId(), 0, popularityView.getPopularity(),isConsistent);
////        }
////        if(event.getQuantity() == 53) {
////            int i = 100/0;
////        }
////        popularityViewRepository.save(popularityView);
//
//
//        netChange(event.getProductId(), event.getQuantity(), popularityViewRepository);
////        if(net_changes.containsKey(event.getProductId())) {
////            int temp = net_changes.get(event.getProductId());
////            net_changes.put(event.getProductId(), temp + event.getQuantity());
////        }
////        else {
////            net_changes.put(event.getProductId(), event.getQuantity());
////        }
////        eventsNumber ++;
////        logger.info("[Projection] net change for item {} is {}", event.getProductId(), event.getQuantity());
////        if(eventsNumber >= 5) {
//////            SaveProjection(popularityViewRepository);
////            logger.info("-----------==============------------===========");
////            eventsNumber = 0;
////
////        }
//    }
//
//
//
//
//
//    @EventHandler
//    public void on(ItemQuantityAdjustedEvent event) {
////        Optional<PopularityView>  optionalPopularityView = popularityViewRepository.findById(event.getProductId());
////        PopularityView popularityView;
////
////        if(optionalPopularityView.isPresent()) {
////            popularityView = optionalPopularityView.get();
////            int temp = popularityView.getPopularity();
////            popularityView = new PopularityView(popularityView.getItemId(), popularityView.getPopularity() + event.getNewQuantity() - event.getOldQuantity());
////            logger.info("[PopularityProjection-ItemQunatityAdjusted] Item {} Popularity updated from {} to {}, isConsistent {}", event.getProductId(), temp, popularityView.getPopularity(), isConsistent);
////            if(event.getNewQuantity() == 53 && isConsistent == true) {
////                int i = 100/0;
////            }
////            popularityViewRepository.save(popularityView);
////
////        }
////        else {
////            // Raise error
////        }
//
//        netChange(event.getProductId(), event.getNewQuantity() - event.getOldQuantity(), popularityViewRepository);
//
//    }
//
//    @EventHandler
//    public void on(RemovedItemEvent event) {
////        Optional<PopularityView>  optionalPopularityView = popularityViewRepository.findById(event.getProductId());
////        PopularityView popularityView;
////
////        if(optionalPopularityView.isPresent()) {
////            popularityView = optionalPopularityView.get();
////            int temp = popularityView.getPopularity();
////            popularityView = new PopularityView(popularityView.getItemId(), popularityView.getPopularity() - event.getOldQuantity());
////            popularityViewRepository.save(popularityView);
////            logger.info("[PopularityProjection-ItemRemoved] Item {} Popularity updated from {} to {}", event.getProductId(), temp, popularityView.getPopularity());
////        }
////        else {
////            // Raise error
////        }
//
//        netChange(event.getProductId(), -1 * event.getOldQuantity(), popularityViewRepository);
////        if(net_changes.containsKey(event.getProductId())) {
////            int temp = net_changes.get(event.getProductId());
////            net_changes.put(event.getProductId(), temp  - event.getOldQuantity());
////        }
////        else {
////            net_changes.put(event.getProductId(),  -1 *  event.getOldQuantity());
////        }
////        eventsNumber ++;
////        logger.info("[Projection] net change for item {} is {}", event.getProductId(), -1 * event.getOldQuantity());
////        if(eventsNumber >= 5) {
//////            SaveProjection(popularityViewRepository);
////            logger.info("-----------==============------------===========");
////            eventsNumber = 0;
////
////        }
//    }
//
//
//    @QueryHandler
//    public PopularityView handle(ItemPopularityQuery query) {
//        return popularityViewRepository.findById(query.getItemId()).orElse(new PopularityView("lol", 1));
//    }
//}



package shopping_cart.query;


import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import shopping_cart.coreapi.ItemAddedEvent;
import shopping_cart.coreapi.ItemPopularityQuery;
import shopping_cart.coreapi.ItemQuantityAdjustedEvent;
import shopping_cart.coreapi.RemovedItemEvent;
import java.util.Optional;
import shopping_cart.query.PopularityView;
import shopping_cart.query.PopularityViewRepository;

import java.util.Collections;

@Profile("query")
@ProcessingGroup("shoppingCartProcessor")
@Component
class PopularityProjector {

    boolean isConsistent = true;

    private static final Logger logger = LoggerFactory.getLogger(PopularityProjector.class);

    private final PopularityViewRepository popularityViewRepository;

    public PopularityProjector(PopularityViewRepository popularityViewRepository) {
        this.popularityViewRepository = popularityViewRepository;
        logger.info("[PopularityProjection] has been started");
    }

    @ExceptionHandler
    public void on(Exception ex) {
        this.isConsistent = false;
    }

    @EventHandler
    public void on(ItemAddedEvent event) {
        Optional<PopularityView>  optionalPopularityView = popularityViewRepository.findById(event.getProductId());
        PopularityView popularityView;

        if(optionalPopularityView.isPresent()) {
            popularityView = optionalPopularityView.get();
            int temp = popularityView.getPopularity();
            popularityView = new PopularityView(popularityView.getItemId(), popularityView.getPopularity() + event.getQuantity());
            logger.info("[PopularityProjection-AddItem] Item {} Popularity updated from {} to {}, isconsistent {}", event.getProductId(), temp, popularityView.getPopularity(), isConsistent);
        }
        else {
            popularityView = new PopularityView(event.getProductId(), event.getQuantity());
            logger.info("[PopularityProjection-AddItem] Item {} Popularity updated from {} to {}, isconsistent {}", event.getProductId(), 0, popularityView.getPopularity(),isConsistent);
        }
        if(event.getQuantity() == 53) {
            int i = 100/0;
        }
        popularityViewRepository.save(popularityView);
    }



    @EventHandler
    public void on(ItemQuantityAdjustedEvent event) {
        Optional<PopularityView>  optionalPopularityView = popularityViewRepository.findById(event.getProductId());
        PopularityView popularityView;

        if(optionalPopularityView.isPresent()) {
            popularityView = optionalPopularityView.get();
            int temp = popularityView.getPopularity();
            popularityView = new PopularityView(popularityView.getItemId(), popularityView.getPopularity() + event.getNewQuantity() - event.getOldQuantity());
            logger.info("[PopularityProjection-ItemQunatityAdjusted] Item {} Popularity updated from {} to {}, isConsistent {}", event.getProductId(), temp, popularityView.getPopularity(), isConsistent);
            if(event.getNewQuantity() == 53 && isConsistent == true) {
                int i = 100/0;
            }
            popularityViewRepository.save(popularityView);

        }
        else {
            // Raise error
        }

    }

    @EventHandler
    public void on(RemovedItemEvent event) {
        Optional<PopularityView>  optionalPopularityView = popularityViewRepository.findById(event.getProductId());
        PopularityView popularityView;

        if(optionalPopularityView.isPresent()) {
            popularityView = optionalPopularityView.get();
            int temp = popularityView.getPopularity();
            popularityView = new PopularityView(popularityView.getItemId(), popularityView.getPopularity() - event.getOldQuantity());
            popularityViewRepository.save(popularityView);
            logger.info("[PopularityProjection-ItemRemoved] Item {} Popularity updated from {} to {}", event.getProductId(), temp, popularityView.getPopularity());
        }
        else {
            // Raise error
        }
    }


    @QueryHandler
    public PopularityView handle(ItemPopularityQuery query) {
        return popularityViewRepository.findById(query.getItemId()).orElse(new PopularityView("lol", 1));
    }
}
