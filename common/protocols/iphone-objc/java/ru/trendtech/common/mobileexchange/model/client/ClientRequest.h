
/**
 * Created by ivanenok on 4/14/2014.
 */

@interface ClientRequest : NSObject {
  long clientId;
}

@property(nonatomic) long clientId;
- (void) setClientId:(long)clientId;
@end
