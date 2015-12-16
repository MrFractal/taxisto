
/**
 * Created by max on 06.02.14.
 */

@interface AssignedDriverLocationSubscription : Subscription {
  long driverId;
}

@property(nonatomic) long driverId;
- (void) setDriverId:(long)driverId;
@end
