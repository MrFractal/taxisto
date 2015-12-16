
/**
 * Created by max on 09.04.2014.
 */

@interface LogoutResponse : NSObject {
  BOOL completed;
  int reason;
  NSString * reasonMessage;
}

@property(nonatomic) BOOL completed;
@property(nonatomic) int reason;
@property(nonatomic, retain) NSString * reasonMessage;
- (void) setCompleted:(BOOL)completed;
- (void) setReason:(int)reason;
- (void) setReasonMessage:(NSString *)reasonMessage;
@end
