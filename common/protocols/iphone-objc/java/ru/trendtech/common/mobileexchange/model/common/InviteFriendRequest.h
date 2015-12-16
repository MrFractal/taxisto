
/**
 * Created by ivanenok on 4/14/2014.
 */

@interface InviteFriendRequest : NSObject {
  long driverId;
  NSString * phone;
}

@property(nonatomic) long driverId;
@property(nonatomic, retain) NSString * phone;
- (void) setDriverId:(long)driverId;
- (void) setPhone:(NSString *)phone;
@end
