
/**
 * Created by max on 06.02.14.
 */

@interface RegistrationConfirmResponse : NSObject {
  long clientId;
}

@property(nonatomic) long clientId;
- (void) setClientId:(long)clientId;
@end
