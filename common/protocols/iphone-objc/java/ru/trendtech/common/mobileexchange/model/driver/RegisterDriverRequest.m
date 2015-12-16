#import "RegisterDriverRequest.h"

@implementation RegisterDriverRequest

@synthesize driverInfo;

- (void) dealloc {
  [driverInfo release];
  [super dealloc];
}

@end
