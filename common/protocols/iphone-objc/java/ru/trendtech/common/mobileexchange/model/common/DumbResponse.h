
/**
 * Created by ivanenok on 4/14/2014.
 */

@interface DumbResponse : NSObject {
  long timestamp;
}

@property(nonatomic) long timestamp;
- (void) setTimestamp:(long)timestamp;
@end
