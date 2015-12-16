
/**
 * Created by ivanenok on 4/14/2014.
 */

@interface PushMessage : NSObject {
  int t;
  NSString * o;
}

@property(nonatomic) int t;
@property(nonatomic, retain) NSString * o;
- (void) init;
- (void) setT:(int)t;
- (void) setO:(NSString *)o;
@end
