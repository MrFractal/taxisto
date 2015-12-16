
/**
 * Created by max on 06.02.14.
 */

@interface DriversRatingsRequest : NSObject {
  long driverId;
}

@property(nonatomic) long driverId;
- (void) init;
- (void) setDriverId:(long)driverId;
@end
