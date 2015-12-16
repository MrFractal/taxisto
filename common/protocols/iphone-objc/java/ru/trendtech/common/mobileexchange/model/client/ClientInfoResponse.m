#import "ClientInfoResponse.h"

@implementation ClientInfoResponse

@synthesize clientInfo;

- (void) dealloc {
  [clientInfo release];
  [super dealloc];
}

@end
