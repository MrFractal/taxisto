#import "ClientInfo.h"

@interface LoginClientResponse : NSObject {
  ClientInfo * clientInfo;
}

@property(nonatomic, retain) ClientInfo * clientInfo;
- (void) setClientInfo:(ClientInfo *)clientInfo;
@end
