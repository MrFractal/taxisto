#import "InviteFriendRequest.h"

@implementation InviteFriendRequest

@synthesize driverId;
@synthesize phone;

- (void) dealloc {
  [phone release];
  [super dealloc];
}

@end
