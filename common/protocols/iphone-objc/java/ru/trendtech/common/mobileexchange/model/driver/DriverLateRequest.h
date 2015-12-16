
/**
 * Created by max on 13.02.14.
 */

@interface DriverLateRequest : DriverRequest {
  int time;
}

@property(nonatomic) int time;
- (void) setTime:(int)time;
@end
