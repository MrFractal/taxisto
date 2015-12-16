#import "LoginDriverResponse.h"

@implementation LoginDriverResponse

@synthesize driverInfo;

- (void) dealloc {
  [driverInfo release];
  [super dealloc];
}

@end
