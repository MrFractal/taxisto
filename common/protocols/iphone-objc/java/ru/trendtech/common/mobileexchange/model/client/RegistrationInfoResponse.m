#import "RegistrationInfoResponse.h"

@implementation RegistrationInfoResponse

@synthesize clientId;
@synthesize smsCode;

- (void) dealloc {
  [smsCode release];
  [super dealloc];
}

@end
