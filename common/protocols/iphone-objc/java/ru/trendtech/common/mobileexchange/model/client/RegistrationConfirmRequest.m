#import "RegistrationConfirmRequest.h"

@implementation RegistrationConfirmRequest

@synthesize codeSMS;

- (void) dealloc {
  [codeSMS release];
  [super dealloc];
}

@end
