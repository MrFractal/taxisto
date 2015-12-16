#import "ItemLocation.h"

/**
 * Created by max on 06.02.14.
 */

@interface DriversAroundSubscription : Subscription {
  ItemLocation * currentLocation;
}

@property(nonatomic, retain) ItemLocation * currentLocation;
- (void) setCurrentLocation:(ItemLocation *)currentLocation;
@end
