#import "DriverLocationRequest.h"

@implementation DriverLocationRequest

@synthesize location;

- (void) dealloc {
  [location release];
  [super dealloc];
}

@end
