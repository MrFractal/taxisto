
/**
 * Created by max on 06.02.14.
 */

@interface RatingItem : NSObject {
  long driverId;
  NSString * firstName;
  NSString * lastName;
  int driverRating;
  long driverRatingPosition;
  long driverRatingPositionsTotal;
}

@property(nonatomic) long driverId;
@property(nonatomic, retain) NSString * firstName;
@property(nonatomic) int driverRating;
@property(nonatomic) long driverRatingPosition;
@property(nonatomic) long driverRatingPositionsTotal;
@property(nonatomic, retain) NSString * lastName;
- (id) init;
- (void) setDriverId:(long)driverId;
- (void) setFirstName:(NSString *)firstName;
- (void) setDriverRating:(int)driverRating;
- (void) setDriverRatingPosition:(long)driverRatingPosition;
- (void) setDriverRatingPositionsTotal:(long)driverRatingPositionsTotal;
- (void) setLastName:(NSString *)lastName;
@end
