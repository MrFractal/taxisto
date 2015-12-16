#import "UpdatePasswordResponse.h"

@implementation UpdatePasswordResponse

@synthesize clientId;
@synthesize smsCode;

- (void) dealloc {
  [smsCode release];
  [super dealloc];
}

@end
