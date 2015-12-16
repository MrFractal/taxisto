#import "DriverInfoResponse.h"

@implementation DriverInfoResponse

@synthesize driverInfo;

- (void) dealloc {
  [driverInfo release];
  [super dealloc];
}

@end
