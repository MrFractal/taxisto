
/**
 * Created by ivanenok on 4/14/2014.
 */

@interface DriverRequest : NSObject {
  long driverId;
}

@property(nonatomic) long driverId;
- (void) setDriverId:(long)driverId;
@end
