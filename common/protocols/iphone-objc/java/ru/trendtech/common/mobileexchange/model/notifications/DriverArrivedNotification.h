
/**
 * Created by ivanenok on 4/14/2014.
 */

@interface DriverArrivedNotification : NSObject {
  long id;
  int freeTime;
}

@property(nonatomic) long id;
@property(nonatomic) int freeTime;
- (void) setId:(long)id;
- (void) setFreeTime:(int)freeTime;
@end
