#import "AssignedDriverResponse.h"

@implementation AssignedDriverResponse

@synthesize expectedTime;
@synthesize driverInfo;

- (void) dealloc {
  [driverInfo release];
  [super dealloc];
}

@end
