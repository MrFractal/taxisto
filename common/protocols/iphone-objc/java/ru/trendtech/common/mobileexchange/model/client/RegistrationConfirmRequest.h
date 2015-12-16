
/**
 * Created by max on 06.02.14.
 */

@interface RegistrationConfirmRequest : NSObject {
  NSString * codeSMS;
}

@property(nonatomic, retain) NSString * codeSMS;
- (void) setCodeSMS:(NSString *)codeSMS;
@end
