#import "LoginClientResponse.h"

@implementation LoginClientResponse

@synthesize clientInfo;

- (void) dealloc {
  [clientInfo release];
  [super dealloc];
}

@end
