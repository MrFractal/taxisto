#import "RegisterDriverResponse.h"

@implementation RegisterDriverResponse

@synthesize driverId;
@synthesize login;

- (void) dealloc {
  [login release];
  [super dealloc];
}

@end
