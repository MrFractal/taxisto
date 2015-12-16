#import "AssignedDriverLocationNotification.h"

@implementation AssignedDriverLocationNotification

@synthesize loc;
@synthesize id;

- (void) dealloc {
  [loc release];
  [super dealloc];
}

@end
