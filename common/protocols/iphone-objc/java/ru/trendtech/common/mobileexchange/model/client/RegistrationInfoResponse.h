
/**
 * Created by max on 08.02.14.
 */

@interface RegistrationInfoResponse : NSObject {
  long clientId;
  NSString * smsCode;
}

@property(nonatomic) long clientId;
@property(nonatomic, retain) NSString * smsCode;
- (void) setClientId:(long)clientId;
- (void) setSmsCode:(NSString *)smsCode;
@end
