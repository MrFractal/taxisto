
/**
 * Created by max on 06.02.14.
 */

@interface AssignedDriverNotification : NSObject {
  long id;
  int time;
}

@property(nonatomic) long id;
@property(nonatomic) int time;
- (void) setId:(long)id;
- (void) setTime:(int)time;
@end
