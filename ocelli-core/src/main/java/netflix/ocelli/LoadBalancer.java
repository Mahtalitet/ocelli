package netflix.ocelli;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * A concrete ClientSelector keeps track of all available hosts and returns 
 * the most recent immutable collection of connected {@link C}'s.
 * 
 * @author elandau
 */
public interface LoadBalancer<H, C, M extends Action1<ClientEvent>> {
    /**
     * Return an Observable that when subscribed to will select the next C
     * in the pool.  Call select() for each use of the load balancer.  The Observable
     * will contain some context for selecting the next C.
     * 
     * @return
     */
    Observable<C> choose();

    /**
     * Return an Observable for the Client for this host
     * 
     * @param host
     * @return
     */
    Observable<C> choose(H host);
    
    /**
     * Partition the load balancer using the provided partitioner function.
     * @param partitioner
     * @return
     */
    <K> PartitionedLoadBalancer<H, C, M, K> partition(Func1<H, Observable<K>> partitioner);

    /**
     * @return Stream of all host events
     */
    Observable<HostEvent<H>> events();
    
    /**
     * @return Observable of all hosts (active or not)
     */
    Observable<H> listAllHosts();

    /**
     * @return Return all active hosts ready to serve traffic
     */
    Observable<H> listActiveHosts();
    
    /**
     * @return All clients ready to serve traffic
     */
    Observable<C> listActiveClients();
    
    /**
     * Must be called so that the load balancer will register the stream
     * of host events
     */
    void initialize();

    /**
     * Perform cleanup and unregister
     */
    void shutdown();

    ManagedClient<H, C, M> getClient(H host);
}