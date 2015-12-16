#import "LogoutRequest.h"

@implementation LogoutRequest

@synthesize requesterId;
@synthesize force;
@synthesize deviceInfo;

- (void) dealloc {
  [deviceInfo release];
  [super dealloc];
}

@end
