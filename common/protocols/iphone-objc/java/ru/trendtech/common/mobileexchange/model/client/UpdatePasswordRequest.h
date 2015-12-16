
/**
 * Created by max on 17.03.14.
 */

@interface UpdatePasswordRequest : NSObject {
  long clientId;
  NSString * phone;
  NSString * password;
  NSString * smsCode;
}

@property(nonatomic) long clientId;
@property(nonatomic, retain) NSString * phone;
@property(nonatomic, retain) NSString * password;
@property(nonatomic, retain) NSString * smsCode;
- (void) setClientId:(long)clientId;
- (void) setPhone:(NSString *)phone;
- (void) setPassword:(NSString *)password;
- (void) setSmsCode:(NSString *)smsCode;
@end
