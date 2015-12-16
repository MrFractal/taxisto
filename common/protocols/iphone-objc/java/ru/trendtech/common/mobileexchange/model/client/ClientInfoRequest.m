#import "ClientInfoRequest.h"

@implementation ClientInfoRequest

@synthesize clientId;
@synthesize clientInfo;

- (void) dealloc {
  [clientInfo release];
  [super dealloc];
}

@end
