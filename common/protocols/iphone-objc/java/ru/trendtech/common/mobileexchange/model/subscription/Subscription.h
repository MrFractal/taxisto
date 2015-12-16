
/**
 * Created by ivanenok on 4/14/2014.
 */

@interface Subscription : NSObject {
  BOOL starting;
}

@property(nonatomic) BOOL starting;
- (void) init;
- (void) setStarting:(BOOL)starting;
@end
