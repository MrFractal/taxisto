
/**
 * Created by max on 17.03.14.
 */

@interface UpdatePasswordResponse : NSObject {
  long clientId;
  NSString * smsCode;
}

@property(nonatomic) long clientId;
@property(nonatomic, retain) NSString * smsCode;
- (void) setClientId:(long)clientId;
- (void) setSmsCode:(NSString *)smsCode;
@end
