
/**
 * Created by max on 06.02.14.
 */

@interface PingRequest : NSObject {
  long timestamp;
}

@property(nonatomic) long timestamp;
- (void) setTimestamp:(long)timestamp;
@end
