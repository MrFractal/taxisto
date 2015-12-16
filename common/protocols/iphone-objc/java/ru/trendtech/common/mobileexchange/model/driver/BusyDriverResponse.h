
/**
 * Created by max on 09.02.14.
 */

@interface BusyDriverResponse : NSObject {
  long driverId;
}

@property(nonatomic) long driverId;
- (void) setDriverId:(long)driverId;
@end
