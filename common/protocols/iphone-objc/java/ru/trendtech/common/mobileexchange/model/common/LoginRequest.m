#import "LoginRequest.h"

@implementation LoginRequest

@synthesize login;
@synthesize password;
@synthesize deviceInfoModel;

- (void) dealloc {
  [login release];
  [password release];
  [deviceInfoModel release];
  [super dealloc];
}

@end
