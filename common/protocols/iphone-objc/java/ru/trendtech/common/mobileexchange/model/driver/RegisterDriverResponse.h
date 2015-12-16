
/**
 * Created by max on 09.02.14.
 */

@interface RegisterDriverResponse : NSObject {
  long driverId;
  NSString * login;
}

@property(nonatomic) long driverId;
@property(nonatomic, retain) NSString * login;
- (void) setDriverId:(long)driverId;
- (void) setLogin:(NSString *)login;
@end
