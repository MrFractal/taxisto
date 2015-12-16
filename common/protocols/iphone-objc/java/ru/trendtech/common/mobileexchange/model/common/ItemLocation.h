
/**
 * Created by max on 06.02.14.
 */

@interface ItemLocation : NSObject {
  long a;
  double la;
  double lo;
  BOOL o;
}

@property(nonatomic) long driverId;
@property(nonatomic) double latitude;
@property(nonatomic) double longitude;
@property(nonatomic) BOOL occupied;
- (void) init;
- (void) setDriverId:(long)driverId;
- (void) setLatitude:(double)latitude;
- (void) setLongitude:(double)longitude;
- (void) setOccupied:(BOOL)occupied;
@end
