#import "UpdatePasswordRequest.h"

@implementation UpdatePasswordRequest

@synthesize clientId;
@synthesize phone;
@synthesize password;
@synthesize smsCode;

- (void) dealloc {
  [phone release];
  [password release];
  [smsCode release];
  [super dealloc];
}

@end
