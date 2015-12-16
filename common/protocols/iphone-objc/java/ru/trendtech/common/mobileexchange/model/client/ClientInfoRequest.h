#import "ClientInfo.h"

/**
 * Created by max on 09.02.14.
 */

@interface ClientInfoRequest : NSObject {
  long clientId;
  ClientInfo * clientInfo;
}

@property(nonatomic) long clientId;
@property(nonatomic, retain) ClientInfo * clientInfo;
- (void) setClientId:(long)clientId;
- (void) setClientInfo:(ClientInfo *)clientInfo;
@end
