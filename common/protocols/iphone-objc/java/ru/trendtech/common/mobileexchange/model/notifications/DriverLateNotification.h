
/**
 * Created by max on 13.02.14.
 */

@interface DriverLateNotification : NSObject {
  long id;
  int time;
}

@property(nonatomic) long id;
@property(nonatomic) int time;
- (void) setId:(long)id;
- (void) setTime:(int)time;
@end
