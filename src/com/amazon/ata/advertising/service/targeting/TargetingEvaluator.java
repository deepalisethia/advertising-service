package com.amazon.ata.advertising.service.targeting;

import com.amazon.ata.advertising.service.model.RequestContext;
import com.amazon.ata.advertising.service.targeting.predicate.TargetingPredicate;
import com.amazon.ata.advertising.service.targeting.predicate.TargetingPredicateResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Evaluates TargetingPredicates for a given RequestContext.
 */
public class TargetingEvaluator {
    public static final boolean IMPLEMENTED_STREAMS = true;
    public static final boolean IMPLEMENTED_CONCURRENCY = true;
    private final RequestContext requestContext;

    /**
     * Creates an evaluator for targeting predicates.
     * @param requestContext Context that can be used to evaluate the predicates.
     */
    public TargetingEvaluator(RequestContext requestContext) {
        this.requestContext = requestContext;
    }

    /**
     * Evaluate a TargetingGroup to determine if all of its TargetingPredicates are TRUE or not for the given
     * RequestContext.
     * @param targetingGroup Targeting group for an advertisement, including TargetingPredicates.
     * @return TRUE if all of the TargetingPredicates evaluate to TRUE against the RequestContext, FALSE otherwise.
     */
    public TargetingPredicateResult evaluate(TargetingGroup targetingGroup) {

//        boolean allTruePredicates = true;
//        List<TargetingPredicate> targetingPredicates = targetingGroup.getTargetingPredicates();

        // Create an Executor Service
        ExecutorService executorService = Executors.newCachedThreadPool();

        // Create a Futures List
//        List<Future<TargetingPredicate>> futureList = new ArrayList<>();
//
//        for (TargetingPredicate predicate : targetingPredicates) {
//            // What is the task to submit?? Which class implements Callable or do we have to change one?
//            TargetingPredicateResult predicateResult = predicate.evaluate(requestContext);
//            if (predicateResult.isTrue()) {
//                futureList.add()
//            }
//        }


        // TRIAL TO GET TIMEOUT EXCEPTION TO WORK
//        List<Future<Boolean>> truePredicateResults = new ArrayList<>();
//        for (Future<Boolean> result : truePredicateResults) {
//            try {
//                if (!result.get()) {
//                    allTruePredicates = false;
//                }
//            } catch (TimeoutException e) {
//                allTruePredicates = false;
//            }
//        }


//        for (TargetingPredicate predicate : targetingPredicates) {
//            TargetingPredicateResult predicateResult = predicate.evaluate(requestContext);
//            if (!predicateResult.isTrue()) {
//                allTruePredicates = false;
//                break;
//            }
//        }

//        for (Future<TargetingPredicate> targetingPredicateFuture : futureList) {
//            try {
//                targetingPredicates.add(targetingPredicateFuture.get());
//            } catch (ExecutionException | InterruptedException e) {
//                e.printStackTrace();
//            }
            // catch (TimeoutException e) {
//                System.out.println();
//                allTruePredicates = false;


//        return allTruePredicates ? TargetingPredicateResult.TRUE :
//                                   TargetingPredicateResult.FALSE;


        try {
            return executorService.submit(() -> targetingGroup
                    .getTargetingPredicates()
                    .parallelStream()
                    .allMatch(predicate -> predicate.evaluate(requestContext).isTrue())
                    ? TargetingPredicateResult.TRUE :
                    TargetingPredicateResult.FALSE).get();
            } catch (ExecutionException | InterruptedException | CancellationException e) {
                        e.printStackTrace();
            }
        executorService.shutdown();
        return TargetingPredicateResult.FALSE;
//        return targetingGroup
//                .getTargetingPredicates()
//                .stream()
//                .allMatch(predicate -> predicate.evaluate(requestContext).isTrue())
//                ? TargetingPredicateResult.TRUE :
//                TargetingPredicateResult.FALSE;


    }
}
