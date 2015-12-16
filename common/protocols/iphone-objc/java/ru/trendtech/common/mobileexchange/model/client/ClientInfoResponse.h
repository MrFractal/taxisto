#import "ClientInfo.h"

/**
 * Created by max on 09.02.14.
 */

@interface ClientInfoResponse : NSObject {
  ClientInfo * clientInfo;
}

@property(nonatomic, retain) ClientInfo * clientInfo;
- (void) setClientInfo:(ClientInfo *)clientInfo;
@end
